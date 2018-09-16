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
        composite?.add(KeyboardManager(this).status().subscribe {
            when (it) {
                OPEN -> onKeyboardChange(appIcon, 0.dpToPixels(this))
                CLOSED -> onKeyboardChange(appIcon, 72.dpToPixels(this))
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
        else usernameWrapper.error = ""

        // check password
        if (pass.isEmpty()) passwordWrapper.error = getString(R.string.pass_error)
        else passwordWrapper.error = ""

        return username.isNotEmpty() && pass.isNotEmpty()
    }

    override fun render() {
        viewModel.loginLive.observe(this, Observer { state ->
            when (state) {
                is LoginSuccess -> Navigation.toMainActivity(this)
                is LoginError -> showLoginError {
                    cancelable = true
                    isBackGroundTransparent = false
                    closeIconClickListener { dialog?.dismiss() }
                }
            }
        })

        viewModel.progress.observe(this, Observer {
            if (it == View.VISIBLE) loadingDialog.show()
            else loadingDialog.hide()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        composite?.dispose()
    }

    private fun onKeyboardChange(view: View, top: Int) {
        if (top == 0) animateLogo(view, -250f)
        else animateLogo(view, 0f)

        val param = view.layoutParams as ConstraintLayout.LayoutParams
        param.setMargins(0, top, 10, 10)
        view.layoutParams = param
    }

    private fun animateLogo(view: View, y: Float) {
        view.animate()
                .translationY(y)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
    }
}