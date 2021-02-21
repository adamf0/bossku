package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.adam.bossku.BR;

public class Kurir extends BaseObservable implements Parcelable {
    private String id;
    private String name;
    private String code;
    private String service;
    private String estimation;
    private double price;

    public Kurir(){

    }
    public Kurir(String id,String name,String code,String service,String estimation,double price){
        setId(id);
        setName(name);
        setCode(code);
        setService(service);
        setEstimation(estimation);
        setPrice(price);
    }

    protected Kurir(Parcel in) {
        id = in.readString();
        name = in.readString();
        code = in.readString();
        service = in.readString();
        estimation = in.readString();
        price = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(service);
        dest.writeString(estimation);
        dest.writeDouble(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Kurir> CREATOR = new Creator<Kurir>() {
        @Override
        public Kurir createFromParcel(Parcel in) {
            return new Kurir(in);
        }

        @Override
        public Kurir[] newArray(int size) {
            return new Kurir[size];
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
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        notifyPropertyChanged(BR.code);
    }

    @Bindable
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
        notifyPropertyChanged(BR.service);
    }

    @Bindable
    public String getEstimation() {
        return estimation;
    }

    public void setEstimation(String estimation) {
        this.estimation = estimation;
        notifyPropertyChanged(BR.estimation);
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }
}