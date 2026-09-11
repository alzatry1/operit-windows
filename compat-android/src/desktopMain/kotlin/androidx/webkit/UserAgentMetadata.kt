package androidx.webkit

/**
 * androidx.webkit.UserAgentMetadata 桌面版。
 * Builder 链式构造与真实 API 一致；桌面无 WebView 消费方，值仅保存。
 */
class UserAgentMetadata private constructor(
    val brandVersionList: List<BrandVersion>?,
    val fullVersion: String?,
    val platform: String?,
    val platformVersion: String?,
    val architecture: String?,
    val model: String?,
    val isMobile: Boolean,
    val bitness: Int,
    val isWow64: Boolean,
) {

    class BrandVersion private constructor(
        val brand: String?,
        val majorVersion: String?,
        val fullVersion: String?,
    ) {
        class Builder {
            private var brand: String? = null
            private var majorVersion: String? = null
            private var fullVersion: String? = null

            fun setBrand(brand: String?): Builder = apply { this.brand = brand }
            fun setMajorVersion(majorVersion: String?): Builder = apply { this.majorVersion = majorVersion }
            fun setFullVersion(fullVersion: String?): Builder = apply { this.fullVersion = fullVersion }

            fun build(): BrandVersion = BrandVersion(brand, majorVersion, fullVersion)
        }
    }

    class Builder {
        private var brandVersionList: List<BrandVersion>? = null
        private var fullVersion: String? = null
        private var platform: String? = null
        private var platformVersion: String? = null
        private var architecture: String? = null
        private var model: String? = null
        private var mobile: Boolean = true
        private var bitness: Int = 0
        private var wow64: Boolean = false

        fun setBrandVersionList(brandVersionList: List<BrandVersion>?): Builder =
            apply { this.brandVersionList = brandVersionList }

        fun setFullVersion(fullVersion: String?): Builder = apply { this.fullVersion = fullVersion }
        fun setPlatform(platform: String?): Builder = apply { this.platform = platform }
        fun setPlatformVersion(platformVersion: String?): Builder = apply { this.platformVersion = platformVersion }
        fun setArchitecture(architecture: String?): Builder = apply { this.architecture = architecture }
        fun setModel(model: String?): Builder = apply { this.model = model }
        fun setMobile(mobile: Boolean): Builder = apply { this.mobile = mobile }
        fun setBitness(bitness: Int): Builder = apply { this.bitness = bitness }
        fun setWow64(wow64: Boolean): Builder = apply { this.wow64 = wow64 }

        fun build(): UserAgentMetadata = UserAgentMetadata(
            brandVersionList, fullVersion, platform, platformVersion,
            architecture, model, mobile, bitness, wow64,
        )
    }
}
