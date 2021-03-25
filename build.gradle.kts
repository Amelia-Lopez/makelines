import org.gradle.api.JavaVersion.VERSION_11
import org.gradle.jvm.tasks.Jar
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    `java-library`
    groovy
    application                                            // includes java plugin, easier to execute during testing
    jacoco                                                 // code coverage
    id("com.github.kt3k.coveralls") version "2.8.1"        // coveralls.io
    id("com.github.johnrengelman.shadow") version "5.2.0"  // creates an executable fat JAR (includes dependencies)
    id("edu.sc.seis.macAppBundle") version "2.3.0"         // creates an OS X bundle, will break in Gradle 7
    id("org.openjfx.javafxplugin") version "0.0.9"         // adds support for audio
}

allprojects {
    group = "amylopez"
    version = "0.9.0-SNAPSHOT"
}

java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
}

val jar: Jar by tasks
jar.apply {
    manifest.attributes.apply {
        put("Implementation-Title", "Make Lines")
        put("Implementation-Version", archiveVersion)
        put("Built-By", System.getProperty("user.name"))
        put("Build-Jdk", org.gradle.internal.jvm.Jvm.current())
        put("Created-By", "Gradle ${project.gradle.gradleVersion}")
    }
}

repositories {
    mavenCentral()
}

val mainClassNameProp = "amylopez.makelines.MakelinesApp"

application {
    mainClassName = mainClassNameProp // will break in Gradle 8
}

macAppBundle {
    mainClassName = mainClassNameProp
    icon = "build-resources/osx/icons/icon_256.icns"
}

jacoco {
    toolVersion = "0.8.6"
}

(getTasksByName("jacocoTestReport", false).first() as JacocoReport).apply {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

javafx {
    version = "15.0.1"
    modules("javafx.media", "javafx.swing")
}

dependencies {
    implementation("com.google.inject:guice:3.0")
    implementation("commons-configuration:commons-configuration:1.9")
    implementation("commons-beanutils:commons-beanutils:1.8.3")
    implementation("commons-jxpath:commons-jxpath:1.3")
    implementation("ch.qos.logback:logback-core:1.0.13")
    implementation("ch.qos.logback:logback-classic:1.0.13")
    implementation("org.slf4j:slf4j-api:1.7.5")

    testImplementation("junit:junit:4.12")
    testImplementation("org.codehaus.groovy:groovy-all:2.4.9")
    testImplementation("org.spockframework:spock-core:1.1-groovy-2.4-rc-3")
}
