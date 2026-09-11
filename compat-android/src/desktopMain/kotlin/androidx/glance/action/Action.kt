package androidx.glance.action

import androidx.glance.GlanceModifier

/**
 * androidx.glance.action 桌面 stub。
 * Action 必须是普通接口（非 fun interface）——否则 `clickable { }` 的 lambda
 * 会与 clickable(Action) 构成 SAM 歧义。真实 glance 的 Action 同样是普通接口。
 */
interface Action

/** 由 lambda 构造的回调 Action（对应真实实现的 RunCallbackAction）。 */
internal class RunCallbackAction(private val block: suspend () -> Unit) : Action {
    suspend fun run() = block()
}

/** androidx.glance.action.clickable：接收 Action（stub，丢弃并返回自身）。 */
fun GlanceModifier.clickable(onClick: Action): GlanceModifier = this

/** androidx.glance.action.clickable：lambda 版，包装成 RunCallbackAction。 */
fun GlanceModifier.clickable(block: suspend () -> Unit): GlanceModifier =
    clickable(RunCallbackAction(block))
