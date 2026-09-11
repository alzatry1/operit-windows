package dalvik.system

import java.net.URL
import java.net.URLClassLoader

/**
 * dalvik.system.DexClassLoader 的桌面等价。
 * Android 上加载 dex；桌面无 dex，包壳 URLClassLoader（若插件要装 jar 可用 URLClassLoader 语义）。——Nova 注
 */
open class DexClassLoader(
    dexPath: String?,
    optimizedDirectory: String?,
    librarySearchPath: String?,
    parent: ClassLoader?,
) : URLClassLoader(arrayOf(), parent)
