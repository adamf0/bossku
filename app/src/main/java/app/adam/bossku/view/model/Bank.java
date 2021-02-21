package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.adam.bossku.BR;

public class Bank extends BaseObservable implements Parcelable {
    private String id;
    private String bank_name;
    private String account_name;
    private String account_number;

    public Bank(){

    }
    public Bank(String id, String bank_name, String account_name, String account_number){
        setId(id);
        setBank_name(bank_name);
        setAccount_name(account_name);
        setAccount_number(account_number);
    }

    protected Bank(Parcel in) {
        id = in.readString();
        bank_name = in.readString();
        account_name = in.readString();
        account_number = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(bank_name);
        dest.writeString(account_name);
        dest.writeString(account_number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.code);
    }

    @Bindable
    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
        notifyPropertyChanged(BR.code);
    }

    @Bindable
    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
        notifyPropertyChanged(BR.code);
    }

    @Bindable
    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
        notifyPropertyChanged(BR.code);
    }
}