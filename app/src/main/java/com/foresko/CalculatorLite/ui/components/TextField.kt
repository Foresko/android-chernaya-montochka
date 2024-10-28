package com.foresko.CalculatorLite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foresko.CalculatorLite.ui.enumClass.TextFieldType
import com.foresko.CalculatorLite.ui.theme.LoanTheme
import com.foresko.CalculatorLite.utils.visualTransformations.currencyVisualTransformation
import com.foresko.CalculatorLite.utils.visualTransformations.percentVisualTransformation
import textFieldValueRegex
import java.util.Currency


@Composable
fun SumTextField(
    sum: String,
    sumRegexChange: (String) -> Unit,
    textFieldType: TextFieldType,
) {
    val visualTrans = when (textFieldType) {
        TextFieldType.LOAN -> currencyVisualTransformation(
            currency = Currency.getInstance("RUB"),
            transformText = { AnnotatedString(it) }
        )

        TextFieldType.RATE -> percentVisualTransformation()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current

    val keyboardType = when (textFieldType) {
        TextFieldType.RATE -> KeyboardType.Number
        else -> KeyboardType.Number
    }

    val maxAllowedLength = when (textFieldType) {
        TextFieldType.LOAN -> 9
        TextFieldType.RATE -> 5
    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LoanTheme.colors.textFieldColor)
    ) {
        BasicTextField(
            value = sum,
            onValueChange = { newValue ->
                val processedValue = when (textFieldType) {
                    TextFieldType.RATE -> textFieldValueRegex(newValue, "2")
                    else -> textFieldValueRegex(newValue)
                }
                if (processedValue.length <= maxAllowedLength) {
                    sumRegexChange(processedValue)
                }
            },
            textStyle = TextStyle(
                color = LoanTheme.colors.black,
                fontSize = 24.sp,
                lineHeight = 29.sp,
                fontWeight = FontWeight(600),
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            ),
            maxLines = 1,
            singleLine = true,
            cursorBrush = SolidColor(LoanTheme.colors.black),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            visualTransformation = visualTrans,
            decorationBox = { innerTextField ->
                innerTextField()
                if (sum.isEmpty()) {
                    when (textFieldType) {
                        TextFieldType.LOAN -> {
                            Text(
                                buildAnnotatedString {
                                    pushStyle(LoanTheme.textStyles.decoratingText.toSpanStyle())
                                    append("0")
                                    pop()
                                    pushStyle(LoanTheme.textStyles.decoratingText.toSpanStyle())
                                    append(" ₽")
                                },
                                style = TextStyle(
                                    color = LoanTheme.colors.decoratingText.copy(alpha = 0.3f),
                                    fontSize = 24.sp,
                                    lineHeight = 29.sp,
                                    fontWeight = FontWeight(500),
                                )
                            )
                        }

                        TextFieldType.RATE -> {
                            Text(
                                buildAnnotatedString {
                                    pushStyle(LoanTheme.textStyles.decoratingText.toSpanStyle())
                                    append("0")
                                    pop()
                                    pushStyle(LoanTheme.textStyles.decoratingText.toSpanStyle())
                                    append(" %")
                                },
                                style = TextStyle(
                                    color = LoanTheme.colors.decoratingText.copy(alpha = 0.3f),
                                    fontSize = 24.sp,
                                    lineHeight = 29.sp,
                                    fontWeight = FontWeight(500),
                                )
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(start = 14.dp)
                .weight(1f, true)
                .focusable()
        )
    }
}

