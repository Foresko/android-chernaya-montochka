package com.foresko.LoanCalculator.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foresko.LoanCalculator.R
import com.foresko.LoanCalculator.ui.theme.LoanTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DateTextField(
    date: String,
    onDateChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LoanTheme.colors.textFieldColor)
            .border(1.dp, LoanTheme.colors.borderTextField, RoundedCornerShape(16.dp))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .clickable {
                showDatePicker(context) { selectedDate ->
                    onDateChange(selectedDate)
                }
            }.padding(start = 16.dp).padding(vertical = 14.dp)
        )

        BasicTextField(
            value = date,
            onValueChange = { newValue ->
                if (isValidDate(newValue)) { // Проверка на корректность формата
                    onDateChange(newValue)
                }
            },
            textStyle = TextStyle(
                color = LoanTheme.colors.black,
                fontSize = 20.sp,
                lineHeight = 24.sp,
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
            visualTransformation = VisualTransformation.None, // Отключите currencyVisualTransformation
            decorationBox = {
                    innerTextField ->
                innerTextField()
                if (date.isEmpty()) {
                    Text(
                        buildAnnotatedString {
                            pushStyle(LoanTheme.textStyles.dateDecoratingText.toSpanStyle())
                            append(stringResource(id = R.string.date))
                        },
                        style = TextStyle(
                            color = LoanTheme.colors.decoratingText.copy(alpha = 0.3f),
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(500),
                        )
                    )
                }
            },
            modifier = Modifier
                .padding(start = 14.dp)
                .weight(1f, true)
        )
    }
}

// Функция для проверки корректности даты
fun isValidDate(date: String): Boolean {
    // Здесь можно добавить регулярное выражение или другую логику для проверки формата
    return true // Простое предположение
}

// Функция для отображения datePicker
fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    // Здесь добавьте логику для отображения datePicker и передачи выбранной даты
}
