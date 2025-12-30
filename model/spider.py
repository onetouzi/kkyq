import os
import time
import requests
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from PIL import Image
from io import BytesIO
from webdriver_manager.chrome import ChromeDriverManager

# ================= 配置参数 =================
TARGET_URL = "https://cn.bing.com/images/search?q=%E5%9B%BE%E4%B9%A6%E5%B0%81%E9%9D%A2%E5%9B%BE%E7%89%87&qpvt=%e5%9b%be%e4%b9%a6%e5%b0%81%e9%9d%a2%e5%9b%be%e7%89%87&form=IGRE&first=1&cw=1177&ch=775"
SAVE_DIR = "book_cover_images"  # 图片保存目录
MAX_IMAGES = 500  # 最大爬取图片数（可调整）
SCROLL_PAUSE_TIME = 2  # 滚动加载间隔（秒）
TIMEOUT = 10  # 元素等待超时（秒）

# ================= 创建保存目录 =================
os.makedirs(SAVE_DIR, exist_ok=True)

# ================= 配置无头浏览器 =================
chrome_options = Options()
# 启用无头模式（无界面运行）
chrome_options.add_argument("--headless=new")
# 规避反爬：模拟正常浏览器请求头
chrome_options.add_argument(
    "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
)
# 禁用GPU、图片加载（提升速度，可选）
chrome_options.add_argument("--disable-gpu")
chrome_options.add_experimental_option("prefs", {"profile.managed_default_content_settings.images": 2})

# 自动下载匹配的Chrome驱动
service = Service(ChromeDriverManager().install())
driver = webdriver.Chrome(service=service, options=chrome_options)

# ================= 核心爬取逻辑 =================
def crawl_bing_images():
    driver.get(TARGET_URL)
    wait = WebDriverWait(driver, TIMEOUT)
    image_urls = set()  # 去重：用集合存储图片URL

    # 1. 滚动加载更多图片（Bing图片默认懒加载）
    last_height = driver.execute_script("return document.body.scrollHeight")
    while len(image_urls) < MAX_IMAGES:
        # 滚动到页面底部
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(SCROLL_PAUSE_TIME)  # 等待加载

        # 点击"查看更多"按钮（如果存在）
        try:
            load_more_btn = wait.until(
                EC.element_to_be_clickable((By.CSS_SELECTOR, ".btn_seemore"))
            )
            driver.execute_script("arguments[0].click();", load_more_btn)
            time.sleep(SCROLL_PAUSE_TIME)
        except:
            pass  # 没有更多按钮时继续

        # 2. 提取图片URL（Bing图片的真实URL在 img 标签的 src 或 data-src 属性中）
        img_elements = driver.find_elements(By.CSS_SELECTOR, "img.mimg")
        for img in img_elements:
            img_url = img.get_attribute("src") or img.get_attribute("data-src")
            if img_url and "http" in img_url:
                image_urls.add(img_url)

        # 3. 检查是否已达最大数量或无新图片
        new_height = driver.execute_script("return document.body.scrollHeight")
        if new_height == last_height and len(image_urls) >= MAX_IMAGES:
            break
        last_height = new_height

    # 4. 下载图片并保存
    print(f"共获取到 {len(image_urls)} 张图片，开始下载...")
    success_count = 0
    for idx, img_url in enumerate(image_urls, 1):
        try:
            # 下载图片（添加超时和请求头，避免被拦截）
            response = requests.get(
                img_url,
                timeout=10,
                headers={"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"},
                stream=True
            )
            response.raise_for_status()  # 抛出HTTP错误

            # 验证图片有效性并调整尺寸（统一为224x224，适配模型训练）
            img = Image.open(BytesIO(response.content))
            img = img.resize((224, 224), Image.Resampling.LANCZOS)
            img.save(os.path.join(SAVE_DIR, f"book_cover_{idx}.jpg"), "JPEG")
            success_count += 1
            print(f"下载成功 {idx}/{len(image_urls)}: {img_url}")

        except Exception as e:
            print(f"下载失败 {idx}/{len(image_urls)}: {img_url} -> 错误：{str(e)[:50]}")
            continue

    print(f"\n爬取完成！成功下载 {success_count} 张图片，保存至 {SAVE_DIR}")

# ================= 执行爬取 =================
if __name__ == "__main__":
    try:
        crawl_bing_images()
    finally:
        driver.quit()  # 确保浏览器关闭