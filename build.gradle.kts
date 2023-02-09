import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.0"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.21"
	kotlin("plugin.spring") version "1.7.21"
	kotlin("plugin.jpa") version "1.7.21"
	kotlin("plugin.allopen") version "1.7.21"


	id("org.hibernate.orm") version "6.1.6.Final"

}
allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.Embeddable")
	annotation("jakarta.persistence.MappedSuperclass")
}



group = "de.rehatech"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
	repositories {
		mavenCentral()
		maven{
			url = uri("https://jitpack.io")
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
	// https://mvnrepository.com/artifact/com.google.code.gson/gson
	implementation("com.google.code.gson:gson:2.10")
	annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
	implementation("com.squareup.okhttp3:okhttp:4.10.0")
	implementation("org.javassist:javassist:3.29.2-GA")

	implementation("javax.xml.bind:jaxb-api:2.3.1")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Füge eigene Dritt Library hinzu. Weitere Infos unter https://developerlife.com/2021/02/06/publish-kotlin-library-as-gradle-dep/
	implementation("com.github.Rehatech22-23:smartHomeSharedLibrary:0.2.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
