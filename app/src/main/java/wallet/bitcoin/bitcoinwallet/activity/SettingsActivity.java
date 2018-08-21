package wallet.bitcoin.bitcoinwallet.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.auth.PinLockActivity;
import wallet.bitcoin.bitcoinwallet.auth.facebook.FacebookHelper;
import wallet.bitcoin.bitcoinwallet.dialog.CurrencySelectDialogHelper;
import wallet.bitcoin.bitcoinwallet.dialog.GameDialog;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.AppPreferenceManager;
import wallet.bitcoin.bitcoinwallet.helper.OnFinishCallback;
import wallet.bitcoin.bitcoinwallet.helper.RateUsDialogHelper;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.model.User;
import wallet.bitcoin.bitcoinwallet.providers.SettingProvider;
import wallet.bitcoin.bitcoinwallet.rest.request.AccessTokenRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.SeedResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.SendFromWalletResponse;

public class SettingsActivity extends AppCompatActivity implements SettingProvider.SettingsProviderCallback {

    private static final int CODE_IMPORT = 12;

    public static final String LOGOUT = "LOGOUT";
    public static final String UPDATE = "UPDATE";

    @BindView(R.id.parentContent)
    protected LinearLayout parentContent;

    @BindView(R.id.abBack)
    protected ImageView abBack;

    private AbstructSetting settingCurrency;

    private boolean onRateGoneToGP = false;
    private long gpTime;
    private GameDialog gameDialog;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarColor(this);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        List<SettingProvider.SettingModel> models = SettingProvider.provide(this);
        for (SettingProvider.SettingModel model : models){
            AbstructSetting settingView = getSettingView(model.type);
            settingView.bind(model);
            parentContent.addView(settingView.parent);

            if (model.title.equalsIgnoreCase(App.getContext().getResources().getString(R.string.local_cur))){
                settingCurrency = settingView;
            }
        }

        Utility.setDrawableColor(abBack, App.getContext().getResources().getColor(R.color.white));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (onRateGoneToGP && System.currentTimeMillis() - gpTime > 10 * 1000){
            if (gameDialog != null){
                gameDialog.onReceiveTicket();
            }
            onRateGoneToGP = false;
        }
    }


    @Override
    public void onPinChangeCLicked() {
        Intent intent = new Intent(SettingsActivity.this, PinLockActivity.class);
        intent.putExtra(PinLockActivity.CHANGE, true);
        startActivity(intent);
    }

    @Override
    public void onLogout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_descr_dlg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        App.getCurrentUser().reset();
                        App.forceLoadCurrentUser();
                        App.updateUser();

                        FacebookHelper.signOut();

                        Intent data = new Intent();
                        data.putExtra(LOGOUT, true);
                        setResult(RESULT_OK, data);
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onShareClicked() {
        Utility.share(this);
    }

    @Override
    public void onRateClicked() {
        RateUsDialogHelper.rate(this);
    }

    @Override
    public void onMnemonicCodeClicked() {
        Intent intent = new Intent(this, MyMnemonicCodeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onImportMnemonicCodeClicked() {
        Intent intent = new Intent(this, ImportMnemonicCodeActivity.class);
        startActivityForResult(intent, CODE_IMPORT);
    }

    @Override
    public void onChangeLocalCurrency() {
        CurrencySelectDialogHelper.show(this, new CurrencySelectDialogHelper.CurrencyDialogCallback() {
            @Override
            public void onSelectedCurrency(int currencyCode) {
                App.getCurrentUser().localCurrency = currencyCode;
                App.updateUser();

                ((SettingView) settingCurrency).tvRight.setText(App.getCurrentUser().getRateName());
            }
        });
    }

    @Override
    public void onAboutClicked() {
        Utility.openSite(this, App.getContext().getResources().getString(R.string.site_url));
    }

    @Override
    public void onTermsOfUseClicked() {
        Utility.openSite(this, App.getContext().getResources().getString(R.string.terms_of_use_url));
    }

    @Override
    public void onPrivacyPolicyClicked() {
        Utility.openSite(this, App.getContext().getResources().getString(R.string.privacy_policy_url));
    }

    @Override
    public void onSupportClicked() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{App.getContext().getResources().getString(R.string.email_feedback)});
        i.putExtra(Intent.EXTRA_SUBJECT, "User #" + App.getCurrentUser().username);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {}
    }

    @Override
    public void onTelegramClicked() {
        Utility.openSite(this, App.getContext().getResources().getString(R.string.telegram_url));
    }

    @Override
    public void onBuyBitcoins() {
        Intent intent = new Intent(this, BuyBitcoinActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickGame() {
        AppPreferenceManager.getInstance().increaseGameDialogAmount();

        gameDialog = GameDialog.show(this, new GameDialog.GameDialogCallback() {
            @Override
            public void onOpenDescrLink() {
                Utility.openSite(SettingsActivity.this, App.getContext().getResources().getString(R.string.game_rules_url));
            }

            @Override
            public void onRateOnGP() {
                onRateGoneToGP = true;
                gpTime = System.currentTimeMillis();

                Uri uri = Uri.parse("market://details?id=" + App.getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + App.getContext().getPackageName())));
                }
            }
        });
    }

    public AbstructSetting getSettingView(int type){
        switch (type){
            case SettingProvider.SettingModel.TYPE_HEADER: {
                View item = LayoutInflater.from(this).inflate(R.layout.setting_header_item, null);
                SettingHeader settingView = new SettingHeader(item);
                return settingView;
            }
            case SettingProvider.SettingModel.TYPE_ITEM: {
                View item = LayoutInflater.from(this).inflate(R.layout.setting_item, null);
                SettingView settingView = new SettingView(item);
                return settingView;
            }
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CODE_IMPORT:
                if (resultCode == RESULT_OK){
                    Intent dataBundle = new Intent();
                    dataBundle.putExtra(UPDATE, true);
                    setResult(RESULT_OK, dataBundle);
                    finish();
                }
                break;
        }
    }

    protected class SettingView extends AbstructSetting{

        @BindView(R.id.ivIcon)
        public ImageView ivIcon;

        @BindView(R.id.tvTitle)
        public TextView tvTitle;

        @BindView(R.id.tvDescr)
        public TextView tvDescr;

        @BindView(R.id.tvRight)
        public TextView tvRight;

        public SettingView(View parent){
            super(parent);
            ButterKnife.bind(this, parent);
        }

        @Override
        public void bind(final SettingProvider.SettingModel settingModel){
            ivIcon.setImageResource(settingModel.icon);
            Utility.setDrawableColor(ivIcon, App.getContext().getResources().getColor(R.color.text_color));

            tvTitle.setText(settingModel.title);
            if (settingModel.descr == null){
                tvDescr.setVisibility(View.GONE);
            } else {
                tvDescr.setText(settingModel.descr);
            }

            if (settingModel.rihgtValue != null){
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText(settingModel.rihgtValue);
            } else {
                tvRight.setVisibility(View.GONE);
            }

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bind(settingModel);

                    settingModel.listener.onClick(view);
                }
            });
        }
    }

    protected class SettingHeader extends AbstructSetting{

        @BindView(R.id.tvTitle)
        public TextView tvTitle;

        public SettingHeader(View parent){
            super(parent);
            ButterKnife.bind(this, parent);
        }

        @Override
        public void bind(SettingProvider.SettingModel settingModel){
            tvTitle.setText(settingModel.title);
        }
    }

    protected abstract class AbstructSetting{

        protected View parent;

        public AbstructSetting(View parent){
            this.parent = parent;
        }

        public abstract void bind(SettingProvider.SettingModel settingModel);

    }

    @OnClick(R.id.abBack)
    public void onBackClick(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
