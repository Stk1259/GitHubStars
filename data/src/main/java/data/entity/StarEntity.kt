package data.entity

import androidx.room.*
import domain.data.Repo
import domain.data.Star
import domain.data.User
import java.time.LocalDate

@Entity(
    tableName = "star"
)
class StarEntity(
    @PrimaryKey
    @ColumnInfo(name = "starId")
    override val date: LocalDate,
    @ColumnInfo(name = "userId")
    val userId: Int,
    @ColumnInfo(name = "repoId")
    val repoId: Int,
) : Star{constructor(starEntity: StarEntity, userId: Int, repoId: Int, userEntity: UserEntity) : this(
    date = starEntity.date, userId, repoId)}