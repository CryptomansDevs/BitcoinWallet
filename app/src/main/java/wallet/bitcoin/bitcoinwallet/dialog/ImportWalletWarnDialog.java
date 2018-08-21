package wallet.bitcoin.bitcoinwallet.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;

public class ImportWalletWarnDialog extends BaseDialogHelper {

    @BindView(R.id.btnImport)
    public Button btnImport;

    @BindView(R.id.btnCancel)
    public Button btnCancel;

    private ImportWalletWarnDialogCallback callback;

    public interface ImportWalletWarnDialogCallback {
        void onDoImport();
        void onCancelImport();
    }

    public static ImportWalletWarnDialog show(Context context, ImportWalletWarnDialogCallback callback) {
        return new ImportWalletWarnDialog(context, callback);
    }

    private ImportWalletWarnDialog(Context context, ImportWalletWarnDialogCallback callback) {
        super(context);
        this.callback = callback;

        init();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.import_wallet_warn_dlg, null);
        ButterKnife.bind(this, view);

        return view;
    }

    private void init() {
    }

    @OnClick(R.id.btnImport)
    public void onbtnImport() {
        callback.onDoImport();
        dismiss();
    }


    @OnClick(R.id.btnCancel)
    public void onbtnCancel() {
        callback.onCancelImport();
        dismiss();
    }
}


