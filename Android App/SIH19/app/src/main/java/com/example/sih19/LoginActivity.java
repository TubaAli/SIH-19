package com.example.sih19;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    // Tag used to cancel the request
    String  tag_string_req = "string_req";

    String url = "https://80fae82c.ngrok.io/db/mylogin/";
    SharedPreferences loginSP;

    @InjectView(R.id.input_first_name) EditText _firstNameText;
    @InjectView(R.id.input_last_name) EditText _lastNameText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        loginSP = getSharedPreferences("login",MODE_PRIVATE);
        if(loginSP.getBoolean("logged",false)){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        startService(new Intent(getApplicationContext(), MyService.class));
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String firstName = _firstNameText.getText().toString();
        final String lastName = _lastNameText.getText().toString();
        final String password = _passwordText.getText().toString();

        String query = "?first_name="+firstName+"&last_name="+lastName;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url+query, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                //pDialog.hide();
                onLoginSuccess();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String password = _passwordText.getText().toString();

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            _passwordText.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

