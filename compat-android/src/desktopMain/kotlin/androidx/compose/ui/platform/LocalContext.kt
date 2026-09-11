package androidx.compose.ui.platform

import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.ai.assistance.operit.compat.AppGlobals

/**
 * androidx.compose.ui.platform 的 CompositionLocal 桌面垫片。
 * CMP Desktop 只提供 LocalDensity/LocalUriHandler/LocalViewConfiguration
 *（已核实 ui-desktop-1.9.0.jar 的 CompositionLocalsKt），
 * Android 专属的 LocalContext/LocalView/LocalConfiguration 缺失，这里补齐。
 * 与官方 jar 同包共存（split-package），编译期无冲突。
 */

/** LocalContext：默认值为全局 applicationContext。 */
val LocalContext: ProvidableCompositionLocal<Context> =
    staticCompositionLocalOf { AppGlobals.applicationContext }

/** LocalView：默认值为全局 View 单例（桌面无真实 View 树）。 */
val LocalView: ProvidableCompositionLocal<View> =
    staticCompositionLocalOf { AppGlobals.rootView }

/** LocalConfiguration：默认值为全新 Configuration（1280x720 等桌面默认值）。 */
val LocalConfiguration: ProvidableCompositionLocal<Configuration> =
    staticCompositionLocalOf { Configuration() }
