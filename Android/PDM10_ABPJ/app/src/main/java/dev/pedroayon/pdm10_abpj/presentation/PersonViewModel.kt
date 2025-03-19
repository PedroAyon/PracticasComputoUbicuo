package dev.pedroayon.pdm10_abpj.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dev.pedroayon.pdm10_abpj.data.local.AppDatabase
import dev.pedroayon.pdm10_abpj.domain.model.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_database"
    ).build()
    private val dao = db.personDao()

    private val _persons = MutableStateFlow<List<Person>>(emptyList())
    val persons: StateFlow<List<Person>> = _persons

    init {
        viewModelScope.launch {
            _persons.value = dao.getAll()
        }
    }

    fun addPerson(name: String, age: Int, gender: String, degree: String, isNational: Boolean) {
        viewModelScope.launch {
            dao.insert(Person(name = name, age = age, gender = gender, degree = degree, isNational = isNational))
            _persons.value = dao.getAll()
        }
    }
}