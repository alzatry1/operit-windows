package com.joaomgcd.taskerpluginlibrary.condition

import android.content.Context
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultCondition

/** taskerpluginlibrary condition runner（无常规输入，带 update）（P3-B2 新增，编译级 stub）。 */
abstract class TaskerPluginRunnerConditionNoInput<TOutput, TUpdate> {

    open val isEvent: Boolean get() = false

    abstract fun getSatisfiedCondition(
        context: Context,
        input: TaskerInput<Unit>,
        update: TUpdate?,
    ): TaskerPluginResultCondition<TOutput>
}
