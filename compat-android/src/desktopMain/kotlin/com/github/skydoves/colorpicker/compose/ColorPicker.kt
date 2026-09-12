package com.github.skydoves.colorpicker.compose

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * com.github.skydoves.colorpicker.compose（skydoves 取色器）桌面编译级垫片。
 * 桌面版 v1 提供 API 形状 + 通过 controller 同步颜色，UI 实装见 P7。——Nova 注
 */

/** 颜色选择结果回调包络。 */
class ColorEnvelope(
    val color: Color,
    val hexCode: String,
    val fromUser: Boolean,
)

/** 取色器控制器。 */
class ColorPickerController {
    private val _selectedColor = androidx.compose.runtime.mutableStateOf(Color.White)
    /** 选中色（非空 State，app 用 `by controller.selectedColor` 委托）。——Nova 注 */
    val selectedColor: androidx.compose.runtime.State<Color> get() = _selectedColor
    internal var onColorChanged: ((ColorEnvelope) -> Unit)? = null

    fun setWheelImageBitmap(bitmap: android.graphics.Bitmap?) {}
    fun selectByColor(color: Color, fromUser: Boolean) {
        _selectedColor.value = color
        onColorChanged?.invoke(ColorEnvelope(color, "#%08X".format(color.value.toInt()), fromUser))
    }
}

@Composable
fun rememberColorPickerController(): ColorPickerController =
    androidx.compose.runtime.remember { ColorPickerController() }

@Composable
fun HsvColorPicker(
    modifier: Modifier = Modifier,
    controller: ColorPickerController = rememberColorPickerController(),
    onColorChanged: (ColorEnvelope) -> Unit = {},
    initialColor: Color = Color.White,
) {
    controller.onColorChanged = onColorChanged
}

@Composable
fun ImageColorPicker(
    modifier: Modifier = Modifier,
    controller: ColorPickerController = rememberColorPickerController(),
    paletteImageBitmap: android.graphics.Bitmap? = null,
    onColorChanged: (ColorEnvelope) -> Unit = {},
) {
    controller.onColorChanged = onColorChanged
}

@Composable
fun AlphaSlider(
    modifier: Modifier = Modifier,
    controller: ColorPickerController,
    onColorChanged: ((ColorEnvelope) -> Unit)? = null,
    /** 真实库的棋格背景色参数（桌面 stub 收下但不渲染）。——Nova 注 */
    tileOddColor: Color = Color.White,
    tileEvenColor: Color = Color.LightGray,
) {}

@Composable
fun BrightnessSlider(
    modifier: Modifier = Modifier,
    controller: ColorPickerController,
    onColorChanged: ((ColorEnvelope) -> Unit)? = null,
) {}

/** AlphaTile：选中色预览块（app 传入 controller）。——Nova 注 */
@Composable
fun AlphaTile(
    modifier: Modifier = Modifier,
    controller: ColorPickerController = rememberColorPickerController(),
) {
    androidx.compose.foundation.layout.Box(
        modifier.background(controller.selectedColor.value)
    )
}
