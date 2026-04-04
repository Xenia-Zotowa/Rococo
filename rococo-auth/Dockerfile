# Stage 1: Build
FROM gradle:9.0-jdk21-alpine AS build
WORKDIR /build

RUN mkdir -p rococo-auth/src/main/java/io/student/rococo
RUN mkdir -p rococo-auth/src/main/resources

RUN echo "rootProject.name = 'rococo'" > settings.gradle
RUN echo "include 'rococo-auth'" >> settings.gradle

COPY root-build.gradle ./build.gradle
COPY build.gradle ./rococo-auth/
COPY src ./rococo-auth/src

RUN gradle bootJar -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /build/rococo-auth/build/libs/*.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]