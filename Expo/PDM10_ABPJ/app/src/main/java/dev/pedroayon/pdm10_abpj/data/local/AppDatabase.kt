package dev.pedroayon.pdm10_abpj.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.pedroayon.pdm10_abpj.data.dao.PersonDao
import dev.pedroayon.pdm10_abpj.domain.model.Person

@Database(entities = [Person::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}
