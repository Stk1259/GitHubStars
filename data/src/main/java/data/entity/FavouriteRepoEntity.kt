package data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import domain.data.Repo

@Entity(tableName = "favouriteRepo")
class FavouriteRepoEntity (
    @ColumnInfo(name = "favouriteRepoId")
    @PrimaryKey(autoGenerate = false)
    override val id: Int,
    override val repoName: String,
    override val stargazersCount: Int,
    val userName: String,
    override var favouriteStatus: Boolean,
    val createdAt: Long
    ): Repo
