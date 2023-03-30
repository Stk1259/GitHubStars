package data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import data.entity.StargazersEntity

@Dao
interface StargazerDao {
    @Transaction
    @Query("SELECT * FROM star WHERE repoId IN (SELECT repoId FROM repo WHERE repoName = :repoName)")
    suspend fun getStargazers(repoName: String): List<StargazersEntity>
}