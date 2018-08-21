package wallet.bitcoin.bitcoinwallet.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.fragment.SendFragment;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.model.Fee;

public class ConfirmSendDialogHelper extends BaseDialogHelper {

    public interface OnConfirmedCallback{
        void onConfirm(String feeParam);
        void onCancel();
    }

    @BindView(R.id.tvFee)
    public TextView tvFee;

    @BindView(R.id.low)
    public Button low;

    @BindView(R.id.optimal)
    public Button optimal;

    @BindView(R.id.fast)
    public Button fast;

    @BindView(R.id.btnSend)
    public Button btnSend;

    @BindView(R.id.rlFee)
    public RelativeLayout rlFee;

    private String feeParam = Fee.OPTIMAL_FEE;

    private OnConfirmedCallback callback;

    public static ConfirmSendDialogHelper show(Context context, OnConfirmedCallback callback) {
        return new ConfirmSendDialogHelper(context, callback);
    }

    private ConfirmSendDialogHelper(Context context, OnConfirmedCallback callback) {
        super(context);
        this.callback = callback;

        init();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.confirm_send_dlg, null);
        ButterKnife.bind(this, view);

        return view;
    }

    private void init() {
        setFeeVisibility(feeParam);
    }

    public void onFeeChosen(String feeParam) {
        this.feeParam = feeParam;
    }

    @OnClick(R.id.low)
    public void onLowFeeClicked() {
        onFeeChosen(Fee.SLOW_FEE);
        setFeeVisibility(Fee.SLOW_FEE);
    }

    @OnClick(R.id.optimal)
    public void onOptimalFeeClicked() {
        onFeeChosen(Fee.OPTIMAL_FEE);
        setFeeVisibility(Fee.OPTIMAL_FEE);
    }

    @OnClick(R.id.tvFee)
    public void ontvFeelicked() {
    }

    @OnClick(R.id.bottom)
    public void onbottomlicked() {
    }

    @OnClick(R.id.fast)
    public void onFastFeeClicked() {
        onFeeChosen(Fee.FAST_FEE);
        setFeeVisibility(Fee.FAST_FEE);
    }

    @OnClick(R.id.rlFee)
    public void onFeeClicked() {
        dismiss();
    }

    @OnClick(R.id.btnSend)
    public void onSendClicked() {
        callback.onConfirm(feeParam);
        dismiss();
    }

    public void setFeeVisibility(String feeMode) {
        rlFee.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);

        selectFeeBtn(feeMode);
    }

    public void setFeeInVisibility() {
        rlFee.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
    }

    public boolean isFeeVisible() {
        if (rlFee.getVisibility() == View.VISIBLE) {
            return true;
        }

        return false;
    }

    public void selectFeeBtn(String mode) {
        fast.setSelected(false);
        optimal.setSelected(false);
        low.setSelected(false);

        StringBuilder feeValue = new StringBuilder();

        if (mode.equalsIgnoreCase(Fee.FAST_FEE)) {
            fast.setSelected(true);
            feeValue.append(App.getCurrentUser().fastFee)
                    .append(" ")
                    .append(App.getContext().getResources().getString(R.string.satoshi_per_byte));
        } else if (mode.equalsIgnoreCase(Fee.SLOW_FEE)) {
            low.setSelected(true);
            feeValue.append(App.getCurrentUser().slowFee)
                    .append(" ")
                    .append(App.getContext().getResources().getString(R.string.satoshi_per_byte));
        } else {
            optimal.setSelected(true);
            feeValue.append(App.getCurrentUser().optimalFee)
                    .append(" ")
                    .append(App.getContext().getResources().getString(R.string.satoshi_per_byte));
        }

        tvFee.setText(App.getContext().getResources().getString(R.string.fee) + " " + feeValue.toString());
    }
}




