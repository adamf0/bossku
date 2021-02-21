package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.adapter.DaftarBelanjaAdapter;
import app.adam.bossku.view.adapter.ItemDaftarBelanjaAdapter;
import app.adam.bossku.view.model.Alamat;
import app.adam.bossku.view.model.Bank;
import app.adam.bossku.view.model.ItemKeranjang;
import app.adam.bossku.view.model.Kecamatan;
import app.adam.bossku.view.model.Keranjang;
import app.adam.bossku.view.model.Kota;
import app.adam.bossku.view.model.Kurir;
import app.adam.bossku.view.model.Provinsi;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class PembayaranBarangActivity extends AppCompatActivity implements ItemDaftarBelanjaAdapter.ItemDaftarBelanjaListener, DaftarBelanjaAdapter.DaftarBelanjaListener {
    RecyclerView rv_list;
    TextView txt_nama_lengkap,txt_telp,txt_alamat,txt_metode_pembayaran,txt_total_harga,txt_total_ongkir,txt_total_bayar;
    ImageView img_nama_lengkap,img_telp,img_alamat,img_metode_pembayaran;
    ImageButton btn_alamat,btn_metode_pembayaran;
    Button btn_bayar;
    DaftarBelanjaAdapter cardAdapter;

    Bank bank=null;
    Alamat alamat=null;
    ArrayList<Keranjang> list_keranjang = null;
    double total;
    boolean isDefault=false;
    SweetAlertDialog dialog_loading;
    double total_ongkir=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_barang);

        txt_nama_lengkap = findViewById(R.id.txt_nama_lengkap_pembayaran_barang);
        img_nama_lengkap = findViewById(R.id.img_nama_lengkap_pembayaran_barang);

        txt_telp = findViewById(R.id.txt_telp_pembayaran_barang);
        img_telp = findViewById(R.id.img_telp_pembayaran_barang);

        txt_alamat = findViewById(R.id.txt_alamat_pembayaran_barang);
        img_alamat = findViewById(R.id.img_alamat_pembayaran_barang);
        btn_alamat = findViewById(R.id.btn_alamat_pembayaran_barang);

        txt_metode_pembayaran = findViewById(R.id.txt_metode_pembayaran_pembayaran_barang);
        img_metode_pembayaran = findViewById(R.id.img_metode_pembayaran_pembayaran_barang);
        btn_metode_pembayaran = findViewById(R.id.btn_metode_pembayaran_pembayaran_barang);

        rv_list = findViewById(R.id.rv_list_belanja_pembayaran_barang);
        txt_total_harga = findViewById(R.id.txt_total_harga_pembayaran_barang);
        txt_total_ongkir = findViewById(R.id.txt_total_ongkir_pembayaran_barang);
        txt_total_bayar = findViewById(R.id.txt_total_bayar_pembayaran_barang);
        btn_bayar = findViewById(R.id.btn_bayar_pembayaran_barang);
        Toolbar mtoolbar = findViewById(R.id.toolbar_pembayaran_barang);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> PembayaranBarangActivity.super.onBackPressed());

        Bundle b = getIntent().getExtras();
        if(b != null){
            bank   = b.getParcelable("bank");
            alamat   = b.getParcelable("alamat");
            list_keranjang = b.getParcelableArrayList("list_keranjang");

            load_data();

            if(list_keranjang !=null && list_keranjang.size()>0) {
                normalisasiKurir(list_keranjang);
                rv_list.setLayoutManager(new LinearLayoutManager(this));
                cardAdapter = new DaftarBelanjaAdapter(this, this,this);
                cardAdapter.setListItem(list_keranjang);
                cardAdapter.notifyDataSetChanged();
                rv_list.setAdapter(cardAdapter);

                HitungTotalHargaBarang(list_keranjang, false);
//                for (int i=0;i<list_keranjang.size();i++) {
//                    Keranjang keranjang = list_keranjang.get(i);
//                    ArrayList<ItemKeranjang> list_item_keranjang = keranjang.getItemKeranjang();
//                    for (int j = 0; j < list_item_keranjang.size(); j++) {
//                        ItemKeranjang item = list_item_keranjang.get(j);
//                        Log.i("app-log data", String.format("%s - %s %s x %s", keranjang.getNama_toko(),item.getNama_produk(), item.getHarga_satuan(), item.getJumlah_beli()));
//                    }
//                }
                HitungOngkir(txt_total_ongkir,true,-1,-1);
                if (alamat != null && bank != null) {
                    if (cekKurir(list_keranjang)) {
                        btn_bayar.setAlpha(1.0f);
                        btn_bayar.setEnabled(true);
                    }
                    else {
                        btn_bayar.setAlpha(0.5f);
                        btn_bayar.setEnabled(false);
                    }
                }
                if (alamat != null) {
                    img_alamat.setImageResource(R.drawable.ic_check);
                    txt_alamat.setText(
                            String.format("%s, %s, %s\n%s, %s",
                                    alamat.getAddress(),
                                    alamat.getKecamatan().getName(),
                                    alamat.getKota().getFullName(),
                                    alamat.getProvinsi().getName(),
                                    alamat.getKecamatan().getPostal_kode()
                            )
                    );
                }
                if (bank != null) {
                    img_metode_pembayaran.setImageResource(R.drawable.ic_check);
                    txt_metode_pembayaran.setText(bank.getBank_name());
                }

                btn_alamat.setOnClickListener(v -> {
                    Intent i = new Intent(PembayaranBarangActivity.this, PilihAlamatActivity.class);
                    i.putExtra("activity", "PembayaranBarangActivity");
                    i.putExtra("bank", bank);
                    i.putExtra("alamat", alamat);
                    i.putExtra("list_keranjang", list_keranjang);
                    startActivity(i);
                });
                btn_metode_pembayaran.setOnClickListener(v -> {
                    Intent i = new Intent(PembayaranBarangActivity.this, PilihBankActivity.class);
                    i.putExtra("bank", bank);
                    i.putExtra("alamat", alamat);
                    i.putExtra("list_keranjang", list_keranjang);
                    i.putExtra("activity", "PembayaranBarangActivity");
                    startActivity(i);
                });
                btn_bayar.setOnClickListener(v -> {
                    if(list_keranjang.size()>0) {
                        request_precheckout();
                    }
                    else{
                        Toast.makeText(PembayaranBarangActivity.this, "Kesalahan Sistem", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else{
            txt_nama_lengkap.setText("N/a");
            txt_telp.setText("N/a");
            txt_alamat.setText("N/a");
            txt_metode_pembayaran.setText("N/a");
            txt_total_harga.setText("N/a");
            txt_total_ongkir.setText("N/a");
            txt_total_bayar.setText("N/a");

            btn_bayar.setAlpha(0.5f);
            btn_bayar.setEnabled(false);
        }
    }

    public void HitungTotalHargaBarang(ArrayList<Keranjang> list_keranjang,boolean isUpdate){
        total = 0f;
        for (int i=0;i<list_keranjang.size();i++){
            Keranjang keranjang = list_keranjang.get(i);
            if(!isUpdate) {
                total += keranjang.getTotal_harga();
            }
            else{
                ArrayList<ItemKeranjang> itemKeranjang = keranjang.getItemKeranjang();
                for (int j=0;j<itemKeranjang.size();j++){
                    ItemKeranjang x = itemKeranjang.get(j);
                    total+=x.getHarga_satuan()*x.getJumlah_beli();
                }
            }
        }
        txt_total_harga.setText(Util.rupiahFormat(total));
        txt_total_bayar.setText(Util.rupiahFormat(total));
    }

    public void normalisasiKurir(ArrayList<Keranjang> list_keranjang){
        if(list_keranjang !=null && list_keranjang.size()>0){
            for (int i=0;i<list_keranjang.size();i++){
                ArrayList<ItemKeranjang> list_item_keranjang = list_keranjang.get(i).getItemKeranjang();
                if(list_item_keranjang != null && list_item_keranjang.size()>0){
                    for (int j=0;j<list_item_keranjang.size();j++){
                        ItemKeranjang item = list_item_keranjang.get(j);
                        if(j>0 && item.getKurir() == null && list_item_keranjang.get(0).getKurir()!=null){
                            list_item_keranjang.get(j).setKurir(list_item_keranjang.get(0).getKurir());
                            Log.i("app-log normal",list_item_keranjang.get(j).getKurir().getService());
                        }
                    }
                }
            }
        }
    }

    public boolean cekKurir(ArrayList<Keranjang> list_keranjang){
        int real = 0;
        int select = 0;

        if(list_keranjang !=null && list_keranjang.size()>0){
            for (int i=0;i<list_keranjang.size();i++){
                ArrayList<ItemKeranjang> list_item_keranjang = list_keranjang.get(i).getItemKeranjang();
                Kurir tmp_kurir = null;
                if(list_item_keranjang != null && list_item_keranjang.size()>0){
                    for (int j=0;j<list_item_keranjang.size();j++){
                        ItemKeranjang item = list_item_keranjang.get(j);
                        if(j==0){
                            tmp_kurir = item.getKurir();
                        }

                        if(tmp_kurir !=null)
                            select++;
                        else if(item.getKurir() != null){
                            select++;
                        }
                        real++;
                    }
                }
            }

            return real > 0 && real == select;
        }
        else{
            return false;
        }
    }

    void load_alamat() {
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(PembayaranBarangActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_shipping_address_list("user/shipping_address/list", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pembayaran barang]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if (status.equals("success")) {
                            JSONArray datas = res.getJSONArray("data");

                            if(datas.length()>0) {
                                for (int i = 0; i < datas.length(); i++) {
                                    JSONObject obj = datas.getJSONObject(i);
                                    JSONObject prov = obj.getJSONObject("province");
                                    JSONObject kota = obj.getJSONObject("city");
                                    JSONObject kec = obj.getJSONObject("subdistrict");

                                    if (obj.getInt("is_default_address") == 1) {
                                        alamat = new Alamat(
                                                obj.getString("id"),
                                                obj.getString("name"),
                                                obj.getString("address"),
                                                new Provinsi(prov.getString("province_id"), prov.getString("province")),
                                                new Kota(kota.getString("city_id"), kota.getString("city_name"), kota.getString("type")),
                                                new Kecamatan(kec.getString("subdistrict_id"), kec.getString("subdistrict_name"), null),
                                                obj.getInt("is_default_address")
                                        );
                                        load_postal(dialog_loading);
                                    }
                                    else{
                                        dialog_loading.dismissWithAnimation();
                                        Toast.makeText(PembayaranBarangActivity.this, "Belum ada alamat utama", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else{
                                dialog_loading.dismissWithAnimation();
                                Toast.makeText(PembayaranBarangActivity.this, "Tidak ada data alamat", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            dialog_loading.dismissWithAnimation();
                            Toast.makeText(PembayaranBarangActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    dialog_loading.dismissWithAnimation();
                    Toast.makeText(PembayaranBarangActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pembayaran barang]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(PembayaranBarangActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PembayaranBarangActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                if(datas.length()>0) {
//                                    for (int i = 0; i < datas.length(); i++) {
//                                        JSONObject obj = datas.getJSONObject(i);
//                                        JSONObject prov = obj.getJSONObject("province");
//                                        JSONObject kota = obj.getJSONObject("city");
//                                        JSONObject kec = obj.getJSONObject("subdistrict");
//
//                                        if (obj.getInt("is_default_address") == 1) {
//                                            alamat = new Alamat(
//                                                    obj.getString("id"),
//                                                    obj.getString("name"),
//                                                    obj.getString("address"),
//                                                    new Provinsi(prov.getString("province_id"), prov.getString("province")),
//                                                    new Kota(kota.getString("city_id"), kota.getString("city_name"), kota.getString("type")),
//                                                    new Kecamatan(kec.getString("subdistrict_id"), kec.getString("subdistrict_name"), null),
//                                                    obj.getInt("is_default_address")
//                                            );
//                                            load_postal(dialog_loading);
//                                        }
//                                        else{
//                                            dialog_loading.dismissWithAnimation();
//                                            Toast.makeText(PembayaranBarangActivity.this, "Belum ada alamat utama", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//                                else{
//                                    dialog_loading.dismissWithAnimation();
//                                    Toast.makeText(PembayaranBarangActivity.this, "Tidak ada data alamat", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            else {
//                                dialog_loading.dismissWithAnimation();
//                                Toast.makeText(PembayaranBarangActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(PembayaranBarangActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PembayaranBarangActivity.this);
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

    void load_postal(final SweetAlertDialog dialog_loading){
        SessionManager session = new SessionManager(PembayaranBarangActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_rajaongkir_get_postalcode(String.format("rajaongkir/get-postalcode/%s",alamat.getKecamatan().getId()), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pembayaran barang]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        txt_alamat.setText(alamat.getAddress());
                        img_alamat.setImageResource(R.drawable.ic_check);
                        if(status.equals("success")){
                            alamat.getKecamatan().setPostal_kode(res.getString("data"));
                        }
                        else{
                            Toast.makeText(PembayaranBarangActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PembayaranBarangActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pembayaran barang]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(PembayaranBarangActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PembayaranBarangActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                            txt_alamat.setText(alamat.getAddress());
//                            img_alamat.setImageResource(R.drawable.ic_check);
//                            if(status.equals("success")){
//                                alamat.getKecamatan().setPostal_kode(res.getString("data"));
//                            }
//                            else{
//                                Toast.makeText(PembayaranBarangActivity.this,message, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(PembayaranBarangActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PembayaranBarangActivity.this);
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

    void load_data(){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(PembayaranBarangActivity.this);
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
                Log.i("app-log [pembayaran barang]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");

                            txt_nama_lengkap.setText(data.getString("name"));
                            img_nama_lengkap.setImageResource(R.drawable.ic_check);
                            txt_telp.setText(data.getString("phone"));
                            img_telp.setImageResource(R.drawable.ic_check);
                        }
                        else {
                            Toast.makeText(PembayaranBarangActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        if(alamat == null){
                            load_alamat();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PembayaranBarangActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pembayaran barang]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(PembayaranBarangActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PembayaranBarangActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//
//                                txt_nama_lengkap.setText(data.getString("name"));
//                                img_nama_lengkap.setImageResource(R.drawable.ic_check);
//                                txt_telp.setText(data.getString("phone"));
//                                img_telp.setImageResource(R.drawable.ic_check);
//                            }
//                            else {
//                                Toast.makeText(PembayaranBarangActivity.this, message, Toast.LENGTH_SHORT).show();
//                            }
//
//                            if(alamat == null){
//                                load_alamat();
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
//                        Toast.makeText(PembayaranBarangActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PembayaranBarangActivity.this);
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

    void req_plus_minus_qty(final boolean isPlus, final int position_daftar_belanja, final int position_daftar_item, final TextView txt_jumlah_beli, final ItemKeranjang item, final TextView txt_berat_beli){
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(PembayaranBarangActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_cart_plus_minus(String.format((isPlus? "user/cart/plus/%s":"user/cart/minus/%s") ,item.getId()), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pembayaran barang]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")) {
                            ArrayList<ItemKeranjang> list_item = cardAdapter.getListItem().get(position_daftar_belanja).getItemKeranjang();
                            for (int i=0;i<list_item.size();i++){
                                ItemKeranjang obj = list_item.get(i);
                                if(obj.getId().equals(item.getId())){
                                    obj.setJumlah_beli( (isPlus? item.getJumlah_beli()+1:item.getJumlah_beli()-1) );

                                    txt_jumlah_beli.setText(String.valueOf(item.getJumlah_beli()));
                                    HitungUlang(item,!isPlus,isPlus);
                                    HitungOngkir(txt_berat_beli,false,position_daftar_belanja,position_daftar_item);
                                }
                            }
                            Toast.makeText(PembayaranBarangActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(PembayaranBarangActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PembayaranBarangActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pembayaran barang]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(PembayaranBarangActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PembayaranBarangActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

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
//                                ArrayList<ItemKeranjang> list_item = cardAdapter.getListItem().get(position_daftar_belanja).getItemKeranjang();
//                                for (int i=0;i<list_item.size();i++){
//                                    ItemKeranjang obj = list_item.get(i);
//                                    if(obj.getId().equals(item.getId())){
//                                        obj.setJumlah_beli( (isPlus? item.getJumlah_beli()+1:item.getJumlah_beli()-1) );
//
//                                        txt_jumlah_beli.setText(String.valueOf(item.getJumlah_beli()));
//                                        HitungUlang(item,!isPlus,isPlus);
//                                        HitungOngkir(txt_berat_beli,false,position_daftar_belanja,position_daftar_item);
//                                    }
//                                }
//                            }
//
//                            Toast.makeText(PembayaranBarangActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(PembayaranBarangActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
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

        for (int x=0;x<list_keranjang.size();x++) {
            Keranjang k = list_keranjang.get(x);
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
        txt_total_harga.setText(Util.rupiahFormat(total));
        txt_total_bayar.setText(Util.rupiahFormat(total));
    }

    @SuppressLint("SetTextI18n")
    void HitungOngkir(TextView txt_berat_beli,boolean isInit,int position_daftar_belanja,int position_daftar_item){
        if(list_keranjang !=null && list_keranjang.size()>0){
            total_ongkir=0;
            for (int i = 0; i < list_keranjang.size(); i++) {
                ArrayList<ItemKeranjang> list_item_keranjang = list_keranjang.get(i).getItemKeranjang();
                if (list_item_keranjang != null && list_item_keranjang.size() > 0) {
                    int berat_total=0;

                    for (int j = 0; j < list_item_keranjang.size(); j++) {
                        ItemKeranjang item = list_item_keranjang.get(j);
                        if (item.getKurir() != null) {
                            berat_total+=(item.getBerat() * item.getJumlah_beli());
                            Log.i("app-log 2",String.format("produk:%s, berat:%s, berat_total:%s, kurir:%s",item.getNama_produk(),(item.getBerat() * item.getJumlah_beli()),berat_total,item.getKurir().getService()));

                            if(isInit) {
                                txt_berat_beli.setText((item.getBerat() * item.getJumlah_beli()) + " Gram");
                            }
                            else if(!isInit && position_daftar_belanja==i && position_daftar_item==j){
                                txt_berat_beli.setText((item.getBerat() * item.getJumlah_beli()) + " Gram");
                            }
                        }
                        else {
                            Toast.makeText(this, "Pilih kurir terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }

                        if(item.getKurir()!=null && j==list_item_keranjang.size()-1)
                            total_ongkir += item.getKurir().getPrice() * Math.ceil((double) berat_total / 1000);
                    }
                }
            }

            txt_total_ongkir.setText(Util.rupiahFormat(total_ongkir));
            double tmp = Double.parseDouble(txt_total_bayar.getText().toString().replace("Rp ","").replace(".",""));
            txt_total_bayar.setText(Util.rupiahFormat(tmp+total_ongkir));
        }
    }

    void request_precheckout(){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(PembayaranBarangActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_transaction_pre_checkout("user/transaction/pre/checkout", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pembayaran barang]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");
                            String invoice_number = data.getString("invoice_number");
                            String data_ = String.valueOf(Util.dataRequestPembayaranBarang(invoice_number,alamat,"1",bank,list_keranjang));
                            Log.i("app-log",data_);

                            //request_checkout(dialog_loading,data_);
                        }
                        else {
                            Toast.makeText(PembayaranBarangActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PembayaranBarangActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pembayaran barang]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(PembayaranBarangActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PembayaranBarangActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                String invoice_number = data.getString("invoice_number");
//                                String data_ = String.valueOf(Util.dataRequestPembayaranBarang(invoice_number,alamat,"1",bank,list_keranjang));
//                                Log.i("app-log",data_);
//
//                                dialog_loading.dismissWithAnimation();
//                                request_checkout(dialog_loading,data_);
//                            }
//                            else {
//                                dialog_loading.dismissWithAnimation();
//                                Toast.makeText(PembayaranBarangActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(PembayaranBarangActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PembayaranBarangActivity.this);
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
    void request_checkout(final SweetAlertDialog dialog_loading, final String data){
        SessionManager session = new SessionManager(PembayaranBarangActivity.this);
        HashMap<String, String> data_ = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data_.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_transaction_checkout("user/transaction/checkout", data, headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        Log.i("app-log", data);

        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                //Log.i("app-log [pembayaran barang]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            Intent i = new Intent(PembayaranBarangActivity.this, TransaksiActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(PembayaranBarangActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PembayaranBarangActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pembayaran barang]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(PembayaranBarangActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PembayaranBarangActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                Intent i = new Intent(PembayaranBarangActivity.this, TransaksiActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(i);
//                            }
//                            Toast.makeText(PembayaranBarangActivity.this, message, Toast.LENGTH_SHORT).show();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        dialog_loading.dismissWithAnimation();
//                        Toast.makeText(PembayaranBarangActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PembayaranBarangActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//            protected Map<String, String> getParams()
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("data", data);
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }

    @Override
    public void PlusItemKeranjang(int position_daftar_belanja, int position_daftar_item, TextView txt_jumlah_beli, ItemKeranjang item, TextView txt_berat_beli) {
        if(!isDefault) {
            req_plus_minus_qty(true, position_daftar_belanja, position_daftar_item, txt_jumlah_beli, item, txt_berat_beli);
        }
        else{
            ArrayList<ItemKeranjang> list_item = cardAdapter.getListItem().get(position_daftar_belanja).getItemKeranjang();
            for (int i=0;i<list_item.size();i++) {
                ItemKeranjang obj = list_item.get(i);
                if (obj.getId().equals(item.getId())) {
                    obj.setJumlah_beli(item.getJumlah_beli() + 1);

                    txt_jumlah_beli.setText(String.valueOf(item.getJumlah_beli()));
                    HitungUlang(item, false, true);
                    HitungOngkir(txt_berat_beli,false,position_daftar_belanja,position_daftar_item);
                }
            }
        }
    }

    @Override
    public void MinusItemKeranjang(int position_daftar_belanja, int position_daftar_item, TextView txt_jumlah_beli, ItemKeranjang item, TextView txt_berat_beli) {
        if(!isDefault) {
            req_plus_minus_qty(false, position_daftar_belanja, position_daftar_item, txt_jumlah_beli, item, txt_berat_beli);
        }
        else{
            ArrayList<ItemKeranjang> list_item = cardAdapter.getListItem().get(position_daftar_belanja).getItemKeranjang();
            for (int i=0;i<list_item.size();i++) {
                ItemKeranjang obj = list_item.get(i);
                if (obj.getId().equals(item.getId())) {
                    obj.setJumlah_beli(item.getJumlah_beli() - 1);

                    txt_jumlah_beli.setText(String.valueOf(item.getJumlah_beli()));
                    HitungUlang(item, true, false);
                    HitungOngkir(txt_berat_beli,false,position_daftar_belanja,position_daftar_item);
                }
            }
        }
    }

    @Override
    public void SelectKurir(ItemKeranjang item) {
        Intent i = new Intent(PembayaranBarangActivity.this, PilihKurirActivity.class);
        i.putExtra("bank",bank);
        i.putExtra("alamat",alamat);
        i.putExtra("list_keranjang",list_keranjang);
        i.putExtra("itemKeranjang",item);
        startActivity(i);
    }

    public void onBackPressed() {
        Intent i = new Intent(this, KeranjangActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void CatatanChange(String text, int position_keranjang) {
        if(list_keranjang!=null && list_keranjang.size()>0) {
            list_keranjang.get(position_keranjang).setCatatan(text);
        }
    }
}