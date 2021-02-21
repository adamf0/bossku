package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.view.model.Alamat;
import app.adam.bossku.view.model.Kecamatan;
import app.adam.bossku.view.model.Kota;
import app.adam.bossku.view.model.Provinsi;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class TambahEditAlamatActivity extends AppCompatActivity {
    TextInputEditText edt_label,edt_alamat,edt_provinsi,edt_kota,edt_kecamatan;
    Button btn_simpan;

    Alamat obj;
    Provinsi provinsi;
    Kota kota;
    Kecamatan kecamatan;
    boolean isUpdate=false;
    int failLoadProv=0;
    int failLoadKota=0;
    int failLoadKec=0;
    int failLoadPostal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_edit_alamat);

        edt_label = findViewById(R.id.edt_label_alamat);
        edt_alamat = findViewById(R.id.edt_alamat_alamat);
        edt_provinsi = findViewById(R.id.edt_provinsi_alamat);
        edt_kota = findViewById(R.id.edt_kota_alamat);
        edt_kecamatan = findViewById(R.id.edt_kecamatan_alamat);
        btn_simpan = findViewById(R.id.btn_simpan_alamat);
        Toolbar mtoolbar = findViewById(R.id.toolbar_alamat);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> TambahEditAlamatActivity.super.onBackPressed());

        Bundle b = getIntent().getExtras();
        if(b != null){
//            label = b.getString("label");
//            alamat = b.getString("alamat");
            obj = b.getParcelable("alamat");
            provinsi = b.getParcelable("provinsi");
            kota = b.getParcelable("kota");
            kecamatan = b.getParcelable("kecamatan");
            isUpdate = b.getBoolean("isUpdate",false);
            if(obj!=null){
                obj.setProvinsi(provinsi);
                obj.setKota(kota);
                obj.setKecamatan(kecamatan);

                if(obj.getName() != null) {
                    edt_label.setText(obj.getName());
                }
                if(obj.getAddress() != null) {
                    edt_alamat.setText(obj.getAddress());
                }
            }
            else{
                obj = new Alamat(null,null,null,null,null,null,-1);
            }

            mtoolbar.setTitle((isUpdate? "Ubah Alamat":"Tambah Alamat"));
            if(isUpdate)
                if(provinsi != null || provinsi.getName()!=null){
                    if(kota != null || kota.getName()!=null || kota.getType()!=null){
                        if(kecamatan != null || kecamatan.getName()==null || kecamatan.getPostal_kode()==null){
                            initData();
                        }
                        else{
                            load_data(false,false,true);
                        }
                    }
                    else{
                        load_data(false,true,false);
                    }
                }
                else{
                    load_data(true,false,false);
                }
//                if(
//                    (provinsi != null || provinsi.getName()==null) ||
//                    (kota != null || kota.getName()==null || kota.getType()==null) ||
//                    (kecamatan != null || kecamatan.getName()==null || kecamatan.getPostal_kode()==null)
//                )
//                    load_data(true,false,false);
//                else
//                    initData();
            else{
                initData();
            }

            btn_simpan.setEnabled(validForm());
        }

        edt_provinsi.setOnClickListener(v -> redirect(true,false,false));
        edt_kota.setOnClickListener(v -> {
            if(provinsi!=null) {
                redirect(false, true, false);
            }
            else{
                Toast.makeText(TambahEditAlamatActivity.this, "Pilih provinsi terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
        edt_kecamatan.setOnClickListener(v -> {
            if(kota!=null){
            redirect(false,false,true);
            }
            else{
                Toast.makeText(TambahEditAlamatActivity.this, "Pilih kota terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        edt_label.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                    obj.setName(s.toString());
                else
                    obj.setName(null);

                btn_simpan.setEnabled(validForm());
            }
        });
        edt_alamat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                    obj.setAddress(s.toString());
                else
                    obj.setAddress(null);

                btn_simpan.setEnabled(validForm());
            }
        });

        btn_simpan.setOnClickListener(v -> {
            if(obj!=null && validForm())
                if(obj.getDef()==-1)
                    preRequest(obj,isUpdate);
                else
                    request(obj,isUpdate);
            else
                Toast.makeText(TambahEditAlamatActivity.this, "isi form dengan benar", Toast.LENGTH_SHORT).show();
        });
    }

    void initData(){
        if(provinsi != null && provinsi.getName()!=null) {
            edt_provinsi.setText(provinsi.getName());
            Log.i("app-log",provinsi.getName());
        }
        if(kota != null && kota.getName()!=null) {
            edt_kota.setText(kota.getFullName());
            Log.i("app-log",kota.getName());
        }
        if(kecamatan != null) {
            if(kecamatan.getName()!=null) {
                edt_kecamatan.setText(kecamatan.getName());
                Log.i("app-log",kecamatan.getName());
            }
            if (kecamatan.getPostal_kode() == null) {
                load_postal(kecamatan);
            }
        }
    }
    void redirect(final boolean pilih_prov, final boolean pilih_kota, final boolean pilih_kec){
        startActivity(
                new Intent(this, PilihProvinsiKotaKecamatanActivity.class)
                        .putExtra("alamat",obj)
                        .putExtra("provinsi",provinsi)
                        .putExtra("kota",kota)
                        .putExtra("kecamatan",kecamatan)

                        .putExtra("pilih_prov",pilih_prov)
                        .putExtra("pilih_kota",pilih_kota)
                        .putExtra("pilih_kec",pilih_kec)
                        .putExtra("isUpdate",isUpdate)
                        .putExtra("activity","TambahEditAlamatActivity")
        );
    }
    void load_data(final boolean isProv, final boolean isKota, final boolean isKec){
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(TambahEditAlamatActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = null;
        if(isProv)
            call = getResponse.request_master_data_provinces_list("master_data/provinces/list", headers);
        else if(isKota)
            call = getResponse.request_master_data_cities_listby(String.format("master_data/cities/listby/%s",provinsi.getId()), headers);
        else if(isKec)
            call = getResponse.request_master_data_subdistricts_listby(String.format("master_data/subdistricts/listby/%s",kota.getId()), headers);

        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [tambah edit alamat]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            JSONArray datas = res.getJSONArray("data");
                            for (int i=0;i<datas.length();i++) {
                                JSONObject obj = datas.getJSONObject(i);

                                if (isProv && obj.getString("province_id").equals(provinsi.getId())) {
                                    provinsi.setName(obj.getString("province"));
                                    initData();

                                    failLoadProv = 0;
                                    load_data(false, true, false);
                                }
                                else if (isKota && obj.getString("city_id").equals(kota.getId())) {
                                    kota.setName(obj.getString("city_name"));
                                    kota.setType(obj.getString("type"));
                                    initData();

                                    failLoadKota = 0;
                                    load_data(false, false, true);
                                }
                                else if (isKec && obj.getString("subdistrict_id").equals(kecamatan.getId())) {
                                    kecamatan.setName(obj.getString("subdistrict_name"));
                                    initData();

                                    failLoadKec = 0;
                                }
                            }
                            btn_simpan.setEnabled(validForm());
                        }
                        else{
                            dialog_loading.dismissWithAnimation();
                            Toast.makeText(TambahEditAlamatActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    dialog_loading.dismissWithAnimation();
                    Toast.makeText(TambahEditAlamatActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [tambah edit alamat]", t.toString());

                if(call.isCanceled()) {
                    dialog_loading.dismissWithAnimation();
                    Toast.makeText(TambahEditAlamatActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    if(failLoadProv<=3 || failLoadKota<=3 || failLoadKec<=3){
                        if(isProv){
                            failLoadProv++;
                            load_data(true,false,false);
                        }
                        else if(isKota){
                            failLoadKota++;
                            load_data(false,true,false);
                        }
                        else if(isKec){
                            failLoadKec++;
                            load_data(false,false,true);
                        }
                    }
                    else {
                        dialog_loading.dismissWithAnimation();
                        btn_simpan.setEnabled(validForm());

                        Toast.makeText(TambahEditAlamatActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
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
//                                JSONArray datas = res.getJSONArray("data");
//                                for (int i=0;i<datas.length();i++) {
//                                    JSONObject obj = datas.getJSONObject(i);
//
//                                    if (isProv && obj.getString("province_id").equals(provinsi.getId())) {
//                                        provinsi.setName(obj.getString("province"));
//                                        initData();
//
//                                        failLoadProv = 0;
//                                        load_data(false, true, false);
//                                    }
//                                    else if (isKota && obj.getString("city_id").equals(kota.getId())) {
//                                        kota.setName(obj.getString("city_name"));
//                                        kota.setType(obj.getString("type"));
//                                        initData();
//
//                                        failLoadKota = 0;
//                                        load_data(false, false, true);
//                                    }
//                                    else if (isKec && obj.getString("subdistrict_id").equals(kecamatan.getId())) {
//                                        kecamatan.setName(obj.getString("subdistrict_name"));
//                                        initData();
//
//                                        failLoadKec = 0;
//                                    }
//                                }
//                                btn_simpan.setEnabled(validForm());
//                            }
//                            else{
//                                Toast.makeText(TambahEditAlamatActivity.this,message, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        if(failLoadProv<=3 || failLoadKota<=3 || failLoadKec<=3){
//                            if(isProv){
//                                failLoadProv++;
//                                load_data(true,false,false);
//                            }
//                            else if(isKota){
//                                failLoadKota++;
//                                load_data(false,true,false);
//                            }
//                            else if(isKec){
//                                failLoadKec++;
//                                load_data(false,false,true);
//                            }
//                        }
//                        else {
//                            dialog_loading.dismissWithAnimation();
//                            btn_simpan.setEnabled(validForm());
//
//                            Toast.makeText(TambahEditAlamatActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(TambahEditAlamatActivity.this);
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
    void load_postal(final Kecamatan kecamatan){
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(TambahEditAlamatActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_rajaongkir_get_postalcode(String.format("rajaongkir/get-postalcode/%s",kecamatan.getId()), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [tambah edit alamat]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            kecamatan.setPostal_kode(res.getString("data"));
                            edt_kecamatan.setText(kecamatan.getName());
                        }
                        else{
                            edt_kecamatan.setText(kecamatan.getName());
                            Toast.makeText(TambahEditAlamatActivity.this,message, Toast.LENGTH_SHORT).show();
                        }

                        btn_simpan.setEnabled(validForm());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    dialog_loading.dismissWithAnimation();
                    Toast.makeText(TambahEditAlamatActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [tambah edit alamat]", t.toString());

                if(call.isCanceled()) {
                    dialog_loading.dismissWithAnimation();
                    Toast.makeText(TambahEditAlamatActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    if(failLoadPostal<=3){
                        failLoadPostal++;
                        load_postal(kecamatan);
                    }
                    else {
                        dialog_loading.dismissWithAnimation();
                        btn_simpan.setEnabled(validForm());
                        Toast.makeText(TambahEditAlamatActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
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
//                                kecamatan.setPostal_kode(res.getString("data"));
//                                edt_kecamatan.setText(kecamatan.getName());
//                            }
//                            else{
//                                edt_kecamatan.setText(kecamatan.getName());
//                                Toast.makeText(TambahEditAlamatActivity.this,message, Toast.LENGTH_SHORT).show();
//                            }
//
//                            btn_simpan.setEnabled(validForm());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        if(failLoadPostal<=3){
//                            failLoadPostal++;
//                            load_postal(kecamatan);
//                        }
//                        else {
//                            dialog_loading.dismissWithAnimation();
//                            btn_simpan.setEnabled(validForm());
//                            Toast.makeText(TambahEditAlamatActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(TambahEditAlamatActivity.this);
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
    void preRequest(final Alamat alamat, final boolean isUpdate) {
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Check Data");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(TambahEditAlamatActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_shipping_address_list("user/shipping_address/list", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [tambah edit alamat]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        //String message = res.getString("message");

                        if (status.equals("success")) {
                            JSONArray datas = res.getJSONArray("data");
                            dialog_loading.dismissWithAnimation();

                            obj.setDef(datas.length()>0? 0:1);
                            request(alamat,isUpdate);
                        } else {
                            dialog_loading.dismissWithAnimation();
                            Toast.makeText(TambahEditAlamatActivity.this, "Tidak ada respon dari server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    dialog_loading.dismissWithAnimation();
                    Toast.makeText(TambahEditAlamatActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [tambah edit alamat]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(TambahEditAlamatActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(TambahEditAlamatActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("app-log",response);
//
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            //String message = res.getString("message");
//
//                            if (status.equals("success")) {
//                                JSONArray datas = res.getJSONArray("data");
//                                dialog_loading.dismissWithAnimation();
//
//                                obj.setDef(datas.length()>0? 0:1);
//                                request(alamat,isUpdate);
//                            } else {
//                                dialog_loading.dismissWithAnimation();
//                                Toast.makeText(TambahEditAlamatActivity.this, "Tidak ada respon dari server", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(TambahEditAlamatActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(TambahEditAlamatActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
    void request(final Alamat alamat, boolean isUpdate){
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        Log.i("app-log",alamat.getName());
        Log.i("app-log",alamat.getAddress());
        Log.i("app-log", alamat.getProvinsi().getId());
        Log.i("app-log", alamat.getKota().getId());
        Log.i("app-log", alamat.getKecamatan().getId());
        Log.i("app-log", alamat.getKecamatan().getPostal_kode());
        Log.i("app-log", String.valueOf(alamat.getDef()));

        SessionManager session = new SessionManager(TambahEditAlamatActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_shipping_address_add_edit(
                (!isUpdate? "user/shipping_address/add":String.format("user/shipping_address/edit/%s",alamat.getId())),
                alamat.getName(),
                alamat.getAddress(),
                alamat.getProvinsi().getId(),
                alamat.getKota().getId(),
                alamat.getKecamatan().getId(),
                alamat.getKecamatan().getPostal_kode(),
                String.valueOf(alamat.getDef()),
                headers
        );

        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [tambah edit alamat]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            startActivity(new Intent(TambahEditAlamatActivity.this, PilihAlamatActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                        Toast.makeText(TambahEditAlamatActivity.this,message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(TambahEditAlamatActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [tambah edit alamat]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(TambahEditAlamatActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(TambahEditAlamatActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                startActivity(new Intent(TambahEditAlamatActivity.this, PilihAlamatActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                            }
//                            Toast.makeText(TambahEditAlamatActivity.this,message, Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        dialog_loading.dismissWithAnimation();
//                        Toast.makeText(TambahEditAlamatActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(TambahEditAlamatActivity.this);
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
//                params.put("name", alamat.getName());
//                params.put("address", alamat.getAddress());
//                params.put("province_id", alamat.getProvinsi().getId());
//                params.put("city_id", alamat.getKota().getId());
//                params.put("subdistrict_id", alamat.getKecamatan().getId());
//                params.put("postal_code", alamat.getKecamatan().getPostal_kode());
//                params.put("is_default_address", String.valueOf(alamat.getDef()));
//
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
    boolean validForm(){
        if(obj!=null ||
           !(obj.getName()==null) ||
           !(obj.getAddress()==null) ||
           (obj.getProvinsi()!=null || !TextUtils.isEmpty(obj.getProvinsi().getName())) ||
           (obj.getKota()!=null || !TextUtils.isEmpty(obj.getKota().getName())) ||
           (obj.getKecamatan()!=null || !TextUtils.isEmpty(obj.getKecamatan().getName())) ||
           !(obj.getDef()==-1)
        )
            return true;
        else
            return false;
    }
}