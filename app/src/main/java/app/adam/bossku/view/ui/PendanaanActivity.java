package app.adam.bossku.view.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.FilePath;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.adapter.PendanaanAdapter;
import app.adam.bossku.view.adapter.PendanaanBeliAdapter;
import app.adam.bossku.view.adapter.PendanaanJualAdapter;
import app.adam.bossku.view.adapter.PendanaanSayaAdapter;
import app.adam.bossku.view.model.Pendanaan;
import app.adam.bossku.view.model.PendanaanBeli;
import app.adam.bossku.view.model.PendanaanJual;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PendanaanActivity extends AppCompatActivity implements
        PendanaanAdapter.PendanaanListener,
        PendanaanSayaAdapter.PendanaanSayaListener,
        TabLayout.OnTabSelectedListener,
        PendanaanBeliAdapter.PendanaanBeliListener,
        PendanaanJualAdapter.PendanaanJualListener{

    RecyclerView rv_list,rv_list_beli,rv_list_jual,rv_list_donasi_saya;
    TabLayout tab_pendanaan;
    int position=1;
    FloatingActionButton btn_tambah;
    ArrayList<Pendanaan> list_item = new ArrayList<>();
    ArrayList<Pendanaan> list_item_donasi_saya = new ArrayList<>();
    //ArrayList<PendanaanBeli> list_item_beli = new ArrayList<>();
    //ArrayList<PendanaanJual> list_item_jual = new ArrayList<>();
    PendanaanAdapter adapter;
    PendanaanSayaAdapter adapterDonasiSaya;
    //PendanaanBeliAdapter adapterBeli;
    //PendanaanJualAdapter adapterJual;
    SpinKitView loader;
    SessionManager session;
    HashMap<String, String> data;

    private final int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    Pendanaan tmp_pendanaan=null;
    SweetAlertDialog dialog_loading;
    boolean isDefault=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendanaan);

        tab_pendanaan = findViewById(R.id.tab_pendanaan);
        rv_list = findViewById(R.id.rv_list_pendanaan);
        rv_list_beli = findViewById(R.id.rv_list_pendanaan_beli);
        rv_list_jual = findViewById(R.id.rv_list_pendanaan_jual);
        rv_list_jual = findViewById(R.id.rv_list_pendanaan_jual);
        rv_list_donasi_saya = findViewById(R.id.rv_list_donasi_saya);
        btn_tambah = findViewById(R.id.btn_tambah_pendanaan);
        loader = findViewById(R.id.loader_pendanaan);
        Toolbar mtoolbar = findViewById(R.id.toolbar_pendanaan);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> PendanaanActivity.super.onBackPressed());

        rv_list.setHasFixedSize(true);
        rv_list_beli.setHasFixedSize(true);
        rv_list_jual.setHasFixedSize(true);
        rv_list_donasi_saya.setHasFixedSize(true);

        session = new SessionManager(this);
        data = session.getSessionLogin();
        requestStoragePermission();

//        if(data.get(SessionManager.KEY_ROLE).equals("4")) {
//            load_all();
//            load_buy();
//            load_sell();
//            tab_pendanaan.addOnTabSelectedListener(this);
//        }
        ViewGroup vTab = (ViewGroup) tab_pendanaan.getChildAt(0);
        if(!data.get(SessionManager.KEY_ROLE).equals("4")) {
            vTab.getChildAt(0).setVisibility(View.GONE);
            vTab.getChildAt(1).setVisibility(View.GONE);
            vTab.getChildAt(2).setVisibility(View.GONE);

            vTab.getChildAt(3).setVisibility(View.VISIBLE);
            vTab.getChildAt(4).setVisibility(View.VISIBLE);
            load_all();
            tab_pendanaan.addOnTabSelectedListener(this);
        }
        else{
            vTab.getChildAt(0).setVisibility(View.VISIBLE);
            vTab.getChildAt(1).setVisibility(View.VISIBLE);
            vTab.getChildAt(2).setVisibility(View.VISIBLE);

            vTab.getChildAt(3).setVisibility(View.GONE);
            vTab.getChildAt(4).setVisibility(View.GONE);
        }
    }
//    void load_all(){
//        loader.setVisibility(View.VISIBLE);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = Route.base_url+String.format(Route.funding_projetcs_list,"time","10","0","all");
//
//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        loader.setVisibility(View.GONE);
//
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//
//                            if(status.equals("success")){
//                                JSONObject data = res.getJSONObject("data");
//                                JSONArray datas = data.getJSONArray("data");
//                                list_item = new ArrayList<>();
//
//                                for (int i=0;i<datas.length();i++){
//                                    JSONObject obj = datas.getJSONObject(i);
//
//                                    list_item.add(
//                                            new Pendanaan(
//                                            obj.getString("id"),
//                                            obj.getString("image"),
//                                            obj.getString("product_name"),
//                                            obj.getJSONObject("umkm_data").getString("brand_name"),
//                                            new BigDecimal("1000"),
//                                            new BigDecimal( obj.getJSONObject("crowd_funding_total").getString("total") ),
//                                            new BigDecimal( obj.getString("amount_of_funds") ),
//                                            100,
//                                            6,
//                                            0
//                                            )
//                                    );
//                                }
//
//                                rv_list.setLayoutManager(new LinearLayoutManager(PendanaanActivity.this));
//                                adapter = new PendanaanAdapter(PendanaanActivity.this,PendanaanActivity.this);
//                                adapter.setListItem(list_item);
//                                adapter.notifyDataSetChanged();
//                                rv_list.setAdapter(adapter);
//                            }
//                            else{
//                                Toast.makeText(PendanaanActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(PendanaanActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                        loader.setVisibility(View.GONE);
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(PendanaanActivity.this);
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
//    }
//    void load_sell(){
//        list_item_jual = new ArrayList<>();
//        list_item_jual.add(new PendanaanJual("1","","unit 0",new BigDecimal("100000000"),100000,99999,new BigDecimal("1000"),1,0));
//
//        rv_list_jual.setLayoutManager(new LinearLayoutManager(this));
//        adapterJual = new PendanaanJualAdapter(this,this);
//        adapterJual.setListItem(list_item_jual);
//        adapterJual.notifyDataSetChanged();
//        rv_list_jual.setAdapter(adapterJual);
//    }
//    void load_buy(){
//        list_item_beli = new ArrayList<>();
//        list_item_beli.add(new PendanaanBeli("2","","unit 2","umkm 2","1,67%",new BigDecimal("3"),new BigDecimal("300000"),-1));
//        list_item_beli.add(new PendanaanBeli("3","","unit 3","umkm 3","2%",new BigDecimal("10"),new BigDecimal("1000000"),1));
//        list_item_beli.add(new PendanaanBeli("4","","unit 4","umkm 4","6%",new BigDecimal("600"),new BigDecimal("2500000"),0));
//
//        rv_list_beli.setLayoutManager(new LinearLayoutManager(this));
//        adapterBeli = new PendanaanBeliAdapter(this,this);
//        adapterBeli.setListItem(list_item_beli);
//        adapterBeli.notifyDataSetChanged();
//        rv_list_beli.setAdapter(adapterBeli);
//    }
    void showFileChooser(Pendanaan item) {
    tmp_pendanaan = item;

    Intent intent = new Intent();
    intent.setType("application/pdf");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Pdf File"), PICK_PDF_REQUEST);
}
    void load_all(){
        if(isDefault) {
            list_item = new ArrayList<>();
            list_item.add(new Pendanaan("1", "", "produk A", "Toko A", new BigDecimal(0), new BigDecimal(1000), new BigDecimal(100000), 0, 0, 0));
            list_item.add(new Pendanaan("2", "", "produk B", "Toko B", new BigDecimal(0), new BigDecimal(0), new BigDecimal(500000), 0, 0, 0));

            rv_list.setLayoutManager(new LinearLayoutManager(PendanaanActivity.this));
            adapter = new PendanaanAdapter(PendanaanActivity.this, false, PendanaanActivity.this);
            adapter.setListItem(list_item);
            adapter.notifyDataSetChanged();
            rv_list.setAdapter(adapter);
        }
        else{
            loader.setVisibility(View.VISIBLE);

            SessionManager session = new SessionManager(PendanaanActivity.this);
            HashMap<String, String> data = session.getSessionLogin();

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Map<String, String>  headers = new HashMap<>();
            headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

            Call<String> call = getResponse.request_projects_list_onprogress(String.format("projects/list/onprogress?filter=%s&limit=%s&page=%s&category=%s", "time", "10", "0", "all"), headers);
            Log.i("app-log", "request to "+call.request().url().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String res_ = response.body();
                    Log.i("app-log [pendanaan]", res_);
                    loader.setVisibility(View.GONE);

                    if(response.isSuccessful()) {
                        try {
                            assert res_ != null;
                            JSONObject res = new JSONObject(res_);
                            String status = res.getString("status");
                            String message = res.getString("message");

                            if (status.equals("success")) {
                                JSONObject data = res.getJSONObject("data");
                                JSONArray datas = data.getJSONArray("data");
                                list_item = new ArrayList<>();

                                for (int i = 0; i < datas.length(); i++) {
                                    JSONObject obj = datas.getJSONObject(i);

                                    list_item.add(
                                            new Pendanaan(
                                                    obj.getString("id"),
                                                    obj.getString("image"),
                                                    obj.getString("product_name"),
                                                    obj.getJSONObject("umkm_data").getString("brand_name"),
                                                    new BigDecimal(0),
                                                    new BigDecimal((obj.isNull("crowd_funding_total") ?
                                                            "0" :
                                                            obj.getJSONObject("crowd_funding_total").getString("total"))
                                                    ),
                                                    new BigDecimal(obj.getString("amount_of_funds")),
                                                    0,
                                                    0,
                                                    0
                                            )
                                    );
                                }

                                rv_list.setLayoutManager(new LinearLayoutManager(PendanaanActivity.this));
                                adapter = new PendanaanAdapter(PendanaanActivity.this, false, PendanaanActivity.this);
                                adapter.setListItem(list_item);
                                adapter.notifyDataSetChanged();
                                rv_list.setAdapter(adapter);
                            }
                            else {
                                Toast.makeText(PendanaanActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(PendanaanActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                        loader.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("app-log [pendanaan]", t.toString());
                    loader.setVisibility(View.GONE);

                    if(call.isCanceled()) {
                        Toast.makeText(PendanaanActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(PendanaanActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                    }
                }
            });

//            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.i("app-log", response);
//                            loader.setVisibility(View.GONE);
//
//                            try {
//                                JSONObject res = new JSONObject(response);
//                                String status = res.getString("status");
//                                String message = res.getString("message");
//
//                                if (status.equals("success")) {
//                                    JSONObject data = res.getJSONObject("data");
//                                    JSONArray datas = data.getJSONArray("data");
//                                    list_item = new ArrayList<>();
//
//                                    for (int i = 0; i < datas.length(); i++) {
//                                        JSONObject obj = datas.getJSONObject(i);
//
//                                        list_item.add(
//                                                new Pendanaan(
//                                                        obj.getString("id"),
//                                                        obj.getString("image"),
//                                                        obj.getString("product_name"),
//                                                        obj.getJSONObject("umkm_data").getString("brand_name"),
//                                                        new BigDecimal(0),
//                                                        new BigDecimal((obj.isNull("crowd_funding_total") ?
//                                                                "0" :
//                                                                obj.getJSONObject("crowd_funding_total").getString("total"))
//                                                        ),
//                                                        new BigDecimal(obj.getString("amount_of_funds")),
//                                                        0,
//                                                        0,
//                                                        0
//                                                )
//                                        );
//                                    }
//
//                                    rv_list.setLayoutManager(new LinearLayoutManager(PendanaanActivity.this));
//                                    adapter = new PendanaanAdapter(PendanaanActivity.this, false, PendanaanActivity.this);
//                                    adapter.setListItem(list_item);
//                                    adapter.notifyDataSetChanged();
//                                    rv_list.setAdapter(adapter);
//                                } else {
//                                    Toast.makeText(PendanaanActivity.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(PendanaanActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                            loader.setVisibility(View.GONE);
//                        }
//                    }
//            ) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    SessionManager session = new SessionManager(PendanaanActivity.this);
//                    HashMap<String, String> data = session.getSessionLogin();
//
//                    Map<String, String> params = new HashMap<>();
//                    params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                    params.put("Accept", "application/json");
//                    return params;
//                }
//            };
//            postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(postRequest);
        }
    }
    void load_my_donate(){
        if(isDefault){
            list_item_donasi_saya = new ArrayList<>();
            list_item_donasi_saya.add(new Pendanaan("1", "Fund A", 10000, "A001", 1, "X001"));
            list_item_donasi_saya.add(new Pendanaan("2", "Fund B", 1000, "A00B", 1, "X002"));

            rv_list_donasi_saya.setLayoutManager(new LinearLayoutManager(PendanaanActivity.this));
            adapterDonasiSaya = new PendanaanSayaAdapter(PendanaanActivity.this, PendanaanActivity.this);
            adapterDonasiSaya.setListItem(list_item_donasi_saya);
            adapterDonasiSaya.notifyDataSetChanged();
            rv_list_donasi_saya.setAdapter(adapterDonasiSaya);
        }
        else {
            loader.setVisibility(View.VISIBLE);

            SessionManager session = new SessionManager(PendanaanActivity.this);
            HashMap<String, String> data = session.getSessionLogin();

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Map<String, String>  headers = new HashMap<>();
            headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

            Call<String> call = getResponse.request_user_funding_my_donation(String.format("user/funding/my_donation/%s?page=%s&limit=%s", "1", "0", "10"), headers);
            Log.i("app-log", "request to "+call.request().url().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String res_ = response.body();
                    Log.i("app-log [pendanaan]", res_);
                    loader.setVisibility(View.GONE);

                    if(response.isSuccessful()) {
                        try {
                            assert res_ != null;
                            JSONObject res = new JSONObject(res_);
                            String status = res.getString("status");
                            String message = res.getString("message");

                            if (status.equals("success")) {
                                JSONObject data = res.getJSONObject("data");
                                JSONArray datas = data.getJSONArray("data");
                                list_item_donasi_saya = new ArrayList<>();

                                for (int i = 0; i < datas.length(); i++) {
                                    JSONObject obj = datas.getJSONObject(i);

                                    list_item_donasi_saya.add(
                                            new Pendanaan(
                                                    obj.getString("funding_id"),
                                                    obj.getString("product_name"),
                                                    obj.getDouble("jumlah_donasi"),
                                                    obj.getString("transaction_code"),
                                                    obj.getInt("transaction_status"),
                                                    obj.getString("id_crowd_funding")
                                            )
                                    );
                                }

                                rv_list_donasi_saya.setLayoutManager(new LinearLayoutManager(PendanaanActivity.this));
                                adapterDonasiSaya = new PendanaanSayaAdapter(PendanaanActivity.this, PendanaanActivity.this);
                                adapterDonasiSaya.setListItem(list_item_donasi_saya);
                                adapterDonasiSaya.notifyDataSetChanged();
                                rv_list_donasi_saya.setAdapter(adapterDonasiSaya);
                            } else {
                                Toast.makeText(PendanaanActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(PendanaanActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("app-log [pendanaan]", t.toString());
                    loader.setVisibility(View.GONE);

                    if(call.isCanceled()) {
                        Toast.makeText(PendanaanActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(PendanaanActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                    }
                }
            });

//            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.i("app-log", response);
//                            loader.setVisibility(View.GONE);
//
//                            try {
//                                JSONObject res = new JSONObject(response);
//                                String status = res.getString("status");
//                                String message = res.getString("message");
//
//                                if (status.equals("success")) {
//                                    JSONObject data = res.getJSONObject("data");
//                                    JSONArray datas = data.getJSONArray("data");
//                                    list_item_donasi_saya = new ArrayList<>();
//
//                                    for (int i = 0; i < datas.length(); i++) {
//                                        JSONObject obj = datas.getJSONObject(i);
//
//                                        list_item_donasi_saya.add(
//                                                new Pendanaan(
//                                                        obj.getString("funding_id"),
//                                                        obj.getString("product_name"),
//                                                        obj.getDouble("jumlah_donasi"),
//                                                        obj.getString("transaction_code"),
//                                                        obj.getInt("transaction_status"),
//                                                        obj.getString("id_crowd_funding")
//                                                )
//                                        );
//                                    }
//
//                                    rv_list_donasi_saya.setLayoutManager(new LinearLayoutManager(PendanaanActivity.this));
//                                    adapterDonasiSaya = new PendanaanSayaAdapter(PendanaanActivity.this, PendanaanActivity.this);
//                                    adapterDonasiSaya.setListItem(list_item_donasi_saya);
//                                    adapterDonasiSaya.notifyDataSetChanged();
//                                    rv_list_donasi_saya.setAdapter(adapterDonasiSaya);
//                                } else {
//                                    Toast.makeText(PendanaanActivity.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(PendanaanActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                            loader.setVisibility(View.GONE);
//                        }
//                    }
//            ) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    SessionManager session = new SessionManager(PendanaanActivity.this);
//                    HashMap<String, String> data = session.getSessionLogin();
//
//                    Map<String, String> params = new HashMap<>();
//                    params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                    params.put("Accept", "application/json");
//                    return params;
//                }
//            };
//            postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(postRequest);
        }
    }

    void requestBill(final Pendanaan pendanaan, final File file){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(PendanaanActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part file_pdf = MultipartBody.Part.createFormData("file_upload", file.getName(), requestFile);
        RequestBody Rtransaction_code = RequestBody.create(MediaType.parse("multipart/form-data"), pendanaan.getTransaction_code());

        Call<String> call = getResponse.request_user_transaction_reciept_input("user/transaction/reciept_input",file_pdf,Rtransaction_code, headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pendanaan]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            rv_list.setVisibility(View.GONE);
                            rv_list_donasi_saya.setVisibility(View.VISIBLE);
                            if(adapterDonasiSaya!=null && adapterDonasiSaya.getItemCount()>0){
                                adapterDonasiSaya.clear();
                                list_item_donasi_saya.clear();
                            }
                            load_my_donate();
                        }
                        else {
                            Toast.makeText(PendanaanActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PendanaanActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pendanaan]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(PendanaanActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PendanaanActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                rv_list.setVisibility(View.GONE);
//                                rv_list_donasi_saya.setVisibility(View.VISIBLE);
//                                if(adapterDonasiSaya!=null && adapterDonasiSaya.getItemCount()>0){
//                                    adapterDonasiSaya.clear();
//                                    list_item_donasi_saya.clear();
//                                }
//                                load_my_donate();
//                            }
//
//                            Toast.makeText(PendanaanActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        dialog_loading.dismissWithAnimation();
//                        Toast.makeText(PendanaanActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PendanaanActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("file_upload", file);
//                params.put("transaction_code", pendanaan.getTransaction_code());
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
    void requestCancel(final Pendanaan pendanaan){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(PendanaanActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_transaction_cancel("user/transaction/cancel",pendanaan.getTransaction_code(), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pendanaan]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            rv_list.setVisibility(View.GONE);
                            rv_list_donasi_saya.setVisibility(View.VISIBLE);
                            if(adapterDonasiSaya!=null && adapterDonasiSaya.getItemCount()>0){
                                adapterDonasiSaya.clear();
                                list_item_donasi_saya.clear();
                            }
                            load_my_donate();
                            Toast.makeText(PendanaanActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(PendanaanActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PendanaanActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pendanaan]", t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(PendanaanActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PendanaanActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                rv_list.setVisibility(View.GONE);
//                                rv_list_donasi_saya.setVisibility(View.VISIBLE);
//                                if(adapterDonasiSaya!=null && adapterDonasiSaya.getItemCount()>0){
//                                    adapterDonasiSaya.clear();
//                                    list_item_donasi_saya.clear();
//                                }
//                                load_my_donate();
//                            }
//
//                            Toast.makeText(PendanaanActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        dialog_loading.dismissWithAnimation();
//                        Toast.makeText(PendanaanActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PendanaanActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("transaction_code", pendanaan.getTransaction_code());
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {}
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                InputStream iStream = getContentResolver().openInputStream(filePath);
                byte[] inputData = Util.getBytes(iStream);
                String encoded = Base64.encodeToString(inputData, Base64.DEFAULT);
                Log.i("app-log file:", encoded);

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Bossku");
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                    }
                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                File file_pdf_copy = new File(mediaStorageDir.getPath(), (tmp_pendanaan!=null? tmp_pendanaan.getTransaction_code():timeStamp) + ".pdf");
                OutputStream op = new FileOutputStream(file_pdf_copy);
                op.write(inputData);
                Log.i("app-log path:", file_pdf_copy.getPath());

                if (filePath == null) {
                    Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
                }
                else if (tmp_pendanaan == null) {
                    Toast.makeText(this, "Kesalahan sistem", Toast.LENGTH_LONG).show();
                }
                else {
                    requestBill(tmp_pendanaan,file_pdf_copy);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void SelectItemPendanaan(Pendanaan item) {
        if(data.get(SessionManager.KEY_ROLE).equals("4")) {
            startActivity(
                    new Intent(this,DetailPendanaanActivity.class)
                            .putExtra("position_tab","all")
                            .putExtra("pendanaan",item)
                            .putExtra("isInvestor",true)
            );
        }
        else{
            startActivity(
                    new Intent(this,DetailPendanaanActivity.class)
                            .putExtra("position_tab","all")
                            .putExtra("pendanaan",item)
                            .putExtra("isInvestor",false)
            );
        }
    }

    @Override
    public void SelectItemCancelPendanaanSaya(Pendanaan item) {
        requestCancel(item);
    }

    @Override
    public void SelectItemBillPendanaanSaya(Pendanaan item) {
        showFileChooser(item);
    }

    @Override
    public void SelectItemPendanaanSaya(Pendanaan item) {
    }

    @Override
    public void SelectItemPendanaanBeli(PendanaanBeli item) {
        startActivity(
                new Intent(this,DetailPendanaanActivity.class)
                        .putExtra("position_tab","beli")
                        .putExtra("pendanaan",item)
        );
    }

    @Override
    public void SelectItemPendanaanJual(PendanaanJual item) {
        startActivity(
                new Intent(this,DetailPendanaanActivity.class).putExtra("position_tab","jual")
        );
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()){
            case 0:
                if(position!=1){
                    position=1;

                    rv_list.setVisibility(View.VISIBLE);
                    rv_list_jual.setVisibility(View.GONE);
                    rv_list_beli.setVisibility(View.GONE);
                    btn_tambah.setVisibility(View.GONE);
                }
                break;
            case 1:
                if(position!=2){
                    position=2;

                    rv_list.setVisibility(View.GONE);
                    rv_list_jual.setVisibility(View.VISIBLE);
                    rv_list_beli.setVisibility(View.GONE);
                    btn_tambah.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if(position!=3){
                    position=3;

                    rv_list.setVisibility(View.GONE);
                    rv_list_jual.setVisibility(View.GONE);
                    rv_list_beli.setVisibility(View.VISIBLE);
                    btn_tambah.setVisibility(View.GONE);
                }
                break;
            case 3:
                rv_list.setVisibility(View.VISIBLE);
                rv_list_donasi_saya.setVisibility(View.GONE);
                if(adapter!=null && adapter.getItemCount()>0){
                    adapter.clear();
                    list_item.clear();
                }
                load_all();
                break;
            case 4:
                rv_list.setVisibility(View.GONE);
                rv_list_donasi_saya.setVisibility(View.VISIBLE);
                if(adapterDonasiSaya!=null && adapterDonasiSaya.getItemCount()>0){
                    adapterDonasiSaya.clear();
                    list_item_donasi_saya.clear();
                }
                load_my_donate();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}