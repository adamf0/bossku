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
import app.adam.bossku.view.adapter.PilihKurirAdapter;
import app.adam.bossku.view.model.Alamat;
import app.adam.bossku.view.model.Bank;
import app.adam.bossku.view.model.ItemKeranjang;
import app.adam.bossku.view.model.Keranjang;
import app.adam.bossku.view.model.Kurir;
import retrofit2.Call;
import retrofit2.Callback;

public class PilihKurirActivity extends AppCompatActivity implements PilihKurirAdapter.KurirListener {
    ArrayList<Kurir> tmp_kurir = new ArrayList<>();
    ArrayList<Kurir> list_kurir = new ArrayList<>();

    SpinKitView loader;
    RecyclerView rv_list;

    Bank bank=null;
    Alamat alamat=null;
    ItemKeranjang itemKeranjang=null;
    ArrayList<Keranjang> list_keranjang = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kurir);

        rv_list = findViewById(R.id.rv_pilih_kurir);
        loader = findViewById(R.id.loader_pilih_kurir);
        Toolbar mtoolbar = findViewById(R.id.toolbar_pilih_kurir);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> PilihKurirActivity.super.onBackPressed());

        Bundle b = getIntent().getExtras();
        if(b != null){
            bank   = b.getParcelable("bank");
            alamat   = b.getParcelable("alamat");
            itemKeranjang   = b.getParcelable("itemKeranjang");
            list_keranjang = b.getParcelableArrayList("list_keranjang");
        }

        load_data();
    }
    void load_data(){
        loader.setVisibility(View.VISIBLE);

        SessionManager session = new SessionManager(PilihKurirActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_courier_list("courier/list", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pilih kurir]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        //String message = res.getString("message");

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");
                            JSONArray datas = data.getJSONArray("data");
                            for (int i=0;i<datas.length();i++){
                                JSONObject obj = datas.getJSONObject(i);
                                tmp_kurir.add(
                                        new Kurir(
                                                obj.getString("id"),
                                                obj.getString("company_name"),
                                                obj.getString("courier_code"),
                                                null,null,0
                                        )
                                );
                            }
                            if(tmp_kurir.size()>0) {
                                if (alamat != null) {
                                    load_detail(tmp_kurir.get(0), 0);
                                }
                                else{
                                    loader.setVisibility(View.GONE);
                                    Toast.makeText(PilihKurirActivity.this,"Belum ada data alamat", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            loader.setVisibility(View.GONE);
                            Toast.makeText(PilihKurirActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PilihKurirActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [kurir]", t.toString());
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(PilihKurirActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PilihKurirActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                            String message = res.getString("message");
//
//                            if(status.equals("success")){
//                                JSONObject data = res.getJSONObject("data");
//                                JSONArray datas = data.getJSONArray("data");
//                                for (int i=0;i<datas.length();i++){
//                                    JSONObject obj = datas.getJSONObject(i);
//                                    tmp_kurir.add(
//                                            new Kurir(
//                                                    obj.getString("id"),
//                                                    obj.getString("company_name"),
//                                                    obj.getString("courier_code"),
//                                                    null,null,0
//                                            )
//                                    );
//                                }
//                                if(tmp_kurir.size()>0) {
//                                    if (alamat != null) {
//                                        load_detail(tmp_kurir.get(0), 0);
//                                    }
//                                    else{
//                                        loader.setVisibility(View.GONE);
//                                        Toast.makeText(PilihKurirActivity.this,"Belum ada data alamat", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                            else{
//                                loader.setVisibility(View.GONE);
//                                Toast.makeText(PilihKurirActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(PilihKurirActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(PilihKurirActivity.this);
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
    void load_detail(final Kurir kurir, final int position){
        Log.i("app-log", kurir.getCode());
        Log.i("app-log", String.valueOf(position));

        SessionManager session = new SessionManager(PilihKurirActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_rajaongkir_shipping_cost("rajaongkir/shipping_cost",alamat.getKecamatan().getId(),"2096",String.valueOf(getBerat()),kurir.getCode(), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pilih kurir]", res_);
                loader.setVisibility(View.GONE);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        //String message = res.getString("message");
                        int pos = position+1;

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");
                            JSONObject rajaongkir = data.getJSONObject("rajaongkir");
                            JSONArray results = rajaongkir.getJSONArray("results");
                            for (int i=0;i<results.length();i++){
                                JSONArray costs = results.getJSONObject(i).getJSONArray("costs");
                                for (int j=0;j<costs.length();j++){
                                    JSONObject obj = costs.getJSONObject(j);
                                    Log.i("app-log ->",obj.getString("service"));
                                    Log.i("app-log ->",obj.getJSONArray("cost").getJSONObject(0).getString("value"));

                                    list_kurir.add(
                                            new Kurir(
                                                    kurir.getId(),
                                                    kurir.getName(),
                                                    kurir.getCode(),
                                                    obj.getString("service"),
                                                    obj.getJSONArray("cost").getJSONObject(0).getString("etd")+" Hari",
                                                    obj.getJSONArray("cost").getJSONObject(0).getDouble("value")
                                            )
                                    );
                                }
                            }
                        }

                        if(tmp_kurir.size()>0 && pos < tmp_kurir.size()) {
                            Log.i("app-log","ex-1");
                            load_detail(tmp_kurir.get(pos), pos);
                        }
                        else {
                            new Handler().postDelayed(() -> {
                                PilihKurirAdapter cardAdapter = new PilihKurirAdapter(PilihKurirActivity.this,PilihKurirActivity.this);
                                cardAdapter.setListItem(list_kurir);
                                cardAdapter.notifyDataSetChanged();
                                rv_list.setLayoutManager(new LinearLayoutManager(PilihKurirActivity.this));
                                rv_list.setHasFixedSize(true);
                                rv_list.setAdapter(cardAdapter);
                                loader.setVisibility(View.GONE);
                            },1500);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PilihKurirActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pilih kurir]", t.toString());
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(PilihKurirActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PilihKurirActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("app-log",response);
//
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//                            int pos = position+1;
//
//                            if(status.equals("success")){
//                                JSONObject data = res.getJSONObject("data");
//                                JSONObject rajaongkir = data.getJSONObject("rajaongkir");
//                                JSONArray results = rajaongkir.getJSONArray("results");
//                                for (int i=0;i<results.length();i++){
//                                    JSONArray costs = results.getJSONObject(i).getJSONArray("costs");
//                                    for (int j=0;j<costs.length();j++){
//                                        JSONObject obj = costs.getJSONObject(j);
//                                        Log.i("app-log ->",obj.getString("service"));
//                                        Log.i("app-log ->",obj.getJSONArray("cost").getJSONObject(0).getString("value"));
//
//                                        list_kurir.add(
//                                                new Kurir(
//                                                        kurir.getId(),
//                                                        kurir.getName(),
//                                                        kurir.getCode(),
//                                                        obj.getString("service"),
//                                                        obj.getJSONArray("cost").getJSONObject(0).getString("etd")+" Hari",
//                                                        obj.getJSONArray("cost").getJSONObject(0).getDouble("value")
//                                                )
//                                        );
//                                    }
//                                }
//                            }
//
//                            if(tmp_kurir.size()>0 && pos < tmp_kurir.size()) {
//                                Log.i("app-log","ex-1");
//                                load_detail(tmp_kurir.get(pos), pos);
//                            }
//                            else {
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        PilihKurirAdapter cardAdapter = new PilihKurirAdapter(PilihKurirActivity.this,PilihKurirActivity.this);
//                                        cardAdapter.setListItem(list_kurir);
//                                        cardAdapter.notifyDataSetChanged();
//                                        rv_list.setLayoutManager(new LinearLayoutManager(PilihKurirActivity.this));
//                                        rv_list.setHasFixedSize(true);
//                                        rv_list.setAdapter(cardAdapter);
//                                        loader.setVisibility(View.GONE);
//                                    }
//                                },1500);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Log.i("app-log","ex-2");
//                        load_detail(tmp_kurir.get(position), position);
//                        Toast.makeText(PilihKurirActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PilihKurirActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String>  params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//            protected Map<String, String> getParams()
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("origin", alamat.getKecamatan().getId());
//                params.put("destination", "2096");
//                params.put("weight", String.valueOf(getBerat()));
//                params.put("courier", kurir.getCode());
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }

    @Override
    public void SelectItemKurir(Kurir item) {
        for (int i=0;i<list_keranjang.size();i++){
            ArrayList<ItemKeranjang> list_item = list_keranjang.get(i).getItemKeranjang();
            for (int j=0;j<list_item.size();j++) {
                ItemKeranjang x = list_item.get(j);
                if(x.getId().equals(itemKeranjang.getId())){
                    x.setKurir(item);
                }
            }
        }
        Intent i = new Intent(this, PembayaranBarangActivity.class);
        i.putExtra("bank",bank);
        i.putExtra("alamat",alamat);
        i.putExtra("list_keranjang",list_keranjang);
        startActivity(i);
    }
    int getBerat(){
        int tmp=0;
        for (int i=0;i<list_keranjang.size();i++){
            ArrayList<ItemKeranjang> list_item = list_keranjang.get(i).getItemKeranjang();
            for (int j=0;j<list_item.size();j++) {
                ItemKeranjang x = list_item.get(j);
                if(x.getId().equals(itemKeranjang.getId())){
                    tmp+=x.getBerat();
                }
            }
        }
        return tmp;
    }
}