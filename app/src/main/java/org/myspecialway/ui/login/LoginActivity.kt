package org.myspecialway.ui.login

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.constraint.ConstraintLayout
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
import java.net.UnknownHostException

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
                .subscribe {status ->
                    when (status) {
                        OPEN -> onKeyboardChange(appIcon, 0.dpToPixels(this))
                        CLOSED -> onKeyboardChange(appIcon, 72.dpToPixels(this))
                    }
                })
    }

    private fun observeInputFields() {
        composite?.add(RxView.clicks(loginButton)
                .filter { handleInputError(passwordTextFiled.text.toString(), usernameTextFiled.text.toString()) }
                .subscribe {
                    hideKeyboard()
                    viewModel.login(LoginAuthData().apply {
                        username = usernameTextFiled.text.toString()
                        password = passwordTextFiled.text.toString()
                    })
                })
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
            is UnknownHostException -> showLoginError {
                content.text = "מצטערים, אירעה תקלה כללית!"
                closeIconClickListener { dialog?.dismiss() }
            }

            is HttpException -> showLoginError { closeIconClickListener { dialog?.dismiss() } }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        composite?.dispose()
    }

    private fun onKeyboardChange(view: View, top: Int) {
        if (top == 0) animateLogo(view, -250f)
        else animateLogo(view, 0f)

        val param = view.layoutParams as ConstraintLayout.LayoutParams
        param.setMargins(0, top, 0, 0)
        view.layoutParams = param
    }

    private fun animateLogo(view: View, y: Float) {
        view.animate()
                .translationY(y)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
    }
}