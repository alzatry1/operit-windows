package com.kyant.backdrop.backdrops

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kyant.backdrop.Backdrop

/** com.kyant.backdrop.backdrops.layerBackdrop 垫片：创建一个 Backdrop 占位。 */
@Composable
fun rememberLayerBackdrop(): Backdrop = remember { Backdrop() }

/** com.kyant.backdrop.backdrops.layerBackdrop Modifier 扩展：把本组件注册为 backdrop 采样源。——Nova 注 */
fun Modifier.layerBackdrop(backdrop: Backdrop): Modifier = this
