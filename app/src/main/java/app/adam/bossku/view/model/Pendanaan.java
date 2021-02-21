package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigDecimal;
import java.math.MathContext;

public class Pendanaan implements Parcelable {
    private String id;
    private String url;
    private String nama_brand;
    private String nama_umkm;
    private int dividen;
    private int sisa_waktu;
    private int status;
    private BigDecimal harga_satuan;
    private BigDecimal total_terkumpul;
    private BigDecimal total_pendanaan;

    private String funding_id;
    private double jumlah_donasi;
    private String transaction_code;
    private int transaction_status;
    private String id_crowd_funding;

    public Pendanaan(){

    }
    public Pendanaan(String id, String url, String nama_brand, String nama_umkm, BigDecimal harga_satuan, BigDecimal total_terkumpul, BigDecimal total_pendanaan, int sisa_waktu, int dividen, int status){
        setId(id);
        setNama_brand(nama_brand);
        setNama_umkm(nama_umkm);
        setHarga_satuan(harga_satuan);
        setTotal_terkumpul(total_terkumpul);
        setTotal_pendanaan(total_pendanaan);
        setSisa_waktu(sisa_waktu);
        setDividen(dividen);
        setStatus(status);
    }

    public Pendanaan(String funding_id,String name,double jumlah_donasi,String transaction_code,int transaction_status,String id_crowd_funding){
        setFunding_id(funding_id);
        setJumlah_donasi(jumlah_donasi);
        setNama_brand(name);
        setTransaction_code(transaction_code);
        setTransaction_status(transaction_status);
        setId_crowd_funding(id_crowd_funding);
    }

    protected Pendanaan(Parcel in) {
        id = in.readString();
        url = in.readString();
        nama_brand = in.readString();
        nama_umkm = in.readString();
        dividen = in.readInt();
        sisa_waktu = in.readInt();
        status = in.readInt();
        funding_id = in.readString();
        jumlah_donasi = in.readDouble();
        transaction_code = in.readString();
        transaction_status = in.readInt();
        id_crowd_funding = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(nama_brand);
        dest.writeString(nama_umkm);
        dest.writeInt(dividen);
        dest.writeInt(sisa_waktu);
        dest.writeInt(status);
        dest.writeString(funding_id);
        dest.writeDouble(jumlah_donasi);
        dest.writeString(transaction_code);
        dest.writeInt(transaction_status);
        dest.writeString(id_crowd_funding);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pendanaan> CREATOR = new Creator<Pendanaan>() {
        @Override
        public Pendanaan createFromParcel(Parcel in) {
            return new Pendanaan(in);
        }

        @Override
        public Pendanaan[] newArray(int size) {
            return new Pendanaan[size];
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

    public String getNama_umkm() {
        return nama_umkm;
    }

    public void setNama_umkm(String nama_umkm) {
        this.nama_umkm = nama_umkm;
    }

    public int getDividen() {
        return dividen;
    }

    public void setDividen(int dividen) {
        this.dividen = dividen;
    }

    public int getSisa_waktu() {
        return sisa_waktu;
    }

    public void setSisa_waktu(int sisa_waktu) {
        this.sisa_waktu = sisa_waktu;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getHarga_satuan() {
        return harga_satuan;
    }

    public void setHarga_satuan(BigDecimal harga_satuan) {
        this.harga_satuan = harga_satuan;
    }

    public BigDecimal getTotal_terkumpul() {
        return total_terkumpul;
    }

    public void setTotal_terkumpul(BigDecimal total_terkumpul) {
        this.total_terkumpul = total_terkumpul;
    }

    public BigDecimal getTotal_pendanaan() {
        return total_pendanaan;
    }

    public void setTotal_pendanaan(BigDecimal total_pendanaan) {
        this.total_pendanaan = total_pendanaan;
    }

    public double getProgress(){
        return 100*(total_terkumpul.divide(total_pendanaan, MathContext.DECIMAL32)).doubleValue();
    }

    public String getFunding_id() {
        return funding_id;
    }

    public void setFunding_id(String funding_id) {
        this.funding_id = funding_id;
    }

    public double getJumlah_donasi() {
        return jumlah_donasi;
    }

    public void setJumlah_donasi(double jumlah_donasi) {
        this.jumlah_donasi = jumlah_donasi;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public int getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(int transaction_status) {
        this.transaction_status = transaction_status;
    }

    public String getId_crowd_funding() {
        return id_crowd_funding;
    }

    public void setId_crowd_funding(String id_crowd_funding) {
        this.id_crowd_funding = id_crowd_funding;
    }
}
