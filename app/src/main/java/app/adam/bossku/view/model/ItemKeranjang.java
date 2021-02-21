package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemKeranjang implements Parcelable {
    private boolean selected;
    private String id;
    private String nama_produk;
    private int berat;
    private double harga_satuan;
    private int jumlah_beli;
    private String foto_produk;
    private Kurir kurir;
    private String catatan;

    ItemKeranjang(){

    }
    public ItemKeranjang(boolean selected,String id, String nama_produk, int berat, double harga_satuan, int jumlah_beli, String foto_produk,Kurir kurir,String catatan){
        setSelected(selected);
        setId(id);
        setNama_produk(nama_produk);
        setBerat(berat);
        setHarga_satuan(harga_satuan);
        setJumlah_beli(jumlah_beli);
        setFoto_produk(foto_produk);
        setKurir(kurir);
        setCatatan(catatan);
    }

    protected ItemKeranjang(Parcel in) {
        selected = in.readByte() != 0;
        id = in.readString();
        nama_produk = in.readString();
        berat = in.readInt();
        harga_satuan = in.readDouble();
        jumlah_beli = in.readInt();
        foto_produk = in.readString();
        kurir = in.readParcelable(Kurir.class.getClassLoader());
        catatan = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(id);
        dest.writeString(nama_produk);
        dest.writeInt(berat);
        dest.writeDouble(harga_satuan);
        dest.writeInt(jumlah_beli);
        dest.writeString(foto_produk);
        dest.writeParcelable(kurir, flags);
        dest.writeString(catatan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemKeranjang> CREATOR = new Creator<ItemKeranjang>() {
        @Override
        public ItemKeranjang createFromParcel(Parcel in) {
            return new ItemKeranjang(in);
        }

        @Override
        public ItemKeranjang[] newArray(int size) {
            return new ItemKeranjang[size];
        }
    };

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

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

    public double getHarga_satuan() {
        return harga_satuan;
    }

    public void setHarga_satuan(double harga_satuan) {
        this.harga_satuan = harga_satuan;
    }

    public int getJumlah_beli() {
        return jumlah_beli;
    }

    public void setJumlah_beli(int jumlah_beli) {
        this.jumlah_beli = jumlah_beli;
    }

    public String getFoto_produk() {
        return foto_produk;
    }

    public void setFoto_produk(String foto_produk) {
        this.foto_produk = foto_produk;
    }

    public Kurir getKurir() {
        return kurir;
    }

    public void setKurir(Kurir kurir) {
        this.kurir = kurir;
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
