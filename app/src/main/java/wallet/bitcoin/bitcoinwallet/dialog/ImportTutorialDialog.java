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

public class ImportTutorialDialog extends BaseDialogHelper {

    @BindView(R.id.btnOk)
    public Button btnOk;

    @BindView(R.id.llImport)
    public LinearLayout llImport;

    private ImportTutorialDialogCallback callback;

    public interface ImportTutorialDialogCallback {
        void importNewAddress();
    }

    public static void show(Context context, ImportTutorialDialogCallback callback) {
        new ImportTutorialDialog(context, callback);
    }

    private ImportTutorialDialog(Context context, ImportTutorialDialogCallback callback) {
        super(context);
        this.callback = callback;

        init();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.import_tutorial_dlg, null);
        ButterKnife.bind(this, view);

        return view;
    }

    private void init() {
    }

    @OnClick(R.id.llImport)
    public void llImportClick() {
        callback.importNewAddress();

        dismiss();
    }

    @OnClick(R.id.btnOk)
    public void btnOkClick() {
        dismiss();
    }

}
