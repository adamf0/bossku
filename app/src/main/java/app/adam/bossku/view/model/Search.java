package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Search implements Parcelable {
    private String id;
    private String judul;

    public Search(){

    }
    public Search(String id, String judul){
        this.id=id;
        this.judul=judul;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    private Search(Parcel in) {
        id= in.readString();
        judul= in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(judul);
    }

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        public Search[] newArray(int size) {
            return new Search[size];
        }
    };
}
