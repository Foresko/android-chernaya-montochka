package com.foresko.CalculatorLite.ui.destinations.bootomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foresko.CalculatorLite.R

import com.foresko.CalculatorLite.ui.enumClass.TimePeriod
import com.foresko.CalculatorLite.ui.theme.LoanTheme
import com.foresko.CalculatorLite.utils.LocalModalBottomSheetState
import com.foresko.CalculatorLite.utils.visualTransformations.currencyFormatter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
@Destination(style = DestinationStyleBottomSheet::class)
@RootNavGraph
fun Calculations(
    differenceInDays: Int,
    sumAmount: String,
    percentRate: String,
    selectedPeriod: TimePeriod
) {
    Column {
        TopAppBar()

        Spacer(modifier = Modifier.height(8.dp))

        CalculationContent(
            differenceInDays = differenceInDays,
            sumAmount = sumAmount,
            percentRate = percentRate
        )

        Spacer(modifier = Modifier.height(8.dp))

        BlockCalculate(
            differenceInDays = differenceInDays,
            sumAmount = sumAmount,
            percentRate = percentRate,
            selectedPeriod = selectedPeriod
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopAppBar() {
    val coroutineScope = rememberCoroutineScope()

    val sheetState = LocalModalBottomSheetState.current

    Column(
        modifier = Modifier
            .background(LoanTheme.colors.white)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    end = 16.dp
                ),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close_round),
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = false),
                    ) {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    },
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun CalculationContent(
    differenceInDays: Int,
    sumAmount: String,
    percentRate: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.money_bag),
                contentDescription = null,
                modifier = Modifier.size(148.dp)
            )

            Text(
                text = stringResource(id = R.string.loan_calculation),
                style = TextStyle(
                    fontWeight = FontWeight(600),
                    fontSize = 16.sp,
                    color = LoanTheme.colors.black,
                    lineHeight = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "$sumAmount ₽, под $percentRate%, на $differenceInDays дней",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = LoanTheme.colors.black,
                    lineHeight = 21.sp
                )
            )
        }
    }
}

@Composable
fun localizedCurrencyString(amount: Double, currencyCode: String): String {
    val format = currencyFormatter(currencyCode)
    return format.format(amount)
}

fun calculateAccruedInterest(
    differenceInDays: Int,
    sumAmount: String,
    percentRate: String,
    selectedPeriod: TimePeriod
): Double {
    val sum = sumAmount.toDoubleOrNull() ?: 0.0
    val rate = percentRate.toDoubleOrNull() ?: 0.0

    return when (selectedPeriod) {
        TimePeriod.DAY -> sum * (rate / 100) * differenceInDays
        TimePeriod.MONTH -> sum * (rate / 100) * (differenceInDays / 30.0)
        TimePeriod.NONE -> 0.0
    }
}

@Composable
fun BlockCalculate(
    differenceInDays: Int,
    sumAmount: String,
    percentRate: String,
    selectedPeriod: TimePeriod
) {
    val accruedInterest = remember(/*keys = listOf(sumAmount, percentRate, */) {
        calculateAccruedInterest(
            differenceInDays,
            sumAmount,
            percentRate,
            selectedPeriod
        )
    }

    val totalAmountToBeRefunded = accruedInterest + (sumAmount.toDoubleOrNull() ?: 0.0)

    val accruedInterestString = localizedCurrencyString(accruedInterest.round(2), "RUB")

    val totalAmountToBeRefundedString =
        localizedCurrencyString(totalAmountToBeRefunded.round(2), "RUB")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(LoanTheme.colors.surface, shape = RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .padding(top = 19.dp, bottom = 17.dp)
        ) {
            Text(
                text = stringResource(id = R.string.Interest_accrued),
                style = TextStyle(
                    fontWeight = FontWeight(400),
                    fontSize = 15.sp,
                    lineHeight = 21.sp
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = accruedInterestString,
                style = LoanTheme.textStyles.numberText,
                color = LoanTheme.colors.black
            )
        }

        Divider(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .fillMaxWidth()
                .height(0.5.dp)
                .background(Color.Black)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .padding(top = 19.dp, bottom = 17.dp)
        ) {
            Text(
                text = stringResource(id = R.string.Amount_to_be_refunded),
                style = TextStyle(
                    fontWeight = FontWeight(400),
                    fontSize = 15.sp,
                    lineHeight = 21.sp
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = totalAmountToBeRefundedString,
                style = LoanTheme.textStyles.numberText,
                color = LoanTheme.colors.black
            )
        }
    }
}

fun Double.round(decimals: Int = 2): Double {
    return "%.${decimals}f".format(Locale.US, this).toDouble()
}



