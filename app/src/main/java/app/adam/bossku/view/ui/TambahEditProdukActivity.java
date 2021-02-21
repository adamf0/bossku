package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.InputFilterMinMax;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.ProductToko;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class TambahEditProdukActivity extends AppCompatActivity {
    Toolbar mtoolbar;
    TextInputEditText edt_nama_produk,edt_kategori_produk,edt_harga_produk,edt_jumlah_produk,edt_berat_produk;
    Editor edt_deskripsi;
    Button btn_simpan;
    SweetAlertDialog dialog_loading;

    String id_produk="";
    String nama_produk="";
    String kategori_produk="";
    String harga_produk="0";
    String jumlah_produk="";
    String berat_produk="";
    String deskripsi_produk="";
    boolean isUpdate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_edit_produk);

        edt_nama_produk = findViewById(R.id.edt_nama_produk_tambah_edit_produk);
        edt_kategori_produk = findViewById(R.id.edt_kategori_produk_tambah_edit_produk);
        edt_harga_produk = findViewById(R.id.edt_harga_produk_tambah_edit_produk);
        edt_jumlah_produk = findViewById(R.id.edt_jumlah_produk_tambah_edit_produk);
        edt_berat_produk = findViewById(R.id.edt_berat_produk_tambah_edit_produk);
        edt_deskripsi = findViewById(R.id.edt_deskripsi_produk_tambah_edit_produk);
        btn_simpan = findViewById(R.id.btn_simpan_tambah_edit_produk);
        mtoolbar = findViewById(R.id.toolbar_tambah_edit_produk);

        mtoolbar.setTitle("Tambah Produk");
        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> TambahEditProdukActivity.super.onBackPressed());

        edt_jumlah_produk.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "999")});
        edt_berat_produk.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "999")});

        findViewById(R.id.action_h1).setOnClickListener(v -> edt_deskripsi.updateTextStyle(EditorTextStyle.H1));

        findViewById(R.id.action_h2).setOnClickListener(v -> edt_deskripsi.updateTextStyle(EditorTextStyle.H2));

        findViewById(R.id.action_h3).setOnClickListener(v -> edt_deskripsi.updateTextStyle(EditorTextStyle.H3));

        findViewById(R.id.action_bold).setOnClickListener(v -> edt_deskripsi.updateTextStyle(EditorTextStyle.BOLD));

        findViewById(R.id.action_Italic).setOnClickListener(v -> edt_deskripsi.updateTextStyle(EditorTextStyle.ITALIC));

        findViewById(R.id.action_indent).setOnClickListener(v -> edt_deskripsi.updateTextStyle(EditorTextStyle.INDENT));

        findViewById(R.id.action_outdent).setOnClickListener(v -> edt_deskripsi.updateTextStyle(EditorTextStyle.OUTDENT));

        findViewById(R.id.action_bulleted).setOnClickListener(v -> edt_deskripsi.insertList(false));

        findViewById(R.id.action_unordered_numbered).setOnClickListener(v -> edt_deskripsi.insertList(true));

        findViewById(R.id.action_hr).setVisibility(View.GONE);
        findViewById(R.id.action_insert_image).setVisibility(View.GONE);
        findViewById(R.id.action_color).setVisibility(View.GONE);

        findViewById(R.id.action_insert_link).setOnClickListener(v -> edt_deskripsi.insertLink());

        findViewById(R.id.action_erase).setOnClickListener(v -> edt_deskripsi.clearAllContents());

        findViewById(R.id.action_blockquote).setOnClickListener(v -> edt_deskripsi.updateTextStyle(EditorTextStyle.BLOCKQUOTE));
        edt_deskripsi.render();

        Bundle b = getIntent().getExtras();
        if(b != null && b.getParcelable("produk")!=null) {
            ProductToko produk = b.getParcelable("produk");
            nama_produk=produk.getNama_produk();
            kategori_produk="";
            harga_produk= String.valueOf(produk.getHarga_produk());
            jumlah_produk= String.valueOf(produk.getJumlah_produk());
            berat_produk= String.valueOf(produk.getBerat_produk());
            id_produk= String.valueOf(produk.getId_produk());
            deskripsi_produk= produk.getDeskripsi_produk();
            isUpdate= b.getBoolean("isUpdate",false);

            mtoolbar.setTitle(isUpdate? "Ubah Produk":"Tambah Produk");
            edt_nama_produk.setText(nama_produk);
            edt_kategori_produk.setText(kategori_produk);
            edt_harga_produk.setText(Util.rupiahFormat(Double.parseDouble(harga_produk)));
            edt_jumlah_produk.setText(jumlah_produk);
            edt_berat_produk.setText(berat_produk);
            edt_deskripsi.getContentDeserialized(deskripsi_produk);
        }

        edt_nama_produk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nama_produk=s.toString();
            }
        });
        edt_kategori_produk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                kategori_produk=s.toString();
            }
        });
        edt_harga_produk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(harga_produk)) {
                    edt_harga_produk.removeTextChangedListener(this);
                    addNominal(edt_harga_produk,0f);
                    edt_harga_produk.addTextChangedListener(this);
                }
            }
        });
        edt_jumlah_produk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                jumlah_produk=s.toString();
            }
        });
        edt_berat_produk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                berat_produk=s.toString();
            }
        });
        edt_deskripsi.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                deskripsi_produk = editText.getText().toString();
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {

            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }
        });
        btn_simpan.setOnClickListener(v -> {
            if(!Util.isNotNullOrEmpty(nama_produk)){
                Toast.makeText(TambahEditProdukActivity.this, "Nama produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(kategori_produk)){
                Toast.makeText(TambahEditProdukActivity.this, "Kategori produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            if (normalizeNominal(edt_harga_produk)==0) {
                Toast.makeText(TambahEditProdukActivity.this, "Harga harus lebih dari 0", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(jumlah_produk)){
                Toast.makeText(TambahEditProdukActivity.this, "Jumlah produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(berat_produk)){
                Toast.makeText(TambahEditProdukActivity.this, "Berat produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(edt_deskripsi.getContentAsHTML())){
                Toast.makeText(TambahEditProdukActivity.this, "Deskripsi produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else{
                deskripsi_produk.replaceAll("\"","&quote;");
                request(id_produk,nama_produk,kategori_produk,harga_produk,jumlah_produk,berat_produk,edt_deskripsi.getContentAsHTML(),isUpdate);
            }
        });
    }

    void request(final String id, final String nama_produk, final String kategori_produk, final String harga_produk, final String jumlah_produk, final String berat_produk, final String deskripsi_produk, final boolean isUpdate){
        Log.i("app-log",String.format("%s - %s - %s - %s - %s - %s",nama_produk,kategori_produk,harga_produk,jumlah_produk,berat_produk,deskripsi_produk));

        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(TambahEditProdukActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call;
        if(!isUpdate)
            call = getResponse.request_user_goods_add("user/goods/add",nama_produk,kategori_produk,harga_produk,jumlah_produk,berat_produk,deskripsi_produk, headers);
        else
            call = getResponse.request_user_goods_edit("user/goods/edit",nama_produk,kategori_produk,harga_produk,jumlah_produk,berat_produk,deskripsi_produk,id, headers);

        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [tambah edit produk]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        String message = res.getString("message");

                        Toast.makeText(TambahEditProdukActivity.this,message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(TambahEditProdukActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [keranjang]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(TambahEditProdukActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(TambahEditProdukActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
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
//                            Toast.makeText(TambahEditProdukActivity.this,message, Toast.LENGTH_SHORT).show();
//                            //req_upload_image
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        dialog_loading.dismissWithAnimation();
//                        Toast.makeText(TambahEditProdukActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(TambahEditProdukActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String>  params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//
//            protected Map<String, String> getParams()
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("name", nama_produk);
//                params.put("category", kategori_produk);
//                params.put("price", harga_produk);
//                params.put("stock", jumlah_produk);
//                params.put("weight", berat_produk);
//                params.put("description", deskripsi_produk);
//                if(isUpdate)
//                    params.put("id", id);
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
    void request_image(){

    }
    void addNominal(EditText edt_harga_produk, double add){
        double parsed = normalizeNominal(edt_harga_produk);
        String formatted = Util.rupiahFormat(parsed+add);
        harga_produk = formatted;
        edt_harga_produk.setText(formatted);
        edt_harga_produk.setSelection(formatted.length());
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
}