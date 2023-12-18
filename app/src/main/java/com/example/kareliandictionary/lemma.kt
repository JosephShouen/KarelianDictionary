package com.example.kareliandictionary

import com.example.sqldelight.hockey.data.SelectTest

data class LemmaInfo(val lemma: String, val lang: String, var listMeanings: String, var listFormGram: List<wordform_gramset>){}
