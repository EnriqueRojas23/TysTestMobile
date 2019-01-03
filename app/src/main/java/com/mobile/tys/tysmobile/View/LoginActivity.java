package com.mobile.tys.tysmobile.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.tys.tysmobile.API.API;
import com.mobile.tys.tysmobile.API.APIService.SeguridadService;
import com.mobile.tys.tysmobile.Model.ValidateUser;
import com.mobile.tys.tysmobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    ValidateUser validateUser = null;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private CheckBox saveLoginCheckBox;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private SharedPreferences userLoggedPreferences;
    private SharedPreferences.Editor userLoggedPrefsEditor;

    private Boolean saveLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_login);
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        saveLoginCheckBox = findViewById(R.id.saveLoginCheckBox);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuildProgressBar(view);
                attemptLogin();
            }
        });


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        userLoggedPreferences = getSharedPreferences("userLoggedPrefs", MODE_PRIVATE);
        userLoggedPrefsEditor = userLoggedPreferences.edit();


        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin) {

            ConnectivityManager cm =
                    (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null ;

            //isConnected = false;

            if(isConnected==false)
            {
                Toast.makeText(LoginActivity.this, "No tiene conexión a internet", Toast.LENGTH_LONG).show();
            }

            mEmailView.setText(loginPreferences.getString("username", ""));
            mPasswordView.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);


            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
            intent.putExtra("mail", loginPreferences.getString("usrred", ""));
            startActivity(intent);
            finish();
        }
    }

    private void attemptLogin() {


        mEmailView.setError(null);
        mPasswordView.setError(null);

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));

            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);


            SeguridadService service =  API.getApi().create(SeguridadService.class);
            Call<ValidateUser> SeguridadCall = service.getLogin( email , password);
            SeguridadCall.enqueue(new Callback<ValidateUser>() {
                @Override
                public void onResponse(Call<ValidateUser> call, Response<ValidateUser> response) {

                    if (response.code() == 200) {
                        validateUser = response.body();

                        if (saveLoginCheckBox.isChecked()) {
                            loginPrefsEditor.putBoolean("saveLogin", true);
                            loginPrefsEditor.putString("username", email);
                            loginPrefsEditor.putString("password", password);
                            loginPrefsEditor.putString("usrred", validateUser.getUsr_str_red() );
                            loginPrefsEditor.commit();

                        } else {
                            loginPrefsEditor.clear();
                            loginPrefsEditor.commit();
                        }


                        userLoggedPrefsEditor.putInt("idusuario", validateUser.getUsr_int_id());
                        userLoggedPrefsEditor.commit();

                        Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                        intent.putExtra("mail", validateUser.getUsr_str_red());
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "No está autorizado", Toast.LENGTH_LONG).show();
                        progressBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ValidateUser> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.dismiss();
                }
            });
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }


    private void BuildProgressBar(View view) {
        progressBar = new ProgressDialog(LoginActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Validando Usuario y Password ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

    }
}
