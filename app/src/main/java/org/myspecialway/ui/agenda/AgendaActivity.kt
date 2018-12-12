package org.myspecialway.ui.agenda

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.agenda_activity.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.ViewType
import org.myspecialway.ui.shared.*

class AgendaActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()

    private lateinit var adapter: AgendaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agenda_activity)
        initToolbar()
        initList()
        viewModel.getDailySchedule()
        render()
    }

    private fun initList() {
        adapter = AgendaAdapter { /** Implement Items Clicks Here */ }
        agendaRecyclerView.layoutManager = LinearLayoutManager(this@AgendaActivity)
        agendaRecyclerView.adapter = adapter
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        backImage.setOnClickListener{finish()}
    }

    override fun render() =
            viewModel.states.observe(this, Observer { state ->
                when (state) {
                    is AgendaState.ListState -> adapter.addData(addSingleImage(state))
                    is AgendaState.CurrentSchedule -> agendaRecyclerView.scrollToPosition(state.position)
                    is AgendaState.Progress -> progress.visibility = state.progress
                    is AgendaState.Failure -> handleError()
                }
            })

    private fun addSingleImage(state: AgendaState.ListState): List<ViewType> =
            state.scheduleList
                    .toMutableList()
                    .apply { add(state.scheduleList.size, SingleImageRes(R.drawable.gohome)) }
                    .toList()

    private fun handleError() {
        Toast.makeText(this@AgendaActivity, getString(org.myspecialway.R.string.scheduleErrorToast), Toast.LENGTH_LONG).show()
        finish()
    }
}