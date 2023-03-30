package data.entity

import androidx.room.*
import domain.data.Repo


@Entity (tableName = "repo")
class RepoEntity(
    @ColumnInfo(name = "repoId")
    @PrimaryKey(autoGenerate = false)
    override val id: Int,
    override val repoName: String,
    override val stargazersCount: Int,
    val userName: String,
    override var favouriteStatus: Boolean,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long
): Repo

