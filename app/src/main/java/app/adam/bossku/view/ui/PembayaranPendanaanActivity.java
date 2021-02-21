package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.Bank;
import app.adam.bossku.view.model.Pendanaan;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class PembayaranPendanaanActivity extends AppCompatActivity {
    String nominal = "0";
    Pendanaan pendanaan=null;
    Bank bank=null;

    EditText edt_nominal;
    Button btn_10k,btn_25k,btn_50k,btn_100k,btn_250k,btn_500k,btn_kirim;
    ImageButton btn_metode_pembayaran;
    TextView txt_metode_pembayaran;
    ImageView img_metode_pembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_pendanaan);

        edt_nominal = findViewById(R.id.edt_nominal_pembayaran_pendanaan);
        btn_10k = findViewById(R.id.btn_10k_pemayaran_pendanaan);
        btn_25k = findViewById(R.id.btn_25k_pemayaran_pendanaan);
        btn_50k = findViewById(R.id.btn_50k_pemayaran_pendanaan);
        btn_100k = findViewById(R.id.btn_100k_pemayaran_pendanaan);
        btn_250k = findViewById(R.id.btn_250k_pemayaran_pendanaan);
        btn_500k = findViewById(R.id.btn_500k_pemayaran_pendanaan);
        btn_kirim = findViewById(R.id.btn_donasi_pemayaran_pendanaan);
        btn_metode_pembayaran = findViewById(R.id.btn_metode_pembayaran_pembayaran_pendanaan);
        txt_metode_pembayaran = findViewById(R.id.txt_metode_pembayaran_pembayaran_pendanaan);
        img_metode_pembayaran = findViewById(R.id.img_metode_pembayaran_pembayaran_pendanaan);
        Toolbar mtoolbar = findViewById(R.id.toolbar_detail_pembayaran_pendanaan);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> PembayaranPendanaanActivity.super.onBackPressed());

        Bundle b = getIntent().getExtras();
        if(b != null) {
            bank = b.getParcelable("bank");
            pendanaan = b.getParcelable("pendanaan");
            nominal = b.getString("nominal","0");
            edt_nominal.setText(Util.rupiahFormat(Double.parseDouble(nominal)));
            enableButtonReq();
        }

        edt_nominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(nominal)) {
                    edt_nominal.removeTextChangedListener(this);
                    addNominal(edt_nominal,0f);
                    edt_nominal.addTextChangedListener(this);
                    enableButtonReq();
                }
            }
        });
        btn_10k.setOnClickListener(v -> {
            if (!edt_nominal.toString().equals(nominal)) {
                addNominal(edt_nominal,10000f);
                enableButtonReq();
            }
        });
        btn_25k.setOnClickListener(v -> {
            if (!edt_nominal.toString().equals(nominal)) {
                addNominal(edt_nominal,25000f);
                enableButtonReq();
            }
        });
        btn_50k.setOnClickListener(v -> {
            if (!edt_nominal.toString().equals(nominal)) {
                addNominal(edt_nominal,50000f);
                enableButtonReq();
            }
        });
        btn_100k.setOnClickListener(v -> {
            if (!edt_nominal.toString().equals(nominal)) {
                addNominal(edt_nominal,100000f);
                enableButtonReq();
            }
        });
        btn_250k.setOnClickListener(v -> {
            if (!edt_nominal.toString().equals(nominal)) {
                addNominal(edt_nominal,250000f);
                enableButtonReq();
            }
        });
        btn_500k.setOnClickListener(v -> {
            if (!edt_nominal.toString().equals(nominal)) {
                addNominal(edt_nominal,500000f);
                enableButtonReq();
            }
        });
        btn_kirim.setOnClickListener(v -> {
            if (normalizeNominal(edt_nominal)==0) {
                Toast.makeText(PembayaranPendanaanActivity.this, "Nominal harus lebih dari 0", Toast.LENGTH_SHORT).show();
            }
            else if(bank==null){
                Toast.makeText(PembayaranPendanaanActivity.this, "Wajib pilih metode pembayaran", Toast.LENGTH_SHORT).show();
            }
            else if(pendanaan==null){
                Toast.makeText(PembayaranPendanaanActivity.this, "Kesalahan sistem", Toast.LENGTH_SHORT).show();
            }
            else{
                request(edt_nominal,pendanaan,bank);
            }
        });
        btn_metode_pembayaran.setOnClickListener(v -> {
            Intent i = new Intent(PembayaranPendanaanActivity.this, PilihBankActivity.class);
            i.putExtra("activity", "PembayaranPendanaanActivity");
            i.putExtra("bank", bank);
            i.putExtra("nominal", String.valueOf(normalizeNominal(edt_nominal)));
            i.putExtra("pendanaan", pendanaan);
            startActivity(i);
        });
    }
    void request(final EditText edt_nominal, final Pendanaan pendanaan, final Bank bank){
        final SweetAlertDialog dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(PembayaranPendanaanActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_funding_make("user/funding/make",String.valueOf(bank.getId()),String.valueOf(pendanaan.getId()),String.valueOf((int) normalizeNominal(edt_nominal)),"1", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [pembayaran pendanaan]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        if(status.equals("success")){
                            Toast.makeText(PembayaranPendanaanActivity.this, message, Toast.LENGTH_SHORT).show();

                            startActivity(
                                    new Intent(PembayaranPendanaanActivity.this, PendanaanActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            );
                        }
                        else {
                            Toast.makeText(PembayaranPendanaanActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(PembayaranPendanaanActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [pembayaran pendanaan]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(PembayaranPendanaanActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PembayaranPendanaanActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                            Toast.makeText(PembayaranPendanaanActivity.this, message, Toast.LENGTH_SHORT).show();
//                            if(status.equals("success")){
//                                startActivity(
//                                        new Intent(PembayaranPendanaanActivity.this, PendanaanActivity.class)
//                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                );
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
//                        Toast.makeText(PembayaranPendanaanActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        )
//        {
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(PembayaranPendanaanActivity.this);
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
//                params.put("channel_id_or_code", String.valueOf(bank.getId()));
//                //Log.i("app-log",String.valueOf(bank.getId()));
//                params.put("id_crowd_funding", String.valueOf(pendanaan.getId()));
//                //Log.i("app-log",String.valueOf(pendanaan.getId()));
//                params.put("jumlah_donasi", String.valueOf((int) normalizeNominal(edt_nominal)));
//                //Log.i("app-log",String.valueOf((int) normalizeNominal(edt_nominal)));
//                params.put("payment_channel", "1");
//                return params;
//            }
//        };
//
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
    void addNominal(EditText edt_nominal,double add){
        double parsed = normalizeNominal(edt_nominal);
        String formatted = Util.rupiahFormat(parsed+add);
        nominal = formatted;
        edt_nominal.setText(formatted);
        edt_nominal.setSelection(formatted.length());
    }
    double normalizeNominal(EditText edt_nominal){
        String replaceable = String.format("[%s,.\\s]", "Rp ");
        String cleanString = edt_nominal.getText().toString().replaceAll(replaceable, "");

        double parsed;
        try {
            parsed = Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            parsed = 0.00;
        }
        return parsed;
    }
    void enableButtonReq(){
        if(bank!=null){
            txt_metode_pembayaran.setText(bank.getBank_name());
            img_metode_pembayaran.setImageResource(R.drawable.ic_check);
        }
        if(bank!=null && Double.parseDouble(String.valueOf(normalizeNominal(edt_nominal)))!=0 && pendanaan!=null){
            btn_kirim.setEnabled(true);
            btn_kirim.setAlpha(1.0f);
        }
    }
}