package com.ai.assistance.operit.ui.features.chat.util

import coil3.ImageLoader

/**
 * Desktop no-op shim：coil3 的 `ImageLoader.Builder.allowHardware` 是 Android-only API
 * （控制 BitmapFactory 硬件位图），desktop/JVM 上没有。截图渲染在桌面本就只有软件位图，
 * 所以这里恒等返回。与 MessageImageGenerator 同包，免 import 自动解析。 ——Nova 注
 */
fun ImageLoader.Builder.allowHardware(enable: Boolean): ImageLoader.Builder = this
