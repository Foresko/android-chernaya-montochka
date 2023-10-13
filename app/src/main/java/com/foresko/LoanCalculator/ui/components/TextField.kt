package com.foresko.LoanCalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foresko.LoanCalculator.ui.theme.LoanTheme
import com.foresko.LoanCalculator.utils.visualTransformations.currencyVisualTransformation
import com.foresko.LoanCalculator.utils.visualTransformations.percentVisualTransformation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SumTextField(
    sum: String,
    sumRegexChange: (String) -> Unit,
    textFieldType: TextFieldType,
) {
    val visualTrans = when (textFieldType) {
        TextFieldType.LOAN -> currencyVisualTransformation()

        TextFieldType.RATE -> percentVisualTransformation()
        else -> VisualTransformation.None
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current

    val maxAllowedLength = when (textFieldType) {
        TextFieldType.LOAN -> 9
        TextFieldType.RATE -> 2
        else -> 2
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LoanTheme.colors.textFieldColor)
            .border(1.dp, LoanTheme.colors.borderTextField, RoundedCornerShape(16.dp)),
    ) {
        BasicTextField(
            value = sum,
            onValueChange = { newValue ->
                if (newValue.length <= maxAllowedLength) {
                    sumRegexChange(newValue) // здесь я предполагаю, что вы хотите вызвать sumRegexChange после проверки длины
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
            maxLines = 1,
            singleLine = true,
            cursorBrush = SolidColor(LoanTheme.colors.black),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
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
        )
    }
}