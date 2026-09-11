package com.kyant.backdrop.effects

import com.kyant.backdrop.BackdropEffectsScope

/** com.kyant.backdrop.effects 垫片：玻璃特效配置项，桌面 v1 不实际渲染。 */

fun BackdropEffectsScope.vibrancy() {
    applied.add("vibrancy")
}

fun BackdropEffectsScope.blur(radius: Float) {
    applied.add("blur:$radius")
}

fun BackdropEffectsScope.lens(refractionHeight: Float, refractionAmount: Float) {
    applied.add("lens:$refractionHeight,$refractionAmount")
}

fun BackdropEffectsScope.lens(refractionHeight: Float, refractionAmount: Float, chromaticAberration: Float) {
    applied.add("lens:$refractionHeight,$refractionAmount,$chromaticAberration")
}
