package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.adam.bossku.BR;
import app.adam.bossku.helper.Util;

public class Product extends BaseObservable implements Parcelable {
    private String id_produk;
    private String nama_produk;
    private String gambar_produk;
    private String deskripsi_produk;
    private int jumlah_produk;
    private String berat_produk;
    private String nama_toko;
    private String daerah_toko;
    private int harga_produk;

    Product(){

    }
    public Product(String id_produk, String nama_produk, String gambar_produk, String deskripsi_produk, int jumlah_produk, String berat_produk, String nama_toko, String daerah_toko, int harga_produk){
        setId_produk(id_produk);
        setNama_produk(nama_produk);
        setGambar_produk(gambar_produk);
        setDeskripsi_produk(deskripsi_produk);
        setJumlah_produk(jumlah_produk);
        setBerat_produk(berat_produk);
        setNama_toko(nama_toko);
        setDaerah_toko(daerah_toko);
        setHarga_produk(harga_produk);
    }

    protected Product(Parcel in) {
        id_produk = in.readString();
        nama_produk = in.readString();
        gambar_produk = in.readString();
        deskripsi_produk = in.readString();
        jumlah_produk = in.readInt();
        berat_produk = in.readString();
        nama_toko = in.readString();
        harga_produk = in.readInt();
        daerah_toko = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Bindable
    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
        notifyPropertyChanged(BR.id_produk);
    }

    @Bindable
    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
        notifyPropertyChanged(BR.nama_produk);
    }

    @Bindable
    public String getGambar_produk() {
        return gambar_produk;
    }

    public void setGambar_produk(String gambar_produk) {
        this.gambar_produk = gambar_produk;
        notifyPropertyChanged(BR.gambar_produk);
    }

    @Bindable
    public String getDeskripsi_produk() {
        return deskripsi_produk;
    }

    public void setDeskripsi_produk(String deskripsi_produk) {
        this.deskripsi_produk = deskripsi_produk;
        notifyPropertyChanged(BR.deskripsi_produk);
    }

    @Bindable
    public int getJumlah_produk() {
        return jumlah_produk;
    }

    public void setJumlah_produk(int jumlah_produk) {
        this.jumlah_produk = jumlah_produk;
        notifyPropertyChanged(BR.jumlah_produk);
    }

    @Bindable
    public String getBerat_produk() {
        return berat_produk;
    }

    public void setBerat_produk(String berat_produk) {
        this.berat_produk = berat_produk;
        notifyPropertyChanged(BR.berat_produk);
    }

    @Bindable
    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
        notifyPropertyChanged(BR.nama_toko);
    }

    @Bindable
    public int getHarga_produk() {
        return harga_produk;
    }

    @Bindable
    public String getHarga_produk_rp() {
        return Util.rupiahFormat(harga_produk);
    }

    public void setHarga_produk(int harga_produk) {
        this.harga_produk = harga_produk;
        notifyPropertyChanged(BR.harga_produk);
    }

    @Bindable
    public String getDaerah_toko() {
        return daerah_toko;
    }

    public void setDaerah_toko(String daerah_toko) {
        this.daerah_toko = daerah_toko;
        notifyPropertyChanged(BR.daerah_toko);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_produk);
        dest.writeString(nama_produk);
        dest.writeString(gambar_produk);
        dest.writeString(deskripsi_produk);
        dest.writeInt(jumlah_produk);
        dest.writeString(berat_produk);
        dest.writeString(nama_toko);
        dest.writeInt(harga_produk);
        dest.writeString(daerah_toko);
    }
}
