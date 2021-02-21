package app.adam.basiclibrary;

import java.util.List;

public interface PermissionListener{
    void onPermissionCheckDone();
    void onPermissionCheckFail(String message, List<String> list_permission_fail);
}