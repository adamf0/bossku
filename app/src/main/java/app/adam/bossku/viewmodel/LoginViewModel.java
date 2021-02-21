package app.adam.bossku.viewmodel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.adam.basiclibrary.BasicLib;
import app.adam.basiclibrary.TextListener;
import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.view.model.component.UserLoginComponent;
import app.adam.bossku.view.ui.SelectRegisterActivity;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginViewModel extends ViewModel {
    public UserLoginComponent component;
    private Loginlistener loginlistener;
    @SuppressLint("StaticFieldLeak")
    public static BasicLib lib;

    public LoginViewModel(){
        this.component = new UserLoginComponent("","");
    }
    public void doLogin(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        //headers.put("Authorization", "251423");
        headers.put("Accept", "application/json");

        Call<String> call = getResponse.request_login("user/login", component.getEmail(),component.getPassword());
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [login]", res_);

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);

                        String status = res.getString("status");
                        String message = res.getString("message");
                        if(status.equals("success")) {
                            JSONObject data = res.getJSONObject("data");
                            String token = data.getString("token");
                            String role = data.getString("role");

                            SessionManager sessionManager = new SessionManager(lib.getAct());
                            sessionManager.createSessionLogin("Bearer "+token, role);

                            loginlistener.onSuccessAuth(message);
                        }
                        else {
                            loginlistener.onFailAuth("something's wrong with API");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    loginlistener.onFailAuth("Fail connect to server");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [login]", t.toString());
                if(call.isCanceled()) {
                    loginlistener.onFailAuth("Request was aborted");
                }else {
                    loginlistener.onFailAuth("Unable to submit post to API.");
                }
            }
        });
//        RequestQueue queue = Volley.newRequestQueue(lib.getAct());
//        String url = Route.base_url+Route.login;
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("app-log",response);
//                        try {
//                            JSONObject res = new JSONObject(response);
//
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//                            if(status.equals("success")) {
//                                JSONObject data = res.getJSONObject("data");
//                                String token = data.getString("token");
//                                String role = data.getString("role");
//
//                                SessionManager sessionManager = new SessionManager(lib.getAct());
//                                sessionManager.createSessionLogin("Bearer "+token, role);
//
//                                loginlistener.onSuccessAuth(message);
//                            }
//                            else {
//                                loginlistener.onFailAuth(message);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        loginlistener.onFailAuth(Util.errorVolley(volleyError));
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("email", component.getEmail());
//                params.put("password", component.getPassword());
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }

    @BindingAdapter("app:create_account")
    public static void create_account(TextView view, String value) {
        lib.setTextView(view);
        lib.setFull_text(value);

        lib.setUnderline(true);
        lib.setColor(lib.getAct().getResources().getColor(R.color.colorPrimary));

        lib.setTarget("Buat akun");
        lib.addClick(new TextListener() {
            @Override
            public void onClick() {
                lib.getAct().startActivity(new Intent(lib.getAct(), SelectRegisterActivity.class));
            }
        });
        lib.implementLink();
    }

    public Loginlistener getLoginlistener() {
        return loginlistener;
    }

    public void setLoginlistener(Loginlistener loginlistener) {
        this.loginlistener = loginlistener;
    }

    public interface Loginlistener{
        void onErrorValidation(String message);
        void onFailAuth(String message);
        void onSuccessAuth(String message);
    }
}
