package android.annotation

/** AOSP 同名注解：标记忽略 Lint 检查。桌面版仅作编译期标记。 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.FILE,
    AnnotationTarget.TYPE,
)
@Retention(AnnotationRetention.BINARY)
annotation class SuppressLint(vararg val value: String)
