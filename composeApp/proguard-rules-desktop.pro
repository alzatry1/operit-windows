# ==========================================================================
# Operit Windows 桌面 release ProGuard 规则 —— Nova 注
# 打包时 ProGuard 对工程里带的 Android 库（okhttp3/logback/ftpserver/pdfbox/
# commons-compress 等）引用的【可选依赖类】报 12686 个未解析引用警告，
# 且 ProGuard 默认警告即错误导致 proguardReleaseJars 失败。
# 这些可选集成类（servlet/conscrypt/asm/osgi/spring/batik/maven/ant/pdfbox/
# javaparser）在桌面运行时根本不会被用到，安全 dontwarn。
# ==========================================================================

# --- servlet / web 可选集成（logback、commons-logging 引用）---
-dontwarn jakarta.servlet.**
-dontwarn javax.servlet.**
-dontwarn jakarta.mail.**

# --- okhttp3 的可选 TLS 平台（conscrypt/openjsse）---
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**
-dontwarn org.bouncycastle.**

# --- commons-compress pack200 用的 ASM ---
-dontwarn org.objectweb.asm.**

# --- ftpserver 的 spring 集成 ---
-dontwarn org.springframework.**

# --- OSGi 集成 ---
-dontwarn org.osgi.**

# --- pdfbox 的字体/图形可选集成 ---
-dontwarn de.rototor.pdfbox.**
-dontwarn org.apache.batik.**

# --- 构建期工具（maven/ant）不应进运行时包 ---
-dontwarn org.apache.maven.**
-dontwarn org.apache.tools.ant.**

# --- javaparser 的可选策略 ---
-dontwarn com.github.javaparser.**

# --- 保险：Compose/Desktop 与 Android 资源元数据的重复类（Note 级，不算错）---
-dontwarn kotlin.**
-dontwarn kotlinx.**

# 保留主类入口
-keep class com.ai.assistance.operit.MainKt { *; }

# Compose runtime 需要保留（反射/重组）
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
