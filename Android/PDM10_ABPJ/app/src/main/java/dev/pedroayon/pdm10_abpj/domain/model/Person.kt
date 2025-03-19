package dev.pedroayon.pdm10_abpj.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int,
    val gender: String,         // "Masculino" o "Femenino"
    val degree: String,         // "Bachillerato", "Licenciatura", "Maestría", "Doctorado"
    val isNational: Boolean
)
