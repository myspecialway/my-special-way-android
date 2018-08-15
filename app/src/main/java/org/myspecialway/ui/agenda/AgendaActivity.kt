package org.myspecialway.ui.agenda
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.agenda_activity.*
import org.myspecialway.App
import org.myspecialway.R
import org.myspecialway.ui.login.RequestCallback
import org.myspecialway.ui.main.MainScreenActivity
import org.myspecialway.ui.main.ScheduleRepository
import org.myspecialway.schedule.gateway.ScheduleResponse

class AgendaActivity : AppCompatActivity()  {
    private var adapter: AgendaAdapter? = null
    private var scheduleRepository: ScheduleRepository? = null
    private val query = Constants.agendaQuery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agenda_activity)
        initToolbar()
        initList()
        scheduleRepository = App.instance?.scheduleRepository
        initAgendaDetails()
    }

    private fun initList() {
        adapter = AgendaAdapter { Log.d("adapter", "clicked on item")}
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

    private fun initAgendaDetails() {
        scheduleRepository!!.getSchedule(query, "", object : RequestCallback<List<ScheduleResponse.Schedule>> {
            override fun onSuccess(result: List<ScheduleResponse.Schedule>) {
                adapter?.list = result
            }

            override fun onFailure(t: Throwable) {
                Toast.makeText(this@AgendaActivity, "לא מתאפשר להציג כרגע את מערכת השעות", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@AgendaActivity, MainScreenActivity::class.java))
                finish()
            }
        })
    }
}

