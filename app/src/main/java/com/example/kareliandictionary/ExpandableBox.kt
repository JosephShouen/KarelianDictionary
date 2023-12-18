package com.example.kareliandictionary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun expandableBox(lemma: LemmaInfo){
    var expanded by remember { mutableStateOf(false) }
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
        ,
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.surfaceVariant
            containerColor = Color.White,
        ),
        onClick = { expanded = !expanded }
    ) {
        Column(
        ) {
            Text(
                text = "${lemma.lemma} [${lemma.lang.toString()}] - ${lemma.listMeanings}",
                modifier = Modifier.padding(12.dp),
                color = Color(0xFF848484)
            )
            if (expanded) {
                Column(){
                    Text(
                        text = """    
                        ${lemma.listMeanings.toString()}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFF262626)
                    )
                    lemma.listFormGram.forEach { lemma ->
                        Text(
                            text = """    
                            ${lemma.wordform} - ${lemma.gramset}
                            """.trimIndent(),
                            modifier = Modifier.padding(12.dp),
                            color = Color(0xFF262626))
                    }
                }
            }
        }
    }
}