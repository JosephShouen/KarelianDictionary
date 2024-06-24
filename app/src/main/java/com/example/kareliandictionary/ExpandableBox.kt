package com.example.kareliandictionary

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableBox(lemma: LemmaInfo, word: String = "") {

    var expanded by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(8.dp),
        //elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            )
//            .background(
//                color = Color(0xFF212121)
//            )
        ,
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.surfaceVariant
//            containerColor = Color.White,
            containerColor = if (isSystemInDarkTheme()) Color(0xFF292929) else Color.White
        ),
        onClick = { expanded = !expanded }
    ) {
        Column(
        ) {
            Text(
                text = "${lemma.lemma} [${lemma.lang.toString()}] - ${lemma.listMeanings}",
                modifier = Modifier.padding(12.dp),
//                color = Color(0xFF848484)
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
            if (expanded) {
                Column(){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = rememberRipple(
                                bounded = true,
                                radius = 250.dp,
                                color = Color.DarkGray
                            ),
                            onClick = {
                                clipboardManager.setText(AnnotatedString("${lemma.listMeanings}"))
                                Toast.makeText(context,
                                    "Скопировано: " + "${lemma.listMeanings}" ,
                                    Toast.LENGTH_SHORT).show()
                            })
                    ){
                        Text(
                            text = """
                        ${lemma.listMeanings.toString()}
                        """.trimIndent(),
                            modifier = Modifier
                                .padding(12.dp)
                            ,
//                            color = Color(0xFF262626)
                            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF262626)
                        )
                    }

                    lemma.listFormGram.forEach { lemma ->
//                        Text(
//                            text = """
//                            ${lemma.wordform} - ${lemma.gramset}
//                            """.trimIndent(),
//                            modifier = Modifier
//                                .padding(12.dp)
//                            ,
//                            color = Color(0xFF262626))
//                        SelectionContainer {
//                            Text(
//                                text = """
//                            ${lemma.wordform} - ${lemma.gramset}
//                            """.trimIndent(),
//                                modifier = Modifier.padding(12.dp),
//                                color = Color(0xFF262626))
//                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = rememberRipple(
                                    bounded = true,
                                    radius = 250.dp,
                                    color = Color.DarkGray
                                ),
                                onClick = {
                                    clipboardManager.setText(AnnotatedString("${lemma.wordform} - ${lemma.gramset}"))
                                    Toast.makeText(context,
                                        "Скопировано: " + "${lemma.wordform} - ${lemma.gramset}" ,
                                        Toast.LENGTH_SHORT).show()
                                })
                        ){
                            Text(
                                text = """
                            ${lemma.wordform} - ${lemma.gramset}
                            """.trimIndent(),
                                modifier = Modifier
                                    .padding(12.dp)
                                ,
                                color = if (isSystemInDarkTheme()) Color.White else Color(0xFF262626))
                        }
                    }
                }
            }
        }
    }
}