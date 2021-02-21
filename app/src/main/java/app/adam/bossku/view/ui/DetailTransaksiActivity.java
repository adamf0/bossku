package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import app.adam.bossku.helper.StatusEcommerce;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.adapter.DetailTransaksiAdapter;
import app.adam.bossku.view.model.PesanBarang;
import app.adam.bossku.view.model.Transaksi;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

import static app.adam.bossku.helper.StatusEcommerce.DIPROSES_PENJUAL;
import static app.adam.bossku.helper.StatusEcommerce.DITOLAK_PENJUAL;
import static app.adam.bossku.helper.StatusEcommerce.MENUNGGU_PEMBAYARAN;
import static app.adam.bossku.helper.StatusEcommerce.MENUNGGU_RESPON_PENJUAL;
import static app.adam.bossku.helper.StatusEcommerce.PAYMENT_VERIFIED;
import static app.adam.bossku.helper.StatusEcommerce.PESANAN_TIBA;
import static app.adam.bossku.helper.StatusEcommerce.SALDO_DITERIMA;
import static app.adam.bossku.helper.StatusEcommerce.SEDANG_PENGIRIMAN;
import static app.adam.bossku.helper.StatusEcommerce.TIME_OUT_PAYMENT;
import static app.adam.bossku.helper.StatusEcommerce.TRANSAKSI_SELESAI;
import static app.adam.bossku.helper.StatusEcommerce.USER_CANCELLED;
import static app.adam.bossku.helper.StatusEcommerce.VERIFY_PAYMENT;

public class DetailTransaksiActivity extends AppCompatActivity {
    RecyclerView rv_list;
    TextView txt_nomor_transaksi,txt_tanggal_pesan,txt_status,txt_nama_pembeli,txt_alamat_pembeli,txt_nama_penjual,txt_alamat_penjual,txt_metode_pembayaran,txt_status_metode_pembayaran,txt_total_ongkir,txt_total_bayar;
    boolean isDefault=true;
    Transaksi transaksi=null;
    SweetAlertDialog dialog_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);

        txt_nomor_transaksi = findViewById(R.id.txt_nomor_transaksi_detail_transaksi);
        txt_tanggal_pesan = findViewById(R.id.txt_tanggal_pesan_detail_transaksi);
        txt_status = findViewById(R.id.txt_status_detail_transaksi);
        txt_nama_pembeli = findViewById(R.id.txt_nama_pembeli_detail_transaksi);
        txt_alamat_pembeli = findViewById(R.id.txt_alamat_pembeli_detail_transaksi);
        txt_nama_penjual = findViewById(R.id.txt_nama_penjual_detail_transaksi);
        txt_alamat_penjual = findViewById(R.id.txt_alamat_penjual_detail_transaksi);
        txt_total_ongkir = findViewById(R.id.txt_total_ongkir_detail_transaksi);
        txt_total_bayar = findViewById(R.id.txt_total_bayar_detail_transaksi);

        txt_metode_pembayaran = findViewById(R.id.txt_metode_pembayaran_detail_transaksi);
        txt_status_metode_pembayaran = findViewById(R.id.txt_status_metode_pembayaran_detail_transaksi);

        rv_list = findViewById(R.id.rv_list_pesan_detail_transaksi);
        Toolbar mtoolbar = findViewById(R.id.toolbar_detail_transaksi);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> DetailTransaksiActivity.super.onBackPressed());
        rv_list.setHasFixedSize(true);

        Bundle b = getIntent().getExtras();
        if(b!=null){
            isDefault = b.getBoolean("isDefault",true);
            transaksi = b.getParcelable("transaksi");
            load_data();
        }
    }

    void load_data_user(){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(DetailTransaksiActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_userdata("user/userdata", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [detail transaksi]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");
                            txt_nama_pembeli.setText(data.getString("name"));
                        }
                        else {
                            Toast.makeText(DetailTransaksiActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(DetailTransaksiActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [detail transaksi]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(DetailTransaksiActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(DetailTransaksiActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                JSONObject data = res.getJSONObject("data");
//                                txt_nama_pembeli.setText(data.getString("name"));
//                            }
//                            else {
//                                Toast.makeText(DetailTransaksiActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(DetailTransaksiActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(DetailTransaksiActivity.this);
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
    void load_data(){
        final ArrayList<PesanBarang> list_pesan_barang = new ArrayList<>();
        if(isDefault){
            list_pesan_barang.add(new PesanBarang("1","Produk 1",5,1000));
            list_pesan_barang.add(new PesanBarang("2","Produk 2",1,5000));

            rv_list.setLayoutManager(new LinearLayoutManager(this));
            DetailTransaksiAdapter cardAdapter = new DetailTransaksiAdapter(this,true);
            cardAdapter.setListItem(list_pesan_barang);
            cardAdapter.notifyDataSetChanged();
            rv_list.setAdapter(cardAdapter);
        }
        else{
            dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            dialog_loading.setTitleText("Loading");
            dialog_loading.setCancelable(false);
            dialog_loading.show();

            SessionManager session = new SessionManager(DetailTransaksiActivity.this);
            HashMap<String, String> data = session.getSessionLogin();

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Map<String, String>  headers = new HashMap<>();
            headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

            Call<String> call = getResponse.request_user_transaction_detail(String.format("user/transaction/detail/%s",transaksi.getNomor_transaksi()), headers);
            Log.i("app-log", "request to "+call.request().url().toString());
            call.enqueue(new Callback<String>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String res_ = response.body();
                    Log.i("app-log [detail transaksi]", res_);
                    dialog_loading.dismissWithAnimation();

                    if(response.isSuccessful()) {
                        try {
                            assert res_ != null;
                            JSONObject res = new JSONObject(res_);
                            String status = res.getString("status");
                            String message = res.getString("message");

                            if(status.equals("success")) {
                                double bayar = 0;
                                double ongkir = 0;
                                JSONObject dataObj = res.getJSONObject("data");
                                txt_nomor_transaksi.setText(dataObj.getString("transaction_code"));
                                txt_tanggal_pesan.setText(
                                        String.format(
                                                "%s %s\n %s %s",
                                                Util.convert_date(dataObj.getString("payment_date"),"yyyy-MM-dd hh:mm:ss","EEE, dd MMM yyyy"),
                                                Util.convert_date(dataObj.getString("payment_date"),"yyyy-MM-dd hh:mm:ss","hh:mm"),
                                                Util.convert_date(dataObj.getString("payment_due_date"),"yyyy-MM-dd hh:mm:ss","EEE, dd MMM yyyy"),
                                                Util.convert_date(dataObj.getString("payment_due_date"),"yyyy-MM-dd hh:mm:ss","hh:mm")+" (Expire)"
                                        )
                                );
                                txt_status.setText(new StatusEcommerce().getStatus(dataObj.getInt("transaction_status")));

                                if(!dataObj.isNull("shipping_address")) {
                                    JSONObject addressObj = dataObj.getJSONObject("shipping_address");
                                    load_data_user();
                                    txt_alamat_pembeli.setText(
                                            String.format("%s, %s, %s\n%s, %s",
                                                    addressObj.getString("address"),
                                                    addressObj.getJSONObject("subdistrict").getString("subdistrict_name"),
                                                    addressObj.getJSONObject("city").getString("type") + " " + addressObj.getJSONObject("city").getString("city_name"),
                                                    addressObj.getJSONObject("province").getString("province"),
                                                    addressObj.getString("postal_code")
                                            )
                                    );
                                }
                                else{
                                    txt_alamat_pembeli.setText("N/a");
                                }
                                //txt_nama_penjual.setText();
                                //txt_alamat_penjual.setText();

                                JSONArray cart = dataObj.getJSONArray("cart");
                                for (int i=0;i<cart.length();i++){
                                    JSONObject cartObj = cart.getJSONObject(i);
                                    JSONObject goods = cartObj.getJSONObject("goods");

                                    list_pesan_barang.add(
                                            new PesanBarang(
                                                    goods.getString("id"),
                                                    goods.getString("name"),
                                                    cartObj.getInt("quantity"),
                                                    goods.getDouble("price"),
                                                    cartObj.getString("user_message"),
                                                    (cartObj.getString("refund_status").equals("") || cartObj.getString("refund_status").equals("null")? null:cartObj.getString("refund_status")),
                                                    goods.getJSONObject("umkm_data").getString("brand_name"),
                                                    goods.getInt("weight"),
                                                    cartObj.getDouble("shipment_cost")
                                            )
                                    );
                                    ongkir+=cartObj.getDouble("shipment_cost");
                                    bayar += (cartObj.getInt("quantity")*goods.getDouble("price"))+ongkir;
                                }
                                txt_total_ongkir.setText(Util.rupiahFormat(ongkir));
                                txt_total_bayar.setText(Util.rupiahFormat(bayar));
                                if(dataObj.getInt("payment_channel")==1) {
                                    txt_metode_pembayaran.setText(dataObj.getJSONObject("bank").getString("bank_name"));
                                    txt_status_metode_pembayaran.setText(new StatusEcommerce().getStatusBank(dataObj.getInt("transaction_status")));
                                }

                                stepBar(dataObj.getInt("transaction_status"));
                                rv_list.setLayoutManager(new LinearLayoutManager(DetailTransaksiActivity.this));
                                DetailTransaksiAdapter cardAdapter = new DetailTransaksiAdapter(DetailTransaksiActivity.this,false);
                                cardAdapter.setListItem(list_pesan_barang);
                                cardAdapter.notifyDataSetChanged();
                                rv_list.setAdapter(cardAdapter);
                            }
                            else {
                                Toast.makeText(DetailTransaksiActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(DetailTransaksiActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                    }
                }
                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("app-log [detail transaksi]", t.toString());
                    dialog_loading.dismissWithAnimation();

                    if(call.isCanceled()) {
                        Toast.makeText(DetailTransaksiActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(DetailTransaksiActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                    }
                }
            });

//            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.i("app-log", response);
//                            dialog_loading.dismissWithAnimation();
//
//                            try {
//                                JSONObject res = new JSONObject(response);
//                                String status = res.getString("status");
//                                String message = res.getString("message");
//
//                                if(status.equals("success")) {
//                                    double bayar = 0;
//                                    double ongkir = 0;
//                                    JSONObject dataObj = res.getJSONObject("data");
//                                    txt_nomor_transaksi.setText(dataObj.getString("transaction_code"));
//                                    txt_tanggal_pesan.setText(
//                                            String.format(
//                                                    "%s %s\n %s %s",
//                                                    Util.convert_date(dataObj.getString("payment_date"),"yyyy-MM-dd hh:mm:ss","EEE, dd MMM yyyy"),
//                                                    Util.convert_date(dataObj.getString("payment_date"),"yyyy-MM-dd hh:mm:ss","hh:mm"),
//                                                    Util.convert_date(dataObj.getString("payment_due_date"),"yyyy-MM-dd hh:mm:ss","EEE, dd MMM yyyy"),
//                                                    Util.convert_date(dataObj.getString("payment_due_date"),"yyyy-MM-dd hh:mm:ss","hh:mm")+" (Expire)"
//                                            )
//                                    );
//                                    txt_status.setText(new StatusEcommerce().getStatus(dataObj.getInt("transaction_status")));
//
//                                    if(!dataObj.isNull("shipping_address")) {
//                                        JSONObject addressObj = dataObj.getJSONObject("shipping_address");
//                                        load_data_user();
//                                        txt_alamat_pembeli.setText(
//                                                String.format("%s, %s, %s\n%s, %s",
//                                                        addressObj.getString("address"),
//                                                        addressObj.getJSONObject("subdistrict").getString("subdistrict_name"),
//                                                        addressObj.getJSONObject("city").getString("type") + " " + addressObj.getJSONObject("city").getString("city_name"),
//                                                        addressObj.getJSONObject("province").getString("province"),
//                                                        addressObj.getString("postal_code")
//                                                )
//                                        );
//                                    }
//                                    else{
//                                        txt_alamat_pembeli.setText("N/a");
//                                    }
//                                    //txt_nama_penjual.setText();
//                                    //txt_alamat_penjual.setText();
//
//                                    JSONArray cart = dataObj.getJSONArray("cart");
//                                    for (int i=0;i<cart.length();i++){
//                                        JSONObject cartObj = cart.getJSONObject(i);
//                                        JSONObject goods = cartObj.getJSONObject("goods");
//
//                                        list_pesan_barang.add(
//                                                new PesanBarang(
//                                                        goods.getString("id"),
//                                                        goods.getString("name"),
//                                                        cartObj.getInt("quantity"),
//                                                        goods.getDouble("price"),
//                                                        cartObj.getString("user_message"),
//                                                        (cartObj.getString("refund_status").equals("") || cartObj.getString("refund_status").equals("null")? null:cartObj.getString("refund_status")),
//                                                        goods.getJSONObject("umkm_data").getString("brand_name"),
//                                                        goods.getInt("weight"),
//                                                        cartObj.getDouble("shipment_cost")
//                                                )
//                                        );
//                                        ongkir+=cartObj.getDouble("shipment_cost");
//                                        bayar += (cartObj.getInt("quantity")*goods.getDouble("price"))+ongkir;
//                                    }
//                                    txt_total_ongkir.setText(Util.rupiahFormat(ongkir));
//                                    txt_total_bayar.setText(Util.rupiahFormat(bayar));
//                                    if(dataObj.getInt("payment_channel")==1) {
//                                        txt_metode_pembayaran.setText(dataObj.getJSONObject("bank").getString("bank_name"));
//                                        txt_status_metode_pembayaran.setText(new StatusEcommerce().getStatusBank(dataObj.getInt("transaction_status")));
//                                    }
//
//                                    stepBar(dataObj.getInt("transaction_status"));
//                                    rv_list.setLayoutManager(new LinearLayoutManager(DetailTransaksiActivity.this));
//                                    DetailTransaksiAdapter cardAdapter = new DetailTransaksiAdapter(DetailTransaksiActivity.this,false);
//                                    cardAdapter.setListItem(list_pesan_barang);
//                                    cardAdapter.notifyDataSetChanged();
//                                    rv_list.setAdapter(cardAdapter);
//                                }
//                                else {
//                                    Toast.makeText(DetailTransaksiActivity.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(DetailTransaksiActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                            dialog_loading.dismissWithAnimation();
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
    @SuppressLint("UseCompatLoadingForDrawables")
    void stepBar(int status){
        ImageView img_step1 = findViewById(R.id.img_step1_status_bar);
        TextView txt_step1 = findViewById(R.id.txt_step1_status_bar);
        LinearLayout ln_step1 = findViewById(R.id.ln_step12_status_bar);

        ImageView img_step2 = findViewById(R.id.img_step2_status_bar);
        TextView txt_step2 = findViewById(R.id.txt_step2_status_bar);
        LinearLayout ln_step2 = findViewById(R.id.ln_step23_status_bar);

        ImageView img_step3 = findViewById(R.id.img_step3_status_bar);
        TextView txt_step3 = findViewById(R.id.txt_step3_status_bar);
        LinearLayout ln_step3 = findViewById(R.id.ln_step34_status_bar);

        ImageView img_step4 = findViewById(R.id.img_step4_status_bar);
        TextView txt_step4 = findViewById(R.id.txt_step4_status_bar);
        LinearLayout ln_step4 = findViewById(R.id.ln_step45_status_bar);

        ImageView img_step5 = findViewById(R.id.img_step5_status_bar);
        TextView txt_step5 = findViewById(R.id.txt_step5_status_bar);
        LinearLayout ln_step5 = findViewById(R.id.ln_step56_status_bar);

        ImageView img_step6 = findViewById(R.id.img_step6_status_bar);
        TextView txt_step6 = findViewById(R.id.txt_step6_status_bar);
        LinearLayout ln_step6 = findViewById(R.id.ln_step67_status_bar);

        ImageView img_step7 = findViewById(R.id.img_step7_status_bar);
        TextView txt_step7 = findViewById(R.id.txt_step7_status_bar);
        LinearLayout ln_step7 = findViewById(R.id.ln_step78_status_bar);

        ImageView img_step8 = findViewById(R.id.img_step8_status_bar);
        TextView txt_step8 = findViewById(R.id.txt_step8_status_bar);

//        if(status==BELUM_CHECKOUT){
//            return "Belum Bayar";
//        }
        if(status==MENUNGGU_PEMBAYARAN || status==USER_CANCELLED || status==TIME_OUT_PAYMENT || status==VERIFY_PAYMENT || status==DITOLAK_PENJUAL || status==SALDO_DITERIMA){
            img_step1.setImageResource(R.drawable.ic_pesan);
            txt_step1.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step1.setBackground(getResources().getDrawable(R.drawable.line_active));
        }
        else if(status==PAYMENT_VERIFIED){
            img_step1.setImageResource(R.drawable.ic_pesan);
            txt_step1.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step1.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step2.setImageResource(R.drawable.ic_bayar);
            txt_step2.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step2.setBackground(getResources().getDrawable(R.drawable.line_active));
        }
        else if(status==MENUNGGU_RESPON_PENJUAL || status==DIPROSES_PENJUAL){
            img_step1.setImageResource(R.drawable.ic_pesan);
            txt_step1.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step1.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step2.setImageResource(R.drawable.ic_bayar);
            txt_step2.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step2.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step3.setImageResource(R.drawable.ic_barang_disiapkan);
            txt_step3.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step3.setBackground(getResources().getDrawable(R.drawable.line_active));
        }
        else if(status==SEDANG_PENGIRIMAN){
            img_step1.setImageResource(R.drawable.ic_pesan);
            txt_step1.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step1.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step2.setImageResource(R.drawable.ic_bayar);
            txt_step2.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step2.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step3.setImageResource(R.drawable.ic_barang_disiapkan);
            txt_step3.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step3.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step4.setImageResource(R.drawable.ic_barang_disiapkan);
            txt_step4.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step4.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step5.setImageResource(R.drawable.ic_ambil_kurir);
            txt_step5.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step5.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step6.setImageResource(R.drawable.ic_menuju_tujuan);
            txt_step6.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step6.setBackground(getResources().getDrawable(R.drawable.line_active));
        }
        else if(status==PESANAN_TIBA){
            img_step1.setImageResource(R.drawable.ic_pesan);
            txt_step1.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step1.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step2.setImageResource(R.drawable.ic_bayar);
            txt_step2.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step2.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step3.setImageResource(R.drawable.ic_barang_disiapkan);
            txt_step3.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step3.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step4.setImageResource(R.drawable.ic_barang_disiapkan);
            txt_step4.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step4.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step5.setImageResource(R.drawable.ic_ambil_kurir);
            txt_step5.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step5.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step6.setImageResource(R.drawable.ic_menuju_tujuan);
            txt_step6.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step6.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step7.setImageResource(R.drawable.ic_barang_terima);
            txt_step7.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step7.setBackground(getResources().getDrawable(R.drawable.line_active));
        }
        else if(status==TRANSAKSI_SELESAI){
            img_step1.setImageResource(R.drawable.ic_pesan);
            txt_step1.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step1.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step2.setImageResource(R.drawable.ic_bayar);
            txt_step2.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step2.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step3.setImageResource(R.drawable.ic_barang_disiapkan);
            txt_step3.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step3.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step4.setImageResource(R.drawable.ic_barang_disiapkan);
            txt_step4.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step4.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step5.setImageResource(R.drawable.ic_ambil_kurir);
            txt_step5.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step5.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step6.setImageResource(R.drawable.ic_menuju_tujuan);
            txt_step6.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step6.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step7.setImageResource(R.drawable.ic_barang_terima);
            txt_step7.setTextColor(getResources().getColor(R.color.text_bar_active));
            ln_step7.setBackground(getResources().getDrawable(R.drawable.line_active));

            img_step8.setImageResource(R.drawable.ic_selesai);
            txt_step8.setTextColor(getResources().getColor(R.color.text_bar_active));
        }
    }

}