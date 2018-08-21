package wallet.bitcoin.bitcoinwallet.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UIHelper {
    public static final int HIDE_KEYBOARD_TIME = 200;

    private static DisplayMetrics metrics;

    private static final int WIDTH_HD = 1080;
    private static final int HEIGHT_HD = 1920;
    private static Context appContext;

    public static int keyboardHeight;

    public static void init(Context c){
        appContext = c;
        metrics = appContext.getResources().getDisplayMetrics();

        keyboardHeight = getPixel(215);
    }

    public static int getW() {
        int width = ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        return width;
    }

    public static int getH() {
        int height = ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        return height;
    }

    public static int getRelativeW() {
        int width = ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        return width / WIDTH_HD;
    }

    public static int getRelativeH() {
        int height = ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        return height / HEIGHT_HD;
    }

    public static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * inverseRation) + (Color.red(color2) * ratio);
        float g = (Color.green(color1) * inverseRation) + (Color.green(color2) * ratio);
        float b = (Color.blue(color1) * inverseRation) + (Color.blue(color2) * ratio);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    public static int getPixel(float dpi){
        return (int)(metrics.density * dpi);
    }

    public static float getPixelF(float dpi){
        return metrics.density * dpi;
    }

    public static float getPixelFHacked(float dpi){
        if (metrics.density <= 1){
            return dpi;
        }

        if (metrics.density <= 2){
            return 2 * dpi;
        }

        if (metrics.density <= 3){
            return 3 * dpi;
        }

        return 4 * dpi;
    }

    public static int getColumnsAmount(int marginLeftPlusMarginRight, int widthElement){
        int minimumPadding = getPixel(7);
        int columnsCount = (getW() - marginLeftPlusMarginRight) / (widthElement + minimumPadding);
        return columnsCount;
    }

    public static int getDPI(int px){
        return (int)(px / metrics.density);
    }

    public static void setKeyboardHeight(int h){
        keyboardHeight = h;
    }

    public static int getKeyboardHeight(){
        return keyboardHeight;
    }

    public static String getData() {
        DateFormat dfDate = new SimpleDateFormat("dd MM");
        String date = dfDate.format(Calendar.getInstance().getTime());
        return date;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                inputManager.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void hideKeyboard(Activity activity, IBinder binder) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (binder != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(binder, 0);//HIDE_NOT_ALWAYS
                inputManager.showSoftInputFromInputMethod(binder, 0);
            }
        }
    }

    public static void showKeyboard(Activity activity, View view) {
        if (activity != null && view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                view.requestFocus();
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static int getActionBarHeight() {
        final TypedArray styledAttributes = appContext.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });

        int result = (int) styledAttributes.getDimension(0, UIHelper.getPixel(40));
        styledAttributes.recycle();

        return result;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) return false;
        return activeNetwork.isConnectedOrConnecting();
    }

    public static String formatMemSize(long size, int value) {
        String result = "";
        if (1024L > size) {
            String info = String.valueOf(size);
            result = (new StringBuilder(info)).append(" B").toString();
        } else if (1048576L > size) {
            String s2 = (new StringBuilder("%.")).append(value).append("f")
                    .toString();
            Object aobj[] = new Object[1];
            Float float1 = Float.valueOf((float) size / 1024F);
            aobj[0] = float1;
            String s3 = String.valueOf(String.format(s2, aobj));
            result = (new StringBuilder(s3)).append(" KB").toString();
        } else if (1073741824L > size) {
            String s4 = (new StringBuilder("%.")).append(value).append("f")
                    .toString();
            Object aobj1[] = new Object[1];
            Float float2 = Float.valueOf((float) size / 1048576F);
            aobj1[0] = float2;
            String s5 = String.valueOf(String.format(s4, aobj1));
            result = (new StringBuilder(s5)).append(" MB").toString();
        } else {
            Object aobj2[] = new Object[1];
            Float float3 = Float.valueOf((float) size / 1.073742E+009F);
            aobj2[0] = float3;
            String s6 = String.valueOf(String.format("%.2f", aobj2));
            result = (new StringBuilder(s6)).append(" GB").toString();
        }
        return result;
    }
}

