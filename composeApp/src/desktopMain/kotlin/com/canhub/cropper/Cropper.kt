package com.canhub.cropper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri

/**
 * com.canhub.cropper（CanHub 图片裁剪库）桌面垫片。
 * 这是 Android 专属的图片裁剪 Activity 库；桌面用桌面原生裁剪/不裁剪直接透传。
 * 提供编译形状，运行时 no-op / 返回原图。——Nova 注
 */

/** 裁剪选项（DSL 风格，全是可写属性）。 */
open class CropImageOptions {
    var guidelines: CropImageView.Guidelines = CropImageView.Guidelines.OFF
    var backgroundColor: Int = 0
    var statusBarColor: Int = 0
    var activityMenuIconColor: Int = 0
    var activityBackgroundColor: Int = 0
    var toolbarColor: Int = 0
    var toolbarBackButtonColor: Int = 0
    var toolbarTitleColor: Int = 0
    var activityTitle: CharSequence? = null
    var cropMenuCropButtonTitle: CharSequence? = null
    var showCropOverlay: Boolean = true
    var showProgressBar: Boolean = true
    var multiTouchEnabled: Boolean = false
    var autoZoomEnabled: Boolean = true
    var fixAspectRatio: Boolean = false
    var aspectRatioX: Int = 1
    var aspectRatioY: Int = 1
    var outputCompressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    var outputCompressQuality: Int = 90
    var outputRequestWidth: Int = 0
    var outputRequestHeight: Int = 0
    var imageSourceIncludeGallery: Boolean = true
    var imageSourceIncludeCamera: Boolean = true
    var onDismissRequest: (() -> Unit)? = null
}

/** CropImageView 相关枚举/常量。 */
object CropImageView {
    enum class Guidelines { OFF, ON_TOUCH, ON }
    enum class CropShape { RECTANGLE, OVAL, RECTANGLE_VERTICAL_ONLY, RECTANGLE_HORIZONTAL_ONLY }
    enum class ScaleType { FIT_CENTER, CENTER, CENTER_CROP, CENTER_INSIDE, MATRIX }
}

/** 裁剪结果。 */
sealed class CropResult {
    abstract val uriContent: Uri?
    abstract val error: Throwable?
    open val isSuccessful: Boolean get() = false
    class Success(val uri: Uri?) : CropResult() {
        override val uriContent: Uri? get() = uri
        override val error: Throwable? get() = null
        override val isSuccessful: Boolean get() = true
    }
    class Cancelled : CropResult() {
        override val uriContent: Uri? get() = null
        override val error: Throwable? get() = null
    }
    class Failure(val exception: Throwable?) : CropResult() {
        override val uriContent: Uri? get() = null
        override val error: Throwable? get() = exception
    }
}

/** 裁剪契约选项。 */
class CropImageContractOptions(
    val uri: Uri?,
    val cropImageOptions: CropImageOptions,
)

/** 裁剪契约（ActivityResultContract）。 */
open class CropImageContract : androidx.activity.result.contract.ActivityResultContract<CropImageContractOptions, CropResult>() {
    override fun createIntent(context: Context, input: CropImageContractOptions): Intent = Intent()
    override fun parseResult(resultCode: Int, intent: Intent?): CropResult {
        // 桌面：no-op，返回取消（调用方应优雅降级）
        return CropResult.Cancelled()
    }
}
