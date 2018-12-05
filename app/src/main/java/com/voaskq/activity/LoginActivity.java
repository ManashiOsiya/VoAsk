package com.voaskq.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.voaskq.R;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.webservices.Api;
import com.voaskq.webservices.ApiFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView privacyPolicy, login, registration;
    EditText username, password;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

   boolean onetimelogin = true;

// String Userid = "428354007A49EF68EC4B6C969F41D61C";

    String Userid="";

    public static final String[] PERMISSION = {

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_NETWORK_STATE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPermissions(PERMISSION);
        initviews();
        setSharedPref();
        clickEvents();

    }

    private void checkPermissions(String... permission) {

        if (Build.VERSION.SDK_INT >= 23 && permission != null) {

            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p : permission) {
                result = ContextCompat.checkSelfPermission(this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {

                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 111);

            }
        }
    }

    private void setSharedPref() {
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        Userid = pref.getString("Userid", "bye");

        if (!Userid.equals("bye")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initviews() {

        privacyPolicy = findViewById(R.id.privacyPolicy);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registration = findViewById(R.id.registration);
    }

    private void clickEvents() {


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("one", "~~~~onetimelogin~~~" + onetimelogin);
                if (onetimelogin) {
                    getLogin();
                    onetimelogin = false;
                }

            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(in);
                finish();

            }
        });

    }

    private void getLogin() {

        final String Username = username.getText().toString();
        String Password = password.getText().toString();


        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(LoginActivity.this);
        progress_spinner.show();


        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getLogin(Username, Password);
        Log.e("url.......", "getLogin: " + call.request().url());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String output = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    output = stringBuilder.toString();
                    Log.e("new api===>>>>>>", "onResponse: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    progress_spinner.dismiss();
                    onetimelogin = true;
                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                        String user_id = jsonObject1.getString("user_id");
                        String Firstname = jsonObject1.getString("first_name");
                        String Lastname = jsonObject1.getString("last_name");

                        editor.putString("User_fl_name", Firstname+" "+Lastname);
                        editor.putString("Userid", user_id);
                        editor.commit();

                        Intent in = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(in);
                        finish();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    progress_spinner.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress_spinner.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        System.exit(0);
    }
}
