package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PesanBarang implements Parcelable {
    private String id;
    private String nama_produk;
    private int jml_beli;
    private double harga_satuan;
    private String catatan;
    private String refund;
    private String toko;
    private double ongkir;
    private int berat;

    PesanBarang(){

    }
    public PesanBarang(String id, String nama_produk, int jml_beli, double harga_satuan){
        setId(id);
        setNama_produk(nama_produk);
        setJml_beli(jml_beli);
        setHarga_satuan(harga_satuan);
    }

    public PesanBarang(String id, String nama_produk, int jml_beli, double harga_satuan, String catatan, String refund, String toko, int berat, double ongkir){
        setId(id);
        setNama_produk(nama_produk);
        setJml_beli(jml_beli);
        setHarga_satuan(harga_satuan);
        setCatatan(catatan);
        setRefund(refund);
        setToko(toko);
        setBerat(berat);
        setOngkir(ongkir);
    }

    protected PesanBarang(Parcel in) {
        id = in.readString();
        nama_produk = in.readString();
        jml_beli = in.readInt();
        harga_satuan = in.readDouble();
        catatan = in.readString();
        refund = in.readString();
        toko = in.readString();
        ongkir = in.readDouble();
        berat = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama_produk);
        dest.writeInt(jml_beli);
        dest.writeDouble(harga_satuan);
        dest.writeString(catatan);
        dest.writeString(refund);
        dest.writeString(toko);
        dest.writeDouble(ongkir);
        dest.writeInt(berat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PesanBarang> CREATOR = new Creator<PesanBarang>() {
        @Override
        public PesanBarang createFromParcel(Parcel in) {
            return new PesanBarang(in);
        }

        @Override
        public PesanBarang[] newArray(int size) {
            return new PesanBarang[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public int getJml_beli() {
        return jml_beli;
    }

    public void setJml_beli(int jml_beli) {
        this.jml_beli = jml_beli;
    }

    public double getHarga_satuan() {
        return harga_satuan;
    }

    public void setHarga_satuan(double harga_satuan) {
        this.harga_satuan = harga_satuan;
    }

    public double getTotal_harga() {
        return harga_satuan*jml_beli;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public double getOngkir() {
        return ongkir;
    }

    public void setOngkir(double ongkir) {
        this.ongkir = ongkir;
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }
}
