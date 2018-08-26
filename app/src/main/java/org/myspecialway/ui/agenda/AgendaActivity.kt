package org.myspecialway.ui.agenda

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.agenda_activity.*
import org.koin.android.architecture.ext.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.ui.main.MainScreenActivity

class AgendaActivity : BaseActivity() {

    private val viewModel: AgendaViewModel by viewModel()

    private lateinit var adapter: AgendaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agenda_activity)
        initToolbar()
        initList()
        render()
    }

    private fun initList() {
        adapter = AgendaAdapter { Log.d("adapter", it.title) }
        agendaRecyclerView.layoutManager = LinearLayoutManager(this@AgendaActivity)
        agendaRecyclerView.itemAnimator = DefaultItemAnimator()
        agendaRecyclerView.adapter = adapter
    }

    private fun initToolbar() {
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setDisplayShowHomeEnabled(true)
    }

    override fun render() {
        viewModel.listDataReady.observe(this, Observer { adapter.list = it ?: listOf() })
        viewModel.progress.observe(this, Observer { progress.visibility = it ?: View.GONE })
        viewModel.failure.observe(this,  Observer { handleError() })
        viewModel.currentSchedulePosition.observe(this, Observer { scrollToSchedule(it) })
    }

    private fun scrollToSchedule(it: Int?) {
        agendaRecyclerView.scrollToPosition(it ?: 0)
    }

    private fun handleError() {
        Toast.makeText(this@AgendaActivity, "לא מתאפשר להציג כרגע את מערכת השעות", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}