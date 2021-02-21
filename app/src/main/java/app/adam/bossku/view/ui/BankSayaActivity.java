package app.adam.bossku.view.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.Route;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.view.adapter.PilihBankSayaAdapter;
import app.adam.bossku.view.model.Bank;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class BankSayaActivity extends AppCompatActivity implements PilihBankSayaAdapter.BankSayaListener {
    ArrayList<Bank> list_bank = new ArrayList<>();
    SpinKitView loader;
    RecyclerView rv_list;
    SweetAlertDialog dialog_loading;
    FloatingActionButton btn_add;
    PilihBankSayaAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_saya);

        rv_list = findViewById(R.id.rv_bank_saya);
        loader = findViewById(R.id.loader_bank_saya);
        btn_add = findViewById(R.id.btn_add_bank_saya);
        Toolbar mtoolbar = findViewById(R.id.toolbar_bank_saya);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> BankSayaActivity.super.onBackPressed());

        load_data();

        btn_add.setOnClickListener(v -> startActivity(new Intent(BankSayaActivity.this, BankSayaTambahActivity.class)));
    }

    void load_data(){
        loader.setVisibility(View.VISIBLE);

        SessionManager session = new SessionManager(BankSayaActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_bank_list(String.format("user/bank/list?page=%s&limit=%s","0","100"), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [bank saya]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");
                            JSONArray datas = data.getJSONArray("data");
                            for (int i=0;i<datas.length();i++){
                                JSONObject obj = datas.getJSONObject(i);
                                list_bank.add(
                                        new Bank(
                                                obj.getString("id"),
                                                obj.getString("bank_name"),
                                                obj.getString("account_name"),
                                                obj.getString("account_number")
                                        )
                                );
                            }
                            new Handler().postDelayed(() -> {
                                cardAdapter = new PilihBankSayaAdapter(BankSayaActivity.this,BankSayaActivity.this);
                                cardAdapter.setListItem(list_bank);
                                cardAdapter.notifyDataSetChanged();
                                rv_list.setLayoutManager(new LinearLayoutManager(BankSayaActivity.this));
                                rv_list.setHasFixedSize(true);
                                rv_list.setAdapter(cardAdapter);
                                loader.setVisibility(View.GONE);
                            },1500);
                        }
                        else{
                            Toast.makeText(BankSayaActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(BankSayaActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                    loader.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [bank saya]", t.toString());
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(BankSayaActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(BankSayaActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("app-response",response);
//
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//
//                            if(status.equals("success")){
//                                JSONObject data = res.getJSONObject("data");
//                                JSONArray datas = data.getJSONArray("data");
//                                for (int i=0;i<datas.length();i++){
//                                    JSONObject obj = datas.getJSONObject(i);
//                                    list_bank.add(
//                                            new Bank(
//                                                    obj.getString("id"),
//                                                    obj.getString("bank_name"),
//                                                    obj.getString("account_name"),
//                                                    obj.getString("account_number")
//                                            )
//                                    );
//                                }
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        cardAdapter = new PilihBankSayaAdapter(BankSayaActivity.this,BankSayaActivity.this);
//                                        cardAdapter.setListItem(list_bank);
//                                        cardAdapter.notifyDataSetChanged();
//                                        rv_list.setLayoutManager(new LinearLayoutManager(BankSayaActivity.this));
//                                        rv_list.setHasFixedSize(true);
//                                        rv_list.setAdapter(cardAdapter);
//                                        loader.setVisibility(View.GONE);
//                                    }
//                                },1500);
//                            }
//                            else{
//                                loader.setVisibility(View.GONE);
//                                Toast.makeText(BankSayaActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        loader.setVisibility(View.GONE);
//                        Toast.makeText(BankSayaActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(BankSayaActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String>  params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
    void request_delete(final String id){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(BankSayaActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_bank_delete("user/bank/delete", id, headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [bank saya]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            Toast.makeText(BankSayaActivity.this,message, Toast.LENGTH_SHORT).show();
                            list_bank.clear();
                            cardAdapter.setListItem(list_bank);
                            cardAdapter.notifyDataSetChanged();
                            load_data();
                        }
                        else{
                            Toast.makeText(BankSayaActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(BankSayaActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [bank saya]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(BankSayaActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(BankSayaActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("app-log",response);
//                        dialog_loading.dismissWithAnimation();
//
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//
//                            if(status.equals("success")){
//                                Toast.makeText(BankSayaActivity.this,message, Toast.LENGTH_SHORT).show();
//                                list_bank.clear();
//                                cardAdapter.setListItem(list_bank);
//                                cardAdapter.notifyDataSetChanged();
//                                load_data();
//                            }
//                            else{
//                                Toast.makeText(BankSayaActivity.this,message, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        dialog_loading.dismissWithAnimation();
//                        Toast.makeText(BankSayaActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(BankSayaActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String>  params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//
//            protected Map<String, String> getParams()
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", id);
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }

    @Override
    public void DeleteItemBankSaya(Bank item) {
        request_delete(item.getId());
    }
}