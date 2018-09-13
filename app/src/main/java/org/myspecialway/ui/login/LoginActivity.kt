package org.myspecialway.ui.login

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_login_layout.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.myspecialway.R
import org.myspecialway.common.BaseActivity
import org.myspecialway.common.Navigation
import org.myspecialway.common.enable
import org.myspecialway.common.hideKeyboard
import java.util.concurrent.TimeUnit

class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)
        viewModel.checkLoggedIn()
        render()
        observeInputFields()
        clickListeners()
    }

    private val composite: CompositeDisposable? = CompositeDisposable()

    private fun observeInputFields() {
        //disable error
        val password = RxTextView.textChanges(passwordTextFiled)
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())

        val user = RxTextView.textChanges(usernameTextFiled)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())

        composite?.add(Observable.combineLatest<CharSequence, CharSequence, Boolean>(password, user, BiFunction { pass, userName ->
            showPasswordError(pass)
            pass.isNotEmpty() && userName.isNotEmpty()
        }).subscribe {
            when (it) {
                true -> loginButton.enable(true)
                false -> loginButton.enable(false)
            }
        })
    }

    private fun showPasswordError(pass: CharSequence) {
        if (pass.isEmpty()) passwordWrapper.error = "נא להזין סיסמא" else passwordWrapper.error = ""
    }

    private fun clickListeners() {
        loginButton.setOnClickListener {
            hideKeyboard()
            viewModel.login(LoginAuthData().apply {
                username = usernameTextFiled.text.toString()
                password = passwordTextFiled.text.toString()
            })
        }
    }

    override fun render() {
        viewModel.progress.observe(this, Observer { progress.visibility = it ?: View.GONE })
        viewModel.loginLive.observe(this, Observer { state ->
            when (state) {
                is LoginSuccess -> Navigation.toMainActivity(this)
                is LoginError -> showLoginError {
                    cancelable = false
                    isBackGroundTransparent = false
                    closeIconClickListener { dialog?.dismiss() }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        composite?.dispose()
    }
}