package domain.data

data class FavouriteRepos(
    val id: Int,
    val repoName: String,
    val stargazersCount: Int,
    val additionalStars: Int,
    val userName: String,
    var favouriteStatus: Boolean
)
