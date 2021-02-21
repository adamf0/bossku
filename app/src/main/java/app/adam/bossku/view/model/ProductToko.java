package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductToko implements Parcelable {
    private String id_produk;
    private String nama_produk;
    private String gambar_produk;
    private int harga_produk;
    private int diskon_produk;
    private int jumlah_produk;
    private int berat_produk;
    private String deskripsi_produk;

    ProductToko(){

    }
    public ProductToko(String id_produk, String nama_produk, String gambar_produk, int harga_produk, int diskon_produk, int jumlah_produk, int berat_produk, String deskripsi_produk){
        setId_produk(id_produk);
        setNama_produk(nama_produk);
        setGambar_produk(gambar_produk);
        setHarga_produk(harga_produk);
        setDiskon_produk(diskon_produk);
        setJumlah_produk(jumlah_produk);
        setBerat_produk(berat_produk);
        setDeskripsi_produk(deskripsi_produk);
    }

    protected ProductToko(Parcel in) {
        id_produk = in.readString();
        nama_produk = in.readString();
        gambar_produk = in.readString();
        harga_produk = in.readInt();
        diskon_produk = in.readInt();
        jumlah_produk = in.readInt();
        berat_produk = in.readInt();
        deskripsi_produk = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_produk);
        dest.writeString(nama_produk);
        dest.writeString(gambar_produk);
        dest.writeInt(harga_produk);
        dest.writeInt(diskon_produk);
        dest.writeInt(jumlah_produk);
        dest.writeInt(berat_produk);
        dest.writeString(deskripsi_produk);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductToko> CREATOR = new Creator<ProductToko>() {
        @Override
        public ProductToko createFromParcel(Parcel in) {
            return new ProductToko(in);
        }

        @Override
        public ProductToko[] newArray(int size) {
            return new ProductToko[size];
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

    public int getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(int harga_produk) {
        this.harga_produk = harga_produk;
    }

    public int getDiskon_produk() {
        return diskon_produk;
    }

    public void setDiskon_produk(int diskon_produk) {
        this.diskon_produk = diskon_produk;
    }

    public int getJumlah_produk() {
        return jumlah_produk;
    }

    public void setJumlah_produk(int jumlah_produk) {
        this.jumlah_produk = jumlah_produk;
    }

    public int getBerat_produk() {
        return berat_produk;
    }

    public void setBerat_produk(int berat_produk) {
        this.berat_produk = berat_produk;
    }

    public String getDeskripsi_produk() {
        return deskripsi_produk;
    }

    public void setDeskripsi_produk(String deskripsi_produk) {
        this.deskripsi_produk = deskripsi_produk;
    }
}
