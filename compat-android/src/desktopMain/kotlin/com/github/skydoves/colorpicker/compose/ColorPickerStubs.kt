package com.github.skydoves.colorpicker.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * colorpicker-compose 的 desktop stub。
 * 该库（skydoves/colorpicker-compose）是 Android-only，未进 desktop 依赖。
 * 桌面移植用最小可编译实现：controller 管选中色状态，picker/slider 渲染选中色占位。
 * 真正的取色由 ColorPickerDialog 自带的手动 HEX/RGB/HSV 输入承担。 ——Nova 注
 */

/** 颜色变化回调载荷。 */
class ColorEnvelope(
    val color: Color,
    val hexCode: String,
    val fromUser: Boolean,
)

/** 取色控制器：持有选中色。 */
class ColorPickerController {
    private val _selectedColor: MutableState<Color> = mutableStateOf(Color.White)
    val selectedColor: State<Color> get() = _selectedColor

    fun selectByColor(color: Color, fromUser: Boolean) {
        _selectedColor.value = color
    }

    fun selectByHsv(hue: Float, saturation: Float, value: Float, alpha: Float = 1f, fromUser: Boolean) {
        _selectedColor.value = Color.hsv(hue, saturation, value, alpha)
    }
}

@Composable
fun rememberColorPickerController(): ColorPickerController = remember { ColorPickerController() }

@Composable
fun AlphaTile(modifier: Modifier = Modifier, controller: ColorPickerController) {
    Box(modifier.background(controller.selectedColor.value))
}

@Composable
fun HsvColorPicker(
    modifier: Modifier = Modifier,
    controller: ColorPickerController,
    onColorChanged: (ColorEnvelope) -> Unit = {},
) {
    Box(modifier.background(controller.selectedColor.value).border(1.dp, Color.Gray))
}

@Composable
fun BrightnessSlider(
    modifier: Modifier = Modifier,
    controller: ColorPickerController,
) {
    Box(modifier.background(controller.selectedColor.value).border(1.dp, Color.Gray))
}

@Composable
fun AlphaSlider(
    modifier: Modifier = Modifier,
    controller: ColorPickerController,
    tileOddColor: Color = Color.White,
    tileEvenColor: Color = Color.LightGray,
) {
    Box(modifier.background(controller.selectedColor.value).border(1.dp, Color.Gray))
}
