package com.zenonrodrigo.paises_mundo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Paises (
    val countries: List<Country>
):Serializable
data class Country (
    var favorito: Boolean = false,
    @SerializedName("name_en")
    val nameEn: String,
    @SerializedName("name_es")
    val nameEs: String,
    @SerializedName("continent_en")
    val continentEn: String,
    @SerializedName("continent_es")
    val continentEs: String,
    @SerializedName("capital_en")
    val capitalEn: String,
    @SerializedName("capital_es")
    val capitalEs: String,
    @SerializedName("dial_code")
    val dialCode: String,
    @SerializedName("code_2")
    val code2: String,
    @SerializedName("code_3")
    val code3: String,
    val tld: String,
    val km2: Double,
    val emoji: String
):Serializable

data class Pregunta(
    val enunciado: String,
    val opciones: List<String>,
    val respuestaCorrecta: String
)

