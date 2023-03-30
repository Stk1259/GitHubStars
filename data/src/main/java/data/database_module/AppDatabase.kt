package data.database_module

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import data.converter.LocalDateConverter
import data.dao.*
import data.entity.*

@Database(entities = [RepoEntity::class, FavouriteRepoEntity::class, UserEntity::class, StarEntity::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao() : RepoDao
    abstract fun favouriteRepoDao(): FavouriteRepoDao
    abstract fun userDao() : UserDao
    abstract fun starDao() : StarDao
    abstract fun stargazerDao() : StargazerDao
}
