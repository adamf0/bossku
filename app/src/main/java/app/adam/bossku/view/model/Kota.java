package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.adam.bossku.BR;

public class Kota extends BaseObservable implements Parcelable {
    private String id;
    private String name;
    private String type;

    Kota(){

    }
    public Kota(String id,String name,String type){
        setId(id);
        setName(name);
        setType(type);
    }

    protected Kota(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Kota> CREATOR = new Creator<Kota>() {
        @Override
        public Kota createFromParcel(Parcel in) {
            return new Kota(in);
        }

        @Override
        public Kota[] newArray(int size) {
            return new Kota[size];
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
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getFullName() {
        return String.format("%s %s",type,name);
    }
}
