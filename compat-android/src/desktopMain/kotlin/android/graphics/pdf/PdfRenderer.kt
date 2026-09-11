package android.graphics.pdf

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.os.ParcelFileDescriptor

/**
 * android.graphics.pdf.PdfRenderer：编译级 stub（桌面 PDF 渲染留给 pdfbox 集成）。
 */
class PdfRenderer(private val input: ParcelFileDescriptor) : AutoCloseable {

    private var closed = false

    val pageCount: Int get() = 0

    open class Page(private val index: Int) : AutoCloseable {
        private var closed = false

        val width: Int get() = 612
        val height: Int get() = 792

        fun getIndex(): Int = index

        fun render(destination: Bitmap, destClip: Rect?, transform: Matrix?, renderMode: Int) {
            destination.eraseColor(android.graphics.Color.WHITE)
        }

        override fun close() { closed = true }
        fun isClosed(): Boolean = closed

        companion object {
            const val RENDER_MODE_FOR_DISPLAY = 1
            const val RENDER_MODE_FOR_PRINT = 2
        }
    }

    fun openPage(index: Int): Page = Page(index)

    override fun close() { closed = true }

    fun shouldScaleForPrinting(): Boolean = false

    fun write(out: android.os.ParcelFileDescriptor, cancelSignal: android.os.CancellationSignal?) {}
}
