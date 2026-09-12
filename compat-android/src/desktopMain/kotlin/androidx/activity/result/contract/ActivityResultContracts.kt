package androidx.activity.result.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest

/**
 * androidx.activity.result.contract.ActivityResultContracts：全部内建 contract。
 * 桌面版 createIntent 构造语义正确的 Intent（便于日志/调试），parseResult 与 Android 一致。
 */

class ActivityResultContracts private constructor() {

    /** 打开已有文档（可持久化权限），input = mimeTypes。 */
    open class OpenDocument : ActivityResultContract<Array<String>, Uri?>() {
        override fun createIntent(context: Context, input: Array<String>): Intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT)
                .putExtra(Intent.EXTRA_MIME_TYPES, input)
                .setType("*/*")

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            if (resultCode == Activity.RESULT_OK) intent?.data else null
    }

    /** 打开多个文档。 */
    open class OpenMultipleDocuments : ActivityResultContract<Array<String>, List<Uri>>() {
        override fun createIntent(context: Context, input: Array<String>): Intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT)
                .putExtra(Intent.EXTRA_MIME_TYPES, input)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                .setType("*/*")

        override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
            if (resultCode != Activity.RESULT_OK || intent == null) return emptyList()
            val result = ArrayList<Uri>()
            intent.data?.let { result.add(it) }
            val clip = intent.clipData
            if (clip != null) {
                for (i in 0 until clip.itemCount) clip.getItemAt(i).uri?.let { result.add(it) }
            }
            return result
        }
    }

    /** 获取任意内容（不可持久化权限），input = mimeType。 */
    open class GetContent : ActivityResultContract<String, Uri?>() {
        override fun createIntent(context: Context, input: String): Intent =
            Intent(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType(input)

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            if (resultCode == Activity.RESULT_OK) intent?.data else null
    }

    /** 获取多个内容。 */
    open class GetMultipleContents : ActivityResultContract<String, List<Uri>>() {
        override fun createIntent(context: Context, input: String): Intent =
            Intent(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType(input)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
            if (resultCode != Activity.RESULT_OK || intent == null) return emptyList()
            val result = ArrayList<Uri>()
            intent.data?.let { result.add(it) }
            val clip = intent.clipData
            if (clip != null) {
                for (i in 0 until clip.itemCount) clip.getItemAt(i).uri?.let { result.add(it) }
            }
            return result
        }
    }

    /** 创建文档，input = 建议文件名。 */
    open class CreateDocument(private val mimeType: String) : ActivityResultContract<String, Uri?>() {
        constructor() : this("*/*")

        override fun createIntent(context: Context, input: String): Intent =
            Intent(Intent.ACTION_CREATE_DOCUMENT)
                .setType(mimeType)
                .putExtra(Intent.EXTRA_TITLE, input)

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            if (resultCode == Activity.RESULT_OK) intent?.data else null
    }

    /** 打开目录树。 */
    open class OpenDocumentTree : ActivityResultContract<Uri?, Uri?>() {
        override fun createIntent(context: Context, input: Uri?): Intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            if (resultCode == Activity.RESULT_OK) intent?.data else null
    }

    /** 选择联系人。 */
    open class PickContact : ActivityResultContract<Unit, Uri?>() {
        override fun createIntent(context: Context, input: Unit): Intent =
            Intent(Intent.ACTION_PICK)

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            if (resultCode == Activity.RESULT_OK) intent?.data else null
    }

    /** 拍照到指定 Uri，input = 输出文件 Uri。 */
    open class TakePicture : ActivityResultContract<Uri, Boolean>() {
        override fun createIntent(context: Context, input: Uri): Intent =
            Intent("android.media.action.IMAGE_CAPTURE")
                .putExtra("output", input.toString())

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
            resultCode == Activity.RESULT_OK
    }

    /** 拍照返回缩略图。 */
    open class TakePicturePreview : ActivityResultContract<Unit, android.graphics.Bitmap?>() {
        override fun createIntent(context: Context, input: Unit): Intent =
            Intent("android.media.action.IMAGE_CAPTURE")

        override fun parseResult(resultCode: Int, intent: Intent?): android.graphics.Bitmap? = null
    }

    /** 录像到指定 Uri。 */
    open class CaptureVideo : ActivityResultContract<Uri, Boolean>() {
        override fun createIntent(context: Context, input: Uri): Intent =
            Intent("android.media.action.VIDEO_CAPTURE")
                .putExtra("output", input.toString())

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
            resultCode == Activity.RESULT_OK
    }

    /** 请求单个权限。 */
    open class RequestPermission : ActivityResultContract<String, Boolean>() {
        override fun createIntent(context: Context, input: String): Intent {
            val perms: Array<String> = arrayOf(input)
            return Intent(ACTION_REQUEST_PERMISSIONS).putExtra(EXTRA_PERMISSIONS, perms)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            if (resultCode != Activity.RESULT_OK) return false
            val grants = intent?.getIntArrayExtra(EXTRA_PERMISSION_GRANT_RESULTS)
            return grants != null && grants.isNotEmpty() &&
                grants.all { it == android.content.pm.PackageManager.PERMISSION_GRANTED }
        }

        companion object {
            const val ACTION_REQUEST_PERMISSIONS = "androidx.activity.result.contract.action.REQUEST_PERMISSIONS"
            const val EXTRA_PERMISSIONS = "androidx.activity.result.contract.extra.PERMISSIONS"
            const val EXTRA_PERMISSION_GRANT_RESULTS = "androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS"
        }
    }

    /** 请求多个权限。 */
    open class RequestMultiplePermissions : ActivityResultContract<Array<String>, Map<String, Boolean>>() {
        override fun createIntent(context: Context, input: Array<String>): Intent {
            val perms: Array<String> = input
            return Intent(RequestPermission.ACTION_REQUEST_PERMISSIONS)
                .putExtra(RequestPermission.EXTRA_PERMISSIONS, perms)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Map<String, Boolean> {
            if (resultCode != Activity.RESULT_OK || intent == null) return emptyMap()
            val permissions = intent.getStringArrayExtra(RequestPermission.EXTRA_PERMISSIONS)
            val grants = intent.getIntArrayExtra(RequestPermission.EXTRA_PERMISSION_GRANT_RESULTS)
            if (permissions == null || grants == null) return emptyMap()
            return permissions.zip(grants.toTypedArray())
                .associate { (p, g) -> p to (g == android.content.pm.PackageManager.PERMISSION_GRANTED) }
        }
    }

    /** 标准 startActivityForResult 语义。 */
    open class StartActivityForResult : ActivityResultContract<Intent, ActivityResult>() {
        override fun createIntent(context: Context, input: Intent): Intent = input

        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult =
            ActivityResult(resultCode, intent)

        companion object {
            const val EXTRA_ACTIVITY_OPTIONS_BUNDLE = "androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE"
        }
    }

    /** 启动 IntentSender。 */
    open class StartIntentSenderForResult : ActivityResultContract<android.content.IntentSender, ActivityResult>() {
        override fun createIntent(context: Context, input: android.content.IntentSender): Intent =
            Intent(ACTION_INTENT_SENDER_REQUEST)
                .putExtra(EXTRA_INTENT_SENDER_REQUEST, input.toString())

        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult =
            ActivityResult(resultCode, intent)

        companion object {
            const val ACTION_INTENT_SENDER_REQUEST = "androidx.activity.result.contract.action.INTENT_SENDER_REQUEST"
            const val EXTRA_INTENT_SENDER_REQUEST = "androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST"
            const val EXTRA_SEND_INTENT_EXCEPTION = "androidx.activity.result.contract.extra.SEND_INTENT_EXCEPTION"
        }
    }

    /** Photo Picker 单选。 */
    open class PickVisualMedia : ActivityResultContract<PickVisualMediaRequest, Uri?>() {
        /** Photo Picker 媒体类型约束（真实 AndroidX 嵌套在 PickVisualMedia 内）。——Nova 注 */
        sealed class VisualMediaType {
            object ImageOnly : VisualMediaType()
            object VideoOnly : VisualMediaType()
            object ImageAndVideo : VisualMediaType()
            data class SingleMimeType(val mimeType: String) : VisualMediaType()
        }

        override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent =
            Intent("android.provider.action.PICK_IMAGES")

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            if (resultCode == Activity.RESULT_OK) intent?.data else null

        companion object {
            @JvmStatic
            fun isPhotoPickerAvailable(context: Context): Boolean = false
        }
    }

    /** Photo Picker 多选。 */
    open class PickMultipleVisualMedia(private val maxItems: Int = 0) : ActivityResultContract<PickVisualMediaRequest, List<Uri>>() {
        override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent =
            Intent("android.provider.action.PICK_IMAGES")

        override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
            if (resultCode != Activity.RESULT_OK || intent == null) return emptyList()
            val result = ArrayList<Uri>()
            intent.data?.let { result.add(it) }
            val clip = intent.clipData
            if (clip != null) {
                for (i in 0 until clip.itemCount) clip.getItemAt(i).uri?.let { result.add(it) }
            }
            return result
        }
    }
}
