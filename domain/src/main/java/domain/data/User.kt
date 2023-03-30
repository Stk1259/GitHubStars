package domain.data

import java.io.Serializable

interface User: Serializable {
    val id: Int
    val name: String
    val avatarUrl: String
}