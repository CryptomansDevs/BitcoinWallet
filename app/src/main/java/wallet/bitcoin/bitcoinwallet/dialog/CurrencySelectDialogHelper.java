package wallet.bitcoin.bitcoinwallet.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.model.User;

public class CurrencySelectDialogHelper extends BaseDialogHelper {

    @BindView(R.id.usd)
    public Button usd;

    @BindView(R.id.eur)
    public Button eur;

    @BindView(R.id.rub)
    public Button rub;

    private CurrencyDialogCallback callback;

    public interface CurrencyDialogCallback {
        void onSelectedCurrency(int currencyCode);
    }

    public static void show(Context context, CurrencyDialogCallback onPrivateKeyImported) {
        new CurrencySelectDialogHelper(context, onPrivateKeyImported);
    }

    private CurrencySelectDialogHelper(Context context, CurrencyDialogCallback callback) {
        super(context);
        this.callback = callback;

        init();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.currency_select, null);
        ButterKnife.bind(this, view);

        return view;
    }

    private void init() {
        switch (App.getCurrentUser().localCurrency){
            case User.USD_CURRENCY:
                usd.setSelected(true);
                break;
            case User.EUR_CURRENCY:
                eur.setSelected(true);
                break;
            case User.RUR_CURRENCY:
                rub.setSelected(true);
                break;
        }
    }

    @OnClick(R.id.usd)
    public void onUSDClick(){
        usd.setSelected(true);
        eur.setSelected(false);
        rub.setSelected(false);

        callback.onSelectedCurrency(User.USD_CURRENCY);
    }

    @OnClick(R.id.eur)
    public void onEURClick(){
        usd.setSelected(false);
        eur.setSelected(true);
        rub.setSelected(false);

        callback.onSelectedCurrency(User.EUR_CURRENCY);
    }

    @OnClick(R.id.rub)
    public void onRUBClick(){
        usd.setSelected(false);
        eur.setSelected(false);
        rub.setSelected(true);

        callback.onSelectedCurrency(User.RUR_CURRENCY);
    }

}

