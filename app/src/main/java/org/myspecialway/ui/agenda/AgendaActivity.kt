package org.myspecialway.ui.agenda

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.agenda_activity.*
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject
import org.myspecialway.App
import org.myspecialway.R
import org.myspecialway.ui.login.RequestCallback
import org.myspecialway.ui.main.MainScreenActivity
import org.myspecialway.ui.main.ScheduleRepository
import org.myspecialway.schedule.gateway.ScheduleResponse

class AgendaActivity : AppCompatActivity() {

    private val viewModel: AgendaViewModel by viewModel()
    private lateinit var adapter: AgendaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agenda_activity)
        initToolbar()
        initList()
        observeData()
    }

    private fun observeData() {
        viewModel.uiData.observe(this, Observer { adapter.list = it ?: listOf() })
        viewModel.failure.observe(this, Observer { handleError() })
    }

    private fun handleError() {
        Toast.makeText(this@AgendaActivity, "לא מתאפשר להציג כרגע את מערכת השעות", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@AgendaActivity, MainScreenActivity::class.java))
        finish()
    }

    private fun initList() {
        adapter = AgendaAdapter { Log.d("adapter", it.lesson.title) }
        val mLayoutManager = LinearLayoutManager(this@AgendaActivity)
        agendaRecyclerView.layoutManager = mLayoutManager
        agendaRecyclerView.itemAnimator = DefaultItemAnimator()
        agendaRecyclerView.adapter = adapter
    }

    private fun initToolbar() {
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setDisplayShowHomeEnabled(true)
    }
}
