package data.repository

import android.util.Log
import data.remote.FormattedGraphData
import data.remote.GraphRemoteList
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.util.*


object Year : GraphRepositoryInterface {
    override fun filterStars(
        comparableDate: LocalDate,
        localStarData: MutableList<FormattedGraphData>,
    ): GraphRemoteList {
        val starsInPeriod = mutableMapOf<Int, MutableList<FormattedGraphData>>()
        val calendar = Calendar.getInstance()
        val legend = mutableListOf<LocalDate>()
        legend.add(comparableDate)
        val filteredLocalStarDate = localStarData.filter { it.starDate.year == comparableDate.year }
        for (k in 1..calendar.getMaximum(Calendar.MONTH) + 1) {
            starsInPeriod[k] =
                filteredLocalStarDate.filter { it.starDate.monthValue == k }.toMutableList()
        }
        Log.d("starsInPeriod", starsInPeriod.toString())
        return GraphRemoteList(starsInPeriod, legend)
    }

    override fun checkForUpdate(comparableDate: LocalDate): Boolean {
        val nowDate = LocalDate.now()
        return nowDate.year == comparableDate.year
    }
}

object Seasons : GraphRepositoryInterface {
    private val listOfSeasons = listOf(
        listOf(Month.JANUARY, Month.FEBRUARY),
        listOf(Month.MARCH, Month.APRIL, Month.MAY),
        listOf(Month.JUNE, Month.JULY, Month.AUGUST),
        listOf(Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER),
        listOf(Month.DECEMBER)
    )

    override fun filterStars(
        comparableDate: LocalDate,
        localStarData: MutableList<FormattedGraphData>,
    ): GraphRemoteList {
        val starsInPeriod = mutableMapOf<Int, MutableList<FormattedGraphData>>()
        val legend = mutableListOf<LocalDate>()
        legend.add(comparableDate)
        val filteredLocalStarDate = localStarData.filter { it.starDate.year == comparableDate.year }
        for (k in 1..listOfSeasons.size) {
            starsInPeriod[k] =
                filteredLocalStarDate.filter { listOfSeasons[k - 1].contains(it.starDate.month) }
                    .toMutableList()
        }
        Log.d("starsInPeriod", starsInPeriod.toString())
        return GraphRemoteList(starsInPeriod, legend)
    }

    override fun checkForUpdate(comparableDate: LocalDate): Boolean {
        val nowDate = LocalDate.now()
        return nowDate.year == comparableDate.year
    }
}

object Month : GraphRepositoryInterface {
    override fun filterStars(
        comparableDate: LocalDate,
        localStarData: MutableList<FormattedGraphData>,
    ): GraphRemoteList {
        val starsInPeriod = mutableMapOf<Int, MutableList<FormattedGraphData>>()
        val legend = mutableListOf<LocalDate>()
        val calendar = Calendar.getInstance()
        calendar.set(
            comparableDate.year, comparableDate.monthValue - 1, comparableDate.dayOfMonth
        )
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val lastDayOfMonth =
            LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
                .toLocalDate()
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
        }

        var calendarStartWeek =
            LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
                .toLocalDate()
        var calendarEndWeek = calendarStartWeek.plusWeeks(1)
        legend.add(calendarStartWeek)
        var endPeriodForLegend = LocalDate.now()
        var key = 1
        while (calendarStartWeek < lastDayOfMonth) {
            starsInPeriod[key] = localStarData.filter {
                it.starDate < calendarEndWeek && (it.starDate >= calendarStartWeek)
            }.toMutableList()
            key += 1
            endPeriodForLegend = calendarEndWeek
            calendarEndWeek = calendarEndWeek.plusWeeks(1)
            calendarStartWeek = calendarStartWeek.plusWeeks(1)
        }
        legend.add(0, endPeriodForLegend)
        Log.d("starsInPeriod", starsInPeriod.toString())
        return GraphRemoteList(starsInPeriod, legend)
    }

    override fun checkForUpdate(comparableDate: LocalDate): Boolean {
        val nowDate = LocalDate.now()
        return nowDate.month == comparableDate.month
    }
}

object Week : GraphRepositoryInterface {
    override fun filterStars(
        comparableDate: LocalDate,
        localStarData: MutableList<FormattedGraphData>,
    ): GraphRemoteList {
        val starsInPeriod = mutableMapOf<Int, MutableList<FormattedGraphData>>()
        val legend = mutableListOf<LocalDate>()
        val calendar = Calendar.getInstance()
        calendar.set(
            comparableDate.year, comparableDate.monthValue - 1, comparableDate.dayOfMonth
        )
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
        }
        var calendarStartWeek =
            LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
                .toLocalDate()
        legend.add(calendarStartWeek)
        for (k in 1..calendar.getMaximum(Calendar.DAY_OF_WEEK)) {
            starsInPeriod[k] = localStarData.filter {
                it.starDate == calendarStartWeek
            }.toMutableList()
            calendarStartWeek = calendarStartWeek.plusDays(1)
        }
        legend.add(0, calendarStartWeek)
        Log.d("starsInPeriod", starsInPeriod.toString())
        return GraphRemoteList(starsInPeriod, legend)
    }

    override fun checkForUpdate(comparableDate: LocalDate): Boolean {
        val nowDate = LocalDate.now()
        return comparableDate > nowDate.minusWeeks(1) && comparableDate < nowDate.plusWeeks(1)
    }
}