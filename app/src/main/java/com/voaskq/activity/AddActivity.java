package com.voaskq.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.voaskq.R;
import com.voaskq.adapter.AddVoteNAskAdapter;
import com.voaskq.modal.AddVoteNAsk;
import com.voaskq.webservices.Api;
import com.voaskq.webservices.ApiFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    EditText description;
    Spinner spinner_select, spinner_category;
    String[] spinner_item = {"Select post type", "Vote", "Ask"};
    String[] spinner_category_item = {"Select Category", "Beauty", "Fashion", "Sport", "Entertainment", "Science", "History", "For fun", "Education", "Random", "World"};
    String tag = "add", SelectedSpinnerItem = "", SelectedCategoryItem = "";

    String Description, Userid;
    ImageView back, camera;
    TextView submit_vote;
    private String FileName = "";
    Uri imageUri;
    ArrayList<File> file_arr = null;
    RecyclerView recyclerView;
    AddVoteNAskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        context = this;
        initviews();
        setSharedPref();
        clickEvents();
    }

    private void setSharedPref() {

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        Userid = pref.getString("Userid", "bye");
    }

    private void initviews() {

        description = findViewById(R.id.description);
        spinner_select = findViewById(R.id.spinner_select);
        spinner_category = findViewById(R.id.spinner_category);
        spinner_select.setOnItemSelectedListener(AddActivity.this);
        spinner_category.setOnItemSelectedListener(AddActivity.this);
        spinner_select.setOnItemSelectedListener(this);
        spinner_category.setOnItemSelectedListener(this);
        submit_vote = findViewById(R.id.submit_vote);
        back = findViewById(R.id.back);
        camera = findViewById(R.id.camera);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        setAdapter();
    }

    private void clickEvents() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSubmit();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SelectedSpinnerItem.equalsIgnoreCase("") /*|| SelectedCategoryItem.equalsIgnoreCase("")*/) {
                    Toast.makeText(AddActivity.this, "First select post type ", Toast.LENGTH_SHORT).show();
                } else {

                    if (SelectedSpinnerItem.equalsIgnoreCase("Ask")) {

                        if (file_arr.size() >= 1) {
                            Toast.makeText(context, "can't add more for ask", Toast.LENGTH_SHORT).show();
                        } else {
                            pickImageFromCamera();
                        }
                    } else {
                        pickImageFromCamera();
                    }
                }
            }
        });
    }

    private void getSubmit() {

        Description = description.getText().toString();
        uploadFile(file_arr);

    }

    private void setAdapter() {

        ArrayAdapter select_adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, spinner_item);
        select_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_select.setAdapter(select_adapter);

        ArrayAdapter select_adapter_category = new ArrayAdapter(context, android.R.layout.simple_spinner_item, spinner_category_item);
        select_adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(select_adapter_category);

    }

    private void refershData() {
        // description.setText("");
        file_arr = new ArrayList<>();
        adapter = new AddVoteNAskAdapter(file_arr, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setListner(new AddVoteNAskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int Pos) {
                switch (v.getId()) {
                    case R.id.delete:

                        Log.e(tag, "Delete click"+Pos);

                        if(file_arr.size()>0){

                            file_arr.remove(Pos);
                            recyclerView.removeViewAt(Pos);
                            adapter.notifyItemRemoved(Pos);
                            adapter.notifyItemRangeChanged(Pos, file_arr.size());

                        }

                        break;
                }
            }
        });


    }


    void uploadFile(ArrayList<File> imagefile) {

        RequestBody newDescription = RequestBody.create(MediaType.parse("multipart/form-data"), Description);
        RequestBody newPostType = RequestBody.create(MediaType.parse("multipart/form-data"), SelectedSpinnerItem);
        RequestBody newSelectedCategoryItem = RequestBody.create(MediaType.parse("multipart/form-data"), SelectedCategoryItem);
        Api api = ApiFactory.getClient().create(Api.class);

        Call<ResponseBody> call = null;

        if (SelectedSpinnerItem.equalsIgnoreCase("Vote")) {

            MultipartBody.Part ImageToUpload = null;
            MultipartBody.Part[] imagePart = new MultipartBody.Part[imagefile.size()];

            for (int i = 0; i < imagefile.size(); i++) {
                if (imagefile.get(i) != null) {
                    Uri selectedUri = Uri.fromFile(imagefile.get(i));
                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    RequestBody reqBodyfileOfUser = RequestBody.create(MediaType.parse(mimeType), imagefile.get(i));
                    ImageToUpload = MultipartBody.Part.createFormData("postimages_" + i, imagefile.get(i).getName(), reqBodyfileOfUser);

                    imagePart[i] = ImageToUpload;
                }
            }
            Log.e("one", "imagePart.length~~~" + imagePart.length);

            call = api.addVote(Userid, newPostType, newDescription, newSelectedCategoryItem, imagePart);

        } else {                                                                        // for ask

            MultipartBody.Part ImageToUpload = null;
            MultipartBody.Part[] imagePart = new MultipartBody.Part[imagefile.size()];

            for (int i = 0; i < imagefile.size(); i++) {
                if (imagefile.get(i) != null) {
                    Uri selectedUri = Uri.fromFile(imagefile.get(i));
                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    RequestBody reqBodyfileOfUser = RequestBody.create(MediaType.parse(mimeType), imagefile.get(i));
                    ImageToUpload = MultipartBody.Part.createFormData("questionimage", imagefile.get(i).getName(), reqBodyfileOfUser);

                    imagePart[i] = ImageToUpload;
                }
            }
            Log.e("one", "imagePart.length~~~" + imagePart.length);


            call = api.addAsk(Userid, newPostType, newDescription, newSelectedCategoryItem, imagePart);
        }
        Log.e(tag, SelectedSpinnerItem + "~~~header ask and vote~~~~~" + call.request().url());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = null;
                try {
                    Log.e("response11", "res~~~~~~" + response.toString());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    output = stringBuilder.toString();
                    Log.e("new api ===>>>>>>", "onResponse: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spinner_select:
                if (position == 0) {
                    SelectedSpinnerItem = "";
                } else {
                    SelectedSpinnerItem = spinner_item[position];
                }
                refershData();
                break;

            case R.id.spinner_category:
                if (position == 0) {
                    SelectedCategoryItem = "";
                } else {
                    SelectedCategoryItem = spinner_category_item[position];
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void pickImageFromCamera() {

        FileName = System.currentTimeMillis() + ".png";
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
                file_arr.add(file);
                adapter.notifyDataSetChanged();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

}
