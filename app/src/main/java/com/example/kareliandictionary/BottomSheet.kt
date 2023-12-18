package com.example.kareliandictionary

import android.util.Log
import android.widget.Spinner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.sqldelight.hockey.data.PlayerQueries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(listLemmas: SnapshotStateList<LemmaInfo>, threadChecker: Boolean, onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = Color.White
    ) {
        if (threadChecker){
            LoadingScreen()
        } else if (listLemmas[0].lemma == "Empty") {
            Empty()
        } else {
            LemmaList(listLemmas)
    }

    }
}

@Composable
fun LemmaList(listLemmas: SnapshotStateList<LemmaInfo>) {
    LazyColumn {
        items(listLemmas) { lemma ->
            expandableBox(lemma)
        }
    }
}

@Composable
fun LoadingScreen() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .size(size = 240.dp)
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

@Composable
fun Empty() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .size(size = 240.dp)
    )
    {
        Text("Ничего не найдено", modifier = Modifier.padding(bottom = 20.dp))
    }
}