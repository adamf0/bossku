package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.adapter.SlideGambarAdapter;
import app.adam.bossku.view.adapter.TestimoniProductAdapter;
import app.adam.bossku.view.model.Product;
import app.adam.bossku.view.model.Testimoni;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class DetailProdukActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    int dotscount;
    ImageView[] dots;
    ImageView img_toko,icon_diskon;
    TextView txt_nama_toko,txt_daerah_toko,txt_nama_produk,txt_merek_produk,txt_harga,txt_diskon,txt_ongkos_kirim,txt_jumlah_produk,txt_berat_produk;
    HtmlTextView txt_deskripsi;
    RecyclerView rv_list_testimoni;
    FloatingActionButton btn_tambah;
    SpinKitView loader;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        viewPager = findViewById(R.id.slider_page_detail_produk);
        sliderDotspanel = findViewById(R.id.slider_dot_detail_produk);
        btn_tambah = findViewById(R.id.btn_pesan_detail_produk);
        img_toko = findViewById(R.id.img_toko_detail_produk);
        txt_nama_toko = findViewById(R.id.txt_nama_toko_detail_produk);
        txt_daerah_toko = findViewById(R.id.txt_daerah_toko_detail_produk);
        txt_nama_produk = findViewById(R.id.txt_nama_produk_detail_produk);
        txt_merek_produk = findViewById(R.id.txt_merek_produk_detail_produk);
        txt_harga = findViewById(R.id.txt_harga_produk_detail_produk);
        txt_jumlah_produk = findViewById(R.id.txt_jumlah_produk_detail_produk);
        txt_berat_produk = findViewById(R.id.txt_berat_produk_detail_produk);
        txt_diskon = findViewById(R.id.txt_diskon_produk_detail_produk);
        icon_diskon = findViewById(R.id.icon_diskon_produk_detail_produk);
        txt_ongkos_kirim = findViewById(R.id.txt_ongkir_detail_produk);
        txt_deskripsi = findViewById(R.id.txt_deskripsi_produk_detail_product);
        rv_list_testimoni = findViewById(R.id.rv_testimoni_produk_detail_product);
        loader = findViewById(R.id.loader_detail_produk);
        layout = findViewById(R.id.layout_detail_produk);
        Toolbar mtoolbar = findViewById(R.id.toolbar_detail_produk);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> DetailProdukActivity.super.onBackPressed());

        int diskon = 500;
        int harga_produk = 12000;
        String harga_lama = Util.rupiahFormat(harga_produk);
        String harga_baru = Util.rupiahFormat((harga_produk-diskon));
        String harga = harga_baru+" "+harga_lama;

        SpannableString span = new SpannableString(harga);
        ForegroundColorSpan txtColor = new ForegroundColorSpan(Color.parseColor("#FF9E9D9D"));
        StrikethroughSpan strike = new StrikethroughSpan();

        span.setSpan(strike, harga.indexOf(harga_lama), harga.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(txtColor, harga.indexOf(harga_lama), harga.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_harga.setText(span);
        txt_diskon.setText(Util.rupiahFormat(diskon));

        Bundle b = getIntent().getExtras();
        if(b != null){
            String action = b.getString("action");
            final Product produk = b.getParcelable("produk");

            if(produk!=null)
                load_data(produk.getId_produk());

            if(action.equals("TokoSayaActivity")){
                btn_tambah.setVisibility(View.INVISIBLE);
                btn_tambah.setEnabled(false);
            }
            else{
                btn_tambah.setVisibility(View.VISIBLE);
                btn_tambah.setEnabled(true);

                btn_tambah.setOnClickListener(v -> {
                    Toast.makeText(DetailProdukActivity.this,"req add_cart",Toast.LENGTH_LONG).show();
                    assert produk != null;
                    addCart(produk);
                });
            }
        }
    }

    void addCart(Product item){
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(DetailProdukActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_cart_add(String.format("user/cart/add/%s",item.getId_produk()), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [detail produk]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")) {
                            Toast.makeText(DetailProdukActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(DetailProdukActivity.this,"something's wrong with API",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(DetailProdukActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [detail produk]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(DetailProdukActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(DetailProdukActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        RequestQueue queue = Volley.newRequestQueue(this);
//        Log.i("app-log",url);
//
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
//                            Toast.makeText(DetailProdukActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        dialog_loading.dismissWithAnimation();
//                        Toast.makeText(DetailProdukActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        )
//        {
//            @Override
//            public Map<String, String> getHeaders() {
//                Log.i("app-log token",data.get(SessionManager.KEY_TOKEN));
//
//                Map<String, String>  params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//
//                return params;
//            };
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
////                List<Header> headers = response.allHeaders;
////                for (int i=0;i<headers.size();i++)
////                    Log.i("app-log",String.format("%s: %s",headers.get(i).getName(),headers.get(i).getValue()));
//
//                return super.parseNetworkResponse(response);
//            }
//        };
//
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);

    }
    void initSlider(ArrayList<String> list_image,boolean isDefault){
        SlideGambarAdapter slideGambarAdapter = new SlideGambarAdapter(this,list_image,isDefault);
        viewPager.setAdapter(slideGambarAdapter);
        dotscount = slideGambarAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    void initTestimoni(){
        ArrayList<Testimoni> list_item = new ArrayList<>();
        Testimoni testimoni1 = new Testimoni("1","Pengguna 1","Sesuai gambar");
        Testimoni testimoni2 = new Testimoni("2","Pengguna 2","Rasanya mantap");
        Testimoni testimoni3 = new Testimoni("3","Pengguna 3","Respon cepat, mantul");

        list_item.add(testimoni1);
        list_item.add(testimoni2);
        list_item.add(testimoni3);

        rv_list_testimoni.setHasFixedSize(true);
        rv_list_testimoni.setLayoutManager(new LinearLayoutManager(this));
        TestimoniProductAdapter cardAdapter = new TestimoniProductAdapter(this);
        cardAdapter.setListItem(list_item);
        cardAdapter.notifyDataSetChanged();
        rv_list_testimoni.setAdapter(cardAdapter);
    }

    void load_data(String search){
        layout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        SessionManager session = new SessionManager(DetailProdukActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_goods_detail(String.format("goods/detail/%s",search), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [detail produk]", res_);
                layout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");
                            final JSONObject goods = data.getJSONObject("goods");
                            JSONArray images = data.getJSONArray("images");
                            final JSONObject brand = data.getJSONObject("brand");

                            ArrayList<String> list_image = new ArrayList<>();
                            for (int i=0;i<images.length();i++){
                                list_image.add(images.getJSONObject(i).getString("location"));
                            }
                            initSlider(list_image,false);
                            initTestimoni();

                            txt_nama_toko.setText(brand.getString("brand_name"));
                            txt_daerah_toko.setText(brand.getJSONObject("user").getJSONObject("city").getString("city_name"));
                            txt_nama_produk.setText(goods.getString("name"));
                            txt_merek_produk.setText(brand.getString("brand_name"));
                            txt_harga.setText(Util.rupiahFormat(goods.getInt("price")));
                            txt_diskon.setText(Util.rupiahFormat(100));
                            txt_jumlah_produk.setText(goods.getString("stock")+" Unit");
                            txt_berat_produk.setText(goods.getString("weight")+" gram");
                            txt_ongkos_kirim.setText(Util.rupiahFormat(9000));
                            txt_deskripsi.setHtml(goods.getString("description"));
                        }
                        else{
                            Toast.makeText(DetailProdukActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            ArrayList<String> list_image = new ArrayList<>();
                            initSlider(list_image,true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(DetailProdukActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [detail produk]", t.toString());
                layout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(DetailProdukActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(DetailProdukActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = Route.base_url+ String.format(Route.good_detail,search);
//        Log.i("app-log",url);
//
//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//
//                            if(status.equals("success")){
//                                JSONObject data = res.getJSONObject("data");
//                                final JSONObject goods = data.getJSONObject("goods");
//                                JSONArray images = data.getJSONArray("images");
//                                final JSONObject brand = data.getJSONObject("brand");
//
//                                ArrayList<String> list_image = new ArrayList<>();
//                                for (int i=0;i<images.length();i++){
//                                    list_image.add(images.getJSONObject(i).getString("location"));
//                                }
//                                initSlider(list_image,false);
//                                initTestimoni();
//
//                                txt_nama_toko.setText(brand.getString("brand_name"));
//                                txt_daerah_toko.setText(brand.getJSONObject("user").getJSONObject("city").getString("city_name"));
//                                txt_nama_produk.setText(goods.getString("name"));
//                                txt_merek_produk.setText(brand.getString("brand_name"));
//                                txt_harga.setText(Util.rupiahFormat(goods.getInt("price")));
//                                txt_diskon.setText(Util.rupiahFormat(100));
//                                txt_jumlah_produk.setText(goods.getString("stock")+" Unit");
//                                txt_berat_produk.setText(goods.getString("weight")+" gram");
//                                txt_ongkos_kirim.setText(Util.rupiahFormat(9000));
//                                txt_deskripsi.setHtml(goods.getString("description"));
//
//                                layout.setVisibility(View.VISIBLE);
//                                loader.setVisibility(View.GONE);
//                            }
//                            else{
//                                Toast.makeText(DetailProdukActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                                layout.setVisibility(View.VISIBLE);
//                                loader.setVisibility(View.GONE);
//
//                                ArrayList<String> list_image = new ArrayList<>();
//                                initSlider(list_image,true);
//                            }
//                            initTestimoni();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(DetailProdukActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                        layout.setVisibility(View.VISIBLE);
//                        loader.setVisibility(View.GONE);
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(DetailProdukActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String>  params = new HashMap<>();
//                params.put("cookie", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
}