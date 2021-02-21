package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Transaksi implements Parcelable {
    private String id;
    private String nomor_transaksi;
    private String tanggal_pesan;
    private String nama_toko;
    private String total_tagihan;
    private String gambar_produk;
    private int status;

    private ArrayList<Product> list_product = new ArrayList<>();
    Transaksi(){

    }
    public Transaksi(String id, String nomor_transaksi, String tanggal_pesan, String nama_toko, String total_tagihan, String gambar_produk, int status){
       setId(id);
       setNomor_transaksi(nomor_transaksi);
       setTanggal_pesan(tanggal_pesan);
       setNama_toko(nama_toko);
       setTotal_tagihan(total_tagihan);
       setGambar_produk(gambar_produk);
       setStatus(status);
    }
    public Transaksi(String nomor_transaksi,ArrayList<Product> list_product){
        setNomor_transaksi(nomor_transaksi);
        setList_product(list_product);
    }

    protected Transaksi(Parcel in) {
        id = in.readString();
        nomor_transaksi = in.readString();
        tanggal_pesan = in.readString();
        nama_toko = in.readString();
        total_tagihan = in.readString();
        gambar_produk = in.readString();
        status = in.readInt();
        list_product = in.createTypedArrayList(Product.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nomor_transaksi);
        dest.writeString(tanggal_pesan);
        dest.writeString(nama_toko);
        dest.writeString(total_tagihan);
        dest.writeString(gambar_produk);
        dest.writeInt(status);
        dest.writeTypedList(list_product);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Transaksi> CREATOR = new Creator<Transaksi>() {
        @Override
        public Transaksi createFromParcel(Parcel in) {
            return new Transaksi(in);
        }

        @Override
        public Transaksi[] newArray(int size) {
            return new Transaksi[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomor_transaksi() {
        return nomor_transaksi;
    }

    public void setNomor_transaksi(String nomor_transaksi) {
        this.nomor_transaksi = nomor_transaksi;
    }

    public String getTanggal_pesan() {
        return tanggal_pesan;
    }

    public void setTanggal_pesan(String tanggal_pesan) {
        this.tanggal_pesan = tanggal_pesan;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }

    public String getTotal_tagihan() {
        return total_tagihan;
    }

    public void setTotal_tagihan(String total_tagihan) {
        this.total_tagihan = total_tagihan;
    }

    public String getGambar_produk() {
        return gambar_produk;
    }

    public void setGambar_produk(String gambar_produk) {
        this.gambar_produk = gambar_produk;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Product> getList_product() {
        return list_product;
    }
    public List<String> getListNameProduct(){
        List<String> list = new ArrayList<>();
        for (Product product:getList_product()) {
            list.add(product.getNama_produk());
        }
        return list;
    }

    public void setList_product(ArrayList<Product> list_product) {
        this.list_product = list_product;
    }
}
