package data.converter

import android.util.Log
import data.remote.FormattedGraphData
import data.remote.GraphRemoteData
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object StarDateConverter {
    fun convertDate(starData: MutableList<GraphRemoteData>): MutableList<FormattedGraphData> {
        Log.d("starDate", starData.toString())
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        fun GraphRemoteData.toFormattedGraphData() = FormattedGraphData(
            formatter.parse(starDate)!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), user
        )
        Log.d("convert", starData.toString())
        return starData.map { it.toFormattedGraphData() }.toMutableList()
    }

}
