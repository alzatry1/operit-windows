package coil3.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import coil3.ImageLoader
import coil3.PlatformContext

/**
 * coil3.compose.LocalImageLoader 桌面 shim（B9o）。
 *
 * coil-compose 的桌面 jvm 变体（3.2.0）没带这个 CompositionLocal，app 用它做
 * `LocalImageLoader provides imageLoader`。按真实语义实现：默认提供一个
 * 基于 PlatformContext 的 ImageLoader 单例。——Nova 注
 */
private val defaultLocalImageLoader: ImageLoader by lazy {
    ImageLoader.Builder(PlatformContext.INSTANCE).build()
}

val LocalImageLoader: ProvidableCompositionLocal<ImageLoader> = compositionLocalOf {
    defaultLocalImageLoader
}
