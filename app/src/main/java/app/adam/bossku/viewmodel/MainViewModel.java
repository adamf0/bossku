package app.adam.bossku.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.adam.basiclibrary.BasicLib;
import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.interfaces.MainListener;
import app.adam.bossku.view.model.Kategori;
import app.adam.bossku.view.model.Product;
import retrofit2.Call;
import retrofit2.Callback;

public class MainViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    public static BasicLib lib;
    private MainListener mainListener;

    public MainViewModel(){

    }

    public MainListener getMainListener() {
        return mainListener;
    }

    public void setMainListener(MainListener mainListener) {
        this.mainListener = mainListener;
    }

    public void loadData(){
        SessionManager session = new SessionManager(lib.getAct());
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_goods_list_random("goods/list/random", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [main]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        //String message = res.getString("message");

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");
                            JSONArray goods = data.getJSONArray("goods");
                            JSONArray category = data.getJSONArray("category_list");

                            ArrayList<Product> list_data = new ArrayList<>();
                            ArrayList<Kategori> list_kategori = new ArrayList<>();
                            //ArrayList<ProductDiskon> list_diskon = new ArrayList<>();
                            //ArrayList<ProductTerlaris> list_terlaris = new ArrayList<>();

                            for (int i=0;i<category.length();i++) {
                                JSONObject obj = category.getJSONObject(i);
                                list_kategori.add(
                                        new Kategori(
                                                null,
                                                obj.getString("name"),
                                                null,
                                                obj.getString("link")
                                        )
                                );
                            }
                            for (int i=0;i<goods.length();i++){
                                JSONObject obj = goods.getJSONObject(i);

                                list_data.add(
                                        new Product(
                                                obj.getString("id"),
                                                obj.getString("name"),//
                                                obj.getString("image"),//
                                                obj.getString("description"),//
                                                0,
                                                null,
                                                obj.getString("brand_name"),//
                                                obj.getString("city_name"),//
                                                obj.getInt("price")//
                                        )
                                );
                            }

//                                ProductDiskon diskon1 = new ProductDiskon("5","Judul Produk 5 Paling Panjang","gambar produk 5","Toko 2",10000,100);
//                                list_diskon.add(diskon1);
//
//                                ProductTerlaris terlaris1 = new ProductTerlaris("8","Judul Produk 8","gambar produk 8","Toko 3",0);
//                                list_terlaris.add(terlaris1);

                            mainListener.onSuccessLoadData(list_data,list_kategori,null,null);
                        }
                        else {
                            mainListener.onFailLoadData("something's wrong with API");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    mainListener.onFailLoadData("Fail connect to server");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [main]", t.toString());
                if(call.isCanceled()) {
                    mainListener.onFailLoadData("request was aborted");
                }else {
                    mainListener.onFailLoadData("Unable to submit post to API.");
                }
            }
        });

//        RequestQueue queue = Volley.newRequestQueue(lib.getAct());
//        String url = Route.base_url+Route.goods_list_random;
//
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
//                                JSONArray goods = data.getJSONArray("goods");
//                                JSONArray category = data.getJSONArray("category_list");
//
//                                ArrayList<Product> list_data = new ArrayList<>();
//                                ArrayList<Kategori> list_kategori = new ArrayList<>();
//                                ArrayList<ProductDiskon> list_diskon = new ArrayList<>();
//                                ArrayList<ProductTerlaris> list_terlaris = new ArrayList<>();
//
//                                for (int i=0;i<category.length();i++) {
//                                    JSONObject obj = category.getJSONObject(i);
//                                    list_kategori.add(
//                                            new Kategori(
//                                                    null,
//                                                    obj.getString("name"),
//                                                    null,
//                                                    obj.getString("link")
//                                            )
//                                    );
//                                }
//                                for (int i=0;i<goods.length();i++){
//                                    JSONObject obj = goods.getJSONObject(i);
//
//                                    list_data.add(
//                                            new Product(
//                                                    obj.getString("id"),
//                                                    obj.getString("name"),//
//                                                    obj.getString("image"),//
//                                                    obj.getString("description"),//
//                                                    0,
//                                                    null,
//                                                    obj.getString("brand_name"),//
//                                                    obj.getString("city_name"),//
//                                                    obj.getInt("price")//
//                                            )
//                                    );
//                                }
//
////                                ProductDiskon diskon1 = new ProductDiskon("5","Judul Produk 5 Paling Panjang","gambar produk 5","Toko 2",10000,100);
////                                list_diskon.add(diskon1);
////
////                                ProductTerlaris terlaris1 = new ProductTerlaris("8","Judul Produk 8","gambar produk 8","Toko 3",0);
////                                list_terlaris.add(terlaris1);
//
//                                mainListener.onSuccessLoadData(list_data,list_kategori,null,null);
//                            }
//                            else{
//                                mainListener.onFailLoadData("Data tidak ditemukan");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        mainListener.onFailLoadData(Util.errorVolley(volleyError));
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(lib.getAct());
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
}
