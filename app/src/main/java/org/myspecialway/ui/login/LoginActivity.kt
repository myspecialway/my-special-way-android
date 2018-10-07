package org.myspecialway.ui.login

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.rxkotlin.toObservable
import kotlinx.android.synthetic.main.activity_login_layout.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.*
import org.myspecialway.common.KeyboardStatus.Closed
import org.myspecialway.common.KeyboardStatus.Open
import org.myspecialway.ui.agenda.EMPTY_TEXT
import retrofit2.HttpException

class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModel()

    private val loadingDialog by lazy { createLoadingDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)
        viewModel.checkIfLoggedIn()
        render()
        observeKeyboard()
        observeInputFields()
    }

    override fun render() {
        // Observe data flow
        viewModel.loginLive.observe(this, Observer { state ->
            when (state) {
                is LoginSuccess -> Navigation.toMainActivity(this)
            }
        })

        // Observe failure
        viewModel.failure.observe(this, Observer { error ->
            handleError(error ?: Throwable())
        })

        // Observe progress
        viewModel.progress.observe(this, Observer { progress ->
            if (progress == View.VISIBLE) loadingDialog.show()
            else loadingDialog.hide()
        })
    }

    private fun handleError(throwable: Throwable) = when (throwable) {
        is HttpException -> showLoginError { closeIconClickListener { dialog?.dismiss() } }
        else -> showLoginError {
            content.text = getString(R.string.login_error_general)
            closeIconClickListener { dialog?.dismiss() }
        }
    }

    private fun observeKeyboard() {
        composite?.add(KeyboardManager(this)
                .status()
                .subscribe { status -> onKeyboardChangeAnimation(status) })
    }

    private fun observeInputFields() {
        RxView.clicks(loginButton)
                .filter { handleInputError(passwordTextFiled.text.toString(), usernameTextFiled.text.toString()) }
                .subscribe {
                    hideKeyboard()
                    viewModel.login(LoginAuthData().apply {
                        username = usernameTextFiled.text.toString()
                        password = passwordTextFiled.text.toString()
                    })
                }
    }

    private fun handleInputError(pass: CharSequence, username: String): Boolean {
        // check user
        if (username.isEmpty()) usernameWrapper.error = getString(R.string.user_error) else usernameWrapper.error = EMPTY_TEXT

        // check password
        if (pass.isEmpty()) passwordWrapper.error = getString(R.string.pass_error) else passwordWrapper.error = EMPTY_TEXT

        return username.isNotEmpty() && pass.isNotEmpty()
    }


    private fun onKeyboardChangeAnimation(status: KeyboardStatus) = when (status) {
        Open -> {
            passwordLayout.animateY(-370f)
            appIcon.animateY(-470f)
        }
        Closed -> {
            passwordLayout.animateY(0f)
            appIcon.animateY(0f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        composite?.dispose()
    }
}