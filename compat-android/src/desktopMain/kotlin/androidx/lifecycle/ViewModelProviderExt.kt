package androidx.lifecycle

import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

/**
 * ViewModelProvider.Factory 的 create(Class) 桥接扩展。
 * KMP 版只有 create(KClass, CreationExtras)，app 里有直接传 Class 的调用点
 * （如 MCPViewModel.Factory(...).create(MCPViewModel::class.java)），此扩展兼容。——Nova 注
 */
fun <T : ViewModel> ViewModelProvider.Factory.create(modelClass: Class<T>): T =
    create(modelClass.kotlin, CreationExtras.Empty)
