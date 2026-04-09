#!/bin/bash

# Убиваем старые процессы
pkill Xvfb 2>/dev/null
pkill firefox 2>/dev/null

# Запускаем Xvfb
Xvfb :99 -screen 0 1920x1080x24 -ac &
export DISPLAY=:99

# Ждем запуска Xvfb
sleep 5

echo "Waiting for services to be ready..."
sleep 10

echo "Running tests with Firefox..."

# Запускаем тесты с Firefox
gradle test --no-daemon \
  -Dselenide.browser=firefox \
  -Dselenide.headless=true \
  -Dselenide.browserSize=1920x1080 \
  -Dselenide.timeout=10000

EXIT_CODE=$?

# Завершаем процессы
pkill Xvfb 2>/dev/null
pkill firefox 2>/dev/null

echo "Tests completed with exit code: $EXIT_CODE"
exit $EXIT_CODE