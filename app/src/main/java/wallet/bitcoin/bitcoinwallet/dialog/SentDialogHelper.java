package wallet.bitcoin.bitcoinwallet.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Utility;

public class SentDialogHelper extends BaseDialogHelper {

    @BindView(R.id.btnOk)
    public Button btnOk;

    @BindView(R.id.tvTxsId)
    public TextView tvTxsId;

    @BindView(R.id.ivCopy)
    public ImageView ivCopy;

    private String txsId;

    public static void show(Context context, String txsId) {
        new SentDialogHelper(context, txsId);
    }

    private SentDialogHelper(Context context, String txsId) {
        super(context);
        this.txsId = txsId;

        init();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sent_dialog, null);
        ButterKnife.bind(this, view);

        return view;
    }

    public void init() {
        Utility.setDrawableColor(ivCopy, App.getContext().getResources().getColor(R.color.colorPrimary));
        tvTxsId.setText(txsId);
    }

    @OnClick(R.id.btnOk)
    public void btnOkClick() {
        dismiss();
    }

    @OnClick(R.id.ivCopy)
    public void btnCopyClicked() {
        Utility.copy(tvTxsId.getText().toString());
        Toasty.success(getContext(), getContext().getResources().getString(R.string.copied)).show();
    }

}