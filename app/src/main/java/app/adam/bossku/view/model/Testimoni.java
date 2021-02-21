package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Testimoni implements Parcelable {
    private String id_user;
    private String nama_pengguna;
    private String deskripsi;

    Testimoni(){

    }
    public Testimoni(String id_user, String nama_pengguna, String deskripsi){
        setId_user(id_user);
        setNama_pengguna(nama_pengguna);
        setDeskripsi(deskripsi);
    }

    protected Testimoni(Parcel in) {
        id_user = in.readString();
        nama_pengguna = in.readString();
        deskripsi = in.readString();
    }

    public static final Creator<Testimoni> CREATOR = new Creator<Testimoni>() {
        @Override
        public Testimoni createFromParcel(Parcel in) {
            return new Testimoni(in);
        }

        @Override
        public Testimoni[] newArray(int size) {
            return new Testimoni[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_user);
        parcel.writeString(nama_pengguna);
        parcel.writeString(deskripsi);
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNama_pengguna() {
        return nama_pengguna;
    }

    public void setNama_pengguna(String nama_pengguna) {
        this.nama_pengguna = nama_pengguna;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
