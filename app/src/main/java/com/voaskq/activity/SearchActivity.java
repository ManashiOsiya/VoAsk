package com.voaskq.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.voaskq.R;
import com.voaskq.adapter.SearchAdapter;
import com.voaskq.helper.MyProgressbar;
import com.voaskq.modal.Search;
import com.voaskq.webservices.Api;
import com.voaskq.webservices.ApiFactory;

import android.widget.SearchView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<Search> searchlist;
    SearchAdapter adapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Userid;
    ImageView  back;
    SearchView searchfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        getSharedPrefdata();
        initviews();
        getSearchData("");
        clickEvents();

    }
    private void getSharedPrefdata() {

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        Userid = pref.getString("Userid", null);
    }

    private void initviews() {

        back=(ImageView)findViewById(R.id.back);
        searchfield = findViewById(R.id.searchView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void clickEvents() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchfield.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                getSearchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

//                getSearchData(newText);
                return false;
            }
        });






//        searchfield.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                CharSequence text =  charSequence;
//                getSearchData(text);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//
//
//            }
//        });

    }

    private void getSearchData(String searchkey) {

        final Dialog progress_spinner = MyProgressbar.LoadingSpinner(SearchActivity.this);
        progress_spinner.show();

        searchlist = new ArrayList<>();
        Api api = ApiFactory.getClient().create(Api.class);
        Call<ResponseBody> call = api.getSearchData(Userid,searchkey);
        Log.e("url   .......", "getSearch: " + call.request().url());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progressDialog.hide();
                String output = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    output = stringBuilder.toString();
                    Log.e("new api==>>>>>>", "onResponse: getSearch" + output);
                    JSONObject jsonObject = new JSONObject(output);

                    progress_spinner.dismiss();

                    if (jsonObject.getString("status").equalsIgnoreCase("SUCCESS")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject result_json = jsonArray.getJSONObject(i);

                            String user_id         =result_json.getString("user_id");
                            String user_name       =result_json.getString("user_name");
                            String first_name      =result_json.getString("first_name");
                            String last_name       =result_json.getString("last_name");
                            String mobile_number   =result_json.getString("mobile_number");
                            String email_address   =result_json.getString("email_address");
                            String gender          =result_json.getString("gender");
                            String create_date     =result_json.getString("create_date");
                            String create_by       =result_json.getString("create_by");
                            String update_date     =result_json.getString("update_date");
                            String update_by       =result_json.getString("update_by");
                            String is_active       =result_json.getString("is_active");
                            String password        =result_json.getString("password");
                            String address         =result_json.getString("address");
                            String zipcode         =result_json.getString("zipcode");
                            String city            =result_json.getString("city");
                            String is_approved     =result_json.getString("is_approved");
                            String picture         =result_json.getString("picture");
                            String about           =result_json.getString("about");
                            String block_status    =result_json.getString("block_status");
                            String follow_id         =result_json.getString("follow_id");

                            Search obj = new Search(user_id, user_name, first_name, last_name, mobile_number, email_address, gender, create_date, create_by, update_date,
                                    update_by, is_active, password, address, zipcode, city,  is_approved,  picture,  about,  block_status,follow_id);
                            searchlist.add(obj);
                        }
                        setList();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("FAILED")) {
                        String msg = jsonObject.getString("msg");
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

    private void setList() {
        adapter = new SearchAdapter(searchlist, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
