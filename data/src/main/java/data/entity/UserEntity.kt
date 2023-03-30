package data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import domain.data.User
import java.time.LocalDate

@Entity(tableName = "user")
class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId")
    override val id: Int,
    override val name: String,
    override val avatarUrl: String,
) : User