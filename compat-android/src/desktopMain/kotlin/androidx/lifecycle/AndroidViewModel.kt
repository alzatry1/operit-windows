package androidx.lifecycle

import android.app.Application

/**
 * androidx.lifecycle.AndroidViewModel 桌面版。
 * KMP 版 lifecycle-viewmodel 不含此类（它属于 Android 专属工件），这里补齐。
 *
 * JVM 签名注意：不能用 `val application: Application` 属性——其 getter 与
 * `getApplication()` 泛型方法擦除后签名相同会冲突。故只存私有字段、只暴露方法。
 */
open class AndroidViewModel(application: Application) : ViewModel() {

    private val applicationRef: Application = application

    @Suppress("UNCHECKED_CAST")
    fun <T : Application> getApplication(): T = applicationRef as T
}
