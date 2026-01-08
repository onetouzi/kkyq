@echo off
setlocal enabledelayedexpansion

set /p dbname=请输入数据库名称:
set sqlpath=.\sql

:: 数据库配置
set dbuser=root
set dbpass=123456

echo 正在连接数据库: %dbname%...

if not exist %sqlpath% (
    echo 错误: 文件夹 %sqlpath% 不存在。
    pause
    exit
)

for /r %sqlpath% %%f in (*.sql) do (
    echo 正在执行: %%~nxf ...
    mysql -u%dbuser% -p%dbpass% %dbname% < "%%f"

    if !errorlevel! equ 0 (
        echo 成功: %%~nxf
    ) else (
        echo 失败: %%~nxf，请检查语法。
        pause
        exit
    )
)

echo ================================
echo 所有脚本运行成功！
pause