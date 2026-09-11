// ObjectBox 实体模块（P3-B3）：纯 Kotlin JVM（非 KMP），由 io.objectbox 插件
// 驱动注解处理器生成 MyObjectBox / 下划线类 / objectbox-models/default.json。
// 实体从 composeApp 迁入（git mv 语义），包名保持 com.ai.assistance.operit.data.model 不变。
plugins {
    // kotlin-jvm/kapt 不带版本：根构建已以 apply false 将 KGP 放上 classpath，继承即可
    kotlin("jvm")
    kotlin("kapt")
    id("io.objectbox") version "5.3.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation("io.objectbox:objectbox-java:5.3.0")
    implementation("io.objectbox:objectbox-kotlin:5.3.0")
    kapt("io.objectbox:objectbox-processor:5.3.0")
}
