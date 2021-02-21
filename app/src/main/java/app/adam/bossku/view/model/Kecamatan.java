package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.adam.bossku.BR;

public class Kecamatan extends BaseObservable implements Parcelable {
    private String id;
    private String name;
    private String postal_kode;

    Kecamatan(){

    }
    public Kecamatan(String id,String name,String postal_kode){
        setId(id);
        setName(name);
        setPostal_kode(postal_kode);
    }

    protected Kecamatan(Parcel in) {
        id = in.readString();
        name = in.readString();
        postal_kode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(postal_kode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Kecamatan> CREATOR = new Creator<Kecamatan>() {
        @Override
        public Kecamatan createFromParcel(Parcel in) {
            return new Kecamatan(in);
        }

        @Override
        public Kecamatan[] newArray(int size) {
            return new Kecamatan[size];
        }
    };

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPostal_kode() {
        return postal_kode;
    }

    public void setPostal_kode(String postal_kode) {
        this.postal_kode = postal_kode;
        notifyPropertyChanged(BR.postal_kode);
    }
}
