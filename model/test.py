import torch
from torchvision import models, transforms
import torch.nn as nn
from PIL import Image
import os
import warnings

warnings.filterwarnings('ignore')

# ===================== 核心配置 =====================
DEVICE = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
MODEL_PATH = 'best_book_classifier.pth'
THRESHOLD = 0.5  # 分类阈值（图书封面判定界限）
PREDICT_DIR = "./val"  # 待预测图片所在文件夹路径（修改这里！）

# 图片预处理（和训练/推理时完全一致）
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])

# ===================== 模型构建与加载（ResNet50） =====================
def build_model():
    """构建ResNet50模型结构（和训练时一致）"""
    model = models.resnet50(pretrained=False)
    for param in model.parameters():
        param.requires_grad = False
    num_ftrs = model.fc.in_features
    model.fc = nn.Sequential(
        nn.Linear(num_ftrs, 128),
        nn.ReLU(),
        nn.Dropout(0.5),
        nn.Linear(128, 1),
        nn.Sigmoid()
    )
    return model

def load_model():
    """加载训练好的模型权重"""
    try:
        model = build_model()
        checkpoint = torch.load(MODEL_PATH, map_location=DEVICE)
        model.load_state_dict(checkpoint['model_state_dict'])
        model.to(DEVICE)
        model.eval()  # 切换到推理模式（关闭Dropout）
        print(f"✅ 模型加载成功！推理设备：{DEVICE}")
        return model
    except FileNotFoundError:
        raise Exception(f"❌ 模型文件不存在：{MODEL_PATH}")
    except KeyError as e:
        raise Exception(f"❌ 模型权重不匹配：{e}（请确认是ResNet50模型）")
    except Exception as e:
        raise Exception(f"❌ 模型加载失败：{str(e)}")

# ===================== 核心预测函数 =====================
def predict_image(model, image_path):
    """
    单张图片预测
    :param model: 加载好的模型实例
    :param image_path: 图片文件路径
    :return: 预测结果字典
    """
    # 1. 检查图片文件是否存在
    if not os.path.exists(image_path):
        return {"status": "error", "message": f"图片文件不存在", "filename": os.path.basename(image_path)}
    
    # 2. 加载并预处理图片
    try:
        image = Image.open(image_path).convert('RGB')
        img_tensor = transform(image).unsqueeze(0).to(DEVICE)  # 添加batch维度
    except Exception as e:
        return {
            "status": "error", 
            "message": f"图片加载失败：{str(e)[:30]}", 
            "filename": os.path.basename(image_path)
        }
    
    # 3. 模型推理
    with torch.no_grad():  # 禁用梯度计算，节省显存
        output = model(img_tensor)
        confidence = round(output.item(), 4)  # 置信度（0~1）
        is_book = confidence >= THRESHOLD     # 是否为图书封面
    
    # 4. 构造结果
    return {
        "status": "success",
        "filename": os.path.basename(image_path),
        "file_path": image_path,
        "is_book": is_book,
        "confidence": confidence,
        "threshold": THRESHOLD,
        "result_desc": "是图书封面" if is_book else "非图书封面"
    }

def batch_predict_from_dir(model, dir_path):
    """
    读取指定文件夹下的所有图片并批量预测
    :param model: 加载好的模型实例
    :param dir_path: 图片文件夹路径
    :return: 批量预测结果列表
    """
    # 1. 检查文件夹是否存在
    if not os.path.exists(dir_path):
        print(f"❌ 文件夹不存在：{dir_path}")
        return []
    
    # 2. 收集文件夹下所有图片文件
    image_extensions = ('.jpg', '.jpeg', '.png', '.bmp', '.gif', '.tiff')
    image_paths = []
    for file_name in os.listdir(dir_path):
        # 过滤非图片文件
        if file_name.lower().endswith(image_extensions):
            image_paths.append(os.path.join(dir_path, file_name))
    
    # 3. 检查是否有图片文件
    if len(image_paths) == 0:
        print(f"❌ 文件夹 {dir_path} 下未找到任何图片文件（支持格式：{image_extensions}）")
        return []
    
    # 4. 批量预测
    print(f"\n📁 开始预测文件夹 {dir_path} 下的图片，共 {len(image_paths)} 张")
    print("-" * 80)
    results = []
    for idx, img_path in enumerate(image_paths, 1):
        result = predict_image(model, img_path)
        results.append(result)
        # 打印单张结果
        if result["status"] == "success":
            print(f"[{idx}/{len(image_paths)}] {result['filename']} → {result['result_desc']}（置信度：{result['confidence']}）")
        else:
            print(f"[{idx}/{len(image_paths)}] {result['filename']} → ❌ {result['message']}")
    
    return results

# ===================== 主函数（自动读取文件夹预测） =====================
if __name__ == "__main__":
    # 1. 加载模型
    try:
        model = load_model()
    except Exception as e:
        print(e)
        exit(1)
    
    # 2. 批量预测指定文件夹下的所有图片
    predict_results = batch_predict_from_dir(model, PREDICT_DIR)
    
    # 3. 输出预测汇总
    if predict_results:
        print("\n" + "="*80)
        print("📊 批量预测汇总")
        print("="*80)
        # 统计结果
        success_count = len([r for r in predict_results if r["status"] == "success"])
        error_count = len([r for r in predict_results if r["status"] == "error"])
        book_count = len([r for r in predict_results if r["status"] == "success" and r["is_book"]])
        non_book_count = len([r for r in predict_results if r["status"] == "success" and not r["is_book"]])
        
        print(f"总图片数：{len(predict_results)} | 成功预测：{success_count} | 预测失败：{error_count}")
        if success_count > 0:
            print(f"判定为图书封面：{book_count} 张 | 判定为非图书封面：{non_book_count} 张")
        
        # 输出失败案例（如果有）
        if error_count > 0:
            print("\n❌ 预测失败的图片：")
            for res in predict_results:
                if res["status"] == "error":
                    print(f"  - {res['filename']}：{res['message']}")
        print("="*80)