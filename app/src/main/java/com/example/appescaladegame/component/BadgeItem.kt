package com.example.appescaladegame.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appescaladegame.R
import com.example.appescaladegame.ui.theme.LightGreen
import java.lang.reflect.Field

@Composable
fun BadgeItem(titre : String, imageName : String) {

    val context = LocalContext.current
    val drawableId = remember (imageName) {
        context.resources.getIdentifier(
            imageName,
            "drawable",
            context.packageName
        )
    }

    Column(
        Modifier
            .background(color = LightGreen, shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = titre,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Image(
            painter= painterResource(id = drawableId),
            contentDescription = titre,
            modifier = Modifier.scale(0.5f)
        )
    }
}