package com.foresko.CalculatorLite.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.foresko.CalculatorLite.R
import com.foresko.CalculatorLite.ui.navGraphs.RootNavigator
import com.foresko.CalculatorLite.ui.destinations.destinations.SelectCountryDestination


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    iconUri: String,
    rootNavigator: RootNavigator
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (iconUri.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(Uri.parse(iconUri)),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .size(34.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            rootNavigator.navigate(SelectCountryDestination())
                        }
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight(600)
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color(0xFFFFFFFF),
            scrolledContainerColor = Color(0xFFFFFFFF),
            titleContentColor = Color.Black
        )
    )
}