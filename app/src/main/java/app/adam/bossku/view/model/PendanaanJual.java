package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class PendanaanJual implements Parcelable {
    private String id;
    private String url;
    private String nama_brand;
    private int total_lembar;
    private int sisa_lembar;
    private BigDecimal total_pendanaan,harga_satuan;
    private int total_peminat;
    private int status;

    public PendanaanJual(){

    }
    public PendanaanJual(String id,String url,String nama_brand,BigDecimal total_pendanaan,int total_lembar,int sisa_lembar,BigDecimal harga_satuan,int total_peminat,int status){
        setId(id);
        setUrl(url);
        setNama_brand(nama_brand);
        setTotal_pendanaan(total_pendanaan);
        setTotal_lembar(total_lembar);
        setSisa_lembar(sisa_lembar);
        setHarga_satuan(harga_satuan);
        setTotal_peminat(total_peminat);
        setStatus(status);
    }

    protected PendanaanJual(Parcel in) {
        id = in.readString();
        url = in.readString();
        nama_brand = in.readString();
        total_lembar = in.readInt();
        sisa_lembar = in.readInt();
        total_peminat = in.readInt();
        status = in.readInt();
    }

    public static final Creator<PendanaanJual> CREATOR = new Creator<PendanaanJual>() {
        @Override
        public PendanaanJual createFromParcel(Parcel in) {
            return new PendanaanJual(in);
        }

        @Override
        public PendanaanJual[] newArray(int size) {
            return new PendanaanJual[size];
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

    public String getNama_brand() {
        return nama_brand;
    }

    public void setNama_brand(String nama_brand) {
        this.nama_brand = nama_brand;
    }

    public void setTotal_pendanaan(BigDecimal total_pendanaan) {
        this.total_pendanaan = total_pendanaan;
    }

    public BigDecimal getTotal_pendanaan() {
        return total_pendanaan;
    }

    public int getTotal_lembar() {
        return total_lembar;
    }

    public void setTotal_lembar(int total_lembar) {
        this.total_lembar = total_lembar;
    }

    public int getSisa_lembar() {
        return sisa_lembar;
    }

    public void setSisa_lembar(int sisa_lembar) {
        this.sisa_lembar = sisa_lembar;
    }

    public BigDecimal getHarga_satuan() {
        return harga_satuan;
    }

    public void setHarga_satuan(BigDecimal harga_satuan) {
        this.harga_satuan = harga_satuan;
    }

    public int getTotal_peminat() {
        return total_peminat;
    }

    public void setTotal_peminat(int total_peminat) {
        this.total_peminat = total_peminat;
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
        parcel.writeInt(total_lembar);
        parcel.writeInt(sisa_lembar);
        parcel.writeInt(total_peminat);
        parcel.writeInt(status);
    }
}
