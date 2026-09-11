import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.navigation.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.uuid)
        }
        desktopMain.dependencies {
            implementation(project(":compat-android"))
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.datastore.preferences)
            implementation(libs.room.runtime)
            implementation(libs.kotlin.reflect)
            implementation(libs.sqlite.bundled)
            implementation(libs.collection)
            implementation(libs.annotation)
            implementation(libs.savedstate)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.cio)
            implementation(libs.okhttp)
            implementation(libs.okhttp.sse)
            implementation(libs.gson)
            implementation(libs.hjson)
            implementation(libs.jsoup)
            implementation(libs.zip4j)
            implementation(libs.commons.compress)
            implementation(libs.commons.io)
            implementation(libs.nanohttpd)
            implementation(libs.java.diff.utils)
            implementation(libs.zxing.core)
            implementation(libs.zxing.javase)
            implementation(libs.pdfbox)
            implementation(libs.junrar)
            implementation(libs.slf4j.api)
            implementation(libs.logback)
            implementation(libs.json.jvm)
            implementation(libs.itextpdf)
            implementation(libs.kxml2)
            implementation(libs.jlatexmath)
            implementation(libs.objectbox.java)
            implementation(libs.objectbox.kotlin)
            implementation(libs.hnswlib.core)
            implementation(libs.jieba)  // 中文分词（TextSegmenter 用）
            implementation(libs.reorderable)
            implementation(libs.swipe)
            implementation(project(":objectbox-models"))
            implementation("io.objectbox:objectbox-windows:5.3.0")
            implementation(libs.onnxruntime)
            implementation(libs.poi)
            implementation(libs.poi.ooxml)
            implementation(libs.poi.scratchpad)  // HWPFDocument(.doc) 在这个里
            implementation(libs.mcp.sdk.client)
            // B3 补齐：terminal/subpack 依赖（与 android-src 坐标对齐）
            implementation("com.jcraft:jsch:0.1.55")
            implementation("org.apache.ftpserver:ftpserver-core:1.2.0")
            implementation("org.apache.ftpserver:ftplet-api:1.2.0")
            implementation("org.apache.sshd:sshd-core:2.10.0")
            implementation("org.apache.sshd:sshd-sftp:2.10.0")
            implementation("org.bouncycastle:bcprov-jdk18on:1.78")
            implementation("com.github.Sable:axml:2.0.0")
            // B4：Coil2→Coil3 迁移（KMP 桌面图像加载）
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.okhttp)
            implementation(libs.coil3.kt)
        }
    }
}

// 迁移期分包编译：./gradlew :composeApp:compileKotlinDesktop -PportScope=util,plugins
// 只编译指定包；-PportExclude=ui 排除指定包。不带属性 = 全量。
val portScope = (project.findProperty("portScope") as String?)
    ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
val portExclude = (project.findProperty("portExclude") as String?)
    ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
if (portScope != null) {
    kotlin.sourceSets.getByName("desktopMain").kotlin.setIncludes(
        portScope.map { "com/ai/assistance/operit/$it/**" } + "com/ai/assistance/operit/Main.kt"
    )
} else if (portExclude != null) {
    kotlin.sourceSets.getByName("desktopMain").kotlin.setExcludes(
        portExclude.map { "com/ai/assistance/operit/$it/**" }
    )
}

compose.desktop {
    application {
        mainClass = "com.ai.assistance.operit.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Exe)
            packageName = "OperitAI"
            packageVersion = "1.0.0"
            vendor = "Operit"
            copyright = "© 2026 Operit"

            windows {
                menu = true
                upgradeUuid = "7c9e2a54-3f1b-4d8e-9a6c-2b5f0e1d8c3a"
                // iconFile.set(project.file("icon.ico")) // TODO: 迁移应用图标
            }
        }
    }
}
