package ui.graph_screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.githubstars.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import domain.entity.GraphList
import domain.entity.period_state.PeriodState
import moxy.ktx.moxyPresenter
import ui.base.BaseActivity
import java.util.*


@SuppressLint("StaticFieldLeak")

class GraphActivity : BaseActivity(), GraphView {

    private val graphPresenter by moxyPresenter {
        GraphPresenter(
            intent.getStringExtra(EXTRA_USERNAME)!!,
            intent.getStringExtra(EXTRA_REPO_NAME)!!,
            intent.getIntExtra(EXTRA_STARGAZERS_COUNT, 0)
        )
    }

    private lateinit var barData: BarData

    private lateinit var barChart: BarChart

    private lateinit var barDataSet: BarDataSet

    private lateinit var barEntriesList: MutableList<BarEntry>

    private lateinit var progressBar: ProgressBar

    private lateinit var stateButton: Button

    companion object {
        private const val EXTRA_USERNAME = "userName"
        private const val EXTRA_REPO_NAME = "repoName"
        private const val EXTRA_STARGAZERS_COUNT = "stargazers_count"
        private val stateTextMap = mapOf(
            PeriodState.YEAR to R.string.button_graph_year,
            PeriodState.SEASON to R.string.button_graph_seasons,
            PeriodState.MONTH to R.string.button_graph_month,
            PeriodState.WEEK to R.string.button_graph_week
        )
        fun createIntent(context: Context, userName: String, repoName: String, stargazersCount: Int): Intent {
            return Intent(context, GraphActivity::class.java)
                .putExtra(EXTRA_USERNAME, userName)
                .putExtra(EXTRA_REPO_NAME, repoName)
                .putExtra(EXTRA_STARGAZERS_COUNT, stargazersCount)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        barChart = findViewById(R.id.chart_stars)
        progressBar = findViewById(R.id.progressBarGraph)
        progressBar.isVisible = true
        graphPresenter.getStarsData()
        stateButton = findViewById(R.id.button_state)
        stateButton.setOnClickListener {
            graphPresenter.requestPeriodChange()
        }
        val nextButton = findViewById<ImageView>(R.id.button_next_period)
        nextButton.setOnClickListener {
            graphPresenter.nextPeriod()
        }
        val prefButton = findViewById<ImageView>(R.id.button_pref_period)
        prefButton.setOnClickListener {
            graphPresenter.prefPeriod()
        }
    }

    override fun showGraph(periodState: PeriodState, graphResponse: GraphList) {
        barEntriesList = mutableListOf()
        graphResponse.starsInPeriod.forEach { (t, u) ->
            barEntriesList.add(
                BarEntry(
                    t.toFloat(), u.size.toFloat()
                )
            )
        }
        val legendByPeriod = when (periodState) {
            PeriodState.YEAR -> {
                graphResponse.legend[0].year
            }
            PeriodState.MONTH -> {
                "${graphResponse.legend[1]} - ${
                    graphResponse.legend[0]
                }"
            }
            PeriodState.WEEK -> {
                "${graphResponse.legend[1]} - ${
                    graphResponse.legend[0]
                }"
            }
            PeriodState.SEASON -> {
                graphResponse.legend[0].year
            }
        }
        barDataSet = BarDataSet(barEntriesList, legendByPeriod.toString())
        barData = BarData(barDataSet)
        barChart.data = barData
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f
        barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.textSize = 16f
        barChart.legend.textSize = 16f
        barChart.xAxis.labelCount = barDataSet.entryCountStacks
        barChart.xAxis.granularity = 1f
        barChart.description.isEnabled = true
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.isEnabled = false
        barChart.description.textSize = 16f
        barChart.setFitBars(true)
        barChart.data.setValueFormatter(ValueToIntFormatter())
        when (periodState) {
            PeriodState.YEAR -> {
                barChart.description.text = getString(R.string.description_number_year)
                barChart.xAxis.valueFormatter = XAxisToIntFormatter()
            }
            PeriodState.SEASON -> {
                barChart.description.text = getString(R.string.description_number_seasons)
                barChart.xAxis.valueFormatter = XAxisSeasonsFormatter(resources)
            }
            PeriodState.MONTH -> {
                barChart.description.text = getString(R.string.description_number_month)
                barChart.xAxis.valueFormatter = XAxisToIntFormatter()
            }
            PeriodState.WEEK -> {
                barChart.description.text = getString(R.string.description_number_week)
                barChart.xAxis.valueFormatter = XAxisWeekFormatter()
            }
        }
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.notifyDataSetChanged()
        barChart.invalidate()
    }

    override fun setLoading(loading: Boolean) {
        progressBar.isVisible = loading
        barChart.isVisible = !loading
    }

    override fun showMessage(errorMessage: Int) {
        Toast.makeText(
            this, errorMessage, Toast.LENGTH_SHORT
        ).show()
    }

    override fun setPeriodState(state: PeriodState) {
        stateButton.setText(stateTextMap[state]!!)
    }
}

class XAxisToIntFormatter : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString()
    }
}


class XAxisSeasonsFormatter(private val resources: Resources) : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val stringArray = resources.getStringArray(R.array.xAxis_seasons)
        return when (value.toInt()) {
            1 -> stringArray[0]
            2 -> stringArray[1]
            3 -> stringArray[2]
            4 -> stringArray[3]
            5 -> stringArray[4]
            else -> {
                value.toInt().toString()
            }
        }
    }
}

class XAxisWeekFormatter : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val dayOfWeek = if (value <= 7) {
            value.toInt()
        } else {
            1
        }
        return dayOfWeek.toString()
    }
}

class ValueToIntFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val formattedValue = if (value > 0) {
            value.toInt().toString()
        } else {
            ""
        }
        return formattedValue
    }
}
