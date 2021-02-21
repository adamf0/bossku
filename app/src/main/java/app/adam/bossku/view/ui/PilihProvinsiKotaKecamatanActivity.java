package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import app.adam.bossku.helper.MaterialSearchView;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.interfaces.onSearchListener;
import app.adam.bossku.view.adapter.PilihProvinsiKotaKecamatanAdapter;
import app.adam.bossku.view.model.Alamat;
import app.adam.bossku.view.model.Kecamatan;
import app.adam.bossku.view.model.Kota;
import app.adam.bossku.view.model.Provinsi;
import retrofit2.Call;
import retrofit2.Callback;

public class PilihProvinsiKotaKecamatanActivity extends AppCompatActivity implements PilihProvinsiKotaKecamatanAdapter.ProvinsiKotaKecamatanListener, onSearchListener {
    ArrayList<Provinsi> list_prov = null;
    ArrayList<Kota> list_kota = null;
    ArrayList<Kecamatan> list_kec = null;
    SpinKitView loader;
    RecyclerView rv_list;
//    String label="";
//    String alamat="";
    Alamat obj=null;
    Provinsi provinsi;
    Kota kota;
    Kecamatan kecamatan;

    String activity = "";
    String avatar = null;
    String name = "";
    String phone = "";
    String date_of_birth = "";
    String position_image = "";
    int openlayout = 1;
    int idx_jk = 0 ;

    PilihProvinsiKotaKecamatanAdapter cardAdapter = null;

    boolean mSearchViewAdded = false;
    MaterialSearchView mSearchView;
    WindowManager mWindowManager;
    boolean searchActive = false;
    boolean isUpdate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_provinsi_kota_kecamatan);

        rv_list = findViewById(R.id.rv_pilih_provinsi_kota_kecamatan);
        loader = findViewById(R.id.loader_pilih_provinsi_kota_kecamatan);
        Toolbar mtoolbar = findViewById(R.id.toolbar_pilih_provinsi_kota_kecamatan);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> PilihProvinsiKotaKecamatanActivity.super.onBackPressed());

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mSearchView = new MaterialSearchView(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setHintText("Search");
        mSearchView.setSeachWithList(false);
        if (mtoolbar != null) {
            mtoolbar.post(new Runnable() {
                @Override
                public void run() {
                    if (!mSearchViewAdded && mWindowManager != null) {
                        mWindowManager.addView(mSearchView, MaterialSearchView.getSearchViewLayoutParams(PilihProvinsiKotaKecamatanActivity.this));
                        mSearchViewAdded = true;
                    }
                }
            });
        }

        Bundle b = getIntent().getExtras();
        if(b != null){
//            label = b.getString("label");
//            alamat = b.getString("alamat");
            obj = b.getParcelable("alamat");
            provinsi = b.getParcelable("provinsi");
            boolean pilih_prov = b.getBoolean("pilih_prov");
            kota = b.getParcelable("kota");
            boolean pilih_kota = b.getBoolean("pilih_kota");
            kecamatan = b.getParcelable("kecamatan");
            boolean pilih_kec = b.getBoolean("pilih_kec");
            isUpdate = b.getBoolean("isUpdate");
            activity = b.getString("activity");

            avatar = b.getString("avatar");
            name = b.getString("nama");
            phone = b.getString("notelp");
            date_of_birth = b.getString("tanggal_lahir");
            idx_jk = b.getInt("jenis_kelamin");
            position_image = b.getString("position_image");
            openlayout = b.getInt("openlayout");

            if(pilih_prov) {
                mtoolbar.setTitle("Pilih Provinsi");
                list_prov = new ArrayList<>();
            }
            else if(pilih_kota) {
                mtoolbar.setTitle("Pilih Kota");
                list_kota = new ArrayList<>();
            }
            else if(pilih_kec) {
                mtoolbar.setTitle("Pilih Kecamatan");
                list_kec = new ArrayList<>();
            }
            load_data(pilih_prov,pilih_kota,pilih_kec);
        }
    }
    void load_data(final boolean isProv, final boolean isKota, final boolean isKec){
        loader.setVisibility(View.VISIBLE);

        SessionManager session = new SessionManager(PilihProvinsiKotaKecamatanActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = null;
        if(isProv)
            call = getResponse.request_master_data_provinces_list("master_data/provinces/list", headers);
        else if(isKota)
            call = getResponse.request_master_data_cities_listby(String.format("master_data/cities/listby/%s",provinsi.getId()), headers);
        else if(isKec)
            call = getResponse.request_master_data_subdistricts_listby(String.format("master_data/subdistricts/listby/%s",kota.getId()), headers);

        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pilih prov kota kec]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            JSONArray datas = res.getJSONArray("data");
                            cardAdapter = new PilihProvinsiKotaKecamatanAdapter(PilihProvinsiKotaKecamatanActivity.this);

                            for (int i=0;i<datas.length();i++) {
                                JSONObject obj = datas.getJSONObject(i);

                                if (isProv) {
                                    list_prov.add(
                                            new Provinsi(
                                                    obj.getString("province_id"),
                                                    obj.getString("province")
                                            )
                                    );
                                }
                                else if (isKota) {
                                    list_kota.add(
                                            new Kota(
                                                    obj.getString("city_id"),
                                                    obj.getString("city_name"),
                                                    obj.getString("type")
                                            )
                                    );
                                }
                                else if (isKec) {
                                    list_kec.add(
                                            new Kecamatan(
                                                    obj.getString("subdistrict_id"),
                                                    obj.getString("subdistrict_name"),
                                                    null
                                            )
                                    );
                                }
                            }


                            new Handler().postDelayed(() -> {
                                rv_list.setLayoutManager(new LinearLayoutManager(PilihProvinsiKotaKecamatanActivity.this));
                                rv_list.setHasFixedSize(true);
                                cardAdapter.setList_prov(list_prov);
                                cardAdapter.setList_kota(list_kota);
                                cardAdapter.setList_kec(list_kec);
                                cardAdapter.notifyDataSetChanged();
                                rv_list.setAdapter(cardAdapter);
                                loader.setVisibility(View.GONE);
                            }, 1500);
                        }
                        else{
                            loader.setVisibility(View.GONE);
                            Toast.makeText(PilihProvinsiKotaKecamatanActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PilihProvinsiKotaKecamatanActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pilih prov kota kec]", t.toString());
                loader.setVisibility(View.GONE);

                if(call.isCanceled()) {
                    Toast.makeText(PilihProvinsiKotaKecamatanActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PilihProvinsiKotaKecamatanActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                                JSONArray datas = res.getJSONArray("data");
//                                cardAdapter = new PilihProvinsiKotaKecamatanAdapter(PilihProvinsiKotaKecamatanActivity.this);
//
//                                for (int i=0;i<datas.length();i++) {
//                                    JSONObject obj = datas.getJSONObject(i);
//
//                                    if (isProv) {
//                                        list_prov.add(
//                                                new Provinsi(
//                                                        obj.getString("province_id"),
//                                                        obj.getString("province")
//                                                )
//                                        );
//                                    }
//                                    else if (isKota) {
//                                        list_kota.add(
//                                                new Kota(
//                                                        obj.getString("city_id"),
//                                                        obj.getString("city_name"),
//                                                        obj.getString("type")
//                                                )
//                                        );
//                                    }
//                                    else if (isKec) {
//                                        list_kec.add(
//                                                new Kecamatan(
//                                                        obj.getString("subdistrict_id"),
//                                                        obj.getString("subdistrict_name"),
//                                                        null
//                                                )
//                                        );
//                                    }
//                                }
//
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        rv_list.setLayoutManager(new LinearLayoutManager(PilihProvinsiKotaKecamatanActivity.this));
//                                        rv_list.setHasFixedSize(true);
//                                        cardAdapter.setList_prov(list_prov);
//                                        cardAdapter.setList_kota(list_kota);
//                                        cardAdapter.setList_kec(list_kec);
//                                        cardAdapter.notifyDataSetChanged();
//                                        rv_list.setAdapter(cardAdapter);
//                                        loader.setVisibility(View.GONE);
//                                    }
//                                }, 1500);
//                            }
//                            else{
//                                loader.setVisibility(View.GONE);
//                                Toast.makeText(PilihProvinsiKotaKecamatanActivity.this,message, Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(PilihProvinsiKotaKecamatanActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PilihProvinsiKotaKecamatanActivity.this);
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
    public void SelectItemProv(Provinsi item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        if(activity.equals("ProfilActivity")){
            startActivity(
                    new Intent(this, ProfilActivity.class)
                            .putExtra("avatar",avatar)
                            .putExtra("nama",name)
                            .putExtra("notelp",phone)
                            .putExtra("tanggal_lahir",date_of_birth)
                            .putExtra("jenis_kelamin",idx_jk)
                            .putExtra("provinsi",item)
                            .putExtra("kota",kota)
                            .putExtra("kecamatan",kecamatan)
                            .putExtra("position_image",position_image)
                            .putExtra("openlayout",openlayout)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            );
        }
        else {
            startActivity(
                    new Intent(this, TambahEditAlamatActivity.class)
                            .putExtra("alamat", obj)
                            .putExtra("provinsi", item)
                            .putExtra("kota", (provinsi != null && item.getId().equals(provinsi.getId()) ? kota : null))
                            .putExtra("kecamatan", (provinsi != null && item.getId().equals(provinsi.getId()) ? kecamatan : null))
                            .putExtra("isUpdate", isUpdate)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            );
        }
    }

    @Override
    public void SelectItemKota(Kota item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        if(activity.equals("ProfilActivity")){
            startActivity(
                    new Intent(this, ProfilActivity.class)
                            .putExtra("avatar",avatar)
                            .putExtra("nama",name)
                            .putExtra("notelp",phone)
                            .putExtra("tanggal_lahir",date_of_birth)
                            .putExtra("jenis_kelamin",idx_jk)
                            .putExtra("provinsi",provinsi)
                            .putExtra("kota",item)
                            .putExtra("kecamatan",kecamatan)
                            .putExtra("position_image",position_image)
                            .putExtra("openlayout",openlayout)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            );
        }
        else {
            startActivity(
                    new Intent(this, TambahEditAlamatActivity.class)
                            .putExtra("alamat", obj)
                            .putExtra("provinsi", provinsi)
                            .putExtra("kota", item)
                            .putExtra("kecamatan", (kota != null && item.getId().equals(kota.getId()) ? kecamatan : null))
                            .putExtra("isUpdate", isUpdate)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            );
        }
    }

    @Override
    public void SelectItemKEc(Kecamatan item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        if(activity.equals("ProfilActivity")){
            startActivity(
                    new Intent(this, ProfilActivity.class)
                            .putExtra("avatar",avatar)
                            .putExtra("nama",name)
                            .putExtra("notelp",phone)
                            .putExtra("tanggal_lahir",date_of_birth)
                            .putExtra("jenis_kelamin",idx_jk)
                            .putExtra("provinsi",provinsi)
                            .putExtra("kota",kota)
                            .putExtra("kecamatan",item)
                            .putExtra("position_image",position_image)
                            .putExtra("openlayout",openlayout)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            );
        }
        else {
            startActivity(
                    new Intent(this, TambahEditAlamatActivity.class)
                            .putExtra("alamat", obj)
                            .putExtra("provinsi", provinsi)
                            .putExtra("kota", kota)
                            .putExtra("kecamatan", item)
                            .putExtra("isUpdate", isUpdate)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            );
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_etc, menu);
        if(searchActive)
            mSearchView.display();

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_etc) {
            mSearchView.display();
            openKeyboard();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        searchActive = false;
        mSearchView.mSearchEditText.setText("");
        mSearchView.hide();
        mSearchView.showProgress(false);
        super.onPause();
    }
    protected void onDestroy(){
        super.onDestroy();

        if (!mSearchViewAdded && mWindowManager != null) {
            mWindowManager.removeView(mSearchView);
        }
    }

    private void openKeyboard(){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
    }

    @Override
    public void onSearch(String query) {

    }

    @Override
    public void onSearchSubmit(String query) {
        if(cardAdapter != null) {
            cardAdapter.getFilter().filter(query);
            searchActive = false;
            mSearchView.hide();
        }
    }

    @Override
    public void searchViewOpened() {

    }

    @Override
    public void searchViewClosed() {

    }

    @Override
    public void onCancelSearch() {
        searchActive = false;
        mSearchView.mSearchEditText.setText("");
        mSearchView.hide();

        if(cardAdapter != null) {
            cardAdapter.getFilter().filter("");
        }
    }
}