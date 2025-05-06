package com.example.workdayp25.data.model

data class Home(val front_default: String)

data class Other(val home: Home)

// Would add data mappers
data class Sprites(
    val other: Other,
    val front_default: String,
    val back_default: String,
    val front_shiny: String,
    val back_shiny: String)

data class Type(val name: String)

data class Types(val type: Type)

data class Stat(val name: String)

data class Stats( val base_stat: String, val stat: Stat)

data class PokemonDetails(
    val id: String,
    val name: String,
    val weight: String,
    val height: String,
    val sprites: Sprites,
    val types: List<Types>,
    val stats: List<Stats>
)