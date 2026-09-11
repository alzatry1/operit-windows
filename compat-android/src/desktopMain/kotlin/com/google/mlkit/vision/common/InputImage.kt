package com.google.mlkit.vision.common

/** com.google.mlkit.vision.common.InputImage 编译级 stub（P3-B4）。 */
open class InputImage private constructor() {

    companion object {
        @JvmStatic
        fun fromBitmap(bitmap: android.graphics.Bitmap, rotationDegrees: Int): InputImage =
            InputImage()

        @JvmStatic
        fun fromFilePath(context: android.content.Context, uri: android.net.Uri): InputImage =
            InputImage()

        @JvmStatic
        fun fromByteArray(byteArray: ByteArray, width: Int, height: Int, rotationDegrees: Int, format: Int): InputImage =
            InputImage()
    }
}
