package app.adam.bossku.view.model.component;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.adam.bossku.BR;

public class RegisterComponent extends BaseObservable {
    private String nama_lengkap;
    private String telp;
    private String email;
    private String password;
    private String kategori;
    private String message;

    public RegisterComponent() {
    }

    public RegisterComponent(String nama_lengkap,String telp,String email,String password,String kategori) {
        setNama_lengkap(nama_lengkap);
        setTelp(telp);
        setEmail(email);
        setPassword(password);
        setKategori(kategori);
        setMessage(null);
    }

    public boolean isValidation() {
        if(TextUtils.isEmpty(getNama_lengkap())){
            setMessage("Nama tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getTelp())){
            setMessage("Telp tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getEmail())){
            setMessage("Email tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getPassword())){
            setMessage("Kata sandi tidak boleh kosong");
            return false;
        }
        else {
            setMessage(null);
            return true;
        }
    }
    public boolean isValidationWithKategori() {
        if(TextUtils.isEmpty(getNama_lengkap())){
            setMessage("Nama tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getTelp())){
            setMessage("Telp tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getEmail())){
            setMessage("Email tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getPassword())){
            setMessage("Kata sandi tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getKategori())){
            setMessage("Kategori wajib pilih");
            return false;
        }
        else {
            setMessage(null);
            return true;
        }
    }

    @Bindable
    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
        notifyPropertyChanged(BR.nama_lengkap);
    }

    @Bindable
    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
        notifyPropertyChanged(BR.telp);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
        notifyPropertyChanged(BR.kategori);
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }
}
