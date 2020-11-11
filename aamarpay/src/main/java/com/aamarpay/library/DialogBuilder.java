package com.aamarpay.library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Objects;

public class DialogBuilder {
    private Context context;
    private AlertDialog alertDialog;

    public DialogBuilder(Context context, AlertDialog alertDialog) {
        this.context = context;
        this.alertDialog = alertDialog;
    }

    @SuppressLint("InflateParams")
    public void showLoading() {
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        // Initialize a new window manager layout parameters
        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.4f);
        // Set alert dialog height equal to screen height 70%
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view = LayoutInflater.from(context).inflate(R.layout.loading_popup, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.loading_popup_old, null);
        }
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
            layoutParams.width = dialogWindowWidth;
            alertDialog.getWindow().setAttributes(layoutParams);
        }
    }

    @SuppressLint("InflateParams")
    public void errorPopUp(String retryMsg) {
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        // Initialize a new window manager layout parameters
        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.7f);
        // Set alert dialog height equal to screen height 70%
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view = LayoutInflater.from(context).inflate(R.layout.error_popup, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.error_popup_old, null);
        }
        TextView retryMessage = view.findViewById(R.id.custom_message);
        retryMessage.setText(retryMsg);
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
            layoutParams.width = dialogWindowWidth;
            alertDialog.getWindow().setAttributes(layoutParams);
        }
    }

    public void dismissDialog() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
