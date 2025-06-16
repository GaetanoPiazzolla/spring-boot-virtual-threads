ARG JAVA_VERSION
ARG SPRING_BOOT_VERSION
ARG SPRING_DEPENDENCY_MANAGEMENT_VERSION
ARG DATASOURCE_TYPE

FROM eclipse-temurin:${JAVA_VERSION}-jdk

ARG JAVA_VERSION
ARG SPRING_BOOT_VERSION
ARG SPRING_DEPENDENCY_MANAGEMENT_VERSION
ARG DATASOURCE_TYPE

WORKDIR /app

COPY gradle gradle
COPY gradlew template.gradle.kts settings.gradle.kts ./

RUN echo "Replacing versions: Java=${JAVA_VERSION}, Spring Boot=${SPRING_BOOT_VERSION}, Spring DM=${SPRING_DEPENDENCY_MANAGEMENT_VERSION}" && \
    sed -i "s|__JAVA_VERSION__|${JAVA_VERSION}|g" template.gradle.kts && \
    sed -i "s|__SPRING_BOOT_VERSION__|${SPRING_BOOT_VERSION}|g" template.gradle.kts && \
    sed -i "s|__SPRING_DEPENDENCY_MANAGEMENT_VERSION__|${SPRING_DEPENDENCY_MANAGEMENT_VERSION}|g" template.gradle.kts && \
    mv template.gradle.kts build.gradle.kts

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon || true

COPY src src

RUN ./gradlew clean build --no-daemon

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java --enable-preview -Duser.timezone=GMT+1 -jar build/libs/spring-boot-virtual-threads-0.0.1-SNAPSHOT.jar"]