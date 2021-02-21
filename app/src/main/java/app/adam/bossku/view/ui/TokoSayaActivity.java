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
import app.adam.bossku.view.adapter.ProdukTokoAdapter;
import app.adam.bossku.view.model.ProductToko;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class TokoSayaActivity extends AppCompatActivity implements ProdukTokoAdapter.ProductTokoListener {
    RecyclerView rv_list;
    FloatingActionButton btn_add;
    SpinKitView loader;
    ArrayList<ProductToko> list_item;
    SweetAlertDialog dialog_loading;
    ProdukTokoAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko_saya);

        rv_list = findViewById(R.id.rv_list_toko_saya);
        btn_add = findViewById(R.id.btn_add_toko_saya);
        loader = findViewById(R.id.loader_toko_saya);
        Toolbar mtoolbar = findViewById(R.id.toolbar_toko_saya);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> TokoSayaActivity.super.onBackPressed());
        list_item = new ArrayList<>();
        load_product();

        btn_add.setOnClickListener(v -> startActivity(
                new Intent(TokoSayaActivity.this, TambahEditProdukActivity.class)
                        .putExtra("isUpdate",true)
        ));
    }

    void load_product(){
        loader.setVisibility(View.VISIBLE);

        SessionManager session = new SessionManager(TokoSayaActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_goods("user/goods/selfproduct", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
//                Log.i("app-log [toko saya]", res_);

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
                                list_item.add(
                                        new ProductToko(
                                                obj.getString("id"),
                                                obj.getString("name"),
                                                obj.getString("image"),
                                                obj.getInt("price"),
                                                0,
                                                obj.getInt("stock"),
                                                obj.getInt("weight"),
                                                obj.getString("description")
                                        )
                                );
                            }

                            new Handler().postDelayed(() -> {
                                loader.setVisibility(View.GONE);
                                rv_list.setLayoutManager(new LinearLayoutManager(TokoSayaActivity.this));
                                cardAdapter = new ProdukTokoAdapter(TokoSayaActivity.this,TokoSayaActivity.this);
                                cardAdapter.setListItem(list_item);
                                cardAdapter.notifyDataSetChanged();
                                rv_list.setAdapter(cardAdapter);
                            }, 1500);
                        } else {
                            loader.setVisibility(View.GONE);
                            Toast.makeText(TokoSayaActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    loader.setVisibility(View.GONE);
                    Toast.makeText(TokoSayaActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [toko saya]", t.toString());
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(TokoSayaActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(TokoSayaActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                    list_item.add(
//                                            new ProductToko(
//                                                    obj.getString("id"),
//                                                    obj.getString("name"),
//                                                    obj.getString("image"),
//                                                    obj.getInt("price"),
//                                                    0,
//                                                    obj.getInt("stock"),
//                                                    obj.getInt("weight"),
//                                                    obj.getString("description")
//                                            )
//                                    );
//                                }
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        loader.setVisibility(View.GONE);
//                                        rv_list.setLayoutManager(new LinearLayoutManager(TokoSayaActivity.this));
//                                        cardAdapter = new ProdukTokoAdapter(TokoSayaActivity.this,TokoSayaActivity.this);
//                                        cardAdapter.setListItem(list_item);
//                                        cardAdapter.notifyDataSetChanged();
//                                        rv_list.setAdapter(cardAdapter);
//                                    }
//                                }, 1500);
//                            } else {
//                                loader.setVisibility(View.GONE);
//
//                                Toast.makeText(TokoSayaActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(TokoSayaActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(TokoSayaActivity.this);
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
    void request_delete(final String id){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(TokoSayaActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_goods_delete("user/goods/delete",id, headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [toko saya]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            Toast.makeText(TokoSayaActivity.this,message, Toast.LENGTH_SHORT).show();
                            list_item.clear();
                            cardAdapter.setListItem(list_item);
                            cardAdapter.notifyDataSetChanged();
                            load_product();
                        }
                        else{
                            Toast.makeText(TokoSayaActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(TokoSayaActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [keranjang]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(TokoSayaActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(TokoSayaActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                Toast.makeText(TokoSayaActivity.this,message, Toast.LENGTH_SHORT).show();
//                                list_item.clear();
//                                cardAdapter.setListItem(list_item);
//                                cardAdapter.notifyDataSetChanged();
//                                load_product();
//                            }
//                            else{
//                                Toast.makeText(TokoSayaActivity.this,message, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(TokoSayaActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(TokoSayaActivity.this);
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
    public void SelectItemProduc(ProductToko item) {
        startActivity(
                new Intent(this, DetailProdukActivity.class)
                        .putExtra("action","TokoSayaActivity")
        );
    }

    @Override
    public void DeleteItemProduc(ProductToko item) {
        request_delete(item.getId_produk());
    }

    @Override
    public void EditItemProduc(ProductToko item) {
        startActivity(
                new Intent(this, TambahEditProdukActivity.class)
                        .putExtra("produk",item)
                        .putExtra("isUpdate",true)
        );
    }
}