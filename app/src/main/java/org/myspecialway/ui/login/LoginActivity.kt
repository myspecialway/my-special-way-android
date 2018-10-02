package org.myspecialway.ui.login

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_login_layout.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.*
import org.myspecialway.common.KeyboardStatus.CLOSED
import org.myspecialway.common.KeyboardStatus.OPEN
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

    private fun observeKeyboard() {
        composite?.add(KeyboardManager(this)
                .status()
                .subscribe {
                    when (it) {
                        OPEN -> onKeyboardChangeAnimation(OPEN)
                        CLOSED -> onKeyboardChangeAnimation(CLOSED)
                    }
                })
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
        if (username.isEmpty()) usernameWrapper.error = getString(R.string.user_error)
        else usernameWrapper.error = EMPTY_TEXT

        // check password
        if (pass.isEmpty()) passwordWrapper.error = getString(R.string.pass_error)
        else passwordWrapper.error = EMPTY_TEXT

        return username.isNotEmpty() && pass.isNotEmpty()
    }

    override fun render() {
        viewModel.loginLive.observe(this, Observer { state ->
            when (state) {
                is LoginSuccess -> Navigation.toMainActivity(this)
                is LoginError -> handleError(state.throwable)
            }
        })

        viewModel.progress.observe(this, Observer {
            if (it == View.VISIBLE) loadingDialog.show()
            else loadingDialog.hide()
        })
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> showLoginError { closeIconClickListener { dialog?.dismiss() } }
            else -> showLoginError {
                content.text = ".לא הצלחנו להכניס אותך למערכת. אנא נסה שוב"
                closeIconClickListener { dialog?.dismiss() }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        composite?.dispose()
    }

    private fun onKeyboardChangeAnimation(status: KeyboardStatus) {
        when (status) {
             OPEN -> {
                passwordLayout.animateY(-370f)
                appIcon.animateY(-470f)
            }
            CLOSED -> {
                passwordLayout.animateY(0f)
                appIcon.animateY(0f)
            }
        }
    }

    private fun View.animateY(y: Float) =
            animate()
                    .translationY(y)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()

}