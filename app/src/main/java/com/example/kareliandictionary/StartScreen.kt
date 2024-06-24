package com.example.kareliandictionary

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.Database
import java.util.concurrent.CompletableFuture

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StartScreen(prefs: SharedPreferences, navController: NavController) {
    val context: Context = LocalContext.current
    var threadChecker by remember { mutableStateOf(false) }
    val firstStart = prefs.getBoolean("firstStart", true)
    Log.d("firstStart", firstStart.toString())
//    Log.d("firstStart", threadChecker.toString())

    val listLemmas = remember { mutableStateListOf<LemmaInfo>() }
    Log.d("TestLaunch", "Before Driver Old")
    var driver: SqlDriver = remember { AndroidSqliteDriver(Database.Schema, context, "tiny_big_vepkar.db") }
    Log.d("TestLaunch", "After Driver Old")
    var chosenLang: List<Long> = remember { mutableListOf(1, 4, 5, 6, 7) }

    val threadCreateDB = CompletableFuture.supplyAsync {
        Log.d("TestLaunch", "Async")
        if (firstStart) {
            Log.d("TestLaunch", "Editor Start")
            val editor = prefs.edit()
            editor.putBoolean("firstStart", false)
            editor.apply()
            Log.d("TestLaunch", "Editor End")

            Log.d("CreateDB", "ThreadStarted")
            createDB(context)
            getLemmaInfo(driver, "Terveh", chosenLang, listLemmas)
            Log.d("CreateDB", "ThreadEnded")
        }
    }

    threadCreateDB.thenAccept {
        Log.d("CreateDB", "It works before")
        threadChecker = true
        Log.d("CreateDB", "It works")
    }

    LaunchedEffect(key1 = threadChecker) {
        Log.d("CreateDB", "Effects")
        Log.d("threadChecker", threadChecker.toString())
        if (threadChecker) {
            Log.d("CreateDB", "RealEffects")
            threadChecker = false // необязательно
            navController.navigate(Screens.Search.route)
            Log.d("CreateDB", "RealEffectsHappened")
            Log.d("threadCheckerIf", threadChecker.toString())
        }
    }

    // Another way of calling launch passing only the block parameter
    // context and start parameters are set to their default values

//    LaunchedEffect(true) {
//        CoroutineScope(Dispatchers.Default).launch {
//            Log.d("CreateDB", "ThreadStarted")
//            CreateDB(context)
//            //getLemmaInfo(driver, input_value, chosenlang, listLemmas)
//            val editor = prefs.edit()
//            editor.putBoolean("firstStart", false)
//            editor.apply()
//            Log.d("CreateDB", "ThreadEnded")
//        }
//    }
//    Log.d("CreateDB", "After Launch")

//    thread(
//        start = true) {
//        CreateDB(context)
//        //getLemmaInfo(driver, input_value, chosenlang, listLemmas)
//        val editor = prefs.edit()
//        editor.putBoolean("firstStart", false)
//        editor.apply()
//        Log.d("CreateDB", "ThreadEnded")
//    }

//    val threadCreateDB = Thread {
//
//        CreateDB(context)
//        //getLemmaInfo(driver, input_value, chosenlang, listLemmas)
//        val editor = prefs.edit()
//        editor.putBoolean("firstStart", false)
//        editor.apply()
//        Log.d("CreateDB", "ThreadEnded")
//
//    }
//    threadCreateDB.start()

//    if (!firstStart){
//        Log.d("CreateDB", "Screen Changed")
//        navController.navigate("search_screen")
//    }

    Box(
//        modifier = Modifier.background(
//            color = Color(0xFFf4f4f4)
//        )
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        )
        {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(size = 80.dp)
                    .padding(bottom = 20.dp),
                color = Color.Red
            )
        }
    }
}

//suspend fun longRunningTask(context: Context, prefs: SharedPreferences){
//    CreateDB(context)
//    //getLemmaInfo(driver, input_value, chosenlang, listLemmas)
//    val editor = prefs.edit()
//    editor.putBoolean("firstStart", false)
//    editor.apply()
//    Log.d("CreateDB", "ThreadEnded")
//}