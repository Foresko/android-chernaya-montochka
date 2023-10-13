package com.foresko.LoanCalculator.utils.visualTransformations

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

class CustomOffsetMapping(
    private val originalLength: Int,
    private val transformedLength: Int
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        return if (offset <= originalLength) offset else transformedLength
    }

    override fun transformedToOriginal(offset: Int): Int {
        return if (offset <= originalLength) offset else originalLength
    }
}

@Composable
fun currencyVisualTransformation(): VisualTransformation {
    return VisualTransformation { text ->
        val input = text.text
        val transformedText = buildAnnotatedString {
            append(input)
            if (input.isNotEmpty()) {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500)
                    )
                ) {
                    append(" ₽")
                }
            }
        }

        TransformedText(
            transformedText,
            CustomOffsetMapping(text.length, transformedText.length)
        )
    }
}
