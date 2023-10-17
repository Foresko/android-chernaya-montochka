package com.foresko.LoanCalculator.utils.visualTransformations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@Composable
fun currencyVisualTransformation(
    currency: Currency,
    type: String = "",
    transformText: CurrencyStyleNumberVisualTransformation.CurrencyStyleNumberVisualTransformationScope.(formattedText: String) -> AnnotatedString
): CurrencyStyleNumberVisualTransformation {
    return remember(androidx.compose.ui.text.intl.Locale.current, currency, type) {
        CurrencyStyleNumberVisualTransformation(
            locale = Locale.getDefault(),
            currency = currency,
            type = type,
            transformText = transformText
        )
    }
}

@Stable
class CurrencyStyleNumberVisualTransformation(
    private val locale: Locale,
    private val currency: Currency,
    private val type: String,
    private val transformText: CurrencyStyleNumberVisualTransformationScope.(formattedText: String) -> AnnotatedString
) : VisualTransformation {

    interface CurrencyStyleNumberVisualTransformationScope {

        val integerPartIndices: IntRange

        val decimalSeparatorIndex: Int

        val fractionPartIndices: IntRange

        val currencySymbolIndices: IntRange
    }

    private class NumberOffsetMapping(
        private val originalToTransformedOffsets: IntArray,
        private val transformedToOriginalOffsets: IntArray
    ) :
        OffsetMapping {

        override fun originalToTransformed(offset: Int): Int {
            return originalToTransformedOffsets[offset]
        }

        override fun transformedToOriginal(offset: Int): Int {
            return transformedToOriginalOffsets[offset]
        }
    }

    private fun getDecimalFormat(): DecimalFormat {
        val decimalFormatSymbols = DecimalFormatSymbols(locale).apply {
            currencySymbol = try {
                currencySymbolMap[this@CurrencyStyleNumberVisualTransformation.currency.currencyCode.uppercase()]
                    ?: currency.symbol
            } catch (ex: Exception) {
                currencySymbol
            }
        }

        val decimalFormat = NumberFormat.getCurrencyInstance(locale).apply {
            currency = this@CurrencyStyleNumberVisualTransformation.currency
        } as DecimalFormat
        decimalFormat.decimalFormatSymbols = decimalFormatSymbols

        return decimalFormat
    }

    private val decimalFormat = getDecimalFormat()

    override fun filter(text: AnnotatedString): TransformedText {
        val number = text.text.toBigDecimalOrNull() ?: return TransformedText(
            text = text,
            offsetMapping = OffsetMapping.Identity
        )

        val decimalSeparatorIndex = text.text.indexOf('.')

        val decimalSeparatorLength = when (decimalSeparatorIndex == -1) {
            true -> 0
            false -> 1
        }

        val integerPartIndices = when (decimalSeparatorIndex == -1) {
            true -> 0.rangeTo(text.text.lastIndex)
            false -> 0 until decimalSeparatorIndex
        }

        val integerPartLength = integerPartIndices.length

        val fractionPartIndices = when (decimalSeparatorIndex) {
            -1, text.text.lastIndex -> IntRange.EMPTY
            else -> (decimalSeparatorIndex + 1).rangeTo(text.text.lastIndex)
        }

        val fractionPartLength = fractionPartIndices.length

        var prefix =
            when (number.signum()) {
                0, 1 -> decimalFormat.positivePrefix
                else -> decimalFormat.negativePrefix
            }

        var suffix =
            when (number.signum()) {
                0, 1 -> decimalFormat.positiveSuffix
                else -> decimalFormat.negativeSuffix
            }

        if (type.isNotEmpty()) {
            if (prefix.isNotEmpty()) {
                prefix = "${prefix.trim()}$type "
            } else {
                suffix = "${suffix}$type"
            }
        }

        val groupingSeparatorCount =
            when (integerPartLength % decimalFormat.groupingSize == 0) {
                true -> integerPartLength / decimalFormat.groupingSize - 1
                false -> integerPartLength / decimalFormat.groupingSize
            }

        val transformedTextLength =
            prefix.length
                .plus(integerPartLength)
                .plus(groupingSeparatorCount)
                .plus(decimalSeparatorLength)
                .plus(fractionPartLength)
                .plus(suffix.length)

        val transformedTextBuilder = StringBuilder(transformedTextLength)

        val originalToTransformedOffsets = IntArray(text.text.length + 1)
        val transformedToOriginalOffsets = IntArray(transformedTextLength + 1)

        var transformedTextOffset = 0

        transformedTextBuilder.append(prefix)

        repeat(prefix.length) {
            transformedToOriginalOffsets[transformedTextOffset++] = 0
        }

        originalToTransformedOffsets[0] =
            transformedTextOffset

        transformedToOriginalOffsets[transformedTextOffset] = 0

        transformedTextOffset += 1

        integerPartIndices.forEach { index ->
            val reversedIndex = integerPartIndices.last - index

            transformedTextBuilder.append(text.text[index])

            originalToTransformedOffsets[index + 1] = transformedTextOffset
            transformedToOriginalOffsets[transformedTextOffset] = index + 1

            transformedTextOffset += 1

            if (reversedIndex != 0 && reversedIndex % decimalFormat.groupingSize == 0) {
                transformedTextBuilder.append(decimalFormat.decimalFormatSymbols.groupingSeparator)
                transformedToOriginalOffsets[transformedTextOffset++] = index + 1
            }
        }

        if (decimalSeparatorIndex != -1) {
            transformedTextBuilder.append(decimalFormat.decimalFormatSymbols.decimalSeparator)
            originalToTransformedOffsets[decimalSeparatorIndex + 1] = transformedTextOffset
            transformedToOriginalOffsets[transformedTextOffset] = decimalSeparatorIndex + 1
            transformedTextOffset += 1
        }

        fractionPartIndices.forEach { index ->
            transformedTextBuilder.append(text.text[index])
            originalToTransformedOffsets[index + 1] = transformedTextOffset
            transformedToOriginalOffsets[transformedTextOffset] = index + 1
            transformedTextOffset += 1
        }

        transformedTextBuilder.append(suffix)

        repeat(suffix.length) {
            transformedToOriginalOffsets[transformedTextOffset] = text.text.lastIndex + 1
            transformedTextOffset += 1
        }

        val formattedText = transformedTextBuilder.toString()

        val scope = object : CurrencyStyleNumberVisualTransformationScope {

            override val integerPartIndices: IntRange
                get() = integerPartIndices

            override val decimalSeparatorIndex: Int
                get() = decimalSeparatorIndex

            override val fractionPartIndices: IntRange
                get() = fractionPartIndices

            override val currencySymbolIndices: IntRange =
                when (
                    val startIndex =
                        formattedText.indexOf(decimalFormat.decimalFormatSymbols.currencySymbol)
                ) {
                    -1 -> IntRange.EMPTY
                    else -> startIndex.until(startIndex + decimalFormat.decimalFormatSymbols.currencySymbol.length)
                }
        }

        val newText = with(scope) { transformText(formattedText) }

        check(newText.length == formattedText.length) { "transformed text length should be equals to formatted text length" }

        return TransformedText(
            newText,
            NumberOffsetMapping(originalToTransformedOffsets, transformedToOriginalOffsets)
        )
    }

    private val IntRange.length: Int
        get() = when {
            isEmpty() -> 0
            step > 0 -> last - start + 1
            else -> start - last + 1
        }

    override fun toString(): String {
        return "CurrencyStyleNumberVisualTransformation(locale=$locale)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrencyStyleNumberVisualTransformation

        if (locale != other.locale) return false
        if (currency != other.currency) return false
        if (type != other.type) return false
        if (transformText != other.transformText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = locale.hashCode()
        result = 31 * result + currency.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + transformText.hashCode()
        return result
    }
}