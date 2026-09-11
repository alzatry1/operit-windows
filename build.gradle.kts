plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
}

// 全局强制 Java 编译用 UTF-8：Windows CI runner 默认 windows-1252，源码含中文会编不过。——Nova 注
allprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
}
