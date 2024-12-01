package com.example.appescaladegame.service

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.appescaladegame.modele.Badge
import com.example.appescaladegame.modele.Mur
import com.example.appescaladegame.modele.Progression
import com.example.appescaladegame.modele.Titre
import com.example.appescaladegame.modele.Utilisateur
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.math.pow

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
        //resetFiles();
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
                titre = Titre("Novice", listOf(0xFFEAA48A, 0xFF3E2120)),
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
            titre = Titre("Novice", listOf(0xFFEAA48A, 0xFF3E2120)),
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
        increaseUtilisateurExp(newMur.score);

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

    private fun increaseUtilisateurExp(score : Double) {
        var totalExp = utilisateurInBD.progression.expActuelle + score;
        var expNextLevel = (utilisateurInBD.progression.niveauActuel + 1 / 0.05).pow(2.0)


        while(totalExp >= expNextLevel)  {
            utilisateurInBD.progression.niveauActuel++;
            totalExp -= expNextLevel
            expNextLevel = (utilisateurInBD.progression.niveauActuel + 1 / 0.05).pow(2.0)
        }

        utilisateurInBD.progression.expActuelle = totalExp;
        verifierTitre();
        verifierBadges();

        saveUtilisateur();
    }

    private fun verifierTitre() {
        val niveauActuel = utilisateurInBD.progression.niveauActuel;

        if(niveauActuel <= 10) {
            utilisateurInBD.titre = Titre("Novice", listOf(0xFFEAA48A, 0xFF3E2120));
        }
        else if(niveauActuel <= 30) {
            utilisateurInBD.titre = Titre("Amateur", listOf(0xFFD6DEF7, 0xFF4B5F97));
        }
        else if(niveauActuel <= 50) {
            utilisateurInBD.titre = Titre("Avancé", listOf(0xFF3F9471, 0xFF074E30));
        }
    }

    private fun verifierBadges() {
        val listeBadges = utilisateurInBD.badges


        if(mursInBD.size == 1 && !listeBadges.contains(Badge("Premier Mur", "permier_mur_badge.png"))) {
            listeBadges.add(Badge("Premier Mur", "permier_mur_badge.png"))
        }


        if(mursInBD.filter { mur ->  mur.nbEssais == 1}.size == 10 && !listeBadges.contains(Badge("10eme Flash", "10eme_flash_badge.png"))) {
            listeBadges.add(Badge("10eme Flash", "10eme_flash_badge.png"))
        }

        if(mursInBD.size >= 20 && !listeBadges.contains(Badge("20 Murs", "20_murs_badge.png"))) {
            listeBadges.add(Badge("20 Murs", "20_murs_badge.png"))
        }
    }


}
