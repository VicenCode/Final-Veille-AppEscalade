package com.example.appescaladegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appescaladegame.page.PageAccueil
import com.example.appescaladegame.page.PageInscription
import com.example.appescaladegame.service.FileService
import com.example.appescaladegame.ui.theme.AppEscaladeGameTheme
import java.time.LocalDateTime

lateinit var fileService : FileService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val userFilePath = "${this.filesDir}/utilisateur_data.json"
        val mursFilePath = "${this.filesDir}/murs_data.json"
        fileService = FileService(fichierJsonMursPath = mursFilePath, fichierJsonUtilisateurPath = userFilePath);
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}


@Composable
fun App() {
    val navController = rememberNavController();
    val startDestination : String;

    if(!fileService.isUtilisateurInitialise()) {
        startDestination = "page_inscription";
    }
    else {
        startDestination = "page_accueil";
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("page_inscription") { PageInscription(navController) }
        composable("page_accueil") { PageAccueil(navController) }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    AppEscaladeGameTheme {
//        Greeting("Android")
//    }
//}