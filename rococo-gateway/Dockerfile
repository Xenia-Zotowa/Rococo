# Stage 1: Build
FROM gradle:9.0-jdk21-alpine AS build
WORKDIR /build

RUN mkdir -p rococo-gateway/src/main/java/io/student/rococo
RUN mkdir -p rococo-gateway/src/main/resources

RUN echo "rootProject.name = 'rococo'" > settings.gradle
RUN echo "include 'rococo-gateway'" >> settings.gradle

COPY root-build.gradle ./build.gradle
COPY build.gradle ./rococo-gateway/
COPY src ./rococo-gateway/src

RUN gradle bootJar -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /build/rococo-gateway/build/libs/*.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]