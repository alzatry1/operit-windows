package com.joaomgcd.taskerpluginlibrary.config

import android.content.Context
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput

/**
 * taskerpluginlibrary config 体系（P3-B2 新增，编译级 stub）。
 * 桌面无 Tasker 宿主：helper 的 onCreate/finishForTasker 记日志并直接结束。
 */

interface TaskerPluginConfig<TInput> {
    val context: Context
    val inputForTasker: TaskerInput<TInput>
    fun assignFromInput(input: TaskerInput<TInput>)
}

/** 无输入的 Tasker 插件配置。 */
interface TaskerPluginConfigNoInput : TaskerPluginConfig<Unit> {
    override val inputForTasker: TaskerInput<Unit>
        get() = TaskerInput(Unit)

    override fun assignFromInput(input: TaskerInput<Unit>) {}
}

/** TaskerPluginConfigHelper（有输入有输出）。 */
open class TaskerPluginConfigHelper<TInput, TOutput, TRunner>(
    val config: TaskerPluginConfig<TInput>,
) {
    open val inputClass: Class<TInput>
        get() = throw NotImplementedError("TaskerPluginConfigHelper.inputClass not implemented")

    open val outputClass: Class<TOutput>
        get() = throw NotImplementedError("TaskerPluginConfigHelper.outputClass not implemented")

    open val runnerClass: Class<TRunner>
        get() = throw NotImplementedError("TaskerPluginConfigHelper.runnerClass not implemented")

    open fun addToStringBlurb(input: TaskerInput<TInput>, blurbBuilder: StringBuilder) {}

    open fun onCreate() {}

    fun finishForTasker() {}
}

/** TaskerPluginConfigHelperConditionNoInput（条件型，无常规输入）。 */
open class TaskerPluginConfigHelperConditionNoInput<TOutput, TUpdate, TRunner>(
    val config: TaskerPluginConfig<Unit>,
) {
    open val outputClass: Class<TOutput>
        get() = throw NotImplementedError("TaskerPluginConfigHelperConditionNoInput.outputClass not implemented")

    open val runnerClass: Class<TRunner>
        get() = throw NotImplementedError("TaskerPluginConfigHelperConditionNoInput.runnerClass not implemented")

    open fun addToStringBlurb(input: TaskerInput<Unit>, blurbBuilder: StringBuilder) {}

    open fun onCreate() {}

    fun finishForTasker() {}
}
