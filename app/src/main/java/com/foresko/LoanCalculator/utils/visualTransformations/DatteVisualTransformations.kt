package com.foresko.LoanCalculator.utils.visualTransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 10) text.text.substring(0..9) else text.text
        val sb = StringBuilder()
        for (i in 0 until 10) {
            when (i) {
                2, 5 -> sb.append('.')
            }
            if (i < trimmed.length) {
                sb.append(trimmed[i])
            } else {
                sb.append(' ')
            }
        }
        return TransformedText(AnnotatedString(sb.toString()), identityOffsetMap(trimmed.length))
    }

    private fun identityOffsetMap(originalLength: Int): OffsetMapping {
        return object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset.coerceIn(0, originalLength)
            override fun transformedToOriginal(offset: Int): Int = offset.coerceIn(0, originalLength)
        }
    }
}


