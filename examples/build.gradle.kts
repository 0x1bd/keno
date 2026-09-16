plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":"))
}

kotlin {
    jvmToolchain(25)
}

application {
    mainClass = "org.kvxd.keno.examples.MainKt"
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}
