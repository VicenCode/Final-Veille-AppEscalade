package com.example.appescaladegame.service

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.appescaladegame.modele.Mur
import com.example.appescaladegame.modele.Progression
import com.example.appescaladegame.modele.Utilisateur
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileService(
    private val fichierJsonMursPath : String ,
    private val fichierJsonUtilisateurPath : String,
) {

    var utilisateurInBD : Utilisateur;
    var mursInBD : MutableList<Mur>;

    private fun resetFiles() {
        File(fichierJsonMursPath).delete();
        File(fichierJsonUtilisateurPath).delete();
    }

    init {
        resetFiles();
        if(isFichierExiste(fichierJsonUtilisateurPath) || isFichierExiste(fichierJsonMursPath)) {
            utilisateurInBD = readUserData();
            mursInBD = readMursData();
        }
        else {
            val userDefault= Utilisateur(
                userName = "",
                progression =  Progression(
                    expActuelle = 0.0,
                    niveauActuel = 1,
                ),
                titre = "Novice",
                badges = mutableListOf()
            );

            utilisateurInBD = userDefault;
            mursInBD = mutableListOf();

            var json = Json.encodeToString(utilisateurInBD)
            File(fichierJsonUtilisateurPath).writeText(json);
            json = Json.encodeToString(mursInBD)
            File(fichierJsonMursPath).writeText(json);
        }
    }

    fun initUtilisateur(username: String) {
        utilisateurInBD = Utilisateur(
            userName = username,
            progression =  Progression(
                expActuelle = 0.0,
                niveauActuel = 1,
            ),
            titre = "Novice",
            badges = mutableListOf()
        );

        saveUtilisateur();
    }


    fun isFichierExiste(pathFichier : String) : Boolean {
        val fichier = File(pathFichier)
        return fichier.exists();
    }

    fun isUtilisateurInitialise(): Boolean {
        return isFichierExiste(fichierJsonUtilisateurPath) && utilisateurInBD.userName.isNotBlank()
    }

    fun saveMur(newMur : Mur) {
        mursInBD.add(newMur);
        val json = Json.encodeToString(mursInBD);
        File(fichierJsonMursPath).writeText(json);
    }

    fun readMursData() : MutableList<Mur> {
        val mursJson = File(fichierJsonMursPath).readText();
        return Json.decodeFromString(mursJson);
    }

    fun readUserData() : Utilisateur {
        val utilisateurJson = File(fichierJsonUtilisateurPath).readText();
        return Json.decodeFromString(utilisateurJson);
    }

    private fun saveUtilisateur() {
        val json = Json.encodeToString(utilisateurInBD);
        File(fichierJsonUtilisateurPath).writeText(json);
    }


}
