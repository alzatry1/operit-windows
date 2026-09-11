package androidx.activity.result

/** androidx.activity.result.PickVisualMediaRequest：Photo Picker 请求参数。 */
class PickVisualMediaRequest private constructor(
    val mediaType: VisualMediaType,
) {
    class Builder {
        private var mediaType: VisualMediaType = VisualMediaType.ImageAndVideo
        fun setMediaType(mediaType: VisualMediaType): Builder = apply { this.mediaType = mediaType }
        fun build(): PickVisualMediaRequest = PickVisualMediaRequest(mediaType)
    }

    companion object {
        @JvmStatic
        fun create(mediaType: VisualMediaType = VisualMediaType.ImageAndVideo): PickVisualMediaRequest =
            PickVisualMediaRequest(mediaType)
    }

    /** 媒体类型约束。 */
    sealed class VisualMediaType {
        object ImageOnly : VisualMediaType()
        object VideoOnly : VisualMediaType()
        object ImageAndVideo : VisualMediaType()
        data class SingleMimeType(val mimeType: String) : VisualMediaType()
    }
}
