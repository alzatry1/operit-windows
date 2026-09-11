package androidx.compose.ui.platform

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * androidx.compose.ui.platform 的 ComposeView 体系桌面垫片。
 * 桌面无法把 Compose 渲染进 Android View 树：setContent 仅记录 composable 到字段，
 * Content() 被调用时才执行（正常桌面流程不会触发）。仅供编译兼容。
 */

/** androidx.compose.ui.platform.ViewCompositionStrategy。 */
fun interface ViewCompositionStrategy {

    fun installFor(view: AbstractComposeView): () -> Unit

    companion object {
        val DisposeOnDetachedFromWindow: ViewCompositionStrategy = NoopStrategy("DisposeOnDetachedFromWindow")

        val DisposeOnDetachedFromWindowOrReleasedFromPool: ViewCompositionStrategy =
            NoopStrategy("DisposeOnDetachedFromWindowOrReleasedFromPool")

        val DisposeOnViewTreeLifecycleDestroyed: ViewCompositionStrategy =
            NoopStrategy("DisposeOnViewTreeLifecycleDestroyed")

        val Default: ViewCompositionStrategy = DisposeOnDetachedFromWindowOrReleasedFromPool

        fun DisposeOnLifecycleDestroyed(lifecycleOwner: LifecycleOwner): ViewCompositionStrategy =
            NoopStrategy("DisposeOnLifecycleDestroyed(owner)")

        fun DisposeOnLifecycleDestroyed(lifecycle: Lifecycle): ViewCompositionStrategy =
            NoopStrategy("DisposeOnLifecycleDestroyed(lifecycle)")
    }
}

private class NoopStrategy(private val name: String) : ViewCompositionStrategy {
    override fun installFor(view: AbstractComposeView): () -> Unit {
        Log.d("ViewCompositionStrategy", "$name.installFor() 桌面无组合宿主，no-op")
        return {}
    }
}

/** androidx.compose.ui.platform.AbstractComposeView。 */
abstract class AbstractComposeView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /** setContent 记录的 composable（命名避开 JVM 签名冲突：属性 setter 与 setContent fun 不同名）。 */
    protected var contentBlock: (@Composable () -> Unit)? = null
        private set

    private var compositionStrategy: ViewCompositionStrategy = ViewCompositionStrategy.Default
    private var disposeHook: (() -> Unit)? = null
    private var bgColor: Int = 0

    val hasContent: Boolean
        get() = contentBlock != null

    fun setContent(content: @Composable () -> Unit) {
        contentBlock = content
    }

    fun setViewCompositionStrategy(strategy: ViewCompositionStrategy) {
        compositionStrategy = strategy
        disposeHook = strategy.installFor(this)
    }

    fun disposeComposition() {
        disposeHook?.invoke()
        disposeHook = null
        contentBlock = null
    }

    override fun setBackgroundColor(color: Int) {
        bgColor = color
    }

    @Composable
    protected abstract fun Content()
}

/** androidx.compose.ui.platform.ComposeView。 */
class ComposeView : AbstractComposeView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Composable
    override fun Content() {
        contentBlock?.invoke()
    }
}
