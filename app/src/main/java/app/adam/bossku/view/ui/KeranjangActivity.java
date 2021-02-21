package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.adapter.ItemKeranjangAdapter;
import app.adam.bossku.view.adapter.KeranjangAdapter;
import app.adam.bossku.view.model.Alamat;
import app.adam.bossku.view.model.Bank;
import app.adam.bossku.view.model.ItemKeranjang;
import app.adam.bossku.view.model.Keranjang;
import app.adam.bossku.view.model.Product;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class KeranjangActivity extends AppCompatActivity implements ItemKeranjangAdapter.ItemKeranjangListener {
    RecyclerView rv_list;
    KeranjangAdapter cardAdapter;
    TextView txt_total_bayar;
    Button btn_bayar;
    CardView layout_total_harga;
    SpinKitView loader;

    Bank bank=null;
    Alamat alamat=null;
    ArrayList<Keranjang> list_keranjang_select = new ArrayList<>();
    double total = 0f;
    boolean isDefault=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        rv_list = findViewById(R.id.rv_list_keranjang);
        txt_total_bayar = findViewById(R.id.txt_total_bayar_keranjang);
        btn_bayar = findViewById(R.id.btn_bayar_keranjang);
        loader = findViewById(R.id.loader_keranjang);
        layout_total_harga = findViewById(R.id.layout_total_harga_keranjang);
        Toolbar mtoolbar = findViewById(R.id.toolbar_keranjang);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> KeranjangActivity.super.onBackPressed());
        rv_list.setHasFixedSize(true);
        load_data(isDefault);

        btn_bayar.setOnClickListener(v -> {
            Intent i = new Intent(KeranjangActivity.this, PembayaranBarangActivity.class);
            i.putExtra("bank",bank);
            i.putExtra("alamat",alamat);
            i.putExtra("list_keranjang",list_keranjang_select);
            startActivity(i);
        });
    }

    void load_data(boolean isDefault){
        final ArrayList<Keranjang> list_keranjang = new ArrayList<>();

        if(isDefault) {
            ArrayList<ItemKeranjang> list_item1 = new ArrayList<>();
            list_item1.add(new ItemKeranjang(false, "1", "Produk 1", 200,1000f, 5, "foto", null,"warna hijau"));
            list_item1.add(new ItemKeranjang(false, "2", "Produk 2", 300,5000f, 1, "foto", null,"jangan lupa diasah"));
            Keranjang transaksi1 = new Keranjang("A01", "Toko 1", list_item1, 10000f,"");

            ArrayList<ItemKeranjang> list_item2 = new ArrayList<>();
            list_item2.add(new ItemKeranjang(false, "dxJEJvG8_zlr0ESUW1vD5", "Bubur Ayam", 400,10000f, 1, "foto", null,"testing"));
            list_item2.add(new ItemKeranjang(false, "dxJEJvG8_zlr0ESUW1vD4", "Teh Manis", 500,6000f, 1, "foto", null,"gula dikit aja"));
            Keranjang transaksi2 = new Keranjang("A02", "Juragan Celoteh", list_item2, 10000f,"");

            list_keranjang.add(transaksi2);
            list_keranjang.add(transaksi1);

            rv_list.setLayoutManager(new LinearLayoutManager(this));
            cardAdapter = new KeranjangAdapter(this, this);
            cardAdapter.setListItem(list_keranjang);
            cardAdapter.notifyDataSetChanged();
            rv_list.setAdapter(cardAdapter);
        }
        else {
            layout_total_harga.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);

            SessionManager session = new SessionManager(KeranjangActivity.this);
            HashMap<String, String> data = session.getSessionLogin();

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Map<String, String>  headers = new HashMap<>();
            headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

            Call<String> call = getResponse.request_user_cart_list("user/cart/list", headers);
            Log.i("app-log", "request to "+call.request().url().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String res_ = response.body();
                    Log.i("app-log [keranjang]", res_);
                    layout_total_harga.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);

                    if(response.isSuccessful()) {
                        try {
                            assert res_ != null;
                            JSONObject res = new JSONObject(res_);
                            String status = res.getString("status");
                            String message = res.getString("message");

                            if(status.equals("success")) {
                                JSONArray data = res.getJSONArray("data");

                                for (int i = 0; i < data.length(); i++) {
                                    ArrayList<ItemKeranjang> list_item = new ArrayList<>();
                                    JSONObject obj = data.getJSONObject(i);
                                    JSONObject barang = obj.getJSONObject("goods");
                                    JSONObject toko = barang.getJSONObject("user").getJSONObject("umkm_data");
                                    double total = barang.getDouble("price")*obj.getInt("quantity");

                                    list_item.add(
                                            new ItemKeranjang(
                                                    false,
                                                    barang.getString("id"),
                                                    barang.getString("name"),
                                                    barang.getInt("weight"),
                                                    barang.getDouble("price"),
                                                    obj.getInt("quantity"),
                                                    obj.getString("goods_image"),
                                                    null,
                                                    ""
                                            )
                                    );
                                    Keranjang transaksi = new Keranjang(obj.getString("goods_id"), toko.getString("brand_name"), list_item, total,"");
                                    list_keranjang.add(transaksi);
                                }

                                rv_list.setLayoutManager(new LinearLayoutManager(KeranjangActivity.this));
                                cardAdapter = new KeranjangAdapter(KeranjangActivity.this, KeranjangActivity.this);
                                cardAdapter.setListItem(list_keranjang);
                                cardAdapter.notifyDataSetChanged();
                                rv_list.setAdapter(cardAdapter);
                            }
                            else {
                                Toast.makeText(KeranjangActivity.this,message,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(KeranjangActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("app-log [keranjang]", t.toString());
                    layout_total_harga.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);

                    if(call.isCanceled()) {
                        Toast.makeText(KeranjangActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(KeranjangActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                    }
                }
            });

//            SessionManager session = new SessionManager(KeranjangActivity.this);
//            final HashMap<String, String> data = session.getSessionLogin();
//            String url = Route.base_url + Route.list_cart;
//
//            RequestQueue queue = Volley.newRequestQueue(this);
//            Log.i("app-log", url);
//
//            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.i("app-log", response);
//                            layout_total_harga.setVisibility(View.VISIBLE);
//                            loader.setVisibility(View.GONE);
//
//                            try {
//                                JSONObject res = new JSONObject(response);
//                                String status = res.getString("status");
//                                String message = res.getString("message");
//
//                                if(status.equals("success")) {
//                                    JSONArray data = res.getJSONArray("data");
//
//                                    for (int i = 0; i < data.length(); i++) {
//                                        ArrayList<ItemKeranjang> list_item = new ArrayList<>();
//                                        JSONObject obj = data.getJSONObject(i);
//                                        JSONObject barang = obj.getJSONObject("goods");
//                                        JSONObject toko = barang.getJSONObject("user").getJSONObject("umkm_data");
//                                        double total = barang.getDouble("price")*obj.getInt("quantity");
//
//                                        list_item.add(
//                                                new ItemKeranjang(
//                                                        false,
//                                                        barang.getString("id"),
//                                                        barang.getString("name"),
//                                                        barang.getInt("weight"),
//                                                        barang.getDouble("price"),
//                                                        obj.getInt("quantity"),
//                                                        obj.getString("goods_image"),
//                                                        null,
//                                                        ""
//                                                )
//                                        );
//                                        Keranjang transaksi = new Keranjang(obj.getString("goods_id"), toko.getString("brand_name"), list_item, total,"");
//                                        list_keranjang.add(transaksi);
//                                    }
//
//                                    rv_list.setLayoutManager(new LinearLayoutManager(KeranjangActivity.this));
//                                    cardAdapter = new KeranjangAdapter(KeranjangActivity.this, KeranjangActivity.this);
//                                    cardAdapter.setListItem(list_keranjang);
//                                    cardAdapter.notifyDataSetChanged();
//                                    rv_list.setAdapter(cardAdapter);
//                                }
//                                else {
//                                    Toast.makeText(KeranjangActivity.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(KeranjangActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                            layout_total_harga.setVisibility(View.VISIBLE);
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

    void req_plus_minus_qty(final boolean isPlus, final int position_keranjang, final TextView txt_jumlah_item, final ItemKeranjang item){
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(KeranjangActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_cart_plus_minus(String.format((isPlus? "user/cart/plus/%s":"user/cart/minus/%s") ,item.getId()), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [keranjang]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")) {
                            ArrayList<ItemKeranjang> list_item = cardAdapter.getListItem().get(position_keranjang).getItemKeranjang();
                            for (int i=0;i<list_item.size();i++){
                                ItemKeranjang obj = list_item.get(i);
                                if(obj.getId().equals(item.getId())){
                                    obj.setJumlah_beli( (isPlus? item.getJumlah_beli()+1:item.getJumlah_beli()-1) );

                                    txt_jumlah_item.setText(String.valueOf(item.getJumlah_beli()));
                                    HitungUlang(item,!isPlus,isPlus);
                                }
                            }
                            Toast.makeText(KeranjangActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(KeranjangActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(KeranjangActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [keranjang]", t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(KeranjangActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(KeranjangActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        RequestQueue queue = Volley.newRequestQueue(this);
//        Log.i("app-log", url);
//
//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("app-log", response);
//                        dialog_loading.dismissWithAnimation();
//
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//
//                            if(status.equals("success")) {
//                                ArrayList<ItemKeranjang> list_item = cardAdapter.getListItem().get(position_keranjang).getItemKeranjang();
//                                for (int i=0;i<list_item.size();i++){
//                                    ItemKeranjang obj = list_item.get(i);
//                                    if(obj.getId().equals(item.getId())){
//                                        obj.setJumlah_beli( (isPlus? item.getJumlah_beli()+1:item.getJumlah_beli()-1) );
//
//                                        txt_jumlah_item.setText(String.valueOf(item.getJumlah_beli()));
//                                        HitungUlang(item,!isPlus,isPlus);
//                                    }
//                                }
//                            }
//
//                            Toast.makeText(KeranjangActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(KeranjangActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                        dialog_loading.dismissWithAnimation();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Log.i("app-log token", data.get(SessionManager.KEY_TOKEN));
//
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//
//                return params;
//            }
//
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                return super.parseNetworkResponse(response);
//            }
//        };
//
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }

    void HitungUlang(ItemKeranjang item,boolean kurang,boolean tambah){
        double plusminus=0;

        for (int x=0;x<list_keranjang_select.size();x++) {
            Keranjang k = list_keranjang_select.get(x);
            ArrayList<ItemKeranjang> lik = k.getItemKeranjang();
            for (int y = 0; y < lik.size(); y++) {
                ItemKeranjang ik = lik.get(y);
                if (ik.getId().equals(item.getId())) {
                    if(kurang){
                        plusminus -= ik.getHarga_satuan();
                    }
                    if(tambah){
                        plusminus += ik.getHarga_satuan();
                    }
                    k.setTotal_harga(k.getTotal_harga()+plusminus);
                }
            }
        }
        total += plusminus;
        txt_total_bayar.setText(Util.rupiahFormat(total));
    }

    @SuppressLint("LongLogTag")
    @Override
    public void ChangeItem(ArrayList<ItemKeranjang> list_item, ItemKeranjang old_item) {
        if(list_item.size()>0){
            ArrayList<Keranjang> list_keranjang = cardAdapter.getListItem();
            for (int i=0;i<list_keranjang.size();i++){
                ArrayList<ItemKeranjang> list_item_keranjang_select = new ArrayList<>();
                double total_harga_item = 0f;

                Keranjang keranjang = list_keranjang.get(i);
                ArrayList<ItemKeranjang> list_item_keranjang = keranjang.getItemKeranjang();

                for (int j=0;j<list_item_keranjang.size();j++){
                    ItemKeranjang item = list_item_keranjang.get(j);
                    if(item.getSelected()){
                        //before
                        for (int x=0;x<list_keranjang_select.size();x++) {
                            Keranjang k = list_keranjang_select.get(x);
                            ArrayList<ItemKeranjang> lik = k.getItemKeranjang();
                            for (int y = 0; y < list_item_keranjang.size(); y++) {
                                if (lik.contains(item)) {
                                    total -= item.getHarga_satuan() * item.getJumlah_beli();
                                    lik.remove(item);
                                }
                            }
                            if(lik.size()==0)
                                list_keranjang_select.remove(k);
                        }

                        //after
                        Log.i("app-log set item keranjang",String.format("%s %s x %s",item.getNama_produk(),item.getHarga_satuan(),item.getJumlah_beli()));
                        list_item_keranjang_select.add(item);

                        double tmp = item.getHarga_satuan() * item.getJumlah_beli();
                        total_harga_item+=tmp;
                    }
                }

                total+=total_harga_item;
                if(list_item_keranjang_select.size()>0) {
                    Log.i("app-log set list keranjang", keranjang.getNama_toko());
                    list_keranjang_select.add(new Keranjang(keranjang.getId(), keranjang.getNama_toko(), list_item_keranjang_select, total_harga_item,""));
                }
            }

            //uncheck
            if(old_item != null) {
                for (int i=0;i<list_keranjang_select.size();i++){
                    Keranjang keranjang = list_keranjang_select.get(i);
                    ArrayList<ItemKeranjang> list_item_keranjang = keranjang.getItemKeranjang();

                    for (int j=0;j<list_item_keranjang.size();j++){
                        if(list_item_keranjang.contains(old_item)){
                            total -= old_item.getHarga_satuan() * old_item.getJumlah_beli();
                            list_item_keranjang.remove(old_item);
                        }
                    }
                    if(list_item_keranjang.size()==0){
                        list_keranjang_select.remove(keranjang);
                    }
                }
            }

            txt_total_bayar.setText(Util.rupiahFormat(total));
            btn_bayar.setEnabled(true);
            btn_bayar.setAlpha(1.0f);

            Log.i("app-log size change",list_item.size()+"");
        }
        else{
            txt_total_bayar.setText(Util.rupiahFormat(0));
            btn_bayar.setEnabled(false);
            btn_bayar.setAlpha(0.5f);
        }
    }

    @Override
    public void SelectItemKeranjang(ItemKeranjang item) {
        Product produk = new Product(
                item.getId(),
                item.getNama_produk(),
                item.getFoto_produk(),
                null,
                0,
                null,
                null,
                null,
                0
        );

        Intent i = new Intent(this, DetailProdukActivity.class);
        i.putExtra("action","KeranjangActivity");
        i.putExtra("produk",produk);
        startActivity(i);
    }

    @Override
    public void PlusItemKeranjang(int position_keranjang, TextView txt_jumlah_item, ItemKeranjang item) {
        if(!isDefault) {
            req_plus_minus_qty(true, position_keranjang, txt_jumlah_item, item);
        }
        else{
            ArrayList<ItemKeranjang> list_item = cardAdapter.getListItem().get(position_keranjang).getItemKeranjang();
            for (int i=0;i<list_item.size();i++) {
                ItemKeranjang obj = list_item.get(i);
                if (obj.getId().equals(item.getId())) {
                    obj.setJumlah_beli(item.getJumlah_beli() + 1);

                    txt_jumlah_item.setText(String.valueOf(item.getJumlah_beli()));
                    HitungUlang(item, false, true);
                }
            }
        }
    }

    @Override
    public void MinusItemKeranjang(int position_keranjang, TextView txt_jumlah_item, ItemKeranjang item) {
        if(!isDefault) {
            req_plus_minus_qty(false, position_keranjang, txt_jumlah_item, item);
        }
        else{
            ArrayList<ItemKeranjang> list_item = cardAdapter.getListItem().get(position_keranjang).getItemKeranjang();
            for (int i=0;i<list_item.size();i++) {
                ItemKeranjang obj = list_item.get(i);
                if (obj.getId().equals(item.getId())) {
                    obj.setJumlah_beli(item.getJumlah_beli() - 1);

                    txt_jumlah_item.setText(String.valueOf(item.getJumlah_beli()));
                    HitungUlang(item, true, false);
                }
            }
        }
    }

    //catatan: blm ada request
    @Override
    public void RemoveItemKeranjang(int position_keranjang,int position_item_keranjang) {
        ArrayList<Keranjang> list = cardAdapter.getListItem();
        for (int i=0;i<list.size();i++){
            ArrayList<ItemKeranjang> list_item = list.get(position_keranjang).getItemKeranjang();
            for (int j=0;j<list_item.size();j++){
                if(j==position_item_keranjang){
                    list_item.remove(position_item_keranjang);
                }
            }
            if(list_item.size()==0){
                list.remove(position_keranjang);
            }
        }
        cardAdapter.setListItem(list);
        cardAdapter.notifyDataSetChanged();
    }
}