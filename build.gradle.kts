plugins {
    java
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

group = "gae.piaz"
version = "0.0.1-SNAPSHOT"
description = "spring-boot-virtual-threads"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
        exclude(group = "com.zaxxer", module = "HikariCP")
    }
    implementation("io.agroal:agroal-spring-boot-starter:2.7.1")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("org.postgresql:postgresql")
}

tasks {
    val preview = "--enable-preview"
    withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        jvmArgs = mutableListOf(preview)
    }
    withType<JavaExec> {
        jvmArgs = mutableListOf(preview)
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add(preview)
        options.compilerArgs.add("-Xlint:preview")
    }
}