package br.com.caelum.security;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

public class Permissions {
    private static final int CODE = 123;
    private static ArrayList<String> permissionList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void doPermission(Activity activity) {
        String[] permissions = {Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.INTERNET, Manifest.permission.RECEIVE_SMS };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(String permission: permissions) {
                if(activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }

            request(activity);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void request(Activity activity) {
        String[] array = permissionList.toArray(new String[]{});

        if (permissionList.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(array, CODE);
            }
        }
    }
}
