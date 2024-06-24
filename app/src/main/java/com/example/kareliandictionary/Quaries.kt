package com.example.kareliandictionary

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import app.cash.sqldelight.db.SqlDriver
import com.example.Database
import com.example.sqldelight.hockey.data.PlayerQueries
import com.example.sqldelight.hockey.data.SelectTest


fun getLemmaInfo(driver: SqlDriver, inputValue: String,
                 chosenLang: List<Long>, listLemmas: SnapshotStateList<LemmaInfo>){
    Log.d("TestLaunch", "getLemmaInfo")
    val database = Database(driver)
    val playerQueries: PlayerQueries = database.playerQueries

    val rusCheck = Regex("[\\u0400-\\u04FF]").containsMatchIn(inputValue)
    if (rusCheck) {
        val listMeaningLang = playerQueries.selectRusMeaning(meaning = inputValue).executeAsList()
        Log.d("Meaning", listMeaningLang.toString())
        if (listMeaningLang.isNullOrEmpty()){
            val emptyLemma = LemmaInfo("Empty", "", "", mutableListOf(WordformGramset("", "", 0, 0, 0, 0, 0)))
            listLemmas.add(emptyLemma)
        } else {
            val lemmasId = playerQueries.selectLemmaMeaning(listMeaningLang).executeAsList()
            Log.d("Meaning", lemmasId.toString())
            searching(playerQueries, lemmasId, listLemmas)
        }
    } else {
        val wordform = playerQueries.selectWordforms(inputValue).executeAsList()
        if (wordform.isNullOrEmpty()){
            val emptyLemma = LemmaInfo("Empty", "", "", mutableListOf(WordformGramset("", "", 0, 0, 0, 0, 0)))
            listLemmas.add(emptyLemma)
        } else {
            val lemmasId = playerQueries.selectLemmaId(wordform[0], chosenLang).executeAsList()
            searching(playerQueries, lemmasId, listLemmas)
        }
    }
}

fun searching(playerQueries: PlayerQueries, lemma: List<Long>, listLemmas: SnapshotStateList<LemmaInfo>) {
    val meaningId = playerQueries.selectMeaningId(lemma).executeAsList().groupBy { it.lemma_id }
//    Log.d("TestLemmas", meaningId.toString())
//    Log.d("TestLemmas", meaningId.size.toString())

    var listTest: List<SelectTest> = playerQueries.selectTest(lemma).executeAsList()
//    Log.d("TestLemmas", listTest.toString())
//    Log.d("TestLemmas", listTest.size.toString())

    val groupingLemma = listTest.groupBy { it.lemma_id }
//    Log.d("TestLemmas", groupingLemma.toString())
//    Log.d("TestLemmas", groupingLemma.size.toString())

    for (lemma in groupingLemma) {
        Log.d("TestLemmas", lemma.toString())
        val caseInfo = mutableListOf<WordformGramset>()
        val tenseInfo = mutableListOf<WordformGramset>()
        for (info in lemma.value) {
            if (info.gram_id_case != null && info.gram_id_number != null &&info.name_ru_ != null){
                val number = if (info.gram_id_number.toString() == "1") "ед.ч." else "мн.ч."
                caseInfo.add(WordformGramset(info.wordform, number + " " + info.name_ru_.replace("&shy;", ""), info.gram_id_number, info.gram_id_case, info.gram_id_tense, info.gram_id_mood, info.gram_id_person))
            } else if (info.gram_id_tense != null && info.gram_id_number != null && info.gram_id_person != null && info.gram_id_mood != null) {
                val number = if (info.gram_id_number.toString() == "1") "ед.ч." else "мн.ч."
                tenseInfo.add(WordformGramset(info.wordform, "${info.tense_name} ${info.mood_name} $number ${info.person_name}", info.gram_id_number, info.gram_id_case, info.gram_id_tense, info.gram_id_mood, info.gram_id_person))
            }
        }
        val meaning = meaningId[lemma.key]
        val sortedCase = caseInfo.sortedBy { it.gramsetCase }
        //val sortedTense = tenseInfo.sortedBy { it.gramsetNum }
        val sortedTense = tenseInfo.sortedWith(compareBy({ it.gramsetTense }, { it.gramsetMood }, { it.gramsetPerson }, { it.gramsetNum }))
//        Log.d("TestLemmas", sortedCase.toString())
//        Log.d("TestLemmas", sortedTense.toString())
        if (!meaning.isNullOrEmpty())
        {
//            Log.d("Meaning", meaningId.toString())
//            Log.d("Meaning", meaning.toString())
//            Log.d("Meaning", meaning[0].lemma.toString())
            listLemmas.add(LemmaInfo(meaning[0].lemma, lemma.value[0].name_ru.toString(), meaning[0].meanings_texts, sortedCase+sortedTense))
        }

    }
}