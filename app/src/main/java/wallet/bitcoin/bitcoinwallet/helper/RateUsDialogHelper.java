package wallet.bitcoin.bitcoinwallet.helper;

import android.app.Activity;

import rateusdialoghelper.RateDialogHelper;
import wallet.bitcoin.bitcoinwallet.R;

public class RateUsDialogHelper {

    public static void rate(Activity activity){
        int colorInactive = App.getContext().getResources().getColor(R.color.colorPrimaryDark);
        int colorActive = App.getContext().getResources().getColor(R.color.colorAccent);

        RateDialogHelper rateDialogHelper = new RateDialogHelper.Builder()
                .setRatingColorActive(colorActive)
                .setRatingColorInactive(colorInactive)
                .setTitleAppNameColor(colorActive)
                .setCancelColor(colorInactive)
                .setAppName(App.getContext().getResources().getString(R.string.wallet_name))
                .setRateColor(colorActive)
                .setFeedbackEmail(App.getContext().getResources().getString(R.string.email_feedback))
                .build();

        rateDialogHelper.showRateDialogImmidiatly(activity);
    }
}
