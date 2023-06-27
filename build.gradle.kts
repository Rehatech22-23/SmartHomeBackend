import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.8"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
	kotlin("plugin.allopen") version "1.8.22"


	id("org.hibernate.orm") version "6.1.6.Final"

}
allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.Embeddable")
	annotation("jakarta.persistence.MappedSuperclass")
}



group = "de.rehatech"
version = "1.0.0"
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
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
	// https://mvnrepository.com/artifact/com.google.code.gson/gson
	implementation("com.google.code.gson:gson:2.10.1")
	annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
	implementation("com.squareup.okhttp3:okhttp:4.11.0")
	implementation("org.javassist:javassist:3.29.2-GA")

	implementation("javax.xml.bind:jaxb-api:2.3.1")
	implementation("org.postgresql:postgresql:42.5.4")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation ("org.mockito:mockito-inline:5.2.0")

	// Füge eigene Dritt Library hinzu. Weitere Infos unter https://developerlife.com/2021/02/06/publish-kotlin-library-as-gradle-dep/
	implementation("com.github.Rehatech22-23:smartHomeSharedLibrary:1.0")
	implementation("com.github.Rehatech22-23:homeekt:1.0")

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
