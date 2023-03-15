package ui.graph_screen

import domain.entity.GraphList
import domain.entity.period_state.PeriodState
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ui.base.BaseView

interface GraphView : BaseView {
    @AddToEndSingle
    fun showGraph(periodState: PeriodState, graphResponse: GraphList)

    @AddToEndSingle
    fun setLoading(loading: Boolean)

    @AddToEndSingle
    fun setPeriodState(state: PeriodState)

    @OneExecution
    fun showMessage(errorMessage: Int)
}