FROM gradle:9.0-jdk21-jammy

# Установка Chrome
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    xvfb \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

ENV DISPLAY=:99

WORKDIR /app

COPY build.gradle .
COPY settings.gradle* .
COPY src ./src

CMD sh -c "Xvfb :99 -screen 0 1920x1080x24 -ac +extension GLX +render -noreset & \
           sleep 3 && \
           gradle test --no-daemon"