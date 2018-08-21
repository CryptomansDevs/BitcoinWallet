package wallet.bitcoin.bitcoinwallet.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;

public class BuyBitcoinDlgHelper extends BaseDialogHelper {

    @BindView(R.id.btnOk)
    public Button btnOk;

    @BindView(R.id.btnShow)
    public Button btnShow;

    private BuyBitcoinDlgCallback callback;

    public interface BuyBitcoinDlgCallback {
        void onBuyBitcoinsClicked();
        void onShowBitcoinsAddressClicked();
    }

    public static BuyBitcoinDlgHelper show(Context context, BuyBitcoinDlgCallback callback) {
        return new BuyBitcoinDlgHelper(context, callback);
    }

    private BuyBitcoinDlgHelper(Context context, BuyBitcoinDlgCallback callback) {
        super(context);
        this.callback = callback;

        init();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.buy_btc_dlg, null);
        ButterKnife.bind(this, view);

        return view;
    }

    private void init() {
    }

    @OnClick(R.id.btnOk)
    public void btnOkClick() {
        callback.onBuyBitcoinsClicked();
        dismiss();
    }


    @OnClick(R.id.btnShow)
    public void btnShowClick() {
        callback.onShowBitcoinsAddressClicked();
        dismiss();
    }
}

