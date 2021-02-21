package app.adam.basiclibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BasicLib extends Base{
    protected Activity act;
    private Bcrypt bcrypt;
    private Image image;
    private Location location;
    private Permission permission;
    private Text text;

    public BasicLib(Activity act){
        this.act = act;
        bcrypt = new Bcrypt();
        image = new Image(getAct());
        location = new Location(getAct());
        permission = new Permission(getAct());
        text = new Text(getAct());
    }
    public Activity getAct() {
        return act;
    }

    @Override
    public boolean onCheck(String text, String hash) {
        return bcrypt.onCheck(text,hash);
    }

    @Override
    public String onHash(String text, int length) {
        return bcrypt.onHash(text,length);
    }

    @Override
    public Bitmap loadImageAsset(String file_name, String folder) {
        return image.loadImageAsset(file_name,folder);
    }

    @Override
    public Bitmap loadImagePath(String path) {
        return image.loadImagePath(path);
    }

    @Override
    public Bitmap rotateImage(float angle) {
        return image.rotateImage(angle);
    }

    @Override
    public Bitmap resizeImage(int maxSize) {
        return image.resizeImage(maxSize);
    }

    @Override
    public Bitmap qualityImage(int quality) {
        return image.qualityImage(quality);
    }

    @Override
    public String imageToStr() {
        return image.imageToStr();
    }

    @Override
    public void imageSource(Bitmap source) {
        image.source(source);
    }

    @Override
    public void imageView(ImageView view) {
        image.image(view);
    }

    @Override
    public Bitmap getImage() {
        return image.getImage();
    }

    @Override
    public void getLocationNow(LocationListener locationListener) {
        location.getLocationNow(locationListener);
    }

    @Override
    public boolean checkAndRequestPermissions() {
        return permission.checkAndRequestPermissions();
    }

    @Override
    public void reRequestCheckAndRequestPermissions() {
        permission.reRequestCheckAndRequestPermissions();
    }

    @Override
    public void permissionListener(PermissionListener permissionListener) {
        permission.permissionListener(permissionListener);
    }

    @Override
    public void addPermission(String name_permission) {
        permission.addPermission(name_permission);
    }

    @Override
    public void addPermission(List<String> list_permission) {
        permission.addPermission(list_permission);
    }

    @Override
    public void openPermission() {
        permission.openPermission();
    }

    @Override
    public void addRequest(int code_request) {
        permission.addRequest(code_request);
    }

    @Override
    public int getRequest() {
        return permission.getRequest();
    }

    @Override
    public void checkInPermission(String name_permission, int status) {
        permission.checkInPermission(name_permission,status);
    }

    @Override
    public void checkGrantPerMission() {
        permission.checkGrantPerMission();
    }

    @Override
    public void setFull_text(String full_text) {
        text.setFull_text(full_text);
    }

    @Override
    public void setTarget(String target) {
        text.setTarget(target);
    }

    @Override
    public void setTextView(TextView textView) {
        text.setTextView(textView);
    }

    @Override
    public void setUnderline(boolean underline) {
        text.setUnderline(underline);
    }

    @Override
    public void setColor(String color) {
        text.setColor(color);
    }

    @Override
    public void setColor(int color) {
        text.setColor(color);
    }

    @Override
    public void addClick(TextListener textListener) {
        text.addClick(textListener);
    }

    @Override
    public void implementLink() {
        text.implement();
    }

    @Override
    public void setTimer(int minute, final TimerListener timerListener) {
        text.setTimer(minute, timerListener);
    }
}
