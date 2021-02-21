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
import androidx.recyclerview.widget.GridLayoutManager;
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
import app.adam.bossku.view.adapter.ProdukKategoriAdapter;
import app.adam.bossku.view.model.Kategori;
import retrofit2.Call;
import retrofit2.Callback;

public class KategoriActivity extends AppCompatActivity implements ProdukKategoriAdapter.ProductCategoryListener {
    ArrayList<Kategori> list_kategori = new ArrayList<>();
    SpinKitView loader;
    RecyclerView rv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        rv_list = findViewById(R.id.rv_list_kategori);
        loader = findViewById(R.id.loader_kategori);
        Toolbar mtoolbar = findViewById(R.id.toolbar_kategori);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> KategoriActivity.super.onBackPressed());

        load_data();
    }

    void load_data(){
        loader.setVisibility(View.VISIBLE);

        SessionManager session = new SessionManager(KategoriActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_master_data_category_list("master_data/category/list", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [kategori]", res_);

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

                                list_kategori.add(
                                        new Kategori(
                                                obj.getString("id"),
                                                obj.getString("name"),
                                                null,
                                                obj.getString("link")
                                        )
                                );
                            }

                            new Handler().postDelayed(() -> {
                                ProdukKategoriAdapter cardAdapter = new ProdukKategoriAdapter(KategoriActivity.this,KategoriActivity.this);
                                cardAdapter.setListItem(list_kategori);
                                cardAdapter.notifyDataSetChanged();
                                rv_list.setLayoutManager(new GridLayoutManager(KategoriActivity.this,4));
                                rv_list.setHasFixedSize(true);
                                rv_list.setAdapter(cardAdapter);
                                loader.setVisibility(View.GONE);
                            },1500);
                        }
                        else{
                            Toast.makeText(KategoriActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(KategoriActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                    loader.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [kategori]", t.toString());
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(KategoriActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(KategoriActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//
//                                for (int i=0;i<datas.length();i++){
//                                    JSONObject obj = datas.getJSONObject(i);
//
//                                    list_kategori.add(
//                                            new Kategori(
//                                                    obj.getString("id"),
//                                                    obj.getString("name"),
//                                                    null,
//                                                    obj.getString("link")
//                                            )
//                                    );
//                                }
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ProdukKategoriAdapter cardAdapter = new ProdukKategoriAdapter(KategoriActivity.this,KategoriActivity.this);
//                                        cardAdapter.setListItem(list_kategori);
//                                        cardAdapter.notifyDataSetChanged();
//                                        rv_list.setLayoutManager(new GridLayoutManager(KategoriActivity.this,4));
//                                        rv_list.setHasFixedSize(true);
//                                        rv_list.setAdapter(cardAdapter);
//                                        loader.setVisibility(View.GONE);
//                                    }
//                                },1500);
//                            }
//                            else{
//                                Toast.makeText(KategoriActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                                loader.setVisibility(View.GONE);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(KategoriActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                        loader.setVisibility(View.GONE);
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(KategoriActivity.this);
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
    public void SelectItemProducCategory(Kategori item) {
        startActivity(
                new Intent(this,CariProdukActivity.class)
                        .putExtra("search",item.getLink())
                        .putExtra("type","kategori")
        );
    }
}