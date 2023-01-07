FROM arm64v8/eclipse-temurin:19-jdk-focal as builder

##
#  First Stage
##
WORKDIR /src
COPY src ./src/
COPY gradle ./gradle/
COPY build.gradle.kts ./
COPY gradlew ./
COPY settings.gradle.kts ./
RUN chmod 777 ./gradlew
RUN ./gradlew clean build

##
#  Second Stage
##
FROM arm64v8/eclipse-temurin:19-jre
EXPOSE 8080
COPY --from=builder /src/build/libs/spring-boot-app*  /app/spring-boot.jar
ENTRYPOINT ["java","-Duser.timezone=GMT+1","-jar","/app/spring-boot.jar"]