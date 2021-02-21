package app.adam.basiclibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.app.ActivityCompat.requestPermissions;

class Permission{
    private int code_request;
    private PermissionListener listener;
    private List<String> permission_yg_dibutuhkan = new ArrayList<>();
    private List<String> permission_ditolak = new ArrayList<>();
    private Map<String, Integer> permission = new HashMap<>();

    protected Activity act;

    public Permission(Activity act) {
        this.act = act;
    }

    public void permissionListener(PermissionListener permissionListener){
        listener = permissionListener;
    }

    //Manifest.permission
    public void addPermission(String permission){
        permission_yg_dibutuhkan.add(permission);
    }
    public void addPermission(List<String> permission){
        permission_yg_dibutuhkan.addAll(permission);
    }

    @SuppressLint("ObsoleteSdkInt")
    boolean checkAndRequestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission_yg_dibutuhkan.isEmpty()) {
                requestPermissions(act, permission_yg_dibutuhkan.toArray(new String[permission_yg_dibutuhkan.size()]), code_request);
                return false;
            }
        }

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        listener.onPermissionCheckDone();
        return true;
    }
    public void addRequest(int code_request){
        this.code_request = code_request;
    }
    public int getRequest(){
        return code_request;
    }
    public void checkInPermission(String name_permission, int status){
        permission.put(name_permission, status);
    }
    public void checkGrantPerMission(){
        int clear=0;
        for (String x: permission_yg_dibutuhkan) {
            if(permission.get(x).equals(PackageManager.PERMISSION_GRANTED))
                clear++;
            else
                permission_ditolak.add(x);
        }

        if(clear==permission_yg_dibutuhkan.size())
            listener.onPermissionCheckDone();
        else
            listener.onPermissionCheckFail("Izin Ditolak",permission_ditolak);
    }

    public void reRequestCheckAndRequestPermissions(){
        Log.i("app-log ", "recall");
        new AlertDialog.Builder(act)
                .setMessage("ada "+permission_ditolak.size()+" izin yang belum di aktifkan")
                .setPositiveButton("Aktifkan Sekarang",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            checkAndRequestPermissions();
                        }
                    }
                })
                .create()
                .show();
    }
    public void openPermission(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", act.getPackageName(), null);
        intent.setData(uri);
        act.startActivity(intent);
    }
}
