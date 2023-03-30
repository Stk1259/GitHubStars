package ui.graph_screen

import domain.data.StarList
import domain.data.period_state.PeriodState
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ui.base.BaseView

interface GraphView : BaseView {
    @AddToEndSingle
    fun showGraph(periodState: PeriodState, graphResponse: StarList)

    @AddToEndSingle
    fun setLoading(loading: Boolean)

    @AddToEndSingle
    fun setPeriodState(state: PeriodState)

    @OneExecution
    fun showMessage(errorMessage: Int)
}