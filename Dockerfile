# ---------- build stage ----------
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

# Copy Gradle wrapper first (cache)
COPY app/online-library/gradlew ./gradlew
COPY app/online-library/gradle ./gradle
COPY app/online-library/build.gradle ./build.gradle
COPY app/online-library/settings.gradle ./settings.gradle
RUN chmod +x ./gradlew

# Copy sources
COPY app/online-library/src ./src

# Build runnable jar
RUN ./gradlew --no-daemon clean bootJar

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre-jammy
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

RUN useradd -m -u 10001 appuser
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar /app/app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
  CMD curl -fsS http://localhost:8080/actuator/health || exit 1

USER appuser
ENTRYPOINT ["java","-jar","/app/app.jar"]
