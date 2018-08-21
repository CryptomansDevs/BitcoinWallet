package wallet.bitcoin.bitcoinwallet.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.choota.dev.ctimeago.TimeAgo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.internal.Util;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.fragment.SendFragment;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.UIHelper;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.model.WalletAddress;
import wallet.bitcoin.bitcoinwallet.rest.response.GetTxsResponse;

public class SendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 31;
    private static final int FOOTER = 32;
    private static final int ITEM = 33;

    private List<SendModel> addresses;

    private OnSendAdapterCallback callback;

    private SendHeaderHolder sendHeaderHolder;

    public interface OnSendAdapterCallback{
        void onQrClicked(int pos);
        void hideKeyboard();
    }

    public static class SendModel{
        public String address;
        public float amount;
        boolean isFooter = false;
        boolean isHeader = false;

        private SendModel(){}

        public boolean isModel(){
            if (!isHeader && !isFooter){
                return true;
            }

            return false;
        }

        public static SendModel create(){
            return new SendModel();
        }

        public static SendModel createFooter(){
            SendModel sendModel = new SendModel();
            sendModel.isFooter = true;

            return sendModel;
        }

        public static SendModel createHeader(){
            SendModel sendModel = new SendModel();
            sendModel.isHeader = true;

            return sendModel;
        }
    }

    public SendAdapter(OnSendAdapterCallback callback) {
        this.callback = callback;
        this.addresses = new ArrayList<>();
    }

    public void setItems(List<SendModel> results) {
        this.addresses = results;
        notifyDataSetChanged();
    }

    public List<SendModel> getItems(){
        return addresses;
    }

    public void updateItem(int pos, String barcode){
        SendModel sendModel = addresses.get(pos);
        sendModel.address = barcode;
        notifyItemChanged(pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM:{
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_item, parent, false);
                SendViewHolder addressViewHolder = new SendViewHolder(parentView);
                return addressViewHolder;
            }

            case FOOTER: {
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_footer, parent, false);
                SendFooterHolder addressViewHolder = new SendFooterHolder(parentView);
                return addressViewHolder;
            }

            case HEADER: {
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_header, parent, false);
                sendHeaderHolder = new SendHeaderHolder(parentView);
                return sendHeaderHolder;
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mHolder, int position) {
        final int pos = getHolderPosition(mHolder, position);

        if (pos < 0) {
            return;
        }

        int type = getItemViewType(pos);
        switch (type){
            case ITEM:
                final SendViewHolder holder = (SendViewHolder) mHolder;
                holder.bind(addresses.get(pos), pos);
                break;

            case FOOTER:

                break;

            case HEADER:
                final SendHeaderHolder holder1 = (SendHeaderHolder) mHolder;
                holder1.bind(addresses.get(pos), pos);
                break;
        }

    }

    private int getHolderPosition(RecyclerView.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        if (pos < 0) {
            pos = position;
        }

        return pos;
    }

    @Override
    public int getItemViewType(int position) {
        if (addresses.get(position).isFooter){
            return FOOTER;
        }

        if (addresses.get(position).isHeader){
            return HEADER;
        }

        return ITEM;
    }

    @Override
    public int getItemCount() {
        if (addresses == null) {
            return 0;
        }
        return addresses.size();
    }

    public void showProgress(boolean show){
        if (sendHeaderHolder != null) {
            sendHeaderHolder.showProgress(show);
        }
    }

    public void initHeader(float balance, List<WalletAddress> addresses){
        if (sendHeaderHolder != null) {
            sendHeaderHolder.init(balance, addresses);
        }
    }

    public String getSelectedAddress(){
        if (sendHeaderHolder != null) {
            return sendHeaderHolder.selectedId;
        }

        return null;
    }

    public boolean getIsLeftOver(){
        if (sendHeaderHolder != null) {
            return sendHeaderHolder.cbLeftOver.isChecked();
        }

        return true;
    }

    protected class SendHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.spinnerPresets)
        public Spinner spinnerPresets;

        @BindView(R.id.tvDescrTransaction)
        public TextView tvDescrTransaction;

        @BindView(R.id.tvLeftover)
        public TextView tvLeftover;

        @BindView(R.id.cbLeftOver)
        public CheckBox cbLeftOver;

        public String selectedId = null;

        public SendHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cbLeftOver.setButtonDrawable(Utility.getCheckBoxDrawable());
        }

        public void init(float balance, final List<WalletAddress> addresses){
            SpinnerAdapter adapter = createAdapter(balance, addresses);
            spinnerPresets.setAdapter(adapter);
            spinnerPresets.requestFocus();

            spinnerPresets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, final int position, long id) {
                    if (position == 0){
                        selectedId = null;
                    } else {
                        selectedId = addresses.get(position - 1).id;
                    }
                    onNewItemSelected();

                    settvDescrTransactionText(position == 0);
                    checkTvLeftOver();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            settvDescrTransactionText(true);
            checkTvLeftOver();
            onNewItemSelected();
        }

        private void onNewItemSelected() {
//            if (address == null || address.size() <= position) {
//                return;
//            }
        }


        private SpinnerAdapter createAdapter(float balance, List<WalletAddress> addresses) {
            return new SpinnerAdapter(true, balance, itemView.getContext(),
                    R.layout.spinner_item,
                    addresses);
        }

        public void showProgress(boolean show){
            if (show){
                spinnerPresets.setVisibility(View.GONE);
            } else {
                spinnerPresets.setVisibility(View.VISIBLE);
            }
        }

        private void checkTvLeftOver() {
            if (cbLeftOver.isChecked()) {
                tvLeftover.setText(R.string.leftover_return_to_new_address);
            } else {
                tvLeftover.setText(R.string.leftover_return_to_this_address);
            }
        }

        @OnCheckedChanged(R.id.cbLeftOver)
        public void cbLeftOverChanged() {
            checkTvLeftOver();
        }

        private void settvDescrTransactionText(boolean isFromWallet) {
            if (isFromWallet) {
                tvDescrTransaction.setText(R.string.from_wallet);
                cbLeftOver.setChecked(true);
                cbLeftOver.setAlpha(0.4f);
                cbLeftOver.setEnabled(false);
            } else {
                tvDescrTransaction.setText(R.string.from_address);
                cbLeftOver.setChecked(true);
                cbLeftOver.setAlpha(1f);
                cbLeftOver.setEnabled(true);
            }
        }

        public void bind(final SendModel address, int pos) {

        }
    }

    protected class SendFooterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llAdd)
        protected LinearLayout llAdd;

        @BindView(R.id.ivAdd)
        protected ImageView ivAdd;

        public SendFooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            Utility.setDrawableColor(ivAdd, App.getContext().getResources().getColor(R.color.colorAccent));
        }

        @OnClick(R.id.llAdd)
        public void onAddClicked(){
            addresses.add(addresses.size() - 1, SendModel.create());
            notifyItemInserted(addresses.size() - 2);
            notifyItemChanged(addresses.size() - 1);
        }
    }

    protected class SendViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivClose)
        protected ImageView ivClose;

        @BindView(R.id.etAddress)
        protected EditText etAddress;

        @BindView(R.id.etAmount)
        protected EditText etAmount;

        @BindView(R.id.tvAmountInUsd)
        protected EditText tvAmountInUsd;

        @BindView(R.id.tvUsd)
        protected TextView tvUsd;

        @BindView(R.id.ivQr)
        protected ImageView ivQr;

        private SendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            Utility.setDrawableColor(ivQr, App.getContext().getResources().getColor(R.color.text_color));
            Utility.setDrawableColor(ivClose, App.getContext().getResources().getColor(R.color.text_color));

            etAddress.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        //do here your stuff f
                        callback.hideKeyboard();
                        return true;
                    }
                    return false;
                }
            });

            etAmount.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        //do here your stuff f
                        callback.hideKeyboard();
                        return true;
                    }
                    return false;
                }
            });

            tvAmountInUsd.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        //do here your stuff f
                        callback.hideKeyboard();
                        return true;
                    }
                    return false;
                }
            });
        }

        public void bind(final SendModel address, int pos) {
            if (pos == 1){
                ivClose.setVisibility(View.GONE);
            } else {
                ivClose.setVisibility(View.VISIBLE);
            }

            etAddress.setText(address.address);
            etAmount.setText(""+address.amount);
            setTexts(address.amount);

            etAddress.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    address.address = etAddress.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });

            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (etAmount.isFocused()) {
                        try {
                            address.amount = Float.parseFloat(etAmount.getText().toString());
                        } catch (Throwable t) {
                            address.amount = 0;
                        }

                        setTexts(address.amount);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });


            tvAmountInUsd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (tvAmountInUsd.isFocused()){
                        try {
                            float amountInUsd = Float.parseFloat(tvAmountInUsd.getText().toString());
                            float curRate = App.getCurrentUser().getRate();

                            address.amount = amountInUsd / curRate;
                        } catch (Throwable t){
                            address.amount = 0;
                        }

                        setBTCTexts(address.amount);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
        }

        private void setTexts(double amount){
            float curRate = App.getCurrentUser().getRate();
            tvAmountInUsd.setText(Utility.getDoubleStringFormatForCurrency(curRate * amount));
            tvUsd.setText(App.getCurrentUser().getRateName());
        }

        private void setBTCTexts(double amount){
            etAmount.setText(Utility.getDoubleStringFormatNoSign(amount));
        }

        @OnClick(R.id.ivQr)
        public void onQRClicked(){
            if (getAdapterPosition() >= 0) {
                callback.onQrClicked(getAdapterPosition());
            }
        }

        @OnClick(R.id.ivClose)
        public void onCloseClicked(){
            int pos = this.getAdapterPosition();
            if (pos >= 0){
                addresses.remove(pos);
                notifyItemRemoved(pos);
            }
        }

    }

}


