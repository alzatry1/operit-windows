package androidx.work

/**
 * androidx.work.BackoffPolicy 顶层枚举（真身是顶层，不是 WorkRequest 嵌套）。——Nova 注
 */
enum class BackoffPolicy { EXPONENTIAL, LINEAR }
