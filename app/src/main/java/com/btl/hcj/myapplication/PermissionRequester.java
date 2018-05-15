package com.btl.hcj.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

public class PermissionRequester {

    public static final int NOT_SUPPORT_VERSION = 2;
    public static final int ALREADY_GRANTED = -1;

    public static final int REQUEST_PERMISSION = 0;

    private Activity context;
    private Builder builder;

    private void setBuilder(Builder builder) {
        this.builder = builder;
    }

    private PermissionRequester(Activity context) {
        this.context = context;
    }

    public int requests(final String permission, final int requestCode, final OnClickDenyButtonListener denyAction) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);

            if(context.shouldShowRequestPermissionRationale(permission)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle(builder.getTitle())
                        .setMessage(builder.getMsg())
                        .setPositiveButton(builder.getPositiveButtonName(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                context.requestPermissions(new String[]{permission}, requestCode);
                            }
                        })
                        .setNegativeButton(builder.getNegativeButtonName(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                denyAction.onClick(context);
                            }
                        }).create().show();

                return REQUEST_PERMISSION;
            } else {
                context.requestPermissions(new String[] {permission}, requestCode);
                return REQUEST_PERMISSION;
            }
        } else {
            return NOT_SUPPORT_VERSION;
        }
    }

    public static class Builder {
        private PermissionRequester requester;

        public Builder(Activity context) {
            requester = new PermissionRequester(context);
        }

        private String title = "권한 요청";
        private String msg = "기능의 사용을 위해 권한이 필요합니다.";
        private String positiveButtonName = "네";
        private String negativeButtonName = "아니오";

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public String getPositiveButtonName() {
            return positiveButtonName;
        }

        public Builder setPositiveButtonName(String positiveButtonName) {
            this.positiveButtonName = positiveButtonName;
            return this;
        }

        public String getNegativeButtonName() {
            return negativeButtonName;
        }

        public Builder setNagativeButtonName(String negativeButtonName) {
            this.negativeButtonName = negativeButtonName;
            return this;
        }

        public PermissionRequester create() {
            this.requester.setBuilder(this);
            return this.requester;
        }
    }

    public interface OnClickDenyButtonListener {
        public void onClick(Activity activity);
    }

}
