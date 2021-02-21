package app.adam.bossku.view.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class Pengaturan implements Parcelable {
    private Drawable icon;
    private int id;
    private String judul;
    private String deskripsi;

    public Pengaturan(){

    }
    public Pengaturan(int id, Drawable icon, String judul, String deskripsi){
        setId(id);
        setIcon(icon);
        setJudul(judul);
        setDeskripsi(deskripsi);
    }

    protected Pengaturan(Parcel in) {
        id = in.readInt();
        judul = in.readString();
        deskripsi = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(judul);
        dest.writeString(deskripsi);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pengaturan> CREATOR = new Creator<Pengaturan>() {
        @Override
        public Pengaturan createFromParcel(Parcel in) {
            return new Pengaturan(in);
        }

        @Override
        public Pengaturan[] newArray(int size) {
            return new Pengaturan[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
