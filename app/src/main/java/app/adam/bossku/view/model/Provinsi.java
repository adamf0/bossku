package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.adam.bossku.BR;

public class Provinsi extends BaseObservable implements Parcelable {
    private String id;
    private String name;

    public Provinsi(){

    }
    public Provinsi(String id,String name){
        setId(id);
        setName(name);
    }

    protected Provinsi(Parcel in) {
        setId(in.readString());
        setName(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Provinsi> CREATOR = new Creator<Provinsi>() {
        @Override
        public Provinsi createFromParcel(Parcel in) {
            return new Provinsi(in);
        }

        @Override
        public Provinsi[] newArray(int size) {
            return new Provinsi[size];
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
}
