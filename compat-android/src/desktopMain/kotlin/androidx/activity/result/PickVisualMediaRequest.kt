package androidx.activity.result

import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VisualMediaType

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
}
