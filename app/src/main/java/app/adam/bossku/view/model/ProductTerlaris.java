package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductTerlaris implements Parcelable {
    private String id_produk;
    private String nama_produk;
    private String gambar_produk;
    private String nama_toko;
    private int harga_produk;

    public ProductTerlaris(){

    }
    public ProductTerlaris(String id_produk, String nama_produk, String gambar_produk, String nama_toko, int harga_produk){
        setId_produk(id_produk);
        setNama_produk(nama_produk);
        setGambar_produk(gambar_produk);
        setNama_toko(nama_toko);
        setHarga_produk(harga_produk);
    }

    protected ProductTerlaris(Parcel in) {
        id_produk = in.readString();
        nama_produk = in.readString();
        gambar_produk = in.readString();
        nama_toko = in.readString();
        harga_produk = in.readInt();
    }

    public static final Creator<ProductTerlaris> CREATOR = new Creator<ProductTerlaris>() {
        @Override
        public ProductTerlaris createFromParcel(Parcel in) {
            return new ProductTerlaris(in);
        }

        @Override
        public ProductTerlaris[] newArray(int size) {
            return new ProductTerlaris[size];
        }
    };

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getGambar_produk() {
        return gambar_produk;
    }

    public void setGambar_produk(String gambar_produk) {
        this.gambar_produk = gambar_produk;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }

    public int getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(int harga_produk) {
        this.harga_produk = harga_produk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_produk);
        parcel.writeString(nama_produk);
        parcel.writeString(gambar_produk);
        parcel.writeString(nama_toko);
        parcel.writeInt(harga_produk);
    }
}
