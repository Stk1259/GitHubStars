package domain.data

import java.time.LocalDate

interface FormattedStar: java.io.Serializable {
    var date: LocalDate
    var user: User
}
