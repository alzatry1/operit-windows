package com.canhub.cropper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import android.view.View
import androidx.activity.result.contract.ActivityResultContract

/**
 * com.canhub.cropper 图片裁剪库的桌面编译级垫片。
 * 桌面裁剪实装见 MIGRATION-PLAN P8（文件对话框 + 裁剪 UI）。——Nova 注
 */

/** 裁剪结果。 */
class CropImage {
    class ActivityResult {
        var uriContent: Uri? = null
        var originalUri: Uri? = null
        var cropRect: android.graphics.Rect? = null
        var error: Exception? = null
        val isSuccessful: Boolean get() = error == null && uriContent != null
    }
}

/** 裁剪选项（Parcelable 占位，链式 setter）。 */
open class CropImageOptions : Parcelable {
    var guidelines: Int = 0
    var outputCompressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    var outputCompressQuality: Int = 90
    var outputRequestWidth: Int = 0
    var outputRequestHeight: Int = 0
    var fixAspectRatio: Boolean = false
    var aspectRatioX: Int = 1
    var aspectRatioY: Int = 1
    var multiTouchEnabled: Boolean = false
    var cropShape: Int = 0

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {}

    companion object {
        const val GUIDELINES_OFF = 0
        const val GUIDELINES_ON_TOUCH = 1
        const val GUIDELINES_ON = 2
    }
}

/** 契约入参：uri + options。 */
class CropImageContractOptions(
    val uri: Uri?,
    val options: CropImageOptions,
) {
    // 链式委托到 options
    fun setGuidelines(v: Int): CropImageContractOptions = apply { options.guidelines = v }
    fun setOutputCompressFormat(f: Bitmap.CompressFormat): CropImageContractOptions = apply { options.outputCompressFormat = f }
    fun setOutputCompressQuality(q: Int): CropImageContractOptions = apply { options.outputCompressQuality = q }
    fun setOutputRequestWidth(w: Int): CropImageContractOptions = apply { options.outputRequestWidth = w }
    fun setOutputRequestHeight(h: Int): CropImageContractOptions = apply { options.outputRequestHeight = h }
    fun setFixAspectRatio(b: Boolean): CropImageContractOptions = apply { options.fixAspectRatio = b }
    fun setAspectRatio(x: Int, y: Int): CropImageContractOptions = apply { options.aspectRatioX = x; options.aspectRatioY = y }
    fun setMultiTouchEnabled(b: Boolean): CropImageContractOptions = apply { options.multiTouchEnabled = b }
    fun setCropShape(s: Int): CropImageContractOptions = apply { options.cropShape = s }
}

/** 裁剪契约：桌面 v1 不拉起真实裁剪，回调一个"取消"态结果。 */
class CropImageContract : ActivityResultContract<CropImageContractOptions, CropImage.ActivityResult>() {
    override fun createIntent(context: Context, input: CropImageContractOptions): Intent =
        Intent("com.canhub.cropper.CROP").apply { setData(input.uri) }

    override fun parseResult(resultCode: Int, intent: Intent?): CropImage.ActivityResult =
        CropImage.ActivityResult().apply { error = Exception("桌面版裁剪未实装") }
}

/** CropImageView 视图垫片。 */
open class CropImageView(context: Context) : View(context) {
    fun setImageUriAsync(uri: Uri?) {}
    fun getCroppedImage(): Bitmap? = null
    fun clearImage() {}
    fun setAspectRatio(x: Int, y: Int) {}
    fun setFixedAspectRatio(b: Boolean) {}
}
