package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Keranjang implements Parcelable {
    private String id;
    private String nama_toko;
    private double total_harga;
    private ArrayList<ItemKeranjang> itemKeranjang = new ArrayList<>();
    private String catatan;

    public Keranjang(){

    }
    public Keranjang(String id, String nama_toko,ArrayList<ItemKeranjang> list_item,double total_harga, String catatan){
        setId(id);
        setNama_toko(nama_toko);
        setItemKeranjang(list_item);
        setTotal_harga(total_harga);
        setCatatan(catatan);
    }

    protected Keranjang(Parcel in) {
        id = in.readString();
        nama_toko = in.readString();
        total_harga = in.readDouble();
        itemKeranjang = in.createTypedArrayList(ItemKeranjang.CREATOR);
        catatan = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama_toko);
        dest.writeDouble(total_harga);
        dest.writeTypedList(itemKeranjang);
        dest.writeString(catatan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Keranjang> CREATOR = new Creator<Keranjang>() {
        @Override
        public Keranjang createFromParcel(Parcel in) {
            return new Keranjang(in);
        }

        @Override
        public Keranjang[] newArray(int size) {
            return new Keranjang[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }

    public double getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(double total_harga) {
        this.total_harga = total_harga;
    }

    public ArrayList<ItemKeranjang> getItemKeranjang() {
        return itemKeranjang;
    }

    public void setItemKeranjang(ArrayList<ItemKeranjang> itemKeranjang) {
        this.itemKeranjang = itemKeranjang;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
