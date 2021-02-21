package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.helper.Util;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class BankSayaTambahActivity extends AppCompatActivity {
    Toolbar mtoolbar;
    TextInputEditText edt_nama_bank,edt_nomor_rekening,edt_atas_nama;
    Button btn_simpan;
    SweetAlertDialog dialog_loading;
    String nama_bank="";
    String nomor_rekening="";
    String atas_nama="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_saya_tambah);

        mtoolbar = findViewById(R.id.toolbar_bank_saya_tambah);
        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> BankSayaTambahActivity.super.onBackPressed());

        edt_nama_bank = findViewById(R.id.edt_nama_bank_bank_saya_tambah);
        edt_nomor_rekening = findViewById(R.id.edt_nomor_rekening_bank_saya_tambah);
        edt_atas_nama = findViewById(R.id.edt_atas_nama_bank_saya_tambah);
        btn_simpan = findViewById(R.id.btn_simpan_bank_saya_tambah);

        edt_nama_bank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nama_bank = s.toString();
            }
        });
        edt_nomor_rekening.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nomor_rekening = s.toString();
            }
        });
        edt_atas_nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                atas_nama = s.toString();
            }
        });
        btn_simpan.setOnClickListener(v -> {
            if(!Util.isNotNullOrEmpty(nama_bank)){
                Toast.makeText(BankSayaTambahActivity.this, "Nama bank tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(nomor_rekening)){
                Toast.makeText(BankSayaTambahActivity.this, "Nomor rekening tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(atas_nama)){
                Toast.makeText(BankSayaTambahActivity.this, "Atas nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else{
                request(nama_bank,nomor_rekening,atas_nama);
            }
        });
    }

    void request(final String nama_bank, final String nomor_rekening, final String atas_nama){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(BankSayaTambahActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_bank_add("user/bank/add",nama_bank,nomor_rekening,atas_nama, headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [bank saya tambah]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            Toast.makeText(BankSayaTambahActivity.this,message, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BankSayaTambahActivity.this, BankSayaActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                        else{
                            Toast.makeText(BankSayaTambahActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(BankSayaTambahActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [bank saya tambah]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(BankSayaTambahActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(BankSayaTambahActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                Toast.makeText(BankSayaTambahActivity.this,message, Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(BankSayaTambahActivity.this, BankSayaActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                            }
//                            else{
//                                Toast.makeText(BankSayaTambahActivity.this,message, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(BankSayaTambahActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(BankSayaTambahActivity.this);
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
//                params.put("bank_name", nama_bank);
//                params.put("account_number", nomor_rekening);
//                params.put("account_name", atas_nama);
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
}