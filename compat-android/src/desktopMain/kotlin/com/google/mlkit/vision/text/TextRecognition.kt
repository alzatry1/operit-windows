package com.google.mlkit.vision.text

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage

/**
 * com.google.mlkit.vision.text 编译级 stub（P3-B4）。
 * 桌面无 MLKit 原生模型；process() 返回立即失败的 Task，
 * app 侧走 OCRResult.Error 分支优雅降级。
 */

/** TextRecognizer 配置项公共接口（对齐真实 mlkit 的 TextRecognizerOptionsInterface）。 */
interface TextRecognizerOptionsInterface

/** OCR 识别结果树。 */
open class Text {
    open val text: String get() = ""
    open val textBlocks: List<TextBlock> get() = emptyList()

    open class TextBlock {
        open val text: String get() = ""
        open val lines: List<Line> get() = emptyList()
        open val cornerPoints: Array<android.graphics.Point>? get() = null
        open val boundingBox: android.graphics.Rect? get() = null
        open val recognizedLanguage: String? get() = null
    }

    open class Line {
        open val text: String get() = ""
        open val elements: List<Element> get() = emptyList()
        open val cornerPoints: Array<android.graphics.Point>? get() = null
        open val boundingBox: android.graphics.Rect? get() = null
        open val recognizedLanguage: String? get() = null
        open val confidence: Float get() = 0f
    }

    open class Element {
        open val text: String get() = ""
        open val cornerPoints: Array<android.graphics.Point>? get() = null
        open val boundingBox: android.graphics.Rect? get() = null
        open val confidence: Float get() = 0f
    }
}

/** TextRecognizer：process 返回失败 Task。 */
open class TextRecognizer {
    open fun process(image: InputImage): Task<Text> = failedTask()

    private fun failedTask(): Task<Text> = object : Task<Text>() {
        override val exception: Exception
            get() = UnsupportedOperationException("MLKit text recognition 桌面端不可用")
    }

    open fun close() {}
}

/** TextRecognition 入口。 */
object TextRecognition {
    @JvmStatic
    fun getClient(options: TextRecognizerOptionsInterface): TextRecognizer = TextRecognizer()
}
