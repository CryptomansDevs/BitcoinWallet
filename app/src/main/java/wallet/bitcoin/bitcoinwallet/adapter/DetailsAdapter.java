package wallet.bitcoin.bitcoinwallet.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.choota.dev.ctimeago.TimeAgo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import es.dmoral.toasty.Toasty;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.rest.response.GetTxsResponse;

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 1;
    private static final int FOOTER = 2;

    private List<GetTxsResponse.Result> addresses;

    private String myAddress;

    private boolean showFooter = false;

    public DetailsAdapter(String myAddress) {
        this.myAddress = myAddress;
        this.addresses = new ArrayList<>();
    }

    public void setItems(List<GetTxsResponse.Result> results) {
        this.addresses = results;
        notifyDataSetChanged();
    }

    public void addItems(List<GetTxsResponse.Result> results) {
        this.addresses.addAll(results);
        notifyDataSetChanged();
    }

    public void showFooter(){
        showFooter = true;
        notifyItemInserted(addresses.size());
    }

    public void hideFooter(){
        showFooter = false;
        notifyItemRemoved(addresses.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == addresses.size()){
            return FOOTER;
        }

        return ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM: {
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.txs_item, parent, false);
                DetailsViewHolder addressViewHolder = new DetailsViewHolder(parentView);
                return addressViewHolder;
            }

            case FOOTER: {
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.txs_footer, parent, false);
                HeaderViewHolder addressViewHolder = new HeaderViewHolder(parentView);
                return addressViewHolder;
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
                final DetailsViewHolder holder = (DetailsViewHolder) mHolder;
                holder.bind(addresses.get(pos));
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

    protected class HeaderViewHolder extends RecyclerView.ViewHolder {

        private HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected class DetailsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvStatus)
        protected TextView tvStatus;

        @BindView(R.id.tvAddresses)
        protected TextView tvAddresses;

        @BindView(R.id.tvChangeBalanceInUsd)
        protected TextView tvChangeBalanceInUsd;

        @BindView(R.id.tvSendOrReceived)
        protected TextView tvSendOrReceived;

        @BindView(R.id.tvTime)
        protected TextView tvTime;

        @BindView(R.id.txsId)
        protected TextView txsId;

        @BindView(R.id.tvChangeBalance)
        protected TextView tvChangeBalance;

        @BindView(R.id.ivIcon)
        protected ImageView ivIcon;

        @BindView(R.id.mainParent)
        protected RelativeLayout mainParent;

        private DetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvAddresses.setVisibility(View.GONE);
        }

        @OnLongClick(R.id.mainParent)
        public boolean onLongClickMainParent(){
            Utility.copy(txsId.getText().toString());
            Toasty.success(mainParent.getContext(), App.getContext().getResources().getString(R.string.txs_copied)).show();
            return true;
        }

        public void bind(final GetTxsResponse.Result address) {
            TimeAgo timeAgo = new TimeAgo().locale(itemView.getContext());
            java.util.Date time = new java.util.Date( (long) Long.parseLong(address.date) * 1000);
            String result = timeAgo.getTimeAgo(time);

            tvTime.setText(result);
            txsId.setText(address.txid);

            tvStatus.setText(address.confirmed ? App.getContext().getResources().getString(R.string.confirmed) :
                    App.getContext().getResources().getString(R.string.not_confirmed));

            boolean isSent = false;
            if (address.amount < 0){
                tvSendOrReceived.setText(R.string.sent);
                tvSendOrReceived.setTextColor(App.getContext().getResources().getColor(R.color.red));
                ivIcon.setImageResource(R.drawable.send);
                Utility.setDrawableColor(ivIcon, App.getContext().getResources().getColor(R.color.red));
                tvChangeBalance.setTextColor(App.getContext().getResources().getColor(R.color.red));
            } else {
                tvSendOrReceived.setText(R.string.received);
                tvSendOrReceived.setTextColor(App.getContext().getResources().getColor(R.color.green));
                ivIcon.setImageResource(R.drawable.get);
                Utility.setDrawableColor(ivIcon, App.getContext().getResources().getColor(R.color.green));
                tvChangeBalance.setTextColor(App.getContext().getResources().getColor(R.color.green));
            }

//            if (isSent) {
//                StringBuilder adresses = new StringBuilder();
//                for (GetTxsResponse.InputOutput inputOutput : address.outputs){
//                    adresses.append(inputOutput.address).append(" : ").append(inputOutput.amount).append("\n");
//                }
//                tvAddresses.setText(adresses);
//            } else {
//
//                StringBuilder adresses = new StringBuilder();
//                for (GetTxsResponse.InputOutput inputOutput : address.inputs){
//                    adresses.append(inputOutput.address).append(" : ").append(inputOutput.amount).append("\n");
//                }
//                tvAddresses.setText(adresses);
//            }

            tvChangeBalance.setText(Utility.getDoubleStringFormat(address.amount));
            float curRate = App.getCurrentUser().getRate();
            tvChangeBalanceInUsd.setText(Utility.getDoubleStringFormatForCurrencyWithSign(curRate * address.amount) + " " + App.getCurrentUser().getRateName());

        }

    }

    @Override
    public int getItemCount() {
        if (addresses == null) {
            if (showFooter){
                return 1;
            }

            return 0;
        }
        return addresses.size() + (showFooter ? 1 : 0);
    }

}

