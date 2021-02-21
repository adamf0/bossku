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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.view.adapter.PilihBankAdapter;
import app.adam.bossku.view.model.Alamat;
import app.adam.bossku.view.model.Bank;
import app.adam.bossku.view.model.Keranjang;
import app.adam.bossku.view.model.Pendanaan;
import retrofit2.Call;
import retrofit2.Callback;

public class PilihBankActivity extends AppCompatActivity implements PilihBankAdapter.BankListener {
    ArrayList<Bank> list_bank = new ArrayList<>();
    SpinKitView loader;
    RecyclerView rv_list;

    Bank bank;
    Alamat alamat;
    String activity=null;
    String nominal;
    Pendanaan pendanaan=null;
    ArrayList<Keranjang> list_keranjang = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_bank);

        rv_list = findViewById(R.id.rv_pilih_bank);
        loader = findViewById(R.id.loader_pilih_bank);
        Toolbar mtoolbar = findViewById(R.id.toolbar_pilih_bank);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> PilihBankActivity.super.onBackPressed());

        Bundle b = getIntent().getExtras();
        if(b != null){
            activity   = b.getString("activity");
            bank   = b.getParcelable("bank");
            alamat   = b.getParcelable("alamat");
            list_keranjang = b.getParcelableArrayList("list_keranjang");

            nominal = b.getString("nominal");
            pendanaan = b.getParcelable("pendanaan");
        }

        load_data();
    }
    void load_data(){
        loader.setVisibility(View.VISIBLE);

        SessionManager session = new SessionManager(PilihBankActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_bank_list("bank/list", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pilih bank]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
//                        String message = res.getString("message");

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

                                new Handler().postDelayed(() -> {
                                    PilihBankAdapter cardAdapter = new PilihBankAdapter(PilihBankActivity.this,PilihBankActivity.this);
                                    cardAdapter.setListItem(list_bank);
                                    cardAdapter.notifyDataSetChanged();
                                    rv_list.setLayoutManager(new LinearLayoutManager(PilihBankActivity.this));
                                    rv_list.setHasFixedSize(true);
                                    rv_list.setAdapter(cardAdapter);
                                    loader.setVisibility(View.GONE);
                                },1500);
                            }
                        }
                        else{
                            loader.setVisibility(View.GONE);
                            Toast.makeText(PilihBankActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PilihBankActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [keranjang]", t.toString());
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(PilihBankActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PilihBankActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
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
//
//                                            )
//                                    );
//
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            PilihBankAdapter cardAdapter = new PilihBankAdapter(PilihBankActivity.this,PilihBankActivity.this);
//                                            cardAdapter.setListItem(list_bank);
//                                            cardAdapter.notifyDataSetChanged();
//                                            rv_list.setLayoutManager(new LinearLayoutManager(PilihBankActivity.this));
//                                            rv_list.setHasFixedSize(true);
//                                            rv_list.setAdapter(cardAdapter);
//                                            loader.setVisibility(View.GONE);
//                                        }
//                                    },1500);
//                                }
//                            }
//                            else{
//                                loader.setVisibility(View.GONE);
//                                Toast.makeText(PilihBankActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(PilihBankActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(PilihBankActivity.this);
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

    @Override
    public void SelectItemBank(Bank item) {
        if(activity.equals("PembayaranBarangActivity")) {
            Intent i = new Intent(this, PembayaranBarangActivity.class);
            i.putExtra("bank", item);
            i.putExtra("alamat", alamat);
            i.putExtra("list_keranjang", list_keranjang);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        else if(activity.equals("PembayaranPendanaanActivity")){
            Intent i = new Intent(this, PembayaranPendanaanActivity.class);
            i.putExtra("bank", item);
            i.putExtra("nominal", nominal);
            i.putExtra("pendanaan", pendanaan);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
}