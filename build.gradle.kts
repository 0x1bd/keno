plugins {
    kotlin("jvm") version "2.3.20"
    `java-library`
}

group = "org.kvxd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val skikoVersion = "0.150.1"
val lwjglVersion = "3.4.3"

val hostOs = System.getProperty("os.name").let { name ->
    when {
        name.startsWith("Linux") -> "linux"
        name.startsWith("Mac") -> "macos"
        name.startsWith("Windows") -> "windows"
        else -> error("Unsupported operating system: $name")
    }
}
val hostArch = System.getProperty("os.arch").let { arch ->
    when (arch) {
        "x86_64", "amd64" -> "x64"
        "aarch64", "arm64" -> "arm64"
        else -> error("Unsupported architecture: $arch")
    }
}
val lwjglNatives = "natives-$hostOs${if (hostArch == "arm64") "-arm64" else ""}"

dependencies {
    api("org.jetbrains.skiko:skiko-awt-runtime-$hostOs-$hostArch:$skikoVersion")
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}
