import csv
import os
import requests
from PIL import Image
from io import BytesIO

# ================= 配置参数 =================
CSV_FILE_PATH = "book30-listing-test.csv"  # 替换为你的实际CSV文件路径（如G:/work8/kkyq/mode/books.csv）
SAVE_DIR = "book_dataset/books"    # 目标保存目录
MAX_IMAGES = 600                  # 只爬取前600张
TIMEOUT = 10                      # 图片下载超时时间（秒）

# ================= 创建保存目录 =================
os.makedirs(SAVE_DIR, exist_ok=True)

# ================= 核心爬取逻辑 =================
def crawl_book_covers():
    # 1. 读取CSV文件（自动适配编码+逗号分隔）
    book_data = []
    # 优先尝试中文系统常见编码，再试UTF-8
    encodings = ["gbk", "gb2312", "utf-8", "utf-8-sig"]
    
    for encoding in encodings:
        try:
            with open(CSV_FILE_PATH, "r", encoding=encoding, newline="") as csvfile:
                # 关键修改：使用逗号分隔符（标准CSV），并处理双引号包裹
                reader = csv.reader(csvfile, delimiter=",", quotechar='"')
                for row in reader:
                    if len(row) >= 4:  # 确保行有足够字段（至少包含URL列）
                        filename = row[1].strip()  # 第二列：图片文件名（如044310073X.jpg）
                        img_url = row[2].strip()   # 第三列：图片URL（之前数错列！）
                        # 过滤空文件名/空URL
                        if filename and img_url.startswith(("http://", "https://")):
                            book_data.append((filename, img_url))
            print(f"✅ 成功使用 {encoding} 编码读取CSV文件")
            break  # 编码正确则跳出循环
        except UnicodeDecodeError:
            print(f"❌ {encoding} 编码解码失败，尝试下一个编码...")
            continue
        except Exception as e:
            print(f"❌ 读取CSV出错：{e}")
            return
    
    if not book_data:
        print("❌ ERROR：所有编码都无法解析CSV文件，或文件无有效数据！")
        return
    
    # 2. 限制只爬取前600条
    book_data = book_data[:MAX_IMAGES]
    print(f"📄 共读取到 {len(book_data)} 条有效数据（已限制前{MAX_IMAGES}条），开始爬取...")
    
    # 3. 批量下载图片
    success_count = 0
    fail_count = 0
    for idx, (filename, img_url) in enumerate(book_data, 1):
        try:
            # 发送请求下载图片（添加请求头避免被亚马逊拦截）
            headers = {
                "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            }
            response = requests.get(img_url, headers=headers, timeout=TIMEOUT)
            response.raise_for_status()  # 抛出HTTP错误（如404/500）
            
            # 验证图片有效性并保存
            img = Image.open(BytesIO(response.content))
            # 统一转换为RGB（避免PNG透明通道/格式问题）
            if img.mode != "RGB":
                img = img.convert("RGB")
            save_path = os.path.join(SAVE_DIR, filename)
            img.save(save_path, "JPEG", quality=95)  # 保存为JPEG，保证质量
            
            success_count += 1
            print(f"[{idx}/{MAX_IMAGES}] ✅ 成功：{filename}")
        
        except requests.exceptions.Timeout:
            fail_count += 1
            print(f"[{idx}/{MAX_IMAGES}] ⏳ 超时：{filename} -> {img_url}")
        except requests.exceptions.HTTPError as e:
            fail_count += 1
            print(f"[{idx}/{MAX_IMAGES}] ❌ HTTP错误：{filename} -> {e}")
        except requests.exceptions.ConnectionError:
            fail_count += 1
            print(f"[{idx}/{MAX_IMAGES}] ❌ 连接失败：{filename} -> {img_url}")
        except Exception as e:
            fail_count += 1
            print(f"[{idx}/{MAX_IMAGES}] ❌ 失败：{filename} -> 错误：{str(e)[:50]}")
            continue
    
    # 4. 输出爬取结果统计
    print("\n========== 爬取完成 ==========")
    print(f"✅ 成功下载：{success_count} 张")
    print(f"❌ 下载失败：{fail_count} 张")
    print(f"📂 保存目录：{os.path.abspath(SAVE_DIR)}")

# ================= 执行爬取 =================
if __name__ == "__main__":
    # 先检查CSV文件是否存在
    if not os.path.exists(CSV_FILE_PATH):
        print(f"❌ ERROR：CSV文件不存在！路径：{CSV_FILE_PATH}")
    else:
        crawl_book_covers()