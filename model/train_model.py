import torch  # PyTorch核心库
import torch.nn as nn  # 神经网络层
import torch.optim as optim  # 优化器
from torch.utils.data import DataLoader, Dataset  # 数据集加载工具
from torchvision import transforms, models  # 数据变换、预训练模型
from PIL import Image  # 图片读取
import os  # 文件路径处理
from sklearn.model_selection import train_test_split  # 数据集划分
from sklearn.metrics import precision_score, recall_score, f1_score, confusion_matrix  # 评估指标
import warnings  # 忽略警告
import numpy as np  # 数值计算
import matplotlib.pyplot as plt  # 混淆矩阵可视化

# ========== 强制CUDA配置（核心修改） ==========
# 1. 检查CUDA是否可用，不可用则报错（确保使用GPU训练）
if not torch.cuda.is_available():
    raise RuntimeError("错误：未检测到CUDA设备！请确保安装了CUDA版本的PyTorch，且GPU可用。")

# 2. 设置CUDA设备（优先使用第0块GPU，多GPU可修改）
DEVICE = torch.device("cuda:0")
# 3. 启用CUDA基准模式（加速训练）
torch.backends.cudnn.benchmark = True
# 4. 清空CUDA缓存（避免显存碎片）
torch.cuda.empty_cache()

warnings.filterwarnings('ignore')  # 关闭警告输出
# 设置中文字体（避免混淆矩阵中文乱码）
plt.rcParams['font.sans-serif'] = ['SimHei']
plt.rcParams['axes.unicode_minus'] = False

# 1. 数据集定义（适配CUDA，关闭多线程可能的冲突）
class BookImageDataset(Dataset):
    def __init__(self, image_paths, labels, transform=None):
        self.image_paths = image_paths
        self.labels = labels
        self.transform = transform

    def __len__(self):
        return len(self.image_paths)

    def __getitem__(self, idx):
        img_path = self.image_paths[idx]
        label = self.labels[idx]
        try:
            image = Image.open(img_path).convert('RGB')
            if self.transform:
                image = self.transform(image)
            # 标签转为float32（适配CUDA张量类型）
            return image, torch.tensor(label, dtype=torch.float32)
        except Exception as e:
            print(f"加载图片失败 {img_path}: {e}")
            # 返回空张量（CUDA兼容）
            return torch.zeros(3, 224, 224, device=DEVICE), torch.tensor(0, dtype=torch.float32, device=DEVICE)

# 2. 数据预处理（保持不变）
def get_transforms():
    train_transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.RandomHorizontalFlip(),
        transforms.RandomRotation(10),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])

    val_transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])
    return train_transform, val_transform

# 3. 模型定义（ResNet50 + CUDA初始化）
def build_model():
    # 加载预训练模型到CUDA
    model = models.resnet50(pretrained=True).to(DEVICE)
    # 冻结特征提取层
    for param in model.parameters():
        param.requires_grad = False
    # 修改全连接层（保持在CUDA上）
    num_ftrs = model.fc.in_features
    model.fc = nn.Sequential(
        nn.Linear(num_ftrs, 128),
        nn.ReLU(),
        nn.Dropout(0.5),
        nn.Linear(128, 1),
        nn.Sigmoid()
    ).to(DEVICE)  # 确保全连接层也在CUDA上
    return model

# 4. 评估指标计算函数（保持不变）
def calculate_metrics(true_labels, pred_labels):
    precision = precision_score(true_labels, pred_labels, pos_label=1, zero_division=0)
    recall = recall_score(true_labels, pred_labels, pos_label=1, zero_division=0)
    f1 = f1_score(true_labels, pred_labels, pos_label=1, zero_division=0)
    return precision, recall, f1

# 5. 混淆矩阵可视化函数（保持不变）
def plot_confusion_matrix(true_labels, pred_labels, save_path='confusion_matrix.png'):
    cm = confusion_matrix(true_labels, pred_labels)
    plt.figure(figsize=(8, 6))
    plt.imshow(cm, interpolation='nearest', cmap=plt.cm.Blues)
    plt.title('混淆矩阵（图书封面分类）')
    plt.colorbar()
    tick_marks = np.arange(2)
    plt.xticks(tick_marks, ['非图书封面', '图书封面'], fontsize=12)
    plt.yticks(tick_marks, ['非图书封面', '图书封面'], fontsize=12)

    thresh = cm.max() / 2.
    for i in range(cm.shape[0]):
        for j in range(cm.shape[1]):
            plt.text(j, i, format(cm[i, j], 'd'),
                     ha="center", va="center",
                     color="white" if cm[i, j] > thresh else "black",
                     fontsize=14)

    plt.ylabel('真实标签', fontsize=12)
    plt.xlabel('预测标签', fontsize=12)
    plt.tight_layout()
    plt.savefig(save_path)
    plt.close()
    print(f"混淆矩阵已保存至：{save_path}")

# 6. 训练函数（CUDA优化版）
def train_model(model, train_loader, val_loader, criterion, optimizer, num_epochs=10, resume_path=None):
    # 确保模型在CUDA上
    model = model.to(DEVICE)
    best_acc = 0.0
    best_f1 = 0.0
    start_epoch = 0

    # 加载历史模型（确保映射到CUDA）
    if resume_path:
        if not os.path.exists(resume_path):
            print(f"警告：历史模型文件 {resume_path} 不存在！将从头开始训练。")
        else:
            # 强制加载到CUDA设备
            checkpoint = torch.load(resume_path, map_location=DEVICE)
            model.load_state_dict(checkpoint['model_state_dict'])
            optimizer.load_state_dict(checkpoint['optimizer_state_dict'])
            start_epoch = checkpoint['epoch'] + 1
            best_acc = checkpoint['best_acc']
            best_f1 = checkpoint.get('best_f1', 0.0)
            print(f"成功加载历史模型！从Epoch {start_epoch} 继续训练，历史最佳精度：{best_acc:.4f}，最佳F1：{best_f1:.4f}")

    # 训练循环
    for epoch in range(start_epoch, num_epochs):
        # 训练阶段
        model.train()
        train_loss = 0.0
        train_true = []
        train_pred = []
        # 记录CUDA显存使用
        print(f"\nEpoch {epoch+1} - CUDA显存使用: {torch.cuda.memory_allocated(DEVICE)/1024/1024:.2f} MB")

        for batch_idx, (images, labels) in enumerate(train_loader):
            # 强制将数据移到CUDA（避免CPU张量冲突）
            images = images.to(DEVICE, non_blocking=True)  # 非阻塞传输加速
            labels = labels.to(DEVICE, non_blocking=True).unsqueeze(1)

            optimizer.zero_grad()
            # 前向传播（CUDA加速）
            outputs = model(images)
            loss = criterion(outputs, labels)
            # 反向传播
            loss.backward()
            optimizer.step()

            train_loss += loss.item() * images.size(0)
            preds = (outputs > 0.5).float()
            
            # 转移到CPU计算指标（避免CUDA内存占用）
            train_true.extend(labels.detach().cpu().numpy().flatten())
            train_pred.extend(preds.detach().cpu().numpy().flatten())

            # 每10个批次清空一次CUDA缓存
            if batch_idx % 10 == 0:
                torch.cuda.empty_cache()

        # 训练集指标计算
        train_loss = train_loss / len(train_loader.dataset)
        train_acc = (np.array(train_true) == np.array(train_pred)).mean()
        train_precision, train_recall, train_f1 = calculate_metrics(np.array(train_true), np.array(train_pred))

        # 验证阶段
        model.eval()
        val_loss = 0.0
        val_true = []
        val_pred = []

        with torch.no_grad():  # 禁用梯度计算，节省CUDA显存
            for images, labels in val_loader:
                images = images.to(DEVICE, non_blocking=True)
                labels = labels.to(DEVICE, non_blocking=True).unsqueeze(1)
                outputs = model(images)
                loss = criterion(outputs, labels)

                val_loss += loss.item() * images.size(0)
                preds = (outputs > 0.5).float()
                
                val_true.extend(labels.cpu().numpy().flatten())
                val_pred.extend(preds.cpu().numpy().flatten())

        # 验证集指标计算
        val_loss = val_loss / len(val_loader.dataset)
        val_acc = (np.array(val_true) == np.array(val_pred)).mean()
        val_precision, val_recall, val_f1 = calculate_metrics(np.array(val_true), np.array(val_pred))

        # 打印指标
        print('='*60)
        print(f'【Epoch {epoch + 1}/{num_epochs}】')
        print(f'训练集 - Loss: {train_loss:.4f} | Acc: {train_acc:.4f} | Precision: {train_precision:.4f} | Recall: {train_recall:.4f} | F1: {train_f1:.4f}')
        print(f'验证集 - Loss: {val_loss:.4f} | Acc: {val_acc:.4f} | Precision: {val_precision:.4f} | Recall: {val_recall:.4f} | F1: {val_f1:.4f}')
        print('='*60)

        # 保存最佳模型（CUDA兼容）
        if val_f1 > best_f1:
            best_acc = val_acc
            best_f1 = val_f1
            # 保存时确保参数在CPU（兼容不同设备加载）
            torch.save({
                'epoch': epoch,
                'model_state_dict': model.state_dict(),
                'optimizer_state_dict': optimizer.state_dict(),
                'best_acc': best_acc,
                'best_f1': best_f1
            }, 'best_book_classifier.pth')
            print(f"保存新的最佳模型！Acc: {best_acc:.4f}, F1: {best_f1:.4f}")
            plot_confusion_matrix(np.array(val_true), np.array(val_pred))

        # 每个Epoch结束后清空CUDA缓存
        torch.cuda.empty_cache()

    # 训练结束输出
    print(f'\n训练完成！最终最佳指标（CUDA训练）：')
    print(f'最佳验证集Acc: {best_acc:.4f}')
    print(f'最佳验证集Precision: {val_precision:.4f}')
    print(f'最佳验证集Recall: {val_recall:.4f}')
    print(f'最佳验证集F1-Score: {best_f1:.4f}')

    return model

# 7. 推理函数（强制CUDA）
def predict_image(model_path, image_path):
    if not os.path.exists(model_path):
        print(f"错误：模型文件 {model_path} 不存在！")
        return False, 0.0
    if not os.path.exists(image_path):
        print(f"错误：图片文件 {image_path} 不存在！")
        return False, 0.0

    # 加载模型到CUDA
    model = build_model()
    checkpoint = torch.load(model_path, map_location=DEVICE)
    model.load_state_dict(checkpoint['model_state_dict'])
    model.eval()

    transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])

    try:
        image = Image.open(image_path).convert('RGB')
        image = transform(image).unsqueeze(0).to(DEVICE)  # 移到CUDA
    except Exception as e:
        print(f"图片加载失败：{e}")
        return False, 0.0

    with torch.no_grad():
        output = model(image)
        confidence = output.item()
        is_book = confidence > 0.5

    return is_book, confidence

# 8. 批量评估函数（CUDA版）
def evaluate_dataset(model_path, dataset):
    if not os.path.exists(model_path):
        print(f"错误：模型文件 {model_path} 不存在！")
        return
    
    model = build_model()
    checkpoint = torch.load(model_path, map_location=DEVICE)
    model.load_state_dict(checkpoint['model_state_dict'])
    model.eval()

    dataloader = DataLoader(dataset, batch_size=32, shuffle=False, num_workers=0)  # 关闭多线程避免CUDA冲突
    all_true = []
    all_pred = []

    with torch.no_grad():
        for images, labels in dataloader:
            images = images.to(DEVICE)
            labels = labels.to(DEVICE).unsqueeze(1)
            outputs = model(images)
            preds = (outputs > 0.5).float()
            
            all_true.extend(labels.cpu().numpy().flatten())
            all_pred.extend(preds.cpu().numpy().flatten())

    precision = precision_score(all_true, all_pred, pos_label=1, zero_division=0)
    recall = recall_score(all_true, all_pred, pos_label=1, zero_division=0)
    f1 = f1_score(all_true, all_pred, pos_label=1, zero_division=0)
    acc = (np.array(all_true) == np.array(all_pred)).mean()

    print(f'\n【CUDA批量评估结果】')
    print(f'准确率(Acc): {acc:.4f}')
    print(f'精确率(Precision): {precision:.4f}')
    print(f'召回率(Recall): {recall:.4f}')
    print(f'F1分数(F1-Score): {f1:.4f}')

    plot_confusion_matrix(np.array(all_true), np.array(all_pred), save_path='dataset_confusion_matrix.png')
    return acc, precision, recall, f1

# 9. 主函数（CUDA专用配置）
if __name__ == '__main__':
    # 配置参数（适配CUDA显存）
    DATA_DIR = './book_dataset'
    BATCH_SIZE = 32  # 根据GPU显存调整：1080Ti(11G)用32，3090(24G)用64，显存小则用16/8
    LEARNING_RATE = 0.001
    NUM_EPOCHS = 50

    # 打印CUDA信息
    print(f"===== CUDA训练配置 =====")
    print(f"CUDA设备: {torch.cuda.get_device_name(DEVICE)}")
    print(f"CUDA版本: {torch.version.cuda}")
    print(f"可用GPU数量: {torch.cuda.device_count()}")
    print(f"========================")

    # 加载数据集
    book_paths = [os.path.join(DATA_DIR, 'books', f) for f in os.listdir(os.path.join(DATA_DIR, 'books')) if
                  f.endswith(('jpg', 'png', 'jpeg'))]
    non_book_paths = [os.path.join(DATA_DIR, 'non_books', f) for f in os.listdir(os.path.join(DATA_DIR, 'non_books')) if
                      f.endswith(('jpg', 'png', 'jpeg'))]

    if len(book_paths) == 0 or len(non_book_paths) == 0:
        raise RuntimeError(f"数据集目录 {DATA_DIR} 下的books/或non_books/文件夹为空！请检查数据集。")

    all_paths = book_paths + non_book_paths
    all_labels = [1] * len(book_paths) + [0] * len(non_book_paths)

    # 划分数据集
    train_paths, val_paths, train_labels, val_labels = train_test_split(
        all_paths, all_labels, test_size=0.2, random_state=114, stratify=all_labels
    )

    # 数据变换
    train_transform, val_transform = get_transforms()
    train_dataset = BookImageDataset(train_paths, train_labels, train_transform)
    val_dataset = BookImageDataset(val_paths, val_labels, val_transform)

    # 数据加载器（关闭多线程num_workers=0，避免CUDA报错）
    train_loader = DataLoader(
        train_dataset, 
        batch_size=BATCH_SIZE, 
        shuffle=True, 
        num_workers=0,  # CUDA训练建议设为0
        pin_memory=True  # 固定内存，加速CUDA传输
    )
    val_loader = DataLoader(
        val_dataset, 
        batch_size=BATCH_SIZE, 
        shuffle=False, 
        num_workers=0,
        pin_memory=True
    )

    # 初始化模型、损失函数、优化器（全部在CUDA上）
    model = build_model()
    criterion = nn.BCELoss().to(DEVICE)  # 损失函数移到CUDA
    optimizer = optim.Adam(model.fc.parameters(), lr=LEARNING_RATE)

    # ========== 执行训练（强制CUDA） ==========
    # 场景1：首次训练
    train_model(model, train_loader, val_loader, criterion, optimizer, NUM_EPOCHS)

    # 场景2：加载历史模型继续训练
    # resume_path = 'best_book_classifier.pth'
    # train_model(model, train_loader, val_loader, criterion, optimizer, NUM_EPOCHS, resume_path)

    # 场景3：单张图片推理
    # model_path = 'best_book_classifier.pth'
    # test_image_path = './book_dataset/books/1.jpg'
    # is_book, confidence = predict_image(model_path, test_image_path)
    # print(f"\n图片{test_image_path}预测结果：{'是图书封面' if is_book else '非图书封面'}，置信度：{confidence:.4f}")

    # 场景4：批量评估验证集
    # evaluate_dataset('best_book_classifier.pth', val_dataset)