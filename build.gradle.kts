plugins {
    java
    id("org.springframework.boot") version "3.0.1"
}
apply(plugin = "io.spring.dependency-management")

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.spring.io/milestone")
    }
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("org.postgresql:postgresql")
}

group = "gae.piaz"
version = "0.0.2-SNAPSHOT"
description = "spring-boot-app"

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