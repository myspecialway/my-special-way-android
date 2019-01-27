package org.myspecialway.ui.inactive

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.Navigation
import org.myspecialway.ui.agenda.AgendaState
import org.myspecialway.ui.shared.AgendaViewModel

class InactiveScreenActivity : BaseActivity() {


    private val viewModel: AgendaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inactive_layout)
        render()
    }

    fun onSettingsClick(view: View) {
        Navigation.toSettingsActivity(this, "")
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDailySchedule()
    }

    override fun render() {

        viewModel.states.observe(this, Observer { state ->
            when (state) {
                is AgendaState.CurrentSchedule-> {
                    Navigation.toMainActivity(this)
                }
                is AgendaState.ListState-> {
                    Navigation.toMainActivity(this)
                }

            }
        })
    }

    }
