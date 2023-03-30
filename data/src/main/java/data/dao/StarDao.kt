package data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import data.entity.StarEntity

@Dao
interface StarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(star: List<StarEntity>)
}