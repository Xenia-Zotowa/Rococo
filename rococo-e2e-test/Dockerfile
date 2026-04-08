# Stage 1: Build
FROM gradle:9.0-jdk21-alpine AS build
WORKDIR /build

# Копируем файлы Gradle
COPY build.gradle .
COPY settings.gradle* ./
COPY src ./src

# Собираем тесты
RUN gradle testClasses --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

# Устанавливаем Chrome, зависимости и Gradle
RUN apk add --no-cache \
    chromium \
    chromium-chromedriver \
    mesa-gl \
    dbus \
    ttf-freefont \
    xvfb \
    bash \
    curl \
    gradle

# Устанавливаем переменные окружения
ENV CHROME_BIN=/usr/bin/chromium-browser \
    CHROME_PATH=/usr/lib/chromium/ \
    SELENIDE_HEADLESS=true \
    DISPLAY=:99

WORKDIR /app

# Копируем исходники и билд
COPY --from=build /build /app

# Копируем скрипт запуска
COPY run-tests.sh /app/
RUN chmod +x /app/run-tests.sh

CMD ["./run-tests.sh"]