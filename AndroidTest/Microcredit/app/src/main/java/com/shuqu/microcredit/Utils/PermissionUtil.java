package com.shuqu.microcredit.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import java.util.ArrayList;

/**
 * Class Info ：
 * Created by Lyndon on 16/8/23.
 */
public class PermissionUtil {

    public static boolean checkPermission(Context context, String permissiom){
        int result = context.checkCallingOrSelfPermission(permissiom);
        return (result == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean checkPermission(Context context, ArrayList<String> permissiom){
        for (int i=0; i< permissiom.size(); ++i){
            if(!checkPermission(context, permissiom.get(i))){
                return false;
            }
        }
        return true;
    }

    private void PermissionUtil(){}
}
