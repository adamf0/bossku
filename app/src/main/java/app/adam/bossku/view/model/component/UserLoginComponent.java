package app.adam.bossku.view.model.component;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import app.adam.bossku.BR;

public class UserLoginComponent extends BaseObservable {
    private String email;
    private String password;
    private String message;

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
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    public UserLoginComponent() {
    }

    public UserLoginComponent(String email, String password) {
        setEmail(email);
        setPassword(password);
        setMessage(null);
    }

    public boolean isValidation() {
        if(TextUtils.isEmpty(getEmail())){
            setMessage("Email tidak boleh kosong");
            return false;
        }
        else if(TextUtils.isEmpty(getPassword())){
            setMessage("Kata sandi tidak boleh kosong");
            return false;
        }
        return true;
//        return !TextUtils.isEmpty(getEmail()) &&
//                Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches() &&
//                !TextUtils.isEmpty(getPassword()) &&
//                getPassword().length() > 3;
    }
}
