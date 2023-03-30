package data.converter

import data.remote.FormattedRemoteStar
import data.remote.RemoteStar
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

object StarDateConverter {
    fun convertDate(starData: MutableList<RemoteStar>): MutableList<FormattedRemoteStar> {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        fun RemoteStar.toFormattedGraphData() = FormattedRemoteStar(
            formatter.parse(starDate)!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            user
        )
        return starData.map { it.toFormattedGraphData() }.toMutableList()
    }

}
