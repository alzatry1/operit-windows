package com.ai.assistance.operit.data.db

import io.objectbox.BoxStoreBuilder

/**
 * 桌面兼容垫片（P3-B3）：Android 版 BoxStoreBuilder.androidContext(Context) 在桌面
 * BoxStoreBuilder 上不存在。此处提供同包扩展（对 ObjectBox.kt 调用点自动可见、零修改），
 * 语义为 no-op——桌面 BoxStore 不需要 Context。
 */
fun BoxStoreBuilder.androidContext(context: Any?): BoxStoreBuilder = this
