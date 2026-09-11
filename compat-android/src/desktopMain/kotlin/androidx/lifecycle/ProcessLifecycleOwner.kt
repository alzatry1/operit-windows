package androidx.lifecycle

/**
 * androidx.lifecycle.ProcessLifecycleOwner 桌面版。
 * lifecycle-process 工件无桌面版；桌面单进程模型下进程始终处于前台，
 * 故 lifecycle 直接置为 RESUMED。
 */
class ProcessLifecycleOwner private constructor() : LifecycleOwner {

    private val registry = LifecycleRegistry(this)

    init {
        registry.currentState = Lifecycle.State.RESUMED
    }

    override val lifecycle: Lifecycle
        get() = registry

    companion object {
        private val instance = ProcessLifecycleOwner()

        @JvmStatic
        fun get(): ProcessLifecycleOwner = instance
    }
}
