package com.example.appescaladegame.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appescaladegame.modele.Mur
import com.example.appescaladegame.ui.theme.AccentGreen01
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DateTimeFormatBuilder
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun MurItem(mur: Mur) {
    val format = LocalDateTime.Format {
        dayOfMonth()
        char(' ')
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        year()

        chars(" - ")

        hour()
        char(':')
        minute()
    };

    Column(
        Modifier
            .fillMaxWidth()
            .background(AccentGreen01, shape = RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = mur.dateComplete.format(format))
            Text(text = "+ ${mur.score} XP")
        }

        Spacer(modifier = Modifier.height(4.dp).fillMaxWidth())
        
        Row (
            Modifier
                .background(Color.White, shape = RoundedCornerShape(6.dp))
                .fillMaxWidth().padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = "${mur.nbEssais}")
            Text(text = mur.difficulte)
        }
    }
}