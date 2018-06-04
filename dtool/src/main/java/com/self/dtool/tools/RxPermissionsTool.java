package com.self.dtool.tools;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class RxPermissionsTool {

    public static class Builder{

        private Activity mActivity;
        private List<String> permissionList;

        public Builder(Activity mActivity) {
            this.mActivity = mActivity;
            permissionList=new ArrayList<>();
        }

        public Builder addPermission(@NonNull String permission){
            if(!permissionList.contains(permission)){
                permissionList.add(permission);
            }
            return this;
        }

        public List<String> initPermission(){
            List<String> list=new ArrayList<>();
            for(String permission: permissionList){
                if(ActivityCompat.checkSelfPermission(mActivity,permission)!= PackageManager.PERMISSION_GRANTED){
                    list.add(permission);
                }
            }
            if(list.size()>0){
                ActivityCompat.requestPermissions(mActivity,list.toArray(new String[list.size()]),1);
            }
            return list;
        }
    }
}
