package wallet.bitcoin.bitcoinwallet.helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import wallet.bitcoin.bitcoinwallet.R;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Utility {

    public static final String INTENT_TYPE_TEXT_PLAIN = "text/plain";

    public static final void setStatusBarColor(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        }
    }

    public static final void setStatusBarColorPin(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.mine_shaft));
        }
    }

    public static final void setStatusBarColorAuth(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#51b3fd"));
        }
    }

    static public void setDrawableColor(Drawable drawable, int color) {
        drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    static public void setDrawableColor(ImageView iv, int color) {
        iv.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    static public void setBackgroundColor(View iv, int color) {
        iv.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    static public String getDoubleStringFormat(double n){
        DecimalFormat df = new DecimalFormat("#.########");
        df.setRoundingMode(RoundingMode.CEILING);
        return n > 0 ? "+" + df.format(n) : df.format(n);
    }

    static public String getDoubleStringFormatForCurrencyWithSign(double n){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return n > 0 ? "+" + df.format(n) : df.format(n);
    }

    static public String getDoubleStringFormatNoSign(double n){
        DecimalFormat df = new DecimalFormat("#.########");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(n);
    }

    static public String getDoubleStringFormatForCurrency(double n){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(n);
    }

    public static Drawable getCheckBoxDrawable() {
        return MagicDrawable.createCheckBox(App.getContext().getResources().getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp),
                App.getContext().getResources().getColor(R.color.text_color),
                App.getContext().getResources().getDrawable(R.drawable.ic_check_box_black_24dp),
                App.getContext().getResources().getColor(R.color.text_color));
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) return false;
        return activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isEmpty(String string) {
        if (string == null || string.length() == 0){
            return true;
        }

        return false;
    }

    public static boolean isEmpty(TextView tv) {
        if (tv.getText() == null || tv.getText().toString().length() == 0){
            return true;
        }

        return false;
    }

    public static void copy(String text){
        ClipboardManager clipboard = (ClipboardManager) App.getContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(text, text);
        clipboard.setPrimaryClip(clip);
    }

    public static void openSite(Activity activity, String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static void share(Context context){
        Intent intent = new Intent(Intent.ACTION_SEND);

        String text0 = context.getResources().getString(R.string.share_text);

        intent.setType(INTENT_TYPE_TEXT_PLAIN);
        intent.putExtra(Intent.EXTRA_TEXT, text0);

        String shareTitle = App.getContext().getResources().getString(R.string.share_chooser_title);
        Intent chooser = Intent.createChooser(intent, shareTitle);

        if (intent.resolveActivity(App.getContext().getPackageManager()) != null) {
            context.startActivity(chooser);
        }
    }
}
