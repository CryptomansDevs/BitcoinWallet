package wallet.bitcoin.bitcoinwallet.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;

public class ImportAddressDialog extends BaseDialogHelper{

    @BindView(R.id.btnOk)
    public Button btnOk;

    @BindView(R.id.etPrivate)
    public EditText etPrivate;

    private ImportAddressDialogCallback onPrivateKeyImported;

    public interface ImportAddressDialogCallback{
        void onPrivateKeyImported(String privateKey);
    }

    public static void show(Context context, ImportAddressDialogCallback onPrivateKeyImported){
        new ImportAddressDialog(context, onPrivateKeyImported);
    }

    private ImportAddressDialog(Context context, ImportAddressDialogCallback onPrivateKeyImported){
        super(context);
        this.onPrivateKeyImported = onPrivateKeyImported;

        init();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.import_address, null);
        ButterKnife.bind(this, view);

        return view;
    }

    private void init(){}

    @OnClick(R.id.btnOk)
    public void btnOkClick(){
        onPrivateKeyImported.onPrivateKeyImported(etPrivate.getText().toString());

        dismiss();
    }

}
