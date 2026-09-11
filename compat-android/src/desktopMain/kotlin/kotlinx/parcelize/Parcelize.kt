package kotlinx.parcelize

import android.os.Parcel
import kotlin.reflect.KClass

/**
 * kotlinx.parcelize 注解的编译期垫片。
 * 无编译器插件：@Parcelize 类依赖 android.os.Parcelable 的默认方法编译通过，
 * 运行时不会真正序列化字段（桌面进程内传递足够）。
 */

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Parcelize

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class IgnoredOnParcel

@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
annotation class RawValue

interface Parceler<T> {
    fun create(parcel: Parcel): T
    fun T.write(parcel: Parcel, flags: Int)
    fun newArray(size: Int): Array<T> = throw NotImplementedError("newArray not implemented")
}

@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
annotation class WriteWith<P : Parceler<*>>

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.BINARY)
annotation class TypeParceler<T, P : Parceler<T>>(val value: KClass<out P>)
