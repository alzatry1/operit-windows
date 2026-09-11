package com.joaomgcd.taskerpluginlibrary.output

/**
 * taskerpluginlibrary output（P3-B2 新增，编译级 stub）。
 */

/** 标记 Tasker 输出 POJO。 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaskerOutputObject

/** 标记 Tasker 输出变量（@get: 使用点）。 */
@Target(AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaskerOutputVariable(val value: String)
