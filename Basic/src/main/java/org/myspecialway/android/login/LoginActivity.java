package org.myspecialway.android.login;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.myspecialway.android.R;
import org.myspecialway.android.mainscreen.MainScreenActivity;
import org.myspecialway.android.session.UserSession;
import org.myspecialway.android.utils.LoadingProgressDialog;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private AppCompatEditText usernameEditText;
    private AppCompatEditText passwordEditText;
    private LoadingProgressDialog loadingProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        loadingProgressDialog = new LoadingProgressDialog();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEND){
                login();
                return true;
            }
            return false;
        });

        findViewById(R.id.loginButton).setOnClickListener((v) -> login());
    }

    private void login(){
        hideKeyboard();

        String username = usernameEditText.getText().toString().trim();
        if(username.isEmpty()){
            usernameEditText.setError(getString(R.string.username_empty_error_message));
            return;
        }
        String password = passwordEditText.getText().toString();
        if(password.isEmpty()){
            passwordEditText.setError(getString(R.string.password_empty_error_message));
            return;
        }

        loadingProgressDialog.show(getSupportFragmentManager(), null);

        loginViewModel.login(username, password, new RequestCallback<UserSession>() {
            @Override
            public void onSuccess(UserSession result) {
                startActivity(new Intent(LoginActivity.this, MainScreenActivity.class));
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(LoginActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
                loadingProgressDialog.dismiss();
            }
        });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
