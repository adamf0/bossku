package app.adam.bossku.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Tags implements Parcelable {
    private String id;
    private String title;
    public Tags(){

    }
    public Tags(String id, String title){
        setId(id);
        setTitle(title);
    }

    protected Tags(Parcel in) {
        id = in.readString();
        title = in.readString();
    }

    public static final Creator<Tags> CREATOR = new Creator<Tags>() {
        @Override
        public Tags createFromParcel(Parcel in) {
            return new Tags(in);
        }

        @Override
        public Tags[] newArray(int size) {
            return new Tags[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
    }
}
