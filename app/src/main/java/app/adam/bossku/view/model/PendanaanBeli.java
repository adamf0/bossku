package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.MathContext;

public class PendanaanBeli implements Parcelable {
    private String id;
    private String url;
    private String nama_brand;
    private String nama_umkm;
    private String terbeli_persen;
    private BigDecimal terbeli_lembar;
    private BigDecimal terbeli_rupiah;
    private int status;

    public PendanaanBeli(){

    }
    public PendanaanBeli(String id,String url,String nama_brand,String nama_umkm,String terbeli_persen,BigDecimal terbeli_lembar,BigDecimal terbeli_rupiah,int status){
        setId(id);
        setUrl(url);
        setNama_brand(nama_brand);
        setNama_umkm(nama_umkm);
        setTerbeli_persen(terbeli_persen);
        setTerbeli_lembar(terbeli_lembar);
        setTerbeli_rupiah(terbeli_rupiah);
        setStatus(status);
    }

    protected PendanaanBeli(Parcel in) {
        id = in.readString();
        url = in.readString();
        nama_brand = in.readString();
        nama_umkm = in.readString();
        terbeli_persen = in.readString();
        status = in.readInt();
    }

    public static final Creator<PendanaanBeli> CREATOR = new Creator<PendanaanBeli>() {
        @Override
        public PendanaanBeli createFromParcel(Parcel in) {
            return new PendanaanBeli(in);
        }

        @Override
        public PendanaanBeli[] newArray(int size) {
            return new PendanaanBeli[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNama_umkm() {
        return nama_umkm;
    }

    public void setNama_umkm(String nama_umkm) {
        this.nama_umkm = nama_umkm;
    }

    public String getNama_brand() {
        return nama_brand;
    }

    public void setNama_brand(String nama_brand) {
        this.nama_brand = nama_brand;
    }

    public String getTerbeli_persen() {
        return terbeli_persen;
    }

    public void setTerbeli_persen(String terbeli_persen) {
        this.terbeli_persen = terbeli_persen;
    }

    public BigDecimal getTerbeli_lembar() {
        return terbeli_lembar;
    }

    public void setTerbeli_lembar(BigDecimal terbeli_lembar) {
        this.terbeli_lembar = terbeli_lembar;
    }

    public BigDecimal getTerbeli_rupiah() {
        return terbeli_rupiah;
    }

    public void setTerbeli_rupiah(BigDecimal terbeli_rupiah) {
        this.terbeli_rupiah = terbeli_rupiah;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(url);
        parcel.writeString(nama_brand);
        parcel.writeString(nama_umkm);
        parcel.writeString(terbeli_persen);
        parcel.writeInt(status);
    }
}
