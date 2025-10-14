plugins {
	java
	id("org.springframework.boot") version "3.5.6" apply(false)
	id("io.spring.dependency-management") version "1.1.7"
}

group = "org"
version = "0.0.0"
description = "mmmq"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}


dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.rest-assured:rest-assured")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
