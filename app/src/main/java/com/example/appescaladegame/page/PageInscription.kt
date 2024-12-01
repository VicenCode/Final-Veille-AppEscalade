package com.example.appescaladegame.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appescaladegame.R
import com.example.appescaladegame.fileService
import com.example.appescaladegame.service.FileService
import com.example.appescaladegame.ui.theme.AccentGreen01
import com.example.appescaladegame.ui.theme.AccentGreen02
import com.example.appescaladegame.ui.theme.Dark

val REGEX_VALIDE_INPUT = "^[a-zA-Z0-9]+$".toRegex();

fun saveUsername(username : String) : String {
    if(username.length > 12)
        return "Longeur Maximale de 12 charactères"

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

    //TODO
    // Test Mettre Background image pour input (Voir si à implémenter)

    //val imageBrush = Image(painter = painterResource(R.drawable.input_back1), contentDescription = "image")
//    val imageBrush = ShaderBrush(
//        ImageShader(
//            ImageBitmap.imageResource(id = R.drawable.input_back1_2),
//            //tileModeX = TileMode.Clamp,
//            //tileModeY = TileMode.Clamp
//        )
//    )
//
//    Box (
//        Modifier
//            .size(width = 200.dp, height = 100.dp)
//            //.graphicsLayer { scaleX = 10f; scaleY = 10f }
//            .background(brush = imageBrush)
//            .padding()
//    ){
//        BasicTextField(
//            value = valueInput,
//            onValueChange = {valueInput = it},
//            Modifier
//                .fillMaxWidth()
//                .padding(4.dp),
//            textStyle = TextStyle(
//                color = Color.Black,
//                fontSize = 16.sp,
//                fontFamily = FontFamily.Monospace
//            ),
//            cursorBrush = SolidColor(Color.Black)
//
//        )
//    }
//
//    Spacer(modifier = Modifier.size(20.dp).fillMaxWidth())
    
    Column(
        modifier = Modifier
            .fillMaxSize(),
            //.padding(all = 20.dp),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Bienvenu",
            style = MaterialTheme.typography.displayMedium,
            color = AccentGreen02,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier
                .size(60.dp)
                .fillMaxWidth()
        )


        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.monkey_gb),
                contentDescription = "Image Accueil",
                modifier = Modifier.scale(1.5f)
            )
        }

        Column(
            Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Entrez votre nom d'utilisateur :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier
                .height(30.dp)
                .fillMaxWidth())

            OutlinedTextField(
                value = valueInput,
                onValueChange = ({
                    isError = false;
                    errorMessage = ""
                    valueInput = it
                }),
                shape = RoundedCornerShape(6.dp),
                label = { Text(text = "Entrez votre nom d'utilisateur") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(1, 1, 1, alpha = 0),
                    focusedContainerColor = Color(1, 1, 1, alpha = 0 ),
                    unfocusedLabelColor = Dark,
                    focusedLabelColor = Dark,
                    unfocusedIndicatorColor = AccentGreen01,
                    focusedIndicatorColor = AccentGreen01

                ),
                singleLine = true
            )
            if(isError) {
                Spacer(modifier = Modifier
                    .size(10.dp)
                    .fillMaxWidth())
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier
                    .size(10.dp)
                    .fillMaxWidth())
            }
            else {
                Spacer(
                    modifier = Modifier
                        .size(20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier
                .height(10.dp)
                .fillMaxWidth())
            
            Button(
                onClick = ({
                    var messageRetourne = saveUsername(valueInput);
                    if(messageRetourne.isNotBlank()) {
                        isError = true;
                        errorMessage = messageRetourne;
                    }
                    else {
                        navController.navigate("page_accueil"){
                            popUpTo("page_inscription") {inclusive = true}
                        };
                    }
                }),
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .height(80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen02),
                shape = RectangleShape
            ) {
                Text(
                    text = "Confirmer",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}