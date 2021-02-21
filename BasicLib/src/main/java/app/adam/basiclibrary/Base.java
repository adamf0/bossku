package app.adam.basiclibrary;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

abstract class Base {
    public abstract boolean onCheck(String text, String hash);
    public abstract String onHash(String text, int length);

    public abstract Bitmap loadImageAsset(String file_name, String folder);
    public abstract Bitmap loadImagePath(String path);
    public abstract Bitmap rotateImage(float angle);
    public abstract Bitmap resizeImage(int maxSize);
    public abstract Bitmap qualityImage(int quality);
    public abstract String imageToStr();
    public abstract void imageSource(Bitmap source);
    public abstract void imageView(ImageView view);
    public abstract Bitmap getImage();

    public abstract void getLocationNow(LocationListener locationListener);

    public  abstract  boolean checkAndRequestPermissions();
    public abstract void permissionListener(PermissionListener permissionListener);
    public abstract void addPermission(String name_permission);
    public abstract void addPermission(List<String> list_permission);
    public abstract void reRequestCheckAndRequestPermissions();
    public abstract void openPermission();
    public abstract void addRequest(int code_request);
    public abstract int getRequest();
    public abstract void checkInPermission(String name_permission, int status);
    public abstract void checkGrantPerMission();
    //public abstract void onRequestCallBack(int RequestCode, String[] permissions, int[] grantResults);

    public abstract void setFull_text(String full_text);
    public abstract void setTarget(String target);
    public abstract void setTextView(TextView textView);
    public abstract void setUnderline(boolean underline);
    public abstract void setColor(String color);
    public abstract void setColor(int color);
    public abstract void addClick(TextListener textListener);
    public abstract void implementLink();
    public abstract void setTimer(int minute, final TimerListener timerListener);

}
