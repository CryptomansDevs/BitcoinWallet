package wallet.bitcoin.bitcoinwallet.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;

public class ImortWalletDoneDialog extends BaseDialogHelper {

    public interface OnFinished{
        void onFinished();
    }

    private OnFinished callback;

    public static ImortWalletDoneDialog show(Context context, OnFinished callback) {
        return new ImortWalletDoneDialog(context, callback);
    }

    private ImortWalletDoneDialog(Context context, OnFinished callback) {
        super(context);
        this.callback = callback;

        init();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.import_wallet_done_dlg, null);
        ButterKnife.bind(this, view);

        return view;
    }

    private void init() {
    }

    @OnClick(R.id.btnOk)
    public void onbtnImport() {
        callback.onFinished();
        dismiss();
    }
}



