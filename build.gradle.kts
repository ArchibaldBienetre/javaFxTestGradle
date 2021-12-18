plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.10"
    id("org.beryx.jlink") version "2.24.1"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

var integrationTest = sourceSets.create("integrationTest")
configurations[integrationTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[integrationTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())

val integrationTestJarTask = tasks.register<Jar>(integrationTest.jarTaskName) {
    archiveClassifier.set("integration-tests")
    from(integrationTest.output)
}

val integrationTestTask = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    useJUnitPlatform()
    jvmArgs = listOf("--add-exports", "javafx.graphics/com.sun.javafx.application=org.testfx")

    testClassesDirs = integrationTest.output.classesDirs
    // Make sure we run the 'Jar' containing the tests (and not just the 'classes' folder) so that test resources are also part of the test module
    classpath = configurations[integrationTest.runtimeClasspathConfigurationName] + files(integrationTestJarTask)

    shouldRunAfter(tasks.test)
}

tasks.check {
    dependsOn(integrationTestTask)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainModule.set("com.example.javafxtest")
    mainClass.set("com.example.javafxtest.HelloApplication")
}

val javaFxVersion = "18-ea+8"
javafx {
    version = javaFxVersion
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.openjfx:javafx-base:${javaFxVersion}:linux")
    implementation("org.openjfx:javafx-controls:${javaFxVersion}:linux")
    implementation("org.openjfx:javafx-fxml:${javaFxVersion}:linux")

//    implementation("org.openjfx:javafx-graphics:${javaFxVersion}")
    implementation("org.openjfx:javafx-graphics:${javaFxVersion}:linux")
//    implementation("org.openjfx:javafx-graphics:${javaFxVersion}:win")
//    implementation("org.openjfx:javafx-graphics:${javaFxVersion}:mac")

    var junitVersion = "5.8.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    // module required by junit, it seems
    testImplementation("org.apiguardian:apiguardian-api:1.0.0")


    var testFxVersion = "4.0.16-alpha"
    testImplementation("org.testfx:testfx-core:${testFxVersion}")
    testImplementation("org.testfx:testfx-junit5:${testFxVersion}")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("org.hamcrest:hamcrest-core:2.2")

    "integrationTestImplementation"(project)
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