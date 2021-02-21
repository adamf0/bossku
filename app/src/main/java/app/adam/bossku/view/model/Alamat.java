package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.adam.bossku.BR;

public class Alamat extends BaseObservable implements Parcelable {
    private String id;
    private String name;
    private String address;
    private Provinsi provinsi;
    private Kota kota;
    private Kecamatan kecamatan;
    private int def;

    Alamat(){

    }
    public Alamat(String id,String name,String address,Provinsi provinsi,Kota kota,Kecamatan kecamatan,int isdefault){
        setId(id);
        setName(name);
        setAddress(address);
        setProvinsi(provinsi);
        setKota(kota);
        setKecamatan(kecamatan);
        setDef(isdefault);
    }

    protected Alamat(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        provinsi = in.readParcelable(Provinsi.class.getClassLoader());
        kota = in.readParcelable(Kota.class.getClassLoader());
        kecamatan = in.readParcelable(Kecamatan.class.getClassLoader());
        def = in.readInt();
    }

    public static final Creator<Alamat> CREATOR = new Creator<Alamat>() {
        @Override
        public Alamat createFromParcel(Parcel in) {
            return new Alamat(in);
        }

        @Override
        public Alamat[] newArray(int size) {
            return new Alamat[size];
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
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public Provinsi getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(Provinsi provinsi) {
        this.provinsi = provinsi;
        notifyPropertyChanged(BR.provinsi);
    }

    @Bindable
    public Kota getKota() {
        return kota;
    }

    public void setKota(Kota kota) {
        this.kota = kota;
        notifyPropertyChanged(BR.kota);
    }

    @Bindable
    public Kecamatan getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(Kecamatan kecamatan) {
        this.kecamatan = kecamatan;
        notifyPropertyChanged(BR.kecamatan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeParcelable(provinsi, flags);
        dest.writeParcelable(kota, flags);
        dest.writeParcelable(kecamatan, flags);
        dest.writeInt(def);
    }

    @Bindable
    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
        notifyPropertyChanged(BR.def);
    }
}
