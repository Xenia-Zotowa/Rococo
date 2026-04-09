#!/bin/bash
set -e

# Запуск Xvfb с правильными параметрами
Xvfb :99 -screen 0 1920x1080x24 -ac +extension GLX +render -noreset &
export DISPLAY=:99

# Ждем запуска Xvfb
sleep 3

# Запуск тестов
exec gradle test --no-daemon