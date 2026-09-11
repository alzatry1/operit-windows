package androidx.compose.material3

import android.content.Context
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

/**
 * Android 12+ 动态取色（Material You）的桌面垫片。
 * 桌面无系统壁纸取色，返回一组固定配色。——Nova 注
 */
fun dynamicLightColorScheme(context: Context) = lightColorScheme()

fun dynamicDarkColorScheme(context: Context) = darkColorScheme()
