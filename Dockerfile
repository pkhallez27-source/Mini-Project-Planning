# 1. Official Gradle 8.5 + Java 17 빌드 환경
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle build -x test --no-daemon --stacktrace
RUN rm -f build/libs/*-plain.jar

# 2. 실행 환경 및 서버 가동
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
