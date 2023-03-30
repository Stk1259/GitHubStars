package data.dao

import androidx.room.*
import data.entity.RepoEntity

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repo: RepoEntity)
    @Query("SELECT * FROM repo WHERE userName = :userName ORDER BY createdAt")
    suspend fun getRepo(userName: String): MutableList<RepoEntity>
    @Query("SELECT * FROM repo WHERE repoName = :repoName")
    suspend fun getRepoId(repoName: String): RepoEntity
}