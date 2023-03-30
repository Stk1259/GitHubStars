package domain.data

interface Repo {
    val id: Int
    val repoName: String
    val stargazersCount: Int
    var favouriteStatus: Boolean
}
