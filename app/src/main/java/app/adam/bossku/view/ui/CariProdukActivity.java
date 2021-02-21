package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import app.adam.bossku.view.adapter.CariProdukAdapter;
import app.adam.bossku.view.model.Product;
import retrofit2.Call;
import retrofit2.Callback;

public class CariProdukActivity extends AppCompatActivity implements CariProdukAdapter.ProductListener {
    RecyclerView rv_list;
    SpinKitView loader;
    TextView txt_label;
    ArrayList<Product> list_item = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian_produk);

        rv_list = findViewById(R.id.rv_list_pencarian_produk);
        txt_label = findViewById(R.id.txt_judul_pencarian_produk);
        loader = findViewById(R.id.loader_pencarian_produk);
        Toolbar mtoolbar = findViewById(R.id.toolbar_pencarian_produk);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> CariProdukActivity.super.onBackPressed());

        rv_list.setHasFixedSize(true);

        Bundle b = getIntent().getExtras();
        if(b != null){
            String search = b.getString("search");
            String type = b.getString("type");

            if(type.equals("kategori"))
                txt_label.setText("Cari produk dengan kategori \""+search+"\"");

            load_data(search);
        }
        else {
            Product product1 = new Product("1", "Produk 1", "gambar produk 1", "deskripsi produk 1", 10, "100gr", "Toko 1", "Daerah 1", 1000);
            Product product2 = new Product("2", "Produk 2", "gambar produk 2", "deskripsi produk 2", 20, "200gr", "Toko 2", "Daerah 2", 2000);
            Product product3 = new Product("3", "Produk 3", "gambar produk 3", "deskripsi produk 3", 30, "300gr", "Toko 3", "Daerah 3", 3000);

            list_item.add(product1);
            list_item.add(product2);
            list_item.add(product3);

            rv_list.setLayoutManager(new LinearLayoutManager(this));
            CariProdukAdapter cardAdapter = new CariProdukAdapter(this,this);
            cardAdapter.setListItem(list_item);
            cardAdapter.notifyDataSetChanged();
            rv_list.setAdapter(cardAdapter);
        }
    }

    void load_data(String search){
        loader.setVisibility(View.VISIBLE);

        SessionManager session = new SessionManager(CariProdukActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_goods_category(String.format("goods/category/%s",search), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [cari produk]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if (status.equals("success")) {
                            JSONArray data = res.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);

                                list_item.add(
                                        new Product(
                                                obj.getString("id"),
                                                obj.getString("name"),
                                                obj.getString("image"),
                                                null,
                                                0,
                                                null,
                                                "Toko 1",
                                                "Daerah 1",
                                                obj.getInt("price")
                                        )
                                );
                            }

                            new Handler().postDelayed(() -> {
                                rv_list.setLayoutManager(new LinearLayoutManager(CariProdukActivity.this));
                                CariProdukAdapter cardAdapter = new CariProdukAdapter(CariProdukActivity.this, CariProdukActivity.this);
                                cardAdapter.setListItem(list_item);
                                cardAdapter.notifyDataSetChanged();
                                rv_list.setAdapter(cardAdapter);
                                loader.setVisibility(View.GONE);
                            }, 1500);
                        } else {
                            Toast.makeText(CariProdukActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(CariProdukActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                    loader.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [cari produk]", t.toString());
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(CariProdukActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(CariProdukActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if(type.equals("kategori")) {
//                            try {
//                                JSONObject res = new JSONObject(response);
//                                String status = res.getString("status");
//                                String message = res.getString("message");
//
//                                if (status.equals("success")) {
//                                    JSONArray data = res.getJSONArray("data");
//                                    for (int i = 0; i < data.length(); i++) {
//                                        JSONObject obj = data.getJSONObject(i);
//
//                                        list_item.add(
//                                                new Product(
//                                                        obj.getString("id"),
//                                                        obj.getString("name"),
//                                                        obj.getString("image"),
//                                                        null,
//                                                        0,
//                                                        null,
//                                                        "Toko 1",
//                                                        "Daerah 1",
//                                                        obj.getInt("price")
//                                                )
//                                        );
//                                    }
//
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            rv_list.setLayoutManager(new LinearLayoutManager(CariProdukActivity.this));
//                                            CariProdukAdapter cardAdapter = new CariProdukAdapter(CariProdukActivity.this, CariProdukActivity.this);
//                                            cardAdapter.setListItem(list_item);
//                                            cardAdapter.notifyDataSetChanged();
//                                            rv_list.setAdapter(cardAdapter);
//                                            loader.setVisibility(View.GONE);
//                                        }
//                                    }, 1500);
//                                } else {
//                                    Toast.makeText(CariProdukActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                                    loader.setVisibility(View.GONE);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(CariProdukActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                        loader.setVisibility(View.GONE);
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(CariProdukActivity.this);
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
    public void SelectItemProduct(Product item) {
        Intent i = new Intent(this, DetailProdukActivity.class);
        i.putExtra("action","CariProdukActivity");
        i.putExtra("produk",item);
        startActivity(i);
    }
}