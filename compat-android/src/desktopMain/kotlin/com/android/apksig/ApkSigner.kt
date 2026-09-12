package com.android.apksig

import java.io.File
import java.security.PrivateKey
import java.security.cert.X509Certificate

/**
 * com.android.apksig.ApkSigner 桌面 stub。
 * apksig 是 Android APK 签名库（Android-only），未进 desktop 依赖。
 * 桌面提供 API 形状供编译；sign() 空操作（桌面不做 APK 签名）。——Nova 注
 */
class ApkSigner private constructor(private val signerConfigs: List<SignerConfig>) {

    fun sign() {
        // 桌面空操作：APK 签名是 Android 构建期工具，桌面端不执行。
    }

    class Builder(private val signerConfigs: List<SignerConfig>) {
        private var inputApk: File? = null
        private var outputApk: File? = null
        private var minSdkVersion: Int = 1

        fun setInputApk(file: File): Builder = apply { inputApk = file }
        fun setOutputApk(file: File): Builder = apply { outputApk = file }
        fun setMinSdkVersion(version: Int): Builder = apply { minSdkVersion = version }
        fun build(): ApkSigner = ApkSigner(signerConfigs)
    }

    class SignerConfig private constructor(
        val keyAlias: String,
        val privateKey: PrivateKey,
        val certificates: List<X509Certificate>,
    ) {
        class Builder(
            private val keyAlias: String,
            private val privateKey: PrivateKey,
            private val certificates: List<X509Certificate>,
        ) {
            fun build(): SignerConfig = SignerConfig(keyAlias, privateKey, certificates)
        }
    }
}
