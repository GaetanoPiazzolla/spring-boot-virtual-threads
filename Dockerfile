# -------- First Stage: Build --------
FROM eclipse-temurin:19-jdk-focal AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon || true

COPY src src

RUN ./gradlew clean build --no-daemon

# -------- Second Stage: Runtime --------
FROM eclipse-temurin:19-jre

WORKDIR /app

EXPOSE 8080

COPY --from=builder /app/build/libs/*.jar spring-boot.jar

ENTRYPOINT ["java", "--enable-preview", "-Duser.timezone=GMT+1", "-jar", "spring-boot.jar"]
