package com.foresko.LoanCalculator.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.foresko.LoanCalculator.R
import com.foresko.LoanCalculator.core.rest.Loan
import com.foresko.LoanCalculator.utils.visualTransformations.currencyFormatter
import com.google.firebase.crashlytics.FirebaseCrashlytics

@Composable
fun MicroLoanOffer(
    offer: Loan
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 206.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = Color(0xFFF2F2F8), shape = RoundedCornerShape(16.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            HeaderInfo(
                name = offer.name,
                iconUri = offer.icon,
                rating = offer.rating
            )

            Spacer(modifier = Modifier.height(14.dp))

            MainInfo(
                rateFrom = offer.rateFrom,
                termTo = offer.termTo,
                termFrom = offer.termFrom,
                sumTo = offer.sumTo,
                currency = offer.currency
            )

            Spacer(modifier = Modifier.height(18.dp))

            UrlButton(url = offer.url)
        }
    }
}

@Composable
private fun UrlButton(
    url: String
) {
    val context = LocalContext.current

    val intent = remember(url) {
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 49.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFF60D06C))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false)
            ) {
                try {
                    context.startActivity(intent)
                } catch (ex: Exception) {
                    FirebaseCrashlytics
                        .getInstance()
                        .recordException(ex)
                }
            }
    ) {
        Text(
            text = stringResource(R.string.web),
            color = Color.White,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier
                .padding(vertical = 12.dp)
        )
    }
}

@Composable
private fun HeaderInfo(
    name: String,
    rating: Float,
    iconUri: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter(Uri.parse(iconUri)),
            contentDescription = null,
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = name,
            color = Color.Black,
            fontSize = 19.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(600),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f, true)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = rating.toString(),
            color = Color.Black,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(400),
        )
    }
}

@Composable
private fun MainInfo(
    rateFrom: Float,
    termTo: Int,
    termFrom: Int,
    sumTo: Int,
    currency: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        MainInfoItem(
            name = stringResource(R.string.rate),
            value = stringResource(R.string.rate_val, rateFrom)
        )

        MainInfoItem(
            name = stringResource(R.string.term),
            value = stringResource(R.string.term_val, termFrom, termTo)
        )

        MainInfoItem(
            name = stringResource(R.string.sum),
            value = stringResource(
                R.string.sum_val,
                currencyFormatter(currency).format(sumTo)
            )
        )
    }
}

@Composable
private fun MainInfoItem(
    name: String,
    value: String
) {
    Column {
        Text(
            text = name,
            color = Color(0xB3343D4E),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight(400)
        )

        Text(
            text = value,
            color = Color.Black,
            fontSize = 15.sp,
            lineHeight = 21.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight(600),
        )
    }
}