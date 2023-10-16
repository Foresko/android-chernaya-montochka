package com.foresko.LoanCalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.foresko.LoanCalculator.R
import com.foresko.LoanCalculator.navGraphs.RootNavigator
import com.foresko.LoanCalculator.ui.destinations.destinations.CalculationsDestination
import com.foresko.LoanCalculator.ui.enumClass.TextFieldType
import com.foresko.LoanCalculator.ui.enumClass.TimePeriod
import com.foresko.LoanCalculator.ui.theme.LoanTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun LoanCalculator(
    sumAmount: String,
    changeSumAmount: (String) -> Unit,
    percentRate: String,
    changePercentRate: (String) -> Unit,
    startDate: String,
    changeStartDate: (String) -> Unit,
    finalDate: String,
    changeFinalDate: (String) -> Unit,
    rootNavigator: RootNavigator,
    daysDifference: (String, String) -> Int,
    selectedPeriod: TimePeriod,
    onSelectedPeriodChange: (TimePeriod) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val allFieldsFilled = sumAmount.isNotEmpty() &&
            percentRate.isNotEmpty() &&
            startDate.isNotEmpty() &&
            finalDate.isNotEmpty()

    val buttonColor = if (allFieldsFilled) LoanTheme.colors.borderTextField
    else LoanTheme.colors.noActiveButton.copy(alpha = 0.4F)

    val differenceInDays = remember { mutableStateOf(0) }
    LaunchedEffect(startDate, finalDate) {
        differenceInDays.value = daysDifference(startDate, finalDate)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
            ) { focusManager.clearFocus() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row {
                Column {
                    Text(
                        text = stringResource(id = R.string.loan_amount),
                        style = LoanTheme.textStyles.mainText,
                        letterSpacing = 0.1.sp,
                        color = LoanTheme.colors.decoratingText,
                        modifier = Modifier
                            .padding(start = 7.dp, bottom = 10.dp)
                    )
                    SumTextField(
                        sum = sumAmount,
                        sumRegexChange = changeSumAmount,
                        textFieldType = TextFieldType.LOAN,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.start_date),
                        style = LoanTheme.textStyles.mainText,
                        letterSpacing = 0.1.sp,
                        color = LoanTheme.colors.decoratingText,
                        modifier = Modifier
                            .padding(start = 7.dp, bottom = 10.dp)
                    )
                    DateTextField(
                        date = startDate,
                        onDateChange = changeStartDate
                    )
                }

                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    val minDateForFinalDate = try {
                        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        val date = sdf.parse(startDate)
                        Calendar.getInstance().apply { time = date ?: Date() }
                    } catch (e: Exception) {
                        null
                    }

                    Text(
                        text = stringResource(id = R.string.final_date),
                        style = LoanTheme.textStyles.mainText,
                        letterSpacing = 0.1.sp,
                        color = LoanTheme.colors.decoratingText,
                        modifier = Modifier
                            .padding(start = 7.dp, bottom = 10.dp)
                    )
                    DateTextField(
                        date = finalDate,
                        onDateChange = changeFinalDate,
                        minDate = minDateForFinalDate
                    )
                }
            }

            Spacer(modifier = Modifier.height(31.dp))

            InterestRate(
                selectedPeriod = selectedPeriod,
                onSelectedPeriodChange = onSelectedPeriodChange
            )

            Spacer(modifier = Modifier.height(20.dp))

            SumTextField(
                sum = percentRate,
                sumRegexChange = changePercentRate,
                textFieldType = TextFieldType.RATE,
            )

            Spacer(modifier = Modifier.height(26.dp))

            ButtonSave(
                isClickable = allFieldsFilled,
                color = buttonColor,
                rootNavigator = rootNavigator,
                differenceInDays = differenceInDays.value,
                sumAmount = sumAmount,
                percentRate = percentRate,
                selectedPeriod = selectedPeriod
            )
        }
    }
}

@Composable
fun InterestRate(
    selectedPeriod: TimePeriod,
    onSelectedPeriodChange: (TimePeriod) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.rate),
            style = LoanTheme.textStyles.mainText,
            letterSpacing = 0.1.sp,
            color = LoanTheme.colors.decoratingText,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    if (selectedPeriod == TimePeriod.DAY) LoanTheme.colors.borderTextField
                    else LoanTheme.colors.grayButton
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable { onSelectedPeriodChange(TimePeriod.DAY) }
                .align(Alignment.CenterVertically)
                .scale(0.9f)
        ) {
            Text(
                text = stringResource(id = R.string.inDay),
                style = LoanTheme.textStyles.buttonText,
                letterSpacing = 0.1.sp,
                color = LoanTheme.colors.white,
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    if (selectedPeriod == TimePeriod.MONTH) LoanTheme.colors.borderTextField
                    else LoanTheme.colors.grayButton
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable { onSelectedPeriodChange(TimePeriod.MONTH) }
                .align(Alignment.CenterVertically)
                .scale(0.9f)
        ) {
            Text(
                text = stringResource(id = R.string.inMonth),
                style = LoanTheme.textStyles.buttonText,
                letterSpacing = 0.1.sp,
                color = LoanTheme.colors.white,
            )
        }
    }
}


@Composable
fun ButtonSave(
    isClickable: Boolean,
    color: Color,
    rootNavigator: RootNavigator,
    differenceInDays: Int,
    sumAmount: String,
    percentRate: String,
    selectedPeriod: TimePeriod,
) {
    Box(
        modifier = Modifier
            .zIndex(1f)
    ) {
        Box(
            modifier = Modifier
                .clipToBounds()
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    enabled = isClickable,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {
                    rootNavigator.navigate(
                        CalculationsDestination(
                            differenceInDays = differenceInDays,
                            sumAmount = sumAmount,
                            percentRate = percentRate,
                            selectedPeriod = selectedPeriod
                        )
                    )
                }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = color
            ) {
                androidx.compose.material.Text(
                    text = stringResource(id = R.string.calculate),
                    style = TextStyle(
                        fontWeight = FontWeight(600),
                        fontSize = 16.sp,
                        color = LoanTheme.colors.white,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(vertical = 14.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
