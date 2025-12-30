# -*- coding: utf-8 -*-

from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import torch
import torch.nn as nn
from torchvision import transforms, models
from PIL import Image
import io
import uvicorn
import warnings
import os
import sys

warnings.filterwarnings('ignore')

# ===================== 核心配置 =====================
# 初始化FastAPI应用
app = FastAPI(title="图书封面分类服务", version="1.0")

# 配置CORS（允许跨域）
app.add_middleware(
    allow_origins=["*"],  # 生产环境建议指定具体域名
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 模型/设备配置
DEVICE = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
# ⚠️ 【必填】修改为你的模型文件实际绝对路径（复制你的.pth文件完整路径替换）
MODEL_PATH = 'C:\\ruoyi\\kkyq-rebuild\\model\\best_book_classifier.pth'
THRESHOLD = 0.95  # 分类阈值
MODEL_ARCH = "resnet50"  # 匹配训练时的模型架构
MODEL_PATH = 'best_book_classifier.pth'  # 训练好的模型路径
THRESHOLD = 0.97                         # 分类阈值
MODEL_ARCH = "resnet50"                  # 匹配训练时的模型架构

# 全局变量（模型状态）
model = None
model_loaded = False


# ===================== 模型加载（匹配ResNet50结构 + 修复PyTorch2.6+加载报错） =====================
def build_resnet50_model():
    """构建和训练代码一致的ResNet50模型结构"""
    model = models.resnet50(pretrained=False)  # 仅加载结构，不加载预训练权重
    # 冻结层（和训练代码一致）
    for param in model.parameters():
        param.requires_grad = False
    # 全连接层结构（和训练代码完全一致）
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
    """加载训练好的模型权重 - 已修复PyTorch2.6+加载报错"""
def load_model():
    """加载训练好的模型权重"""
    global model, model_loaded
    try:
        # 1. 检查模型文件是否存在
        if not os.path.exists(MODEL_PATH):
            raise FileNotFoundError(f"模型文件不存在：{MODEL_PATH}")


        # 2. 构建模型结构
        if MODEL_ARCH == "resnet50":
            model = build_resnet50_model()
        else:
            model = models.resnet18(pretrained=False)  # 兼容旧版ResNet18
            num_ftrs = model.fc.in_features
            model.fc = nn.Sequential(
                nn.Linear(num_ftrs, 128),
                nn.ReLU(),
                nn.Dropout(0.5),
                nn.Linear(128, 1),
                nn.Sigmoid()
            )

        # ✅ 核心修复1：添加 weights_only=False 解决PyTorch2.6+权重加载拦截问题
        # ✅ 核心修复2：map_location自动适配CPU/GPU，兼容训练和推理环境不一致
        checkpoint = torch.load(MODEL_PATH, map_location=DEVICE, weights_only=False)

        # 3. 加载权重（兼容CUDA/CPU）
        model.load_state_dict(checkpoint['model_state_dict'])

        # 4. 模型部署配置
        model = model.to(DEVICE)
        model.eval()  # 推理模式（关闭Dropout/BatchNorm训练模式）

        # 5. 更新状态
        model_loaded = True
        print(f"✅ 模型加载成功！")
        print(f"├─ 使用设备：{DEVICE}")
        print(f"├─ 模型架构：{MODEL_ARCH}")
        print(f"└─ 最佳验证F1：{checkpoint.get('best_f1', '未知'):.4f}")


        # 3. 加载权重（兼容CUDA/CPU）
        checkpoint = torch.load(MODEL_PATH, map_location=DEVICE)
        model.load_state_dict(checkpoint['model_state_dict'])
        
        # 4. 模型部署配置
        model = model.to(DEVICE)
        model.eval()  # 推理模式（关闭Dropout/BatchNorm训练模式）
        
        # 5. 更新状态
        model_loaded = True
        print(f"✅ 模型加载成功！使用设备：{DEVICE}，最佳验证F1：{checkpoint.get('best_f1', '未知'):.4f}")
        
    except FileNotFoundError as e:
        model_loaded = False
        print(f"❌ 模型加载失败：{e}")
    except KeyError as e:
        model_loaded = False
        print(f"❌ 模型权重不匹配：{e}（请确认模型架构和训练时一致）")
    except Exception as e:
        model_loaded = False
        print(f"❌ 模型加载异常：{str(e)}")

# ===================== 数据预处理（和训练代码完全一致） =====================
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.RandomHorizontalFlip(p=0),  # 推理时关闭随机翻转
    transforms.RandomRotation(0),  # 推理时关闭随机旋转
    transforms.RandomRotation(0),          # 推理时关闭随机旋转
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])

# ===================== 核心推理函数 =====================
def predict_book_image(image: Image.Image) -> dict:
    """
    图片推理核心函数
    :param image: PIL Image对象
    :return: 推理结果字典
    """
    # 前置校验
    if not model_loaded or model is None:
        return {
            "status": "error",
            "message": "模型未加载成功，无法执行推理"
        }
    try:
        # 1. 图片预处理
        img_tensor = transform(image).unsqueeze(0)  # 添加batch维度 [1,3,224,224]
        img_tensor = img_tensor.to(DEVICE, non_blocking=True)
        # 2. 推理（禁用梯度计算，节省显存）
        with torch.no_grad():
            output = model(img_tensor)
            confidence = output.item()  # 置信度（0~1）
            is_book = confidence >= THRESHOLD
        # 3. 构造结果
        return {
            "status": "success",
            "is_book": bool(is_book),  # 是否为图书封面
            "confidence": round(confidence, 4),  # 置信度（保留4位小数）
            "threshold": THRESHOLD,  # 分类阈值
            "model_arch": MODEL_ARCH,  # 模型架构
            "device": str(DEVICE)  # 推理设备
        }
        # 3. 构造结果
        return {
            "status": "success",
            "is_book": bool(is_book),          # 是否为图书封面
            "confidence": round(confidence, 4),# 置信度（保留4位小数）
            "threshold": THRESHOLD,            # 分类阈值
            "model_arch": MODEL_ARCH,          # 模型架构
            "device": str(DEVICE)              # 推理设备
        }
    
    except Exception as e:
        return {
            "status": "error",
            "message": f"推理失败：{str(e)[:100]}"  # 截断过长错误信息
        }

# ===================== API接口 =====================
@app.post("/predict", summary="图书封面分类推理", response_description="推理结果")
async def predict_image(file: UploadFile = File(..., description="上传的图片文件（支持jpg/png/jpeg）")):
    """
    单张图片推理接口
    - file: 上传的图片文件（支持jpg/png/jpeg）
    """
    # 1. 校验文件类型
    if not file.content_type in ["image/jpeg", "image/png", "image/jpg"]:
        raise HTTPException(
            status_code=400,
            detail=f"不支持的文件类型：{file.content_type}，仅支持jpg/png/jpeg"
        )
    try:
        # 2. 读取并解析图片
        contents = await file.read()
        # 处理超大图片（限制内存占用）
        if len(contents) > 10 * 1024 * 1024:  # 超过10MB
            raise HTTPException(status_code=400, detail="图片大小超过10MB，请压缩后上传")
        # ✅ 修复：图片完整性校验，防止截断/损坏图片报错
        image = Image.open(io.BytesIO(contents)).convert('RGB')
        image.verify()  # 校验图片完整性
        image = Image.open(io.BytesIO(contents)).convert('RGB')  # 重新读取

        # 4. 执行推理
        result = predict_book_image(image)

        # 5. 处理推理结果
        if result["status"] == "error":
            raise HTTPException(status_code=500, detail=result["message"])

        # 3. 转换为PIL Image
        image = Image.open(io.BytesIO(contents)).convert('RGB')
        
        # 4. 执行推理
        result = predict_book_image(image)
        
        # 5. 处理推理结果
        if result["status"] == "error":
            raise HTTPException(status_code=500, detail=result["message"])
        # 6. 返回标准化响应
        return {
            "code": 200,
            "msg": "推理成功",
            "data": {
                "filename": file.filename,
                "is_book": result["is_book"],
                "confidence": result["confidence"],
                "threshold": result["threshold"],
                "model_info": f"{result['model_arch']} ({result['device']})",
            }
        }
    except HTTPException:
        raise  # 抛出已定义的HTTP异常
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"图片处理失败：{str(e)[:100]}（请检查图片格式是否正常）"
        )

@app.get("/health", summary="服务健康检查", response_description="服务状态")
async def health_check():
    """
    服务健康检查接口（可用于监控）
    """
    # 服务状态码：200=正常，503=模型异常
    status_code = 200 if model_loaded else 503
    status_msg = "服务正常" if model_loaded else "模型加载失败，服务不可用"
    return {
        "code": status_code,
        "msg": status_msg,
        "data": {
            "service_name": "book_cover_classifier",
            "model_loaded": model_loaded,
            "model_path": MODEL_PATH,
            "model_arch": MODEL_ARCH,
            "device": str(DEVICE),
            "threshold": THRESHOLD,
            "torch_version": torch.__version__,
            "python_version": f"{sys.version.split()[0]}",
            "python_version": f"{os.sys.version.split()[0]}",
            "service_port": 8000
        }
    }
@app.get("/", summary="服务根路径", response_description="服务信息")
async def root():
    """
    服务根路径，返回基本信息
    """
    return {
        "service": "图书封面分类服务",
        "version": "1.0",
        "docs": "/docs",  # Swagger文档地址
        "redoc": "/redoc",  # ReDoc文档地址
        "docs": "/docs",          # Swagger文档地址
        "redoc": "/redoc",        # ReDoc文档地址
        "endpoints": {
            "predict": "/predict (POST)",
            "health": "/health (GET)"
        },
        "model_status": "✅ 已加载" if model_loaded else "❌ 未加载"
    }
# ===================== 启动服务 =====================
if __name__ == "__main__":
    # 启动前加载模型
    load_model()
    # 启动UVicorn服务
    uvicorn.run(
        app,
        host="0.0.0.0",    # 允许外部访问
        port=8000,         # 服务端口
        workers=1,         # 单进程（避免多进程模型重复加载）
        reload=False       # 生产环境关闭自动重载
    )