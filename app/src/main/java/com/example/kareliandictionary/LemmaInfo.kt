package com.example.kareliandictionary

data class LemmaInfo(val lemma: String, val lang: String, var listMeanings: String, var listFormGram: List<WordformGramset>){}
