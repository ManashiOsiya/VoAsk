package com.voaskq.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.voaskq.R;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.webservices.Api;
import com.voaskq.webservices.ApiFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    TextView privacyPolicy, login, registration, uploadImage;
    EditText firstname, lastname, username, password;
    ImageView previewImage, delete;
    LinearLayout showImageLinear;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private File mFile;
    private String FileName = "";
    Uri imageUri;
    CheckBox termsckeck;

    String tag = "reg";

    String Firstname;
    String Lastname;
    String Username;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initviews();
        setSharedPref();
        clickEvents();
    }

    private void setSharedPref() {
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
    }

    private void initviews() {
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        privacyPolicy = findViewById(R.id.privacyPolicy);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registration = findViewById(R.id.registration);
        uploadImage = findViewById(R.id.uploadImage);
        previewImage = findViewById(R.id.previewImage);
        delete = findViewById(R.id.delete);
        showImageLinear = findViewById(R.id.showImageLinear);
        termsckeck = findViewById(R.id.checkBox);
    }

    private void clickEvents() {

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfile(null);
                showImageLinear.setVisibility(View.GONE);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromCamera();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(termsckeck.isChecked()){
                    setValidaition();
                }else{
                    Toast.makeText(RegistrationActivity.this, "Please Accept Terms of Service  ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void setValidaition() {

        Firstname = firstname.getText().toString();
        Lastname = lastname.getText().toString();
        Username = username.getText().toString();
        Password = password.getText().toString();

        getRegistration();


//        if (Username.equals("")) {
//            Toast.makeText(this, "Please enter username", Toast.LENGTH_LONG).show();
//
//        } else if (Email.equals("")) {
//            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
//
//        } else if (Phoneno.equals("")) {
//
//            Toast.makeText(this, "Please enter Phone Number", Toast.LENGTH_LONG).show();
//
//        } else if (Job.equals("")) {
//            Toast.makeText(this, "Please enter Job Title", Toast.LENGTH_LONG).show();
//
//        } else if (Password.equals("")) {
//            Toast.makeText(this, "Please enter Password", Toast.LENGTH_LONG).show();
//
//        } else if (Confirmpassword.equals("")) {
//
//            Toast.makeText(this, "Please Confirm your Password", Toast.LENGTH_LONG).show();
//
//        } else if (!Password.equals(Confirmpassword)) {
//
//            Toast.makeText(this, "Your Password and Confirm Password not matched", Toast.LENGTH_LONG).show();
//
//            password.setText("");
//            confirmpassword.setText("");
//        } else {
//            getRegistration();
//        }
    }

    private void getRegistration() {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(RegistrationActivity.this);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);

        final Map<String, RequestBody> map = new HashMap<>();

        RequestBody newFirstname = RequestBody.create(MediaType.parse("multipart/form-data"), Firstname);
        RequestBody newLastname = RequestBody.create(MediaType.parse("multipart/form-data"), Lastname);
        RequestBody newUsername = RequestBody.create(MediaType.parse("multipart/form-data"), Username);
        RequestBody newPassword = RequestBody.create(MediaType.parse("multipart/form-data"), Password);

//        if (getFile() != null) {
//
//            Uri selectedUri = Uri.fromFile(getFile());
//            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
//            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
//            RequestBody reqBodyImageOfUser = RequestBody.create(MediaType.parse(mimeType), getFile());
//            RequestBody requestBody = RequestBody.create(MediaType.parse(""), getFile());
//            map.put("image\"; filename=\"" + getFile().getName() + "\"", reqBodyImageOfUser);
//            Log.e(tag, "IMAGE: " + map);
//        }

        MultipartBody.Part ImageToUpload = null;
        MultipartBody.Part imagePart =null;
        {
            if (getFile() != null) {
                Uri selectedUri = Uri.fromFile(getFile());
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                RequestBody reqBodyfileOfUser = RequestBody.create(MediaType.parse(mimeType), getFile());
                ImageToUpload = MultipartBody.Part.createFormData("images", getFile().getName(), reqBodyfileOfUser);
                Log.d("Imageuploadddddd", getFile().getName());
                imagePart = ImageToUpload;
            }
        }

        Call<ResponseBody> call = api.getRegistration(newUsername,
                newPassword,
                newFirstname,
                newLastname,
                imagePart);

        Log.e(tag, "getRegistration: " + call.request().url());

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

                    Log.e(tag, "onResponse: " + output);

                    JSONObject jsonObject = new JSONObject(output);

                    progress_spinner.dismiss();

                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {
                        Toast.makeText(getApplicationContext(), "Registration done", Toast.LENGTH_SHORT).show();

                        JSONObject json1 = jsonObject.getJSONObject("result");

                        Iterator iterator = json1.keys();
                        String key = (String) iterator.next();

                        String picture = "";

                        if (key.equalsIgnoreCase("picture")) {
                            picture = json1.getString("picture");
                        }

                        Log.e(tag, " picture :" + picture);

                        String user_name = json1.getString("user_name");
                        //    String password   =     jsonObject.getString("password");
                        String first_name = json1.getString("first_name");
                        String last_name = json1.getString("last_name");
//                        String picture = json1.getString("picture");
                        String user_id = json1.getString("user_id");

                     //   editor.putString("user_name", user_name);
                     //   //    editor.putString("password", password);
                     //   editor.putString("first_name", first_name);
                     //   editor.putString("last_name", last_name);
                     //   editor.putString("picture", picture);

                        editor.putString("Username", Username);
                        editor.putString("Userid", user_id);
                        editor.commit();

                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {

                        String messsage = jsonObject.getString("message");
                        Toast.makeText(RegistrationActivity.this, messsage, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progress_spinner.dismiss();

                    Toast.makeText(RegistrationActivity.this, "Exception " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress_spinner.dismiss();
            }
        });

    }


    private void pickImageFromCamera() {


        Log.e(tag, "11111111");

        FileName = System.currentTimeMillis() + ".jpg";
        imageUri = getPhotoFileUri(FileName);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

        CropImage.activity(imageUri)
                .start(this);




    }

    public Uri getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "");
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            }

            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File file = new File(resultUri.getPath());
                setfile(file);
                Picasso.with(RegistrationActivity.this)
                        .load(file)
                        .error(R.mipmap.ic_launcher)
                        .fit()
                        .into(previewImage);

                showImageLinear.setVisibility(View.VISIBLE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void setfile(File file) {
        this.mFile = file;
    }

    public File getFile() {
        return mFile;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


}
