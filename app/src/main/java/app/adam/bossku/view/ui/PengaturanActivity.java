package app.adam.bossku.view.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import app.adam.bossku.R;
import app.adam.bossku.helper.SessionManager;
import app.adam.bossku.view.adapter.PengaturanAdapter;
import app.adam.bossku.view.model.Pengaturan;

public class PengaturanActivity extends AppCompatActivity implements PengaturanAdapter.PengaturanListener {
    RecyclerView rv_list;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);

        SessionManager session = new SessionManager(this);
        HashMap<String, String> data = session.getSessionLogin();

        rv_list = findViewById(R.id.rv_list_pengaturan);
        Toolbar mtoolbar = findViewById(R.id.toolbar_pengaturan);

        mtoolbar.setTitleTextColor(Color.WHITE);
        mtoolbar.setSubtitleTextColor(Color.WHITE);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationOnClickListener(view -> PengaturanActivity.super.onBackPressed());

        rv_list.setHasFixedSize(true);

        ArrayList<Pengaturan> list_item = new ArrayList<>();
        list_item.add(new Pengaturan(1,getResources().getDrawable(R.drawable.ic_profil),"Profil Saya","lengkapi data pribadi agar mudah dikenali oleh kami"));
        list_item.add(new Pengaturan(2,getResources().getDrawable(R.drawable.ic_akun),"Akun Saya","ubah email dan password"));
        list_item.add(new Pengaturan(3,getResources().getDrawable(R.drawable.ic_akun),"Alamat Saya","atur alamat saya saat ini"));
        list_item.add(new Pengaturan(4, getResources().getDrawable(R.drawable.ic_pendanaan), "Pendanaan", "lihat dan buka suntikan dana usaha"));
        if(data.get(SessionManager.KEY_ROLE).equals("3") || data.get(SessionManager.KEY_ROLE).equals("5")) {
            list_item.add(new Pengaturan(5, getResources().getDrawable(R.drawable.ic_toko), "Toko Saya", "lihat produk terjual toko saya"));
            list_item.add(new Pengaturan(6, getResources().getDrawable(R.drawable.ic_toko), "Bank Saya", "lihat produk terjual toko saya"));
        }
        list_item.add(new Pengaturan(7,getResources().getDrawable(R.drawable.ic_kontak_kami),"Kontak Kami","hubungi kami jika terjadi kendala"));
        list_item.add(new Pengaturan(8,getResources().getDrawable(R.drawable.ic_keluar),"Keluar Aplikasi","saya ingin tutup aplikasi"));

        rv_list.setLayoutManager(new LinearLayoutManager(this));
        PengaturanAdapter cardAdapter = new PengaturanAdapter(this,this);
        cardAdapter.setListItem(list_item);
        cardAdapter.notifyDataSetChanged();
        rv_list.setAdapter(cardAdapter);
    }

    @Override
    public void SelectPengaturan(Pengaturan item) {
        if(item.getId()==1){
            startActivity(new Intent(this, ProfilActivity.class));
        }
        else if(item.getId()==2){
            startActivity(new Intent(this, AkunActivity.class));
        }
        else if(item.getId()==3){
            startActivity(new Intent(this, PilihAlamatActivity.class));
        }
        else if(item.getId()==4){
            startActivity(new Intent(this, PendanaanActivity.class));
        }
        else if(item.getId()==5){
            startActivity(new Intent(this, TokoSayaActivity.class));
        }
        else if(item.getId()==6){
            startActivity(new Intent(this, BankSayaActivity.class));
        }
        else if(item.getId()==8){
            finishAffinity();
        }
        else {
            Toast.makeText(this, item.getJudul(), Toast.LENGTH_SHORT).show();
        }
    }
}