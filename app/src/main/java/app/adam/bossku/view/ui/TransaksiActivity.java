package app.adam.bossku.view.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import app.adam.bossku.helper.Route;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.view.adapter.TransaksiAdapter;
import app.adam.bossku.view.model.Product;
import app.adam.bossku.view.model.Transaksi;
import retrofit2.Call;
import retrofit2.Callback;

public class TransaksiActivity extends AppCompatActivity implements TransaksiAdapter.TransaksiListener {
    RecyclerView rv_list;
    SpinKitView loader;
    boolean isDefault=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        rv_list = findViewById(R.id.rv_list_transaksi);
        loader = findViewById(R.id.loader_transaksi);
        Toolbar mtoolbar = findViewById(R.id.toolbar_transaksi);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> TransaksiActivity.super.onBackPressed());
        rv_list.setHasFixedSize(true);

        load_data();
    }

    void load_data(){
        final ArrayList<Transaksi> list_item = new ArrayList<>();

        if(isDefault) {
            Transaksi transaksi1 = new Transaksi("1", "A001", "1 Januari 2020", "Toko 1", "10000", "foto", -1);
            Transaksi transaksi2 = new Transaksi("2", "A002", "2 Januari 2020", "Toko 2", "20000", "foto", 0);
            Transaksi transaksi3 = new Transaksi("3", "A003", "3 Januari 2020", "Toko 3", "30000", "foto", 1);

            list_item.add(transaksi1);
            list_item.add(transaksi2);
            list_item.add(transaksi3);

            rv_list.setLayoutManager(new LinearLayoutManager(this));
            TransaksiAdapter cardAdapter = new TransaksiAdapter(this,true,this);
            cardAdapter.setListItem(list_item);
            cardAdapter.notifyDataSetChanged();
            rv_list.setAdapter(cardAdapter);
        }
        else {
            rv_list.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);

            SessionManager session = new SessionManager(TransaksiActivity.this);
            HashMap<String, String> data = session.getSessionLogin();

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Map<String, String>  headers = new HashMap<>();
            headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

            Call<String> call = getResponse.request_user_transaction_list(String.format("user/transaction/list/%s?page=%s&limit=%s","1","0","10"), headers);
            Log.i("app-log", "request to "+call.request().url().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String res_ = response.body();
                    Log.i("app-log [transaksi]", res_);
                    rv_list.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);

                    if(response.isSuccessful()) {
                        try {
                            assert res_ != null;
                            JSONObject res = new JSONObject(res_);
                            String status = res.getString("status");
                            String message = res.getString("message");

                            if(status.equals("success")) {
                                JSONArray data = res.getJSONObject("data").getJSONArray("data");
                                for (int i=0;i<data.length();i++){
                                    JSONObject obj = data.getJSONObject(i);
                                    JSONArray cart = obj.getJSONArray("cart");

                                    String transaction_code = obj.getString("transaction_code");
                                    ArrayList<Product> list_product = new ArrayList<>();
                                    for(int j=0;j<cart.length();j++) {
                                        JSONObject cartObj = cart.getJSONObject(j);
                                        JSONObject goods = cartObj.getJSONObject("goods");

                                        Product product = new Product(
                                                goods.getString("id"),
                                                goods.getString("name"),
                                                "",
                                                null,
                                                0,
                                                null,
                                                null,
                                                null,
                                                0
                                        );
                                        list_product.add(product);
                                    }
                                    list_item.add(new Transaksi(transaction_code, list_product));
                                }
                                rv_list.setLayoutManager(new LinearLayoutManager(TransaksiActivity.this));
                                TransaksiAdapter cardAdapter = new TransaksiAdapter(TransaksiActivity.this, false,TransaksiActivity.this);
                                cardAdapter.setListItem(list_item);
                                cardAdapter.notifyDataSetChanged();
                                rv_list.setAdapter(cardAdapter);
                            }
                            else {
                                Toast.makeText(TransaksiActivity.this,message,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(TransaksiActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("app-log [transaksi]", t.toString());
                    rv_list.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);

                    if(call.isCanceled()) {
                        Toast.makeText(TransaksiActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(TransaksiActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                    }
                }
            });

//            RequestQueue queue = Volley.newRequestQueue(this);
//            Log.i("app-log", url);
//
//            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.i("app-log", response);
//                            rv_list.setVisibility(View.VISIBLE);
//                            loader.setVisibility(View.GONE);
//
//                            try {
//                                JSONObject res = new JSONObject(response);
//                                String status = res.getString("status");
//                                String message = res.getString("message");
//
//                                if(status.equals("success")) {
//                                    JSONArray data = res.getJSONObject("data").getJSONArray("data");
//                                    for (int i=0;i<data.length();i++){
//                                        JSONObject obj = data.getJSONObject(i);
//                                        JSONArray cart = obj.getJSONArray("cart");
//
//                                        String transaction_code = obj.getString("transaction_code");
//                                        ArrayList<Product> list_product = new ArrayList<>();
//                                        for(int j=0;j<cart.length();j++) {
//                                            JSONObject cartObj = cart.getJSONObject(j);
//                                            JSONObject goods = cartObj.getJSONObject("goods");
//
//                                            Product product = new Product(
//                                                    goods.getString("id"),
//                                                    goods.getString("name"),
//                                                    "",
//                                                    null,
//                                                    0,
//                                                    null,
//                                                    null,
//                                                    null,
//                                                    0
//                                            );
//                                            list_product.add(product);
//                                        }
//                                        list_item.add(new Transaksi(transaction_code, list_product));
//                                    }
//                                    rv_list.setLayoutManager(new LinearLayoutManager(TransaksiActivity.this));
//                                    TransaksiAdapter cardAdapter = new TransaksiAdapter(TransaksiActivity.this, false,TransaksiActivity.this);
//                                    cardAdapter.setListItem(list_item);
//                                    cardAdapter.notifyDataSetChanged();
//                                    rv_list.setAdapter(cardAdapter);
//                               }
//                                else {
//                                    Toast.makeText(TransaksiActivity.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(TransaksiActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                            rv_list.setVisibility(View.VISIBLE);
//                            loader.setVisibility(View.GONE);
//                        }
//                    }
//            ) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    Log.i("app-log token", data.get(SessionManager.KEY_TOKEN));
//
//                    Map<String, String> params = new HashMap<>();
//                    params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                    params.put("Accept", "application/json");
//
//                    return params;
//                }
//
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    return super.parseNetworkResponse(response);
//                }
//            };
//
//            postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(postRequest);
        }
    }

    @Override
    public void SelectItemTransaksi(Transaksi item) {
        startActivity(
                new Intent(this, DetailTransaksiActivity.class)
                        .putExtra("isDefault",isDefault)
                        .putExtra("transaksi",item)
        );
    }
}