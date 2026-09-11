package com.joaomgcd.taskerpluginlibrary.action

import android.content.Context
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult

/** taskerpluginlibrary action runner（P3-B2 新增，编译级 stub）。 */
abstract class TaskerPluginRunnerAction<TInput, TOutput> {
    abstract fun run(context: Context, input: TaskerInput<TInput>): TaskerPluginResult<TOutput>
}
