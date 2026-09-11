package com.joaomgcd.taskerpluginlibrary.input

/**
 * taskerpluginlibrary input（P3-B2 新增，编译级 stub）。
 */

/** 标记 Tasker 输入 POJO 根类。 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaskerInputRoot

/** 标记 Tasker 输入字段（键名）。 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaskerInputField(val key: String)

/** Tasker 输入包装：regular 为常规输入。 */
class TaskerInput<TInput>(val regular: TInput) {
    val dynamic: Any? = null
}
