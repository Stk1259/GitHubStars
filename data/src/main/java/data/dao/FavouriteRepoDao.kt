package data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import data.entity.FavouriteRepoEntity

@Dao
interface FavouriteRepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repo: FavouriteRepoEntity)
    @Query("SELECT * FROM favouriteRepo WHERE userName = :userName")
    suspend fun getRepoByUsername(userName: String):List<FavouriteRepoEntity>
    @Query("SELECT * FROM favouriteRepo")
    suspend fun getAllRepo():List<FavouriteRepoEntity>
    @Query("SELECT * FROM favouriteRepo WHERE repoName = :repoName")
    suspend fun getRepoId(repoName: String): FavouriteRepoEntity
    @Query("DELETE FROM favouriteRepo WHERE favouriteRepoId = :favouriteRepoId")
    suspend fun deleteRepo(favouriteRepoId: Int)
}