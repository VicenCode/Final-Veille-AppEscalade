package com.example.appescaladegame.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appescaladegame.fileService
import com.example.appescaladegame.service.FileService

val REGEX_VALIDE_INPUT = "^[a-zA-Z0-9]+$".toRegex();

fun saveUsername(username : String) : String {
    if(!REGEX_VALIDE_INPUT.containsMatchIn(username)) {
        return "Username doit contenir des charactères alphanumériques seulement";
    }

    fileService.initUtilisateur(username);

    return "";
}

@Composable
fun PageInscription(navController: NavController) {
    var valueInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") };

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Bienvenu!",
            style = MaterialTheme.typography.displayMedium,
        )

        Spacer(
            modifier = Modifier
                .size(60.dp)
                .fillMaxWidth()
        )

        TextField(
            value = valueInput,
            onValueChange = ({
                isError = false;
                errorMessage = ""
                valueInput = it
            }),
            shape = RoundedCornerShape(6.dp),
            label = { Text(text = "Entrez votre nom d'utilisateur") },
        )
        if(isError) {
            Spacer(modifier = Modifier.size(10.dp).fillMaxWidth())
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.size(10.dp).fillMaxWidth())
        }
        else {
            Spacer(
                modifier = Modifier
                    .size(20.dp)
                    .fillMaxWidth()
            )
        }

        Button(
            onClick = ({
                var messageRetourne = saveUsername(valueInput);
                if(messageRetourne.isNotBlank()) {
                    isError = true;
                    errorMessage = messageRetourne;
                }
                else {
                    navController.navigate("page_accueil");
                }
            })
        ) {
            Text(text = "Confirmer")
        }
    }
}