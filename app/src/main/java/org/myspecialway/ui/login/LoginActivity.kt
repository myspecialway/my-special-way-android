package org.myspecialway.ui.login

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
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

    override fun render() =
            viewModel.loginState.observe(this, Observer { state ->
                when (state) {
                    is LoginStates.Success -> Navigation.toMainActivity(this)
                    is LoginStates.Failure -> handleError(state.throwable)
                    is LoginStates.Progress -> if (state.view == View.VISIBLE) {
                        loadingDialog.show()
                    } else {
                        loadingDialog.hide()
                    }


                }
            })

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
        disposable = RxView.clicks(loginButton)
                .filter {
                    handleInputError(
                            passwordTextFiled.text.toString(),
                            usernameTextFiled.text.toString())
                }
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

    override fun onStop() {
        super.onStop()
        viewModel.loginState.removeObservers(this)
    }
}