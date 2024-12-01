package com.example.appescaladegame.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.example.appescaladegame.modele.Mur
import com.example.appescaladegame.ui.theme.AccentGreen01
import com.example.appescaladegame.ui.theme.AccentGreen02
import com.example.appescaladegame.ui.theme.Dark
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.IndicatorPosition
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import ir.ehsannarmani.compose_charts.models.VerticalIndicatorProperties
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlinx.datetime.plus
import java.time.YearMonth

@Composable
fun Graphique(murs : MutableList<Mur>, filtre : String) {

    //val label = filtre;
    //var axisXLabels =  mutableListOf<String>();
    //var listeScore = mutableListOf<Double>();

    val axisXLabels = remember(filtre) {
        if(filtre == "Année") {
            mutableListOf(
                "JAN",
                "FEV",
                "MAR",
                "AVR",
                "MAI",
                "JUN",
                "JUL",
                "AOU",
                "SEP",
                "OCT",
                "NOV",
                "DEC"
            )
        } else if(filtre == "Mois") {
            var mur = murs[0]
            val nombreJoursMois = YearMonth.of(mur.dateComplete.year, mur.dateComplete.month).lengthOfMonth();
            //val nombreJoursMois = 15
            mutableListOf<String>().apply {
                for(i in 1..nombreJoursMois) {
                    if(i % 2 == 0) add(i.toString()) else add("")
                }
            }

        } else {
            mutableListOf(
                "LUN",
                "MAR",
                "MER",
                "JEU",
                "VEN",
                "SAM",
                "DIM",
            )
        }
    }

    val listeScore = remember(murs, filtre) {
        if(filtre == "Semaine") {
            MutableList(7) {0.0}.apply {
                murs.forEach{
                    mur ->
                    val index = mur.dateComplete.dayOfWeek.value - 1;
                    this[index] += mur.score;
                }
            }
        }
        else if(filtre == "Mois") {
            val mur = murs[0];
            val nombreJours = YearMonth.of(mur.dateComplete.year, mur.dateComplete.month).lengthOfMonth();

            MutableList(nombreJours) {0.0}.apply {
                murs.forEach {
                    mur ->
                    val index = mur.dateComplete.dayOfMonth - 1;
                    this[index] += mur.score;
                }
            }

        }
        else {
            //val mur = murs[0]
            MutableList(12) {0.0}.apply {
                murs.forEach {
                    mur ->
                    val index = mur.dateComplete.month.number - 1;
                    this[index] += mur.score;
                }
            }
        }
    }

    //println("AA")
    //println("Graphique Filtre: $filtre, Score: $listeScore, Labels: $axisXLabels")


    Column(
        Modifier.fillMaxSize()
    ) {
        LineChart(data = remember(filtre, murs) {
            mutableListOf(
                Line(
                    label = filtre,
                    values = listeScore,
                    color = SolidColor(AccentGreen01),
                    curvedEdges = false,
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(AccentGreen02),
                        strokeWidth = 3.dp,
                    ),

                )
            )
        },
        labelProperties = LabelProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelSmall,
            labels = axisXLabels as List<String>,
            rotationDegreeOnSizeConflict = 90f
        ),
            indicatorProperties = HorizontalIndicatorProperties(
                position = IndicatorPosition.Horizontal.Start,
            ),
            dividerProperties = DividerProperties(

            ),
        )
    }
}