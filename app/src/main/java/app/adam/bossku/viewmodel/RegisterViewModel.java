package app.adam.bossku.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.adam.basiclibrary.BasicLib;
import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.view.model.component.RegisterComponent;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterViewModel extends ViewModel {
    public String[] kategori = {"UMKM Mahasiswa", "UMKM Umum"};
    public RegisterComponent component;
    public MutableLiveData<Boolean> agree = new MutableLiveData<>();
    @SuppressLint("StaticFieldLeak")
    public static BasicLib lib;
    private RegisterListener registerListener;
    public int isUMKM = 0;

    public RegisterViewModel(){
        this.component = new RegisterComponent(null,null,null,null,null);
    }

    public RegisterListener getRegisterListener() {
        return registerListener;
    }

    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    public void register(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        //headers.put("Authorization", "251423");
        headers.put("Accept", "application/json");

        Call<String> call = getResponse.request_register("user/register", id_role(),component.getNama_lengkap(),component.getPassword(),component.getEmail(),component.getTelp());
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [register]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);

                        String status = res.getString("status");
                        String message = res.getString("message");
                        if(status.equals("success")) {
                            registerListener.onSuccessRegister(message);
                        }
                        else {
                            registerListener.onFailRegister("something's wrong with API");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    registerListener.onFailRegister("Fail connect to server");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [register]", t.toString());
                if(call.isCanceled()) {
                    registerListener.onFailRegister("request was aborted");
                }else {
                    registerListener.onFailRegister("Unable to submit post to API.");
                }
            }
        });

//        RequestQueue queue = Volley.newRequestQueue(lib.getAct());
//        String url = Route.base_url+Route.register_user;
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("app-log", response);
//                        try {
//                            JSONObject res = new JSONObject(response);
//
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//                            if(status.equals("success")) {
//                                registerListener.onSuccessRegister(message);
//                            }
//                            else {
//                                registerListener.onFailRegister(message);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        registerListener.onFailRegister(Util.errorVolley(volleyError));
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("id_role", id_role());
//                params.put("name", component.getNama_lengkap());
//                params.put("password", component.getPassword());
//                params.put("email", component.getEmail());
//                params.put("phone", component.getTelp());
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }

    public String id_role(){
        if(isUMKM==0){
            return "1";
        }
        else{
           if(component.getKategori().equals("UMKM Mahasiswa")){
               return "3";
           }
           else if(component.getKategori().equals("UMKM Umum")){
               return "5";
           }
           else{
               return null;
           }
        }
    }

    public CompoundButton.OnCheckedChangeListener checkbox(){
        return (compoundButton, b) -> agree.postValue(b);
    }

    @BindingAdapter("app:adapterKategori")
    public static void adapterKategori(AppCompatAutoCompleteTextView view, String[] list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(lib.getAct(), android.R.layout.select_dialog_item, list);
        view.setThreshold(1);
        view.setAdapter(adapter);
    }

    public interface RegisterListener{
        void onErrorValidation(String message);
        void onSuccessRegister(String message);
        void onFailRegister(String message);
    }
}
