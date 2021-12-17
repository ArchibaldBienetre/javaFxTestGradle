plugins {
    id("java")
    id("application")
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("org.openjfx.javafxplugin") version "0.0.10"
    id("org.beryx.jlink") version "2.24.1"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainModule.set("com.example.javafxtest")
    mainClass.set("com.example.javafxtest.HelloApplication")
}

javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    var javaFxVersion = "11.0.2"
    implementation("org.openjfx:javafx-graphics:${javaFxVersion}:win")
    implementation("org.openjfx:javafx-graphics:${javaFxVersion}:linux")
    implementation("org.openjfx:javafx-graphics:${javaFxVersion}:mac")

    var junitVersion = "5.8.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.test {
    useJUnitPlatform()
}

jlink {
    imageZip.set(project.file("${buildDir}/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}

tasks.jlinkZip {
    group = "distribution"
}