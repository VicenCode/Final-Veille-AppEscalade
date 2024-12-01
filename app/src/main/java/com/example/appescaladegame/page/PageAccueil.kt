package com.example.appescaladegame.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appescaladegame.component.Graphique
import com.example.appescaladegame.component.MurItem
import com.example.appescaladegame.fileService
import com.example.appescaladegame.modele.Mur
import com.example.appescaladegame.ui.theme.AccentGreen02
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val listeFiltres = listOf(
    "Année",
    "Mois",
    "Semaine"
)

@Composable
fun PageAccueil(navController: NavController) {
    val listeMurs = fileService.mursInBD;
    var filtreSelected by remember { mutableStateOf("Semaine") }
    var isExpanded by remember { mutableStateOf(false) }


    //var mursAfficher: MutableList<Mur>;
    var now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault());

    val mursAfficher by remember(filtreSelected, listeMurs) {
        derivedStateOf {

            if(filtreSelected == "Année") {
                listeMurs.filter {
                        mur -> mur.dateComplete.year == now.year
                }.toMutableList()
            }
            else if(filtreSelected == "Mois") {
                listeMurs.filter {
                        mur -> mur.dateComplete.month == now.month
                }.toMutableList()
            }
            else {
                val joursPourDebutSemaine = now.dayOfWeek.minus(now.dayOfWeek.ordinal.toLong())
                listeMurs.filter {
                        mur -> mur.dateComplete.dayOfWeek >= joursPourDebutSemaine
                }.toMutableList()
            }

        }
    }

    Column(
        Modifier.fillMaxSize()
    ) {

        Column(
            //Modifier.fillMaxWidth()
            Modifier.height(60.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {

                Box (
                    Modifier
                        //.width(100.dp)
                        .wrapContentSize(Alignment.TopCenter)
                        .background(
                            color = AccentGreen02,
                            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                        )
                ){
                    Row (
                        Modifier
                            .clickable(onClick = { isExpanded = !isExpanded })
                            .height(50.dp)
                            .width(170.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement =  Arrangement.SpaceBetween,
                    ){
                        Text(
                            text = filtreSelected,
                            color = Color.White
                        )

                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Voir plus de choix",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = !isExpanded },
                        Modifier
                            .height(170.dp)
                            .width(170.dp)
                            .background(color = AccentGreen02)
                    ){
                        for(filtre in listeFiltres) {
                            DropdownMenuItem(
                                text = { Text(text=filtre, color = Color.White) },
                                onClick = {
                                    filtreSelected = filtre;
                                    isExpanded = !isExpanded;
                                }
                            )
                        }
                    }
                }

                IconButton(onClick = { navController.navigate("page_profil") },  ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Go to Page Profil")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp)
        ) {
            Row (
                Modifier
                    //.border(width = 2.dp, color = Color.Red, shape = RectangleShape)
                    .weight(2.5f)
                    .fillMaxWidth()
            ){
                if(mursAfficher.isEmpty()) {
                    Box {
                        Text("Aucune donnée")
                    }
                }
                else {
                    Graphique(murs = mursAfficher, filtre = filtreSelected)
                }
            }

            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
            ){
                Text(
                    text = "Murs (${mursAfficher.size})",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreen02
                )
            }

            Row(
                Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    //.border(width = 2.dp, color = Color.Red, shape = RectangleShape)
                ) {
                if(mursAfficher.size == 0) {
                    Text(text = "Aucun Mur trouvé")
                } else {
                    val mursListeParcourir = mursAfficher.reversed()
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        items(count = mursListeParcourir.size) { index ->
                            val mur = mursListeParcourir[index];
                            MurItem(mur = mur);
                        }
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth(),

                    //.border(width = 2.dp, color = Color.Red, shape = RectangleShape),
                Arrangement.Absolute.Center,
                Alignment.Bottom
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate("page_ajout") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    containerColor = AccentGreen02,
                    contentColor = Color.White      
                ) {
                    //Icon(Icons.Default.Add, contentDescription = "Ajouter un mur")
                    Text(
                        text = "Ajouter un mur",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}