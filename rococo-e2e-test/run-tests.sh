#!/bin/bash

# Убиваем старые процессы если есть
pkill Xvfb 2>/dev/null
pkill chromium 2>/dev/null

# Запускаем Xvfb с правильными параметрами
Xvfb :99 -screen 0 1920x1080x24 -ac +extension GLX +render -noreset &
export DISPLAY=:99

# Ждем запуска Xvfb
sleep 5

# Проверяем что Xvfb работает
if ! pgrep Xvfb; then
    echo "ERROR: Xvfb failed to start"
    exit 1
fi

echo "Waiting for services to be ready..."
sleep 10

echo "Running tests..."

# Запускаем тесты с явными параметрами
gradle test --no-daemon \
  -Dselenide.browser=chrome \
  -Dselenide.headless=true \
  -Dselenide.browserSize=1920x1080 \
  -Dselenide.timeout=10000 \
  -Dselenide.startMaximized=false \
  -Dchromeoptions.args="--no-sandbox,--disable-dev-shm-usage,--disable-gpu,--headless=new,--disable-setuid-sandbox,--disable-software-rasterizer,--disable-features=VizDisplayCompositor,--remote-debugging-port=9222"

EXIT_CODE=$?

# Завершаем процессы
pkill Xvfb 2>/dev/null
pkill chromium 2>/dev/null

exit $EXIT_CODE