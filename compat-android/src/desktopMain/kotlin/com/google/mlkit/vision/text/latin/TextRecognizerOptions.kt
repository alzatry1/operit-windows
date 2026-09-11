package com.google.mlkit.vision.text.latin

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface

/** com.google.mlkit.vision.text.latin.TextRecognizerOptions stub。 */
class TextRecognizerOptions private constructor() : TextRecognizerOptionsInterface {

    class Builder {
        fun build(): TextRecognizerOptions = TextRecognizerOptions()
    }

    companion object {
        @JvmField val DEFAULT_OPTIONS: TextRecognizerOptions = TextRecognizerOptions()
    }
}
