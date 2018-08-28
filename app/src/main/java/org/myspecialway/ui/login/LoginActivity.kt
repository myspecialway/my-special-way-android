package org.myspecialway.ui.login

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login_layout.*
import org.koin.android.architecture.ext.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.Navigation
import org.myspecialway.common.toast

class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)
        render()
        clickListeners()
        viewModel.checkLoggedIn()
    }

    private fun clickListeners() {
        loginButton.setOnClickListener {
            viewModel.login(AuthData().apply {
                username = "student"
                password = "Aa123456"
            })
        }
    }

    override fun render() {
        viewModel.progress.observe(this, Observer { progress.visibility = it ?: View.GONE })
        viewModel.loginLive.observe(this, Observer {  state ->
            when(state) {
                is LoginSuccess -> handleLoginSuccess(state)
                is LoginError -> toast(state.throwable.message ?: "Error")
            }
        })
    }

    private fun handleLoginSuccess(state: LoginSuccess) {
        if (state.success) Navigation.toMainActivity(this)
        else Navigation.toLoginActivity(this)
    }
}