package org.myspecialway.ui.agenda

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.agenda_activity.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.ViewType
import org.myspecialway.ui.shared.AgendaViewModel
import org.myspecialway.ui.shared.CurrentSchedule
import org.myspecialway.ui.shared.ListState

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
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.setHomeAsUpIndicator(R.drawable.back)
    }

    override fun render() {
        viewModel.progress.observe(this, Observer { progress.visibility = it ?: View.GONE })
        viewModel.failure.observe(this, Observer { handleError() })
        viewModel.states.observe(this, Observer { state ->
            when (state) {
                is ListState -> adapter.addData(addSingleImage(state))
                is CurrentSchedule -> agendaRecyclerView.scrollToPosition(state.position)
            }
        })
    }

    private fun addSingleImage(state: ListState): List<ViewType> =
            state.scheduleList
                    .toMutableList()
                    .apply { add(state.scheduleList.size, SingleImageRes(R.drawable.gohome)) }
                    .toList()

    private fun handleError() {
        Toast.makeText(this@AgendaActivity, "לא מתאפשר להציג כרגע את מערכת השעות", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}