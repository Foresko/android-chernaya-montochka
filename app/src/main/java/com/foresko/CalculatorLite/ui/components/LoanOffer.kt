package com.foresko.CalculatorLite.ui.components

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
import com.foresko.CalculatorLite.R
import com.foresko.CalculatorLite.core.rest.StoreInfo
import com.foresko.CalculatorLite.ui.theme.LoanTheme
import com.foresko.CalculatorLite.utils.visualTransformations.currencyFormatter

@Composable
fun MicroLoanOffer(offer: StoreInfo) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .defaultMinSize(minHeight = 206.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = Color(0xFFF2F2F8), shape = RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        MicroLoanContent(offer)
    }
}

@Composable
fun MicroLoanContent(offer: StoreInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HeaderInfo(offer.title, offer.icon, offer.rating)

        Spacer(Modifier.height(14.dp))

        MainInfo(offer)

        Spacer(Modifier.height(18.dp))

        UrlButton(offer.url)
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
            .background(LoanTheme.colors.borderTextField)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false)
            ) {
                try {
                    context.startActivity(intent)
                } catch (ex: Exception) {
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
    iconUri: String,
    rating: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(Uri.parse(iconUri)),
            contentDescription = null,
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = name,
            fontWeight = FontWeight(600),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(12.dp))

        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Spacer(Modifier.width(6.dp))

        Text(text = rating.toString())
    }
}

@Composable
private fun MainInfo(offer: StoreInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MainInfoItem(
            stringResource(R.string.rate),
            stringResource(R.string.rate_val, offer.rateFrom)
        )

        MainInfoItem(
            stringResource(R.string.term),
            stringResource(R.string.term_val, offer.termFrom, offer.termTo)
        )

        MainInfoItem(
            stringResource(R.string.sum),
            stringResource(R.string.sum_val, currencyFormatter(offer.currency).format(offer.sumTo))
        )
    }
}

@Composable
private fun MainInfoItem(
    name: String,
    value: String) {
    Column {
        Text(
            text = name,
            color = Color(0xB3343D4E),
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = value,
            fontWeight = FontWeight(600),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}