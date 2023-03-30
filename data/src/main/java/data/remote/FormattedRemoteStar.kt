package data.remote

import domain.data.FormattedStar
import domain.data.User
import java.time.LocalDate

data class FormattedRemoteStar(
    override var date: LocalDate,
    override var user: User,
): FormattedStar
