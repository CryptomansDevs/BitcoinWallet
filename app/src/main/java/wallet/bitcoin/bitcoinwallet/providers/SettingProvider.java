package wallet.bitcoin.bitcoinwallet.providers;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;

public class SettingProvider {

    public static class SettingModel {

        public static final int TYPE_HEADER = 0;
        public static final int TYPE_ITEM = 1;

        public String title;
        public String descr;
        public String rihgtValue;
        public int icon;
        public View.OnClickListener listener;

        public int type;
    }

    public interface SettingsProviderCallback{
        void onPinChangeCLicked();
        void onLogout();
        void onShareClicked();
        void onRateClicked();
        void onMnemonicCodeClicked();
        void onImportMnemonicCodeClicked();

        void onChangeLocalCurrency();

        void onAboutClicked();
        void onTermsOfUseClicked();
        void onPrivacyPolicyClicked();
        void onSupportClicked();
        void onTelegramClicked();

        void onBuyBitcoins();
        void onClickGame();
    }

    public static List<SettingModel> provide(final SettingsProviderCallback callback){
        List<SettingModel> list = new ArrayList<>();

        list.add(create(R.string.local_cur, R.string.local_cur_descr, App.getCurrentUser().getRateName(), R.drawable.money, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onChangeLocalCurrency();
            }
        }));

        list.add(create(R.string.mnemonic_code, R.string.mnemonic_code_descr, R.drawable.mnemonicpass, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onMnemonicCodeClicked();
            }
        }));

        list.add(create(R.string.import_mnemonic_code, R.string.import_mnemonic_code_descr, R.drawable.addmnemonicpass, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onImportMnemonicCodeClicked();
            }
        }));


        list.add(createHeader(R.string.actions));

        list.add(create(R.string.share, R.drawable.menushare, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onShareClicked();
            }
        }));

//        list.add(create(R.string.game, R.string.game_descr, R.drawable.gambling_ic, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callback.onClickGame();
//            }
//        }));

//        list.add(create(R.string.buy_btc, R.string.buy_btc_descr, R.drawable.ic_exchange, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callback.onBuyBitcoins();
//            }
//        }));

        list.add(create(R.string.send_review, R.drawable.menufeedback, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onRateClicked();
            }
        }));

        list.add(createHeader(R.string.secure));

        list.add(create(R.string.set_pin, R.string.set_pin_descr, R.drawable.ic_lock, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onPinChangeCLicked();
            }
        }));

        list.add(createHeader(R.string.about));

        list.add(create(R.string.about_app, R.drawable.ic_info_outline_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onAboutClicked();
            }
        }));

        list.add(create(R.string.terms_of_use, R.drawable.ic_assignment_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onTermsOfUseClicked();
            }
        }));

        list.add(create(R.string.privacy_policy, R.drawable.ic_assignment_ind_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onPrivacyPolicyClicked();
            }
        }));

        list.add(create(R.string.support, R.drawable.support, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onSupportClicked();
            }
        }));

//        list.add(create(R.string.telega, R.drawable.ic_telegram_app, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callback.onTelegramClicked();
//            }
//        }));

        list.add(createHeader(R.string.account));

        list.add(create(R.string.logout, R.drawable.ic_logout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onLogout();
            }
        }));

        return list;
    }

    private static SettingModel create(@StringRes int title, @DrawableRes int icon, View.OnClickListener listener){
        SettingModel item = new SettingModel();
        item.title = App.getContext().getResources().getString(title);
        item.descr = null;
        item.icon = icon;
        item.listener = listener;
        item.type = SettingModel.TYPE_ITEM;

        return item;
    }

    private static SettingModel create(@StringRes int title, @StringRes int descr, @DrawableRes int icon, View.OnClickListener listener){
        SettingModel item = new SettingModel();
        item.title = App.getContext().getResources().getString(title);
        item.descr = App.getContext().getResources().getString(descr);
        item.icon = icon;
        item.listener = listener;
        item.type = SettingModel.TYPE_ITEM;

        return item;
    }

    private static SettingModel create(@StringRes int title, @StringRes int descr, String rightVal, @DrawableRes int icon, View.OnClickListener listener){
        SettingModel item = new SettingModel();
        item.title = App.getContext().getResources().getString(title);
        item.descr = App.getContext().getResources().getString(descr);
        item.rihgtValue = rightVal;
        item.icon = icon;
        item.listener = listener;
        item.type = SettingModel.TYPE_ITEM;

        return item;
    }

    private static SettingModel createHeader(@StringRes int title){
        SettingModel item = new SettingModel();
        item.title = App.getContext().getResources().getString(title);
        item.type = SettingModel.TYPE_HEADER;

        return item;
    }
}
