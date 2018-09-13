package org.myspecialway.ui.agenda

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.agenda_activity.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity

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

        adapter = AgendaAdapter {  }

        agendaRecyclerView.layoutManager = LinearLayoutManager(this@AgendaActivity)
        agendaRecyclerView.itemAnimator = DefaultItemAnimator()
        agendaRecyclerView.adapter = adapter
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.setHomeAsUpIndicator(R.drawable.back)
    }

    override fun render() {

        viewModel.progress.observe(this, Observer { progress.visibility = it ?: View.GONE })
        viewModel.failure.observe(this, Observer { handleError() })


        viewModel.agendaLive.observe(this, Observer { agenda ->
            when(agenda) {
                is ListData -> {
                    adapter.addData(
                            agenda.scheduleList
                            .toMutableList()
                                    .apply {
                                        add(agenda.scheduleList.size, SingleImageRes(R.drawable.gohome))
                                    }
                            .toList())
                }
                is CurrentSchedule -> scrollToSchedule(agenda.position)

            }
        })
    }

    private fun scrollToSchedule(it: Int?) {
        agendaRecyclerView.scrollToPosition(it ?: 0)
    }

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