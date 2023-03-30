package data.entity

import androidx.room.*

class StargazersEntity(
    @Relation(parentColumn = "userId", entityColumn= "userId")
    val userEntity: UserEntity,
    @Relation(parentColumn = "repoId", entityColumn = "repoId")
    val repoEntity: RepoEntity,
    @Relation(parentColumn = "repoId", entityColumn = "favouriteRepoId" )
    val favouriteRepoEntity: FavouriteRepoEntity,
    @Embedded
    val starEntity: StarEntity
)