package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.adam.bossku.ApiConfig;
import app.adam.bossku.AppConfig;
import app.adam.bossku.R;
import app.adam.bossku.helper.InputFilterMinMax;
import app.adam.bossku.helper.PermissionHelper;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.helper.Util;
import app.adam.bossku.view.model.Kecamatan;
import app.adam.bossku.view.model.Kota;
import app.adam.bossku.view.model.Provinsi;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfilActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String[] jenis_kelamin = {"laki-laki", "perempuan"};
    TextInputEditText edt_nama,edt_telp,edt_tanggal_lahir,edt_provinsi,edt_kota,edt_kecamatan;
    AppCompatAutoCompleteTextView edt_jenis_kelamin;
    SweetAlertDialog dialog_loading;
    DatePickerDialog datePickerDialog;
    ImageView img_foto_pribadi,img_logo_umkm,img_ktp_umkm,img_ktm_umkm_mhs;
    TextInputEditText edt_nama_brand,edt_nomor_ktp,edt_nomor_ktm,edt_kampus,edt_jurusan,edt_semester;
    Button btn_simpan_data_profil,btn_simpan_umkm,btn_simpan_umkm_mhs;
    Toolbar mtoolbar;
    ConstraintLayout data_pribadi,umkm,umkm_mhs;

    String avatar=null;
    String name = "";
    String phone = "";
    String date_of_birth = "";
    int idx_jk=0;
    Kota kota = null;
    Provinsi provinsi=null;
    Kecamatan kecamatan=null;
    SessionManager session;
    HashMap<String, String> data;
    PermissionHelper permissionHelper;

    Uri logo=null;
    String brand_name="";
    String ktp_number="";
    Uri ktp=null;

    Uri ktm=null;
    String ktm_number="";
    String campuss="";
    String department="";
    String semester="";

    int openlayout = 1;
    int select_avatar = 0;
    int select_logo = 1;
    int select_ktp = 2;
    int select_ktm = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        data_pribadi = findViewById(R.id.cl_data_pribadi_profil);
        umkm = findViewById(R.id.cl_data_umkm_profil);
        umkm_mhs = findViewById(R.id.cl_data_umkm_mhs_profil);

        img_foto_pribadi = findViewById(R.id.img_foto_pribadi_profil);
        edt_nama = findViewById(R.id.edt_nama_lengkap_profil);
        edt_telp = findViewById(R.id.edt_telp_profil);
        edt_tanggal_lahir = findViewById(R.id.edt_tanggal_lahir_profil);
        edt_jenis_kelamin = findViewById(R.id.edt_jenis_kelamin_profil);
        edt_provinsi = findViewById(R.id.edt_provinsi_profil);
        edt_kota = findViewById(R.id.edt_kota_profil);
        edt_kecamatan = findViewById(R.id.edt_kecamatan_profil);
        btn_simpan_data_profil = findViewById(R.id.btn_simpan_profil);

        img_logo_umkm = findViewById(R.id.img_logo_umkm_profil);
        edt_nama_brand = findViewById(R.id.edt_nama_brand_umkm_profil);
        edt_nomor_ktp = findViewById(R.id.edt_nomor_ktp_umkm_profil);
        img_ktp_umkm = findViewById(R.id.img_ktp_umkm_profil);
        btn_simpan_umkm = findViewById(R.id.btn_simpan_umkm_profil);

        img_ktm_umkm_mhs = findViewById(R.id.img_ktm_umkm_mhs_profil);
        edt_nomor_ktm = findViewById(R.id.edt_nomor_ktm_umkm_mhs_profil);
        edt_kampus = findViewById(R.id.edt_kampus_umkm_mhs_profil);
        edt_jurusan = findViewById(R.id.edt_jurusan_umkm_mhs_profil);
        edt_semester = findViewById(R.id.edt_semester_umkm_mhs_profil);
        btn_simpan_umkm_mhs = findViewById(R.id.btn_simpan_umkm_mhs_profil);

        mtoolbar = findViewById(R.id.toolbar_profil);
        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> ProfilActivity.super.onBackPressed());

        Util.checkAndRequestPermissions(this);

        session = new SessionManager(this);
        data = session.getSessionLogin();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, jenis_kelamin);
        edt_jenis_kelamin.setThreshold(1);
        edt_jenis_kelamin.setAdapter(adapter);
        edt_jenis_kelamin.setOnItemClickListener((parent, view, position, id) -> idx_jk = position);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            avatar = b.getString("avatar");
            Log.i("app-log [uri intent]",(avatar==null? "null": avatar));
            if(avatar!=null)
                Glide.with(this).load(new File(avatar)).into(img_foto_pribadi);

            name = b.getString("nama");
            phone = b.getString("notelp");
            date_of_birth = b.getString("tanggal_lahir");
            idx_jk = b.getInt("jenis_kelamin",0);
            provinsi = b.getParcelable("provinsi");
            kota = b.getParcelable("kota");
            kecamatan = b.getParcelable("kecamatan");
            openlayout = b.getInt("openlayout",1);

            edt_nama.setText(!Util.isNotNullOrEmpty(name)? "":name);
            edt_telp.setText(!Util.isNotNullOrEmpty(phone)? "":phone);
            edt_tanggal_lahir.setText(!Util.isNotNullOrEmpty(date_of_birth)? "":date_of_birth);
            edt_jenis_kelamin.setText(jenis_kelamin[idx_jk]);
            edt_kota.setText(kota==null? "":kota.getName());
            edt_kecamatan.setText(kecamatan==null? "":kecamatan.getName());
            edt_provinsi.setText(provinsi==null? "":provinsi.getName());
        }
        else {
            load_profile();
        }

        TabLayout tabLayout = findViewById(R.id.tl_profil);
        tabLayout.setVisibility((data.get(SessionManager.KEY_ROLE).equals("3")||data.get(SessionManager.KEY_ROLE).equals("5"))? View.VISIBLE:View.GONE);
        openLayout(openlayout);
        Log.i("app-log","layout=1");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    openlayout=1;
                    openLayout(openlayout);
                    load_profile();
                    Log.i("app-log","layout=1");
                }
                else{
                    if(data.get(SessionManager.KEY_ROLE).equals("5")) {
                        openlayout=2;
                        openLayout(openlayout);
                        load_data_umkm();
                        Log.i("app-log","layout=2");
                    }
                    else {
                        openlayout=3;
                        openLayout(openlayout);
                        load_data_umkm_mhs();
                        Log.i("app-log","layout=3");
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        edt_semester.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "8")});

        edt_provinsi.setOnClickListener(v -> redirect(true,false,false));
        edt_kota.setOnClickListener(v -> {
            if(provinsi!=null) {
                redirect(false, true, false);
            }
            else{
                Toast.makeText(ProfilActivity.this, "Pilih provinsi terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
        edt_kecamatan.setOnClickListener(v -> {
            if(kota!=null){
                redirect(false,false,true);
            }
            else{
                Toast.makeText(ProfilActivity.this, "Pilih kota terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
        edt_tanggal_lahir.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            datePickerDialog = DatePickerDialog.newInstance(ProfilActivity.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setTitle("Pilih Tanggal");
            datePickerDialog.show(getSupportFragmentManager(),"select date");
        });
        edt_nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name=s.toString();
            }
        });
        edt_telp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone=s.toString();
            }
        });
        edt_nama_brand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                brand_name=s.toString();
            }
        });
        edt_nomor_ktp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ktp_number=s.toString();
            }
        });
        edt_nomor_ktm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ktm_number=s.toString();
            }
        });
        edt_kampus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                campuss=s.toString();
            }
        });
        edt_jurusan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                department=s.toString();
            }
        });
        edt_semester.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                semester=s.toString();
            }
        });

        img_foto_pribadi.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, select_avatar);
        });

        img_logo_umkm.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, select_logo);
        });
        img_ktp_umkm.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, select_ktp);
        });

        img_ktm_umkm_mhs.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, select_ktm);
        });

        btn_simpan_data_profil.setOnClickListener(v -> {
            if(!Util.isNotNullOrEmpty(String.valueOf(avatar))){
                Toast.makeText(ProfilActivity.this, "Foto pribadi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(idx_jk<0){
                Toast.makeText(ProfilActivity.this, "Jenis kelamin wajib pilih", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(date_of_birth)){
                Toast.makeText(ProfilActivity.this, "Tanggal lahir tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(provinsi==null){
                Toast.makeText(ProfilActivity.this, "Provinsi wajib pilih", Toast.LENGTH_SHORT).show();
            }
            else if(kota==null){
                Toast.makeText(ProfilActivity.this, "Kota wajib pilih", Toast.LENGTH_SHORT).show();
            }
            else if(kecamatan==null){
                Toast.makeText(ProfilActivity.this, "Kecamatan wajib pilih", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(ProfilActivity.this, "request data pribadi", Toast.LENGTH_SHORT).show();
                request_data_pribadi(avatar,jenis_kelamin[idx_jk],date_of_birth,provinsi.getId(),kota.getId(),kecamatan.getId());
            }
        });
        btn_simpan_umkm.setOnClickListener(v -> {
            if(!Util.isNotNullOrEmpty(brand_name)){
                Toast.makeText(ProfilActivity.this, "Nama brand tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(ktp_number)){
                Toast.makeText(ProfilActivity.this, "Nomor ktp tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(String.valueOf(logo))){
                Toast.makeText(ProfilActivity.this, "Foto logo tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(String.valueOf(ktp))){
                Toast.makeText(ProfilActivity.this, "Foto ktp tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(ProfilActivity.this, "request umkm", Toast.LENGTH_SHORT).show();
                request_data_umkm(logo,brand_name,ktp_number,ktp);
            }
        });
        btn_simpan_umkm_mhs.setOnClickListener(v -> {
            if(!Util.isNotNullOrEmpty(ktm_number)){
                Toast.makeText(ProfilActivity.this, "Nomot ktm tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(campuss)){
                Toast.makeText(ProfilActivity.this, "Kampus tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(department)){
                Toast.makeText(ProfilActivity.this, "Jurusan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(semester)){
                Toast.makeText(ProfilActivity.this, "Semester tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if(!Util.isNotNullOrEmpty(String.valueOf(ktm))){
                Toast.makeText(ProfilActivity.this, "Foto ktm tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(ProfilActivity.this, "request umkm mhs", Toast.LENGTH_SHORT).show();
                request_data_umkm_mhs(ktm_number,campuss,department,semester,ktm);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("app-log", "requestCode " + requestCode + ", resultCode " + resultCode);

        try {
//            if (resultCode == Activity.RESULT_OK) {
//                final Bitmap bitmap;
//                Uri source = data.getData();
//                InputStream input = this.getContentResolver().openInputStream(source);
//                bitmap = BitmapFactory.decodeStream(input, null, null);
//            }
            if (resultCode == RESULT_OK && data!=null) {
                Uri selectedImage = data.getData();
                InputStream iStream = getContentResolver().openInputStream(selectedImage);
                byte[] inputData = Util.getBytes(iStream);

                if(requestCode == select_avatar) {
                    Log.i("app-log uri0",selectedImage.toString());

                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Bossku");
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                        }
                    }

                    String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                    File file_img_copy = new File(mediaStorageDir.getPath(), timeStamp + ".jpg");
                    OutputStream op = new FileOutputStream(file_img_copy);
                    op.write(inputData);
                    Log.i("app-log path:", file_img_copy.getPath());

                    avatar = file_img_copy.getPath();
//                    avatar = getPathFromUri(selectedImage);
                    Glide.with(this).load(selectedImage).into(img_foto_pribadi);
                }
                else if(requestCode == select_logo) {
                    Log.i("app-log uri1",selectedImage.toString());
                    logo = selectedImage;
                    Glide.with(this).load(selectedImage).into(img_logo_umkm);
                }
                else if(requestCode == select_ktp) {
                    Log.i("app-log uri2",selectedImage.toString());
                    ktp = selectedImage;
                    Glide.with(this).load(selectedImage).into(img_ktp_umkm);
                }
                else{
                    Log.i("app-log uri3",selectedImage.toString());
                    ktm = selectedImage;
                    Glide.with(this).load(data.getData()).into(img_ktm_umkm_mhs);
                }
            }
            else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public String getPathFromUri(Uri selectedImage){
        if(selectedImage==null)
            return null;

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(columnIndex);
        cursor.close();

        return path;
    }
    void load_profile(){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_userprofile("user/userprofile", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [profil]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        //String message = res.getString("message");

                        if(status.equals("success")){
                            JSONObject data = res.getJSONObject("data");

                            if(!data.isNull("city")) {
                                JSONObject city = data.getJSONObject("city");
                                kota = new Kota(city.getString("city_id"), city.getString("city_name"), "");
                            }
                            if(!data.isNull("province")) {
                                JSONObject province = data.getJSONObject("province");
                                provinsi = new Provinsi(province.getString("province_id"), province.getString("province"));
                            }
                            if(!data.isNull("subdistrict")) {
                                JSONObject subdistrict = data.getJSONObject("subdistrict");
                                kecamatan = new Kecamatan(subdistrict.getString("subdistrict_id"), subdistrict.getString("subdistrict_name"), "");
                            }
                            name = data.getString("name");
                            phone = data.getString("phone");
                            date_of_birth = data.getString("date_of_birth");
                            idx_jk = (data.isNull("sex") || data.getString("sex").equals("laki-laki")) ? 0:1;
                            load_image_profile(data.isNull("pp")? "":data.getString("pp"));

                            edt_nama.setText(!data.isNull("name")? name:"");
                            edt_telp.setText(!data.isNull("phone")? phone:"");
                            edt_tanggal_lahir.setText(!data.isNull("date_of_birth")? date_of_birth:"");

                            edt_jenis_kelamin.setText(jenis_kelamin[idx_jk]);
                            edt_jenis_kelamin.showDropDown();
                            edt_jenis_kelamin.setSelection(idx_jk);
                            //edt_jenis_kelamin.setListSelection(idx_jk);;
                            edt_jenis_kelamin.performCompletion();

                            edt_kota.setText(kota!=null? kota.getName():"");
                            edt_kecamatan.setText(kecamatan!=null? kecamatan.getName():"");
                            edt_provinsi.setText(provinsi!=null? provinsi.getName():"");
                        }
                        else{
                            Toast.makeText(ProfilActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            edt_nama.setText("");
                            edt_telp.setText("");
                            edt_tanggal_lahir.setText("");
                            edt_kota.setText("");
                            edt_kecamatan.setText("");
                            edt_provinsi.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(ProfilActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [profil]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(ProfilActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ProfilActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }

                edt_nama.setText("");
                edt_telp.setText("");
                edt_tanggal_lahir.setText("");
                //edt_jenis_kelamin.setSelection(0);
                edt_kota.setText("");
                edt_kecamatan.setText("");
                edt_provinsi.setText("");
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    Log.i("app-log",response);
//                    dialog_loading.dismissWithAnimation();
//
//                    try {
//                        JSONObject res = new JSONObject(response);
//                        String status = res.getString("status");
//                        //String message = res.getString("message");
//
//                        if(status.equals("success")){
//                            JSONObject data = res.getJSONObject("data");
//
//                            if(!data.isNull("city")) {
//                                JSONObject city = data.getJSONObject("city");
//                                kota = new Kota(city.getString("city_id"), city.getString("city_name"), "");
//                            }
//                            if(!data.isNull("province")) {
//                                JSONObject province = data.getJSONObject("province");
//                                provinsi = new Provinsi(province.getString("province_id"), province.getString("province"));
//                            }
//                            if(!data.isNull("subdistrict")) {
//                                JSONObject subdistrict = data.getJSONObject("subdistrict");
//                                kecamatan = new Kecamatan(subdistrict.getString("subdistrict_id"), subdistrict.getString("subdistrict_name"), "");
//                            }
//                            name = data.getString("name");
//                            phone = data.getString("phone");
//                            date_of_birth = data.getString("date_of_birth");
//                            idx_jk = (data.isNull("sex") || data.getString("sex").equals("laki-laki")) ? 0:1;
//                            load_image_profile(data.isNull("pp")? "":data.getString("pp"));
//
//                            edt_nama.setText(!data.isNull("name")? name:"");
//                            edt_telp.setText(!data.isNull("phone")? phone:"");
//                            edt_tanggal_lahir.setText(!data.isNull("date_of_birth")? date_of_birth:"");
//
//                            edt_jenis_kelamin.setText(jenis_kelamin[idx_jk]);
//                            edt_jenis_kelamin.showDropDown();
//                            edt_jenis_kelamin.setSelection(idx_jk);
//                            //edt_jenis_kelamin.setListSelection(idx_jk);;
//                            edt_jenis_kelamin.performCompletion();
//
//                            edt_kota.setText(kota!=null? kota.getName():"");
//                            edt_kecamatan.setText(kecamatan!=null? kecamatan.getName():"");
//                            edt_provinsi.setText(provinsi!=null? provinsi.getName():"");
//                        }
//                        else{
//                            Toast.makeText(ProfilActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                            edt_nama.setText("");
//                            edt_telp.setText("");
//                            edt_tanggal_lahir.setText("");
//                            edt_kota.setText("");
//                            edt_kecamatan.setText("");
//                            edt_provinsi.setText("");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                volleyError -> {
//                    dialog_loading.dismissWithAnimation();
//                    Toast.makeText(ProfilActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//
//                    edt_nama.setText("");
//                    edt_telp.setText("");
//                    edt_tanggal_lahir.setText("");
//                    edt_jenis_kelamin.setSelection(0);
//                    edt_kota.setText("");
//                    edt_kecamatan.setText("");
//                    edt_provinsi.setText("");
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(ProfilActivity.this);
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
    void load_data_umkm(){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_umkmprofile("user/umkmprofile", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [profil]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        //String message = res.getString("message");

                        if(status.equals("success")){
                            final JSONObject data = res.getJSONObject("data");
                            load_image_ktp();
                            load_image_logo(data.isNull("brand_logo")? "":data.getString("brand_logo"));
                            edt_nama_brand.setText(data.isNull("brand_name")? "":data.getString("brand_name"));
                            edt_nomor_ktp.setText(data.isNull("ktp_number")? "":data.getString("ktp_number"));
                        }
                        else{
                            Toast.makeText(ProfilActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            edt_nama_brand.setText("");
                            edt_nomor_ktp.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(ProfilActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [profil]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(ProfilActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ProfilActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }

                edt_nama_brand.setText("");
                edt_nomor_ktp.setText("");
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    Log.i("app-log",response);
//                    dialog_loading.dismissWithAnimation();
//
//                    try {
//                        JSONObject res = new JSONObject(response);
//                        String status = res.getString("status");
//                        //String message = res.getString("message");
//
//                        if(status.equals("success")){
//                            final JSONObject data = res.getJSONObject("data");
//                            load_image_ktp();
//                            load_image_logo(data.isNull("brand_logo")? "":data.getString("brand_logo"));
//                            edt_nama_brand.setText(data.isNull("brand_name")? "":data.getString("brand_name"));
//                            edt_nomor_ktp.setText(data.isNull("ktp_number")? "":data.getString("ktp_number"));
//                        }
//                        else{
//                            Toast.makeText(ProfilActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                            edt_nama_brand.setText("");
//                            edt_nomor_ktp.setText("");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                volleyError -> {
//                    dialog_loading.dismissWithAnimation();
//                    Toast.makeText(ProfilActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//
//                    edt_nama_brand.setText("");
//                    edt_nomor_ktp.setText("");
//                }
//        ){
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(ProfilActivity.this);
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
    void load_data_umkm_mhs(){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<String> call = getResponse.request_user_collegerprofile("user/collegerprofile", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res_ = response.body();
                Log.i("app-log [profil]", res_);
                dialog_loading.dismissWithAnimation();

                if(response.isSuccessful()) {
                    try {
                        assert res_ != null;
                        JSONObject res = new JSONObject(res_);
                        String status = res.getString("status");
                        //String message = res.getString("message");

                        if(status.equals("success")){
                            final JSONObject data = res.getJSONObject("data");
                            load_image_ktm();
                            edt_nomor_ktm.setText(data.isNull("ktm_number")? "":data.getString("ktm_number"));
                            edt_kampus.setText(data.isNull("campuss")? "":data.getString("campuss"));
                            edt_jurusan.setText(data.isNull("department")? "":data.getString("department"));
                            edt_semester.setText(data.isNull("semester")? "":data.getString("semester"));
                        }
                        else{
                            Toast.makeText(ProfilActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            edt_nomor_ktm.setText("");
                            edt_kampus.setText("");
                            edt_jurusan.setText("");
                            edt_semester.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(ProfilActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log [profil]", t.toString());
                dialog_loading.dismissWithAnimation();

                if(call.isCanceled()) {
                    Toast.makeText(ProfilActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ProfilActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }

                edt_nomor_ktm.setText("");
                edt_kampus.setText("");
                edt_jurusan.setText("");
                edt_semester.setText("");
            }
        });

//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    Log.i("app-log",response);
//                    dialog_loading.dismissWithAnimation();
//
//                    try {
//                        JSONObject res = new JSONObject(response);
//                        String status = res.getString("status");
//                        //String message = res.getString("message");
//
//                        if(status.equals("success")){
//                            final JSONObject data = res.getJSONObject("data");
//                            load_image_ktm();
//                            edt_nomor_ktm.setText(data.isNull("ktm_number")? "":data.getString("ktm_number"));
//                            edt_kampus.setText(data.isNull("campuss")? "":data.getString("campuss"));
//                            edt_jurusan.setText(data.isNull("department")? "":data.getString("department"));
//                            edt_semester.setText(data.isNull("semester")? "":data.getString("semester"));
//                        }
//                        else{
//                            Toast.makeText(ProfilActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                            edt_nomor_ktm.setText("");
//                            edt_kampus.setText("");
//                            edt_jurusan.setText("");
//                            edt_semester.setText("");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                volleyError -> {
//                    dialog_loading.dismissWithAnimation();
//                    Toast.makeText(ProfilActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//
//                    edt_nomor_ktm.setText("");
//                    edt_kampus.setText("");
//                    edt_jurusan.setText("");
//                    edt_semester.setText("");
//                }
//        ){
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(ProfilActivity.this);
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

    void redirect(final boolean pilih_prov, final boolean pilih_kota, final boolean pilih_kec){
        startActivity(
                new Intent(this, PilihProvinsiKotaKecamatanActivity.class)
                        .putExtra("avatar",avatar)
                        .putExtra("nama",name)
                        .putExtra("notelp",phone)
                        .putExtra("tanggal_lahir",date_of_birth)
                        .putExtra("jenis_kelamin",idx_jk)
                        .putExtra("provinsi",provinsi)
                        .putExtra("kota",kota)
                        .putExtra("kecamatan",kecamatan)

                        .putExtra("pilih_prov",pilih_prov)
                        .putExtra("pilih_kota",pilih_kota)
                        .putExtra("pilih_kec",pilih_kec)
                        .putExtra("activity","ProfilActivity")
                        .putExtra("openlayout",openlayout)
        );
    }

    void request_data_pribadi(String avatar, final String sex, final String date_of_birth, final String province_id, final String city_id, final String subdistrict_id){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

//        RequestBody Rsex = RequestBody.create(MediaType.parse("multipart/form-data"), sex);
//        RequestBody Rdate_of_birth = RequestBody.create(MediaType.parse("multipart/form-data"), date_of_birth);
//        RequestBody Rprovince_id = RequestBody.create(MediaType.parse("multipart/form-data"), province_id);
//        RequestBody Rcity_id = RequestBody.create(MediaType.parse("multipart/form-data"), city_id);
//        RequestBody Rsubdistrict_id = RequestBody.create(MediaType.parse("multipart/form-data"), subdistrict_id);
//
//        File file_avatar = new File(avatar);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file_avatar);
//        MultipartBody.Part file = MultipartBody.Part.createFormData("avatar", file_avatar.getName(), requestFile);

        Bitmap bm = BitmapFactory.decodeFile(avatar);
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
        String file_avatar = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);

        Log.i("app-log",sex);
        Log.i("app-log",date_of_birth);
        Log.i("app-log",province_id);
        Log.i("app-log",city_id);
        Log.i("app-log",subdistrict_id);
        Log.i("app-log",file_avatar);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//        Call<String> call = getResponse.request_user_edit(
//                "user/edit",
//                headers,
//                file,
//                Rsex,
//                Rdate_of_birth,
//                Rprovince_id,
//                Rcity_id,
//                Rsubdistrict_id);
        Call<String> call = getResponse.request_user_edit64(
                "user/edit64",
                headers,
                file_avatar,
                sex,
                date_of_birth,
                province_id,
                city_id,
                subdistrict_id);

        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res = response.body();
//                Log.i("app-log",res);

                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "status:200, data:"+res, Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.i("app-log", call.toString()+" - "+response.toString());
                    Toast.makeText(getApplicationContext(), "status:!200, data:"+res, Toast.LENGTH_SHORT).show();
                }
                dialog_loading.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log",t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(getApplicationContext(),"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Unable to submit post to API."+t.getMessage(),Toast.LENGTH_LONG).show();
                }
                dialog_loading.dismissWithAnimation();
            }
        });
    }
    void request_data_umkm(Uri logo, final String brand_name, final String ktp_number, Uri ktp){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        RequestBody Rbrand_name = RequestBody.create(MediaType.parse("multipart/form-data"), brand_name);
        RequestBody Rktp_number = RequestBody.create(MediaType.parse("multipart/form-data"), ktp_number);

        Log.i("app-log", "[uri] "+logo);
        File file_logo = new File(getPathFromUri(logo));
        RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file_logo);
        MultipartBody.Part file1 = MultipartBody.Part.createFormData("logo", file_logo.getName(), requestFile1);

        Log.i("app-log", "[uri] "+ktp);
        File file_ktp = new File(getPathFromUri(ktp));
        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file_ktp);
        MultipartBody.Part file2 = MultipartBody.Part.createFormData("ktp", file_ktp.getName(), requestFile2);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<String> call = getResponse.request_umkm_edit(
                "user/umkm/edit",
                headers,
                Rbrand_name,
                Rktp_number,
                file1,
                file2);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res = response.body();
                Log.i("app-log",res);

                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "status:200, data:"+res, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "status:!200, data:"+res, Toast.LENGTH_SHORT).show();
                }
                dialog_loading.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log",t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(getApplicationContext(),"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Unable to submit post to API."+t.getMessage(),Toast.LENGTH_LONG).show();
                }
                dialog_loading.dismissWithAnimation();
            }
        });
    }
    void request_data_umkm_mhs(final String ktm_number, final String campuss, final String department, final String semester, Uri ktm){
        dialog_loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog_loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog_loading.setTitleText("Loading");
        dialog_loading.setCancelable(false);
        dialog_loading.show();

        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Log.i("app-log", "[uri] "+ktm);
        Log.i("app-log", "[token] "+data.get(SessionManager.KEY_TOKEN));
        File file_avatar = new File(getPathFromUri(ktm));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file_avatar);
        MultipartBody.Part file = MultipartBody.Part.createFormData("ktm", file_avatar.getName(), requestFile);

        RequestBody Rktm_number = RequestBody.create(MediaType.parse("multipart/form-data"), ktm_number);
        RequestBody Rcampuss = RequestBody.create(MediaType.parse("multipart/form-data"), campuss);
        RequestBody Rdepartment = RequestBody.create(MediaType.parse("multipart/form-data"), department);
        RequestBody Rsemester = RequestBody.create(MediaType.parse("multipart/form-data"), semester);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<String> call = getResponse.request_umkm_colleger_update(
                "user/colleger/edit",
                headers,
                Rktm_number,
                Rcampuss,
                Rdepartment,
                Rsemester,
                file);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String res = response.body();
                Log.i("app-log",res);

                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "status:200, data:"+res, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "status:!200, data:"+res, Toast.LENGTH_SHORT).show();
                }
                dialog_loading.dismissWithAnimation();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("app-log",t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(getApplicationContext(),"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Unable to submit post to API."+t.getMessage(),Toast.LENGTH_LONG).show();
                }
                dialog_loading.dismissWithAnimation();
            }
        });
    }

    void load_image_ktp(){
        Log.i("app-log","start load img ktp");
        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<ResponseBody> call = getResponse.request_user_umkm_ktpimage("user/umkm/ktpimage", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        img_ktp_umkm.setImageBitmap(bmp);
                    }
                }
                else{
                    Toast.makeText(ProfilActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("app-log [profil]", t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(ProfilActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ProfilActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    void load_image_ktm(){
        Log.i("app-log","start load img ktm");
        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<ResponseBody> call = getResponse.request_user_umkm_ktmimage("user/umkm/ktmimage", headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        img_ktm_umkm_mhs.setImageBitmap(bmp);
                    }
                }
                else{
                    Toast.makeText(ProfilActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("app-log [profil]", t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(ProfilActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ProfilActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        ImageRequest imageRequest = new ImageRequest(
//                Route.base_url+Route.ktm_image,
//                response -> img_ktm_umkm_mhs.setImageBitmap(response),
//                0,
//                0,
//                ImageView.ScaleType.FIT_XY,
//                Bitmap.Config.RGB_565,
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(ProfilActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(ProfilActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//        };
//        requestQueue.add(imageRequest);
    }
    void load_image_profile(String image){
        Log.i("app-log","start load img profil");
        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<ResponseBody> call = getResponse.request_user_avatar("https://server.bossku.id/storage/"+image, headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        img_foto_pribadi.setImageBitmap(bmp);
                    }
                }
                else{
                    Toast.makeText(ProfilActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("app-log [profil]", t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(ProfilActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ProfilActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        ImageRequest imageRequest = new ImageRequest(
//                Route.storage+image,
//                response -> img_foto_pribadi.setImageBitmap(response),
//                0,
//                0,
//                ImageView.ScaleType.FIT_XY,
//                Bitmap.Config.RGB_565,
//                volleyError -> Toast.makeText(ProfilActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show()
//        ) {
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(ProfilActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//        };
//        requestQueue.add(imageRequest);
    }
    void load_image_logo(String image){
        Log.i("app-log","start load img logo");
        SessionManager session = new SessionManager(ProfilActivity.this);
        HashMap<String, String> data = session.getSessionLogin();

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Map<String, String>  headers = new HashMap<>();
        headers.put("Authorization", data.get(SessionManager.KEY_TOKEN));

        Call<ResponseBody> call = getResponse.request_user_logo("https://server.bossku.id/storage/"+image, headers);
        Log.i("app-log", "request to "+call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        img_logo_umkm.setImageBitmap(bmp);
                    }
                }
                else{
                    Toast.makeText(ProfilActivity.this,"Fail connect to server",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("app-log [profil]", t.toString());
                if(call.isCanceled()) {
                    Toast.makeText(ProfilActivity.this,"request was aborted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ProfilActivity.this,"Unable to submit post to API.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        ImageRequest imageRequest = new ImageRequest(
//                Route.storage+image,
//                response -> img_logo_umkm.setImageBitmap(response),
//                0,
//                0,
//                ImageView.ScaleType.FIT_XY,
//                Bitmap.Config.RGB_565,
//                volleyError -> Toast.makeText(ProfilActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show()
//        ) {
//            public Map<String, String> getHeaders() {
//                SessionManager session = new SessionManager(ProfilActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//        };
//        requestQueue.add(imageRequest);
    }

    void openLayout(int layout){
        if(layout==1){
            data_pribadi.setVisibility(View.VISIBLE);
            umkm.setVisibility(View.GONE);
            umkm_mhs.setVisibility(View.GONE);
        }
        else if(layout==2){
            data_pribadi.setVisibility(View.GONE);
            umkm.setVisibility(View.VISIBLE);
            umkm_mhs.setVisibility(View.GONE);
        }
        else{
            data_pribadi.setVisibility(View.GONE);
            umkm.setVisibility(View.GONE);
            umkm_mhs.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date_of_birth = String.format("%s-%02d-%02d",year,monthOfYear+1,dayOfMonth);
        edt_tanggal_lahir.setText(date_of_birth);
    }
}