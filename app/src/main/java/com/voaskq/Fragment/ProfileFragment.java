package com.voaskq.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.voaskq.R;
import com.voaskq.activity.LoginActivity;
import com.voaskq.adapter.MainAskAdapter;
import com.voaskq.adapter.MainHomeAdapter;
import com.voaskq.helper.Constant;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.modal.MainAsk;
import com.voaskq.modal.MainHome;
import com.voaskq.webservices.Api;
import com.voaskq.webservices.ApiFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    String tag = "Profile";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    View view;
    Context mContext;
    TextView logout, profile, vote, ask,submit;
    CircleImageView profile_image;
    File mFile;
    String FileName = "";
    Uri imageUri;
    EditText firstname, lastname, email, password,about;
    String Firstname,Lastname,Email,Password,About,Userid,Username;
    LinearLayout ProfileLinear,VoteLinear,AskLinear;

    RecyclerView vote_recyclerView;
    ArrayList<MainHome> vote_list;

    RecyclerView ask_recyclerView;
    ArrayList<MainAsk> ask_list;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = inflater.getContext();

        initviews();
        setSharedPref();
        clickEvents();
        getProfileData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initviews() {
        logout = view.findViewById(R.id.logout);
        ProfileLinear = view.findViewById(R.id.ProfileLinear);
        VoteLinear  = view.findViewById(R.id.VoteLinear);
        profile = view.findViewById(R.id.profile);
        vote = view.findViewById(R.id.vote);
        ask = view.findViewById(R.id.ask);
        submit = view.findViewById(R.id.submit);
        profile_image =  view.findViewById(R.id.profile_image);
        AskLinear  =  view.findViewById(R.id.AskLinear);

        firstname = view. findViewById(R.id.firstname);
        lastname =  view.findViewById(R.id.lastname);
        password = view.findViewById(R.id.password);
        email   = view.findViewById(R.id.email);
        about   = view.findViewById(R.id.about);

        vote_recyclerView = view.findViewById(R.id.main_home_recyclerView);
        vote_recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        vote_recyclerView.setLayoutManager(layoutManager);

        ask_recyclerView = view.findViewById(R.id.main_ask_recyclerView);
        ask_recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        ask_recyclerView.setLayoutManager(layoutManager1);

    }

    private void setSharedPref() {
        pref = mContext.getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        Userid = pref.getString("Userid", "bye");
    }

    private void clickEvents() {

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profile.setBackgroundResource(R.drawable.white_background);
                profile.setTextColor(getResources().getColor( R.color.colorPrimaryDark));

                vote.setBackgroundColor(Color.TRANSPARENT);
                vote.setTextColor(getResources().getColor( R.color.white));

                ask.setBackgroundColor(Color.TRANSPARENT);
                ask.setTextColor(getResources().getColor( R.color.white));

                ProfileLinear.setVisibility(View.VISIBLE);
                VoteLinear  .setVisibility(View.GONE);
                AskLinear  .setVisibility(View.GONE);

            }
        });

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vote.setBackgroundResource(R.drawable.white_background);
                vote.setTextColor(getResources().getColor( R.color.colorPrimaryDark));

                profile.setBackgroundColor(Color.TRANSPARENT);
                profile.setTextColor(getResources().getColor( R.color.white));

                ask.setBackgroundColor(Color.TRANSPARENT);
                ask.setTextColor(getResources().getColor( R.color.white));

                 ProfileLinear.setVisibility(View.GONE);
                 VoteLinear  .setVisibility(View.VISIBLE);
                AskLinear  .setVisibility(View.GONE);

                Constant.VOTE_CURRENT_PAGE_SELECTED = Constant.VOTE_PROFILE_PAGE_SELECTED;

                getVoteData();

            }
        });

        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ask.setBackgroundResource(R.drawable.white_background);
                ask.setTextColor(getResources().getColor( R.color.colorPrimaryDark));

                vote.setBackgroundColor(Color.TRANSPARENT);
                vote.setTextColor(getResources().getColor( R.color.white));

                profile.setBackgroundColor(Color.TRANSPARENT);
                profile.setTextColor(getResources().getColor( R.color.white));


                ProfileLinear.setVisibility(View.GONE);
                VoteLinear  .setVisibility(View.GONE);
                AskLinear  .setVisibility(View.VISIBLE);

                Constant.ASK_CURRENT_PAGE_SELECTED = Constant.ASK_PROFILE_PAGE_SELECTED;


                getAskList();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Userid", null);
                editor.commit();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }
        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImageFromCamera();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setValidaition();

            }
        });


    }

    private void getProfileData() {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(mContext);
        progress_spinner.show();


        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getUserDetail(Userid);
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

                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");


                        String first_name =       jsonObject1.getString("first_name");
                        String last_name =       jsonObject1.getString("last_name");
                        String email_address =      jsonObject1.getString("email_address");
                        String  mabout =     jsonObject1.getString("about");
                        Username =     jsonObject1.getString("user_name");

                        String  picture =  "";

                                try {
                                    picture = jsonObject1.getString("picture");

                                }catch(Exception e){}

                        firstname.setText(first_name);
                        lastname .setText(last_name);
                        email    .setText(email_address);
                        about    .setText(mabout);

                        String image = getResources().getString(R.string.home_userimg_baseurl)+picture;

                        if(!picture.equalsIgnoreCase("")){

                            Picasso.with(mContext)
                                    .load(image)
                                    .error(R.mipmap.ic_launcher)
                                    .fit()
                                    .into(profile_image);
                        }

                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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

    private void setValidaition() {

        Firstname = firstname   .getText().toString();
        Lastname  = lastname    .getText().toString();
        Password  = password    .getText().toString();
        Email     = email       .getText().toString();
        About     = about       .getText().toString();


      //  if (Email.equals(""))
      //      Toast.makeText(mContext, "Please enter email address", Toast.LENGTH_LONG).show();
      //  else
            getSubmit();


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

    private void getSubmit() {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(mContext);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);


      RequestBody newUsername   = RequestBody.create(MediaType.parse("multipart/form-data"),Username);
      RequestBody newFirstname   = RequestBody.create(MediaType.parse("multipart/form-data"),Firstname);
      RequestBody newLastname    = RequestBody.create(MediaType.parse("multipart/form-data"),Lastname );
      RequestBody newPassword    = RequestBody.create(MediaType.parse("multipart/form-data"),Password );
      RequestBody newAbout       = RequestBody.create(MediaType.parse("multipart/form-data"),About    );
      RequestBody newEmail       = RequestBody.create(MediaType.parse("multipart/form-data"),Email    );

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

        Call<ResponseBody> call = api.getUpdateProfile(Userid,
                                                       newUsername ,
                                                       newFirstname,
                                                       newLastname ,
                                                       newPassword ,
                                                       newAbout    ,
                                                       newEmail    ,
                                                       imagePart   );

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

                        String messsage = jsonObject.getString("message");
                        Toast.makeText(mContext, messsage, Toast.LENGTH_LONG).show();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String messsage = jsonObject.getString("message");
                        Toast.makeText(mContext, messsage, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progress_spinner.dismiss();
                    Toast.makeText(mContext, "Exception " + e, Toast.LENGTH_SHORT).show();
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
                .start(getContext(),this);

        CropImage.activity(imageUri)
                .start(getContext(),this);

    }

    public Uri getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "");
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
                Picasso.with(mContext)
                        .load(file)
                        .error(R.mipmap.ic_launcher)
                        .fit()
                        .into(profile_image);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void setfile(File file) { this.mFile = file; }

    public File getFile() { return mFile; }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private void setVoteList() {
        final MainHomeAdapter main_adapter = new MainHomeAdapter(vote_list,mContext);
        vote_recyclerView.setAdapter(main_adapter);
        main_adapter.notifyDataSetChanged();

        main_adapter.setListner(new MainHomeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int Pos) {
                switch (v.getId()) {
                    case R.id.deletevote:

                        deleteVote(vote_list.get(Pos).getPost_id(),Pos,main_adapter);
                        break;


                }
            }
        });
    }

    private void deleteVote(String post_id, final int position, final MainHomeAdapter main_adapter) {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(mContext);
        progress_spinner.show();
        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.deleteVote(Userid,post_id);
        Log.e(tag, "new url : " + call.request().url());
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

                        vote_list.remove(position);

                        vote_recyclerView.removeViewAt(position);
                        main_adapter.notifyItemRemoved(position);
                        main_adapter.notifyItemRangeChanged(position, vote_list.size());


                     //   main_adapter.notifyItemRemoved(position);
                     //   main_adapter.notifyItemRangeChanged(position, vote_list.size());


                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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

    private void getVoteData() {

        vote_list = new ArrayList<>();

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(mContext);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getProfileVote(Userid);
        Log.e(tag, Userid+" getHomeData: " + call.request().url());
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

                        JSONArray result_jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < result_jsonArray.length(); i++) {

                            JSONObject jsonobj = result_jsonArray.getJSONObject(i);

                            String     post_id             = jsonobj.getString("post_id");
                            String     title               = jsonobj.getString("title");
                            String     create_date         = jsonobj.getString("create_date");
                            String     user_id             = jsonobj.getString("user_id");
                            String     post_type           = jsonobj.getString("post_type");
                            String     category            = jsonobj.getString("category");
                            String     spam_report_count   = jsonobj.getString("spam_report_count");
                            String     votes               = jsonobj.getString("votes");
                            String     user_name           = jsonobj.getString("user_name");
                            String     first_name          = jsonobj.getString("first_name");
                            String     last_name           = jsonobj.getString("last_name");
                            String     mobile_number       = jsonobj.getString("mobile_number");
                            String     email_address       = jsonobj.getString("email_address");
                            String     gender              = jsonobj.getString("gender");
                            String     create_by           = jsonobj.getString("create_by");
                            String     update_date         = jsonobj.getString("update_date");
                            String     update_by           = jsonobj.getString("update_by");
                            String     is_active           = jsonobj.getString("is_active");
                            String     password            = jsonobj.getString("password");
                            String     address             = jsonobj.getString("address");
                            String     zipcode             = jsonobj.getString("zipcode");
                            String     city                = jsonobj.getString("city");
                            String     is_approved         = jsonobj.getString("is_approved");
                            String     picture             = jsonobj.getString("picture");
                            String     about               = jsonobj.getString("about");
                            String     block_status        = jsonobj.getString("block_status");
                            JSONArray  images_arr         =  jsonobj.getJSONArray("images");

                            MainHome obj = new MainHome(post_id, title, create_date, user_id, post_type, category, spam_report_count, votes, user_name, first_name, last_name, mobile_number, email_address, gender, create_by, update_date, update_by, is_active, password, address, zipcode, city, is_approved, picture, about, block_status, images_arr );
                            vote_list.add(obj);
                        }
                        setVoteList();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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

    private void setAskList() {
        MainAskAdapter main_adapter = new MainAskAdapter(ask_list, mContext);
        ask_recyclerView.setAdapter(main_adapter);
        main_adapter.notifyDataSetChanged();
    }

    private void getAskList() {

        ask_list = new ArrayList<>();

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(mContext);
        progress_spinner.show();

        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getProfileAsk(Userid);
        Log.e(tag, Userid + " getHomeData: " + call.request().url());
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

                        JSONArray result_jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < result_jsonArray.length(); i++) {

                            JSONObject jsonobj = result_jsonArray.getJSONObject(i);

                            String question_id          = jsonobj.getString("question_id");
                            String description          = jsonobj.getString("description");
                            String create_date          = jsonobj.getString("create_date");
                            String user_id              = jsonobj.getString("user_id");
                            String category             = jsonobj.getString("category");
                            String spam_report_count    = jsonobj.getString("spam_report_count");
                            String picture              = jsonobj.getString("picture");
                            String user_name            = jsonobj.getString("user_name");
                            String first_name           = jsonobj.getString("first_name");
                            String last_name            = jsonobj.getString("last_name");
                            String mobile_number        = jsonobj.getString("mobile_number");
                            String email_address        = jsonobj.getString("email_address");
                            String gender               = jsonobj.getString("gender");
                            String create_by            = jsonobj.getString("create_by");
                            String update_date          = jsonobj.getString("update_date");
                            String update_by            = jsonobj.getString("update_by");
                            String is_active            = jsonobj.getString("is_active");
                            String password             = jsonobj.getString("password");
                            String address              = jsonobj.getString("address");
                            String zipcode              = jsonobj.getString("zipcode");
                            String city                 = jsonobj.getString("city");
                            String is_approved          = jsonobj.getString("is_approved");
                            String about                = jsonobj.getString("about");
                            String block_status         = jsonobj.getString("block_status");
                            String ask_picture          = jsonobj.getString("ask_picture");
                            String total_answers        = jsonobj.getString("total_answers");

                            MainAsk obj = new MainAsk(question_id, description, create_date, user_id, category, spam_report_count, picture, user_name, first_name, last_name,
                                    mobile_number,  email_address,  gender,  create_by, update_date,  update_by,  is_active,  password,  address,  zipcode,
                                    city,  is_approved,  about,  block_status, ask_picture, total_answers);

                            ask_list.add(obj);
                        }
                        setAskList();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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

}