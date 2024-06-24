package com.example.kareliandictionary

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.Database
import com.example.kareliandictionary.ui.theme.Montserrat

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(prefs: SharedPreferences) {
    val context: Context = LocalContext.current

    var arrDrop by remember { mutableStateOf(false) }
    val listLemmas = remember { mutableStateListOf<LemmaInfo>() }
    Log.d("TestLaunch", "Before Driver")
    val driver: SqlDriver = remember {AndroidSqliteDriver(Database.Schema, context, "tiny_big_vepkar.db")}
    Log.d("TestLaunch", "After Driver")
    val chosenLang: List<Long> = remember { mutableListOf(1, 4, 5, 6, 7) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var inputValue by remember { mutableStateOf("") }

    var threadChecker by remember { mutableStateOf(false) }
    val searchThread = Thread {
        threadChecker = true
        getLemmaInfo(driver, inputValue, chosenLang, listLemmas)
        threadChecker = false
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    val firstStart = prefs.getBoolean("firstStart", true)
//    if (firstStart) {
//        Log.d("CreateDB", "ThreadStarted")
//        val threadCreateDB = Thread {
//            CreateDB(context)
//            //getLemmaInfo(driver, input_value, chosenlang, listLemmas)
//            val editor = prefs.edit()
//            editor.putBoolean("firstStart", false)
//            editor.apply()
//            Log.d("CreateDB", "ThreadEnded")
//        }
//        threadCreateDB.start()
//    }

    var showSheet by remember { mutableStateOf(false) }
    if (showSheet) {
        BottomSheet(listLemmas, threadChecker) {
            showSheet = false
        }
    }

    val (height, width) = LocalConfiguration.current.run { screenHeightDp.dp to screenWidthDp.dp }

//    Box(modifier = Modifier.background(
//        color = Color(0xFFf4f4f4)
//    ))
    Box()
    {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End) {
            IconButton(
                modifier = Modifier
                    .size(48.dp),
                onClick = { arrDrop = true }
            ) {
                Icon(
                    Icons.Default.Info,
                    "",
                    modifier = Modifier.size(48.dp).padding(top = 10.dp),
                    tint = Color(0xFF848484)
//                    tint = Color(0xFF263238)
//                    tint = Color.DarkGray
                )
            }
        }

        Column(
            modifier = Modifier
                //.background(Color.White)
                .background(Color.Transparent)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
//                value = input_value.uppercase(),
                value = inputValue,
                onValueChange = {
                    //newText ->
                    //input_value = newText
                    inputValue = it
                },
                placeholder = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
//                            text = " Minä ečin...",
//                            text = "Введите запрос  ",
                            text = "Введите запрос...",
                            fontSize = 35.sp,
//                            fontSize = dpToSp(50.dp),
                            modifier = Modifier.fillMaxWidth(),
                            style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
//                            style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
//                            color = Color(0xFF848484)
//                            color = Color(0xFF848484)
                            color = Color.LightGray
                        )
                    }
                },
//                modifier = Modifier.scale(scaleY = 0.9F, scaleX = 1F),
//                modifier = Modifier.width(width-(24).dp),
                textStyle = TextStyle(
                    fontSize = dpToSp(50.dp),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
//                    color = Color.Black),
//                    color = Color(0xFF848484)),
//                    color = Color.DarkGray
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                ),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        listLemmas.clear()
                        searchThread.start()
                        showSheet = true
                        focusManager.clearFocus()
                    }
                ),
//                trailingIcon = {
//                    Icon(Icons.Default.Clear,
//                        contentDescription = "clear text",
//                        modifier = Modifier
//                            .clickable {
//                                input_value = ""
//                            }
//                            .offset(y = -40.dp)
//                    )
//                }
            )

//            Button(onClick = {
//                val threadCreateDB = Thread {
//                    CreateDB(context)
//                }
//                threadCreateDB.start()
//                },
//                colors = ButtonDefaults.buttonColors(Color.White)
//            )
//            {
//                Text("CreateDB", fontSize = 25.sp)
//            }
        }
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .offset(x=width-55.dp, y=height/2-65.dp),
            onClick = { inputValue = "" }
        ) {
            Icon(
                Icons.Filled.Clear,
                "Clear",
                modifier = Modifier.size(48.dp).padding(top = 10.dp),
                tint = Color(0xFF848484)
//                tint = Color.Black
//                tint = Color.DarkGray
            )
        }

        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))
        ) {
            val x = 300.dp
            val y = 220.dp
            DropdownMenu(
                expanded = arrDrop,
                offset = DpOffset(x = (width/2-x/2), y = (-height/2-y/2)),
                onDismissRequest = { arrDrop = false},
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .padding(10.dp)
//                        .align(alignment = Alignment.Center)
//                    color = Color.Transparent.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .size(width = x, height = y)
                ) {
                    Column(){
                        Text(
                            text = """    
                            Словарь Вепского и Карельского языков
                            Версия 1.0
                            В приложении используется база данных проекта 
                            "Открытый корпус вепсского и карельского языков (ВепКар)"
                            Доступен по адресу: dictorpus.krc.karelia.ru
                            """.trimIndent(),
                            //fontSize = 50.sp,
                            fontSize = dpToSp(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
//                            style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
//                            color = Color(0xFF848484)
                            color = Color(0xFF848484)
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

//@Composable
//fun MainHub(prefs: SharedPreferences){
//    Box(modifier = Modifier.background(
//        color = Color(0xFFf4f4f4)))
//    {
//        val context: Context = LocalContext.current
//        val firstStart = prefs.getBoolean("firstStart", true)
//        val threadCreateDB = Thread {
//            CreateDB(context)
//        }
//        if (firstStart) {
//            Log.d("TestLaunch", "Yes")
//            threadCreateDB.start()
//            val editor = prefs.edit()
//            editor.putBoolean("firstStart", false)
//            editor.apply()
//        }
//
//        if (threadCreateDB.isAlive()) {
//            Log.d("TestLaunch", "Alive")
//            LoadingDBScreen()
//            threadCreateDB.join()
//        } else {
//            Log.d("TestLaunch", "Not Alive")
//            MainScreen(prefs)
//        }
//    }
//}
//
//@Composable
//fun LoadingDBScreen(){
//    Box(modifier = Modifier.background(
//        color = Color(0xFFf4f4f4)))
//    {
//        CircularProgressIndicator(
//            modifier = Modifier
//                .size(size = 80.dp)
//                .padding(bottom = 20.dp),
//            color = Color.Red
//        )
//    }
//}