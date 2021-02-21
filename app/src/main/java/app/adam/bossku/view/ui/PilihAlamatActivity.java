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
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.view.adapter.PilihAlamatAdapter;
import app.adam.bossku.view.model.Alamat;
import app.adam.bossku.view.model.Bank;
import app.adam.bossku.view.model.Kecamatan;
import app.adam.bossku.view.model.Keranjang;
import app.adam.bossku.view.model.Kota;
import app.adam.bossku.view.model.Provinsi;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class PilihAlamatActivity extends AppCompatActivity implements PilihAlamatAdapter.AlamatListener {
    ArrayList<Alamat> list_alamat = new ArrayList<>();
    SpinKitView loader;
    RecyclerView rv_list;
    FloatingActionButton btn_tambah;

    String activity=null;
    Bank bank;
    Alamat alamat;
    ArrayList<Keranjang> list_keranjang = new ArrayList<>();
    PilihAlamatAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_alamat);

        rv_list = findViewById(R.id.rv_pilih_alamat);
        loader = findViewById(R.id.loader_pilih_alamat);
        btn_tambah = findViewById(R.id.btn_tambah_pilih_alamat);
        Toolbar mtoolbar = findViewById(R.id.toolbar_pilih_alamat);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PilihAlamatActivity.super.onBackPressed();
            }
        });

        Bundle b = getIntent().getExtras();
        if (b != null) {
            activity = b.getString("activity",null);
            bank = b.getParcelable("bank");
            alamat = b.getParcelable("alamat");
            list_keranjang = b.getParcelableArrayList("list_keranjang");

            if(activity.equals("PembayaranBarangActivity"))
                btn_tambah.setVisibility(View.GONE);
            else
                btn_tambah.setVisibility(View.VISIBLE);
        }
        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PilihAlamatActivity.this, TambahEditAlamatActivity.class);
                i.putExtra("isUpdate", false);
                i.putExtra("activity","TambahEditAlamatActivity");
                startActivity(i);
            }
        });

        load_data();
    }

    void load_data() {
        loader.setVisibility(View.VISIBLE);

        SessionManager session = new SessionManager(PilihAlamatActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_shipping_address_list("user/shipping_address/list", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pilih alamat]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if (status.equals("success")) {
                            JSONArray datas = res.getJSONArray("data");

                            for (int i = 0; i < datas.length(); i++) {
                                JSONObject obj = datas.getJSONObject(i);
                                JSONObject prov = obj.getJSONObject("province");
                                JSONObject kota = obj.getJSONObject("city");
                                JSONObject kec = obj.getJSONObject("subdistrict");

                                list_alamat.add(
                                        new Alamat(
                                                obj.getString("id"),
                                                obj.getString("name"),
                                                obj.getString("address"),
                                                new Provinsi(prov.getString("province_id"),prov.getString("province")),
                                                new Kota(kota.getString("city_id"),kota.getString("city_name"),kota.getString("type")),
                                                new Kecamatan(kec.getString("subdistrict_id"),kec.getString("subdistrict_name"),obj.getString("postal_code")),
                                                obj.getInt("is_default_address")
                                        )
                                );
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loader.setVisibility(View.GONE);
                                    cardAdapter = new PilihAlamatAdapter(PilihAlamatActivity.this, PilihAlamatActivity.this);
                                    cardAdapter.setListItem(list_alamat);
                                    cardAdapter.setActivity(activity);
                                    cardAdapter.notifyDataSetChanged();
                                    rv_list.setLayoutManager(new LinearLayoutManager(PilihAlamatActivity.this));
                                    rv_list.setHasFixedSize(true);
                                    rv_list.setAdapter(cardAdapter);
                                }
                            }, 1500);
                        } else {
                            loader.setVisibility(View.GONE);
                            Toast.makeText(PilihAlamatActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PilihAlamatActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pilih alamat]", t.toString());
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(PilihAlamatActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PilihAlamatActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                            if (status.equals("success")) {
//                                JSONArray datas = res.getJSONArray("data");
//
//                                for (int i = 0; i < datas.length(); i++) {
//                                    JSONObject obj = datas.getJSONObject(i);
//                                    JSONObject prov = obj.getJSONObject("province");
//                                    JSONObject kota = obj.getJSONObject("city");
//                                    JSONObject kec = obj.getJSONObject("subdistrict");
//
//                                    list_alamat.add(
//                                            new Alamat(
//                                                    obj.getString("id"),
//                                                    obj.getString("name"),
//                                                    obj.getString("address"),
//                                                    new Provinsi(prov.getString("province_id"),prov.getString("province")),
//                                                    new Kota(kota.getString("city_id"),kota.getString("city_name"),kota.getString("type")),
//                                                    new Kecamatan(kec.getString("subdistrict_id"),kec.getString("subdistrict_name"),obj.getString("postal_code")),
//                                                    obj.getInt("is_default_address")
//                                            )
//                                    );
//                                }
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        loader.setVisibility(View.GONE);
//                                        cardAdapter = new PilihAlamatAdapter(PilihAlamatActivity.this, PilihAlamatActivity.this);
//                                        cardAdapter.setListItem(list_alamat);
//                                        cardAdapter.setActivity(activity);
//                                        cardAdapter.notifyDataSetChanged();
//                                        rv_list.setLayoutManager(new LinearLayoutManager(PilihAlamatActivity.this));
//                                        rv_list.setHasFixedSize(true);
//                                        rv_list.setAdapter(cardAdapter);
//                                    }
//                                }, 1500);
//                            } else {
//                                loader.setVisibility(View.GONE);
//
//                                Toast.makeText(PilihAlamatActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(PilihAlamatActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PilihAlamatActivity.this);
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

    @Override
    public void SelectItemAlamat(Alamat item) {
        if(activity!=null && activity.equals("PembayaranBarangActivity")) {
            Intent i = new Intent(this, PembayaranBarangActivity.class);
            i.putExtra("bank", bank);
            i.putExtra("alamat", item);
            i.putExtra("list_keranjang", list_keranjang);
            startActivity(i);
        }
        else{
            startActivity(
                    new Intent(this, TambahEditAlamatActivity.class)
                            .putExtra("alamat",item)
                            .putExtra("provinsi",item.getProvinsi())
                            .putExtra("kota",item.getKota())
                            .putExtra("kecamatan",item.getKecamatan())
                            .putExtra("isUpdate",true)
                            .putExtra("activity","TambahEditAlamatActivity")
            );
        }
    }

    @Override
    public void DelItemAlamat(final Alamat item) {
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(PilihAlamatActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_shipping_address_delete(String.format("user/shipping_address/delete/%s" ,item.getId()), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pilih alamat]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if (status.equals("success")) {
                            cardAdapter.getListItem().remove(item);
                            cardAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(PilihAlamatActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PilihAlamatActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pilih alamat]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(PilihAlamatActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PilihAlamatActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                            if (status.equals("success")) {
//                                cardAdapter.getListItem().remove(item);
//                                cardAdapter.notifyDataSetChanged();
//                            }
//                            Toast.makeText(PilihAlamatActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        dialog_loading.dismissWithAnimation();
//                        Toast.makeText(PilihAlamatActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PilihAlamatActivity.this);
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

    @Override
    public void DefaultItemAlamat(final Alamat item) {
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(PilihAlamatActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_shipping_address_change_default("user/shipping_address/change_default", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pilih alamat]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if (status.equals("success")) {
                            ArrayList<Alamat> list = cardAdapter.getListItem();
                            for (Alamat alamat: list) {
                                if(alamat.getId().equals(item.getId()))
                                    alamat.setDef(1);
                                else
                                    alamat.setDef(0);
                            }
                            cardAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(PilihAlamatActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PilihAlamatActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pilih alamat]", t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(PilihAlamatActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PilihAlamatActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                            if (status.equals("success")) {
//                                ArrayList<Alamat> list = cardAdapter.getListItem();
//                                for (Alamat alamat: list) {
//                                    if(alamat.getId().equals(item.getId()))
//                                        alamat.setDef(1);
//                                    else
//                                        alamat.setDef(0);
//                                }
//                                cardAdapter.notifyDataSetChanged();
//                            }
//                            Toast.makeText(PilihAlamatActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        dialog_loading.dismissWithAnimation();
//                        Toast.makeText(PilihAlamatActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PilihAlamatActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//
//            protected Map<String, String> getParams()
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", item.getId());
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
}