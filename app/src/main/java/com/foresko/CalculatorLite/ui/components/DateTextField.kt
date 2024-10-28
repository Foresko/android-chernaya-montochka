package com.foresko.CalculatorLite.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foresko.CalculatorLite.R
import com.foresko.CalculatorLite.ui.theme.LoanTheme
import java.util.Calendar

@Composable
fun DateTextField(
    date: String,
    onDateChange: (String) -> Unit,
    minDate: Calendar? = null,
    maxDate: Calendar? = null
) {
    val context = LocalContext.current

    var localDate by remember { mutableStateOf(date) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LoanTheme.colors.textFieldColor)
            .clickable {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePicker = DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val formattedDate = "${selectedDayOfMonth.toString().padStart(2, '0')}.${(selectedMonth + 1).toString().padStart(2, '0')}.$selectedYear"
                        localDate = formattedDate
                        onDateChange(formattedDate)
                    },
                    year, month, day
                )
                minDate?.let {
                    datePicker.datePicker.minDate = it.timeInMillis
                }
                maxDate?.let {
                    datePicker.datePicker.maxDate = it.timeInMillis
                }
                datePicker.show()
            }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(vertical = 8.dp)
        )

        Box{
            Text(
                text = if (localDate.isNotEmpty()) localDate else stringResource(id = R.string.date),
                style = TextStyle(
                    color = if (localDate.isNotEmpty()) LoanTheme.colors.black else LoanTheme.colors.decoratingText.copy(alpha = 0.3f),
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(500),
                )
            )
        }
    }
}