package com.github.skydoves.colorpicker.compose

import androidx.compose.runtime.Composable
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
    var selectedColor: androidx.compose.runtime.MutableState<Color>? = null
    internal var onColorChanged: ((ColorEnvelope) -> Unit)? = null

    fun setWheelImageBitmap(bitmap: android.graphics.Bitmap?) {}
    fun selectByColor(color: Color, fromUser: Boolean) {
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
) {}

@Composable
fun BrightnessSlider(
    modifier: Modifier = Modifier,
    controller: ColorPickerController,
    onColorChanged: ((ColorEnvelope) -> Unit)? = null,
) {}
