package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.viewpager.widget.ViewPager;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.adapter.SlideGambarAdapter;
import app.adam.bossku.view.model.Pendanaan;
import retrofit2.Call;
import retrofit2.Callback;

public class DetailPendanaanActivity extends AppCompatActivity {
    ViewPager viewPager,viewPager_ex;
    LinearLayout sliderDotspanel,sliderDotspanel_ex;
    private int dotscount;
    private ImageView[] dots;
    FloatingActionButton btn_beli,btn_beli_ex;
    SpinKitView loader;

    ConstraintLayout cl_layout,cl_layout_ex;
    TextView txt_kategori,txt_nama_brand,txt_nama_umkm,txt_sisa_pendanaan,txt_terjual;
    TextView txt_kategori_ex,txt_nama_brand_ex,txt_nama_umkm_ex,txt_total_pendanaan_ex,txt_total_terkumpul_ex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pendanaan);

        btn_beli = findViewById(R.id.btn_beli_detail_pendanaan);
        btn_beli_ex = findViewById(R.id.btn_beli_detail_pendanaan_ex);
        viewPager = findViewById(R.id.slider_page_detail_pendanaan);
        viewPager_ex = findViewById(R.id.slider_page_detail_pendanaan_ex);
        sliderDotspanel = findViewById(R.id.slider_dot_detail_pendanaan);
        sliderDotspanel_ex = findViewById(R.id.slider_dot_detail_pendanaan_ex);
        loader = findViewById(R.id.loader_detail_pendanaan);
        cl_layout = findViewById(R.id.cl_detail_pendanaan);
        cl_layout_ex = findViewById(R.id.cl_detail_pendanaan_ex);

        txt_kategori = findViewById(R.id.txt_kategori_brand_detail_pendanaan);
        txt_kategori_ex = findViewById(R.id.txt_kategori_brand_detail_pendanaan_ex);
        txt_nama_brand = findViewById(R.id.txt_nama_brand_detail_pendanaan);
        txt_nama_brand_ex = findViewById(R.id.txt_nama_brand_detail_pendanaan_ex);
        txt_nama_umkm = findViewById(R.id.txt_nama_umkm_detail_pendanaan);
        txt_nama_umkm_ex = findViewById(R.id.txt_nama_umkm_detail_pendanaan_ex);
        txt_sisa_pendanaan = findViewById(R.id.txt_sisa_pendanaan_detail_pendanaan);
        txt_terjual = findViewById(R.id.txt_terjual_pendanaan_detail_pendanaan);
        txt_total_pendanaan_ex = findViewById(R.id.txt_total_pendanaan_detail_pendanaan_ex);
        txt_total_terkumpul_ex = findViewById(R.id.txt_total_terkumpul_detail_pendanaan_ex);
        Toolbar mtoolbar = findViewById(R.id.toolbar_detail_pendanaan);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> DetailPendanaanActivity.super.onBackPressed());

        Bundle b = getIntent().getExtras();
        if(b != null){
            //String position_tab = b.getString("position_tab");
            final Pendanaan pendanaan = b.getParcelable("pendanaan");
            boolean isInvestor = b.getBoolean("isInvestor",true);

            if(isInvestor) {
                cl_layout.setVisibility(View.VISIBLE);
                cl_layout_ex.setVisibility(View.GONE);
            }
            else{
                cl_layout.setVisibility(View.GONE);
                cl_layout_ex.setVisibility(View.VISIBLE);
            }
            Log.i("app-log", String.valueOf(isInvestor));
            load_data(pendanaan.getId());
            btn_beli_ex.setOnClickListener(v -> startActivity(
                    new Intent(DetailPendanaanActivity.this,PembayaranPendanaanActivity.class)
                            .putExtra("pendanaan",pendanaan)
            ));
//            if(position_tab.equals("jual")){
//                btn_beli.setVisibility(ViewPager.INVISIBLE);
//                btn_beli.setEnabled(false);
//            }
//            else{
//                if(position_tab.equals("all")){
//                    load_data(pendanaan.getId());
//                }
//                else{
//                    InitSlider(null,true);
//                }
//
//                btn_beli.setVisibility(ViewPager.VISIBLE);
//                btn_beli.setEnabled(true);
//            }
        }
    }
//    void load_data(String id){
//        loader.setVisibility(View.VISIBLE);
//        cl_layout.setVisibility(View.GONE);
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = Route.base_url+String.format(Route.funding_project_detail,id);
//
//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onResponse(String response) {
//                        loader.setVisibility(View.GONE);
//                        cl_layout.setVisibility(View.VISIBLE);
//
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//
//                            if(status.equals("success")){
//                                JSONObject data = res.getJSONObject("data");
//
//                                txt_kategori.setText(data.getJSONObject("category").getString("name"));
//                                txt_nama_brand.setText(data.getString("product_name"));
//                                txt_nama_umkm.setText(data.getJSONObject("umkm_data").getString("brand_name"));
//
//                                BigDecimal total = new BigDecimal(data.getString("amount_of_funds"));
//                                BigDecimal terjual = new BigDecimal(data.getJSONObject("crowd_funding_total").getString("total"));
//                                BigDecimal sisa = total.subtract(terjual);
//
//                                txt_sisa_pendanaan.setText(": "+Util.rupiahFormat(sisa));
//                                txt_terjual.setText(": "+Util.rupiahFormat(terjual));
//
//                                ArrayList<String> list_image = new ArrayList<>();
//                                list_image.add(data.getString("image"));
//                                InitSlider(list_image,false);
//                            }
//                            else{
//                                Toast.makeText(DetailPendanaanActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                                InitSlider(null,true);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(DetailPendanaanActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                        loader.setVisibility(View.GONE);
//                        cl_layout.setVisibility(View.VISIBLE);
//                        InitSlider(null,true);
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(DetailPendanaanActivity.this);
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
//    }
    void load_data(String id){
        loader.setVisibility(View.VISIBLE);
        cl_layout_ex.setVisibility(View.GONE);

        SessionManager session = new SessionManager(DetailPendanaanActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_projects_detail(String.format("projects/detail/%s",id), headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [detail pendanaan]", res_);
                loader.setVisibility(View.GONE);
                cl_layout_ex.setVisibility(View.VISIBLE);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");

                            txt_kategori_ex.setText(data.getJSONObject("category").getString("name"));
                            txt_nama_brand_ex.setText(data.getString("product_name"));
                            txt_nama_umkm_ex.setText(data.getJSONObject("umkm_data").getString("brand_name"));

                            BigDecimal total = new BigDecimal(data.getString("amount_of_funds"));
                            BigDecimal terjual = new BigDecimal(data.getJSONObject("crowd_funding_total").getString("total"));

                            txt_total_pendanaan_ex.setText(Util.rupiahFormat(total));
                            txt_total_terkumpul_ex.setText(Util.rupiahFormat(terjual));

                            ArrayList<String> list_image = new ArrayList<>();
                            list_image.add(data.getString("image"));
                            initSlider(list_image,false);
                        }
                        else{
                            Toast.makeText(DetailPendanaanActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            initSlider(null,true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(DetailPendanaanActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [detail pendanaan]", t.toString());
                loader.setVisibility(View.GONE);
                cl_layout_ex.setVisibility(View.VISIBLE);
                initSlider(null,true);

                if(call.isCanceled()) {
                    Toast.makeText(DetailPendanaanActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(DetailPendanaanActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("app-log",response);
//                        loader.setVisibility(View.GONE);
//                        cl_layout_ex.setVisibility(View.VISIBLE);
//
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//
//                            if(status.equals("success")){
//                                JSONObject data = res.getJSONObject("data");
//
//                                txt_kategori_ex.setText(data.getJSONObject("category").getString("name"));
//                                txt_nama_brand_ex.setText(data.getString("product_name"));
//                                txt_nama_umkm_ex.setText(data.getJSONObject("umkm_data").getString("brand_name"));
//
//                                BigDecimal total = new BigDecimal(data.getString("amount_of_funds"));
//                                BigDecimal terjual = new BigDecimal(data.getJSONObject("crowd_funding_total").getString("total"));
//
//                                txt_total_pendanaan_ex.setText(Util.rupiahFormat(total));
//                                txt_total_terkumpul_ex.setText(Util.rupiahFormat(terjual));
//
//                                ArrayList<String> list_image = new ArrayList<>();
//                                list_image.add(data.getString("image"));
//                                initSlider(list_image,false);
//                            }
//                            else{
//                                Toast.makeText(DetailPendanaanActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                                initSlider(null,true);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(DetailPendanaanActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                        loader.setVisibility(View.GONE);
//                        cl_layout_ex.setVisibility(View.VISIBLE);
//                        initSlider(null,true);
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(DetailPendanaanActivity.this);
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
    void initSlider(ArrayList<String> list_image,boolean isDefault){
        SlideGambarAdapter slideGambarAdapter = new SlideGambarAdapter(this,list_image,isDefault);
        viewPager_ex.setAdapter(slideGambarAdapter);
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
        viewPager_ex.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
}