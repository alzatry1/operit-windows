package com.kyant.backdrop.backdrops

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kyant.backdrop.Backdrop

/** com.kyant.backdrop.backdrops.layerBackdrop 垫片：创建一个 Backdrop 占位。 */
@Composable
fun rememberLayerBackdrop(): Backdrop = remember { Backdrop() }
