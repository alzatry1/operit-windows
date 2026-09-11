package com.joaomgcd.taskerpluginlibrary.runner

import android.content.Context

/**
 * taskerpluginlibrary runner 结果体系（P3-B2 新增，编译级 stub）。
 */

open class TaskerPluginResult<TOutput>

/** 成功结果（注意真实库的类名拼写即为 Sucess）。 */
class TaskerPluginResultSucess<TOutput> : TaskerPluginResult<TOutput> {
    constructor()
    constructor(output: TOutput?)
    constructor(context: Context?, output: TOutput?)
}

/** 错误结果。 */
class TaskerPluginResultError<TOutput> : TaskerPluginResult<TOutput> {
    constructor(exception: Throwable?)
    constructor(errorCode: Int, message: String?)
}

/** 条件结果基类。 */
open class TaskerPluginResultCondition<TOutput> : TaskerPluginResult<TOutput>()

/** 条件满足。 */
class TaskerPluginResultConditionSatisfied<TOutput> : TaskerPluginResultCondition<TOutput> {
    constructor()
    constructor(output: TOutput?)
    constructor(context: Context?, output: TOutput?)
}

/** 条件不满足。 */
class TaskerPluginResultConditionUnsatisfied<TOutput> : TaskerPluginResultCondition<TOutput>()
