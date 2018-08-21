package wallet.bitcoin.bitcoinwallet.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.adapter.viewholder.BuyBitcoinViewHolder;
import wallet.bitcoin.bitcoinwallet.adapter.viewholder.PayCustomOfferViewHolder;

public class PayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements PayCustomOfferViewHolder.PayCustomAdViewHolderCallback,
        BuyBitcoinViewHolder.BuyBitcoinViewHolderCallback {

    public static final int BITCOIN = 1;
    public static final int OFFER = 12;

    private List<PayModel> mPayModels;
    private boolean networkAvailiable;
    private PayAdapterCallback callback;

    public static class PayModel {

        public static final int OFFER = 3;
        public static final int BITCOIN = 4;

        public int type;
        public int icon;
        public String iconUrl;
        public String url;

        public String titleId;
        public String descrId;
        public String btnTextId;

        public static PayModel createBitcoin(String url, String titleId, String descrId, String btnTextId, int icon) {
            PayModel payModel = new PayModel();
            payModel.url = url;
            payModel.type = BITCOIN;
            payModel.titleId = titleId;
            payModel.descrId = descrId;
            payModel.btnTextId = btnTextId;
            payModel.icon = icon;

            return payModel;
        }
    }

    public interface PayAdapterCallback {
        void onGoToSite(String url);
    }

    public PayAdapter(PayAdapterCallback callback) {
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BITCOIN:
                return new BuyBitcoinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_adapter_item, parent, false), this);
            case OFFER:
                return new PayCustomOfferViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_adapter_item, parent, false), this);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case BITCOIN:
                ((BuyBitcoinViewHolder) holder).bind(mPayModels.get(position));
                break;
            case OFFER:
                ((PayCustomOfferViewHolder) holder).bind(mPayModels.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        PayModel payModel = mPayModels.get(position);

        if (payModel.type == PayModel.OFFER) {
            return OFFER;
        }

        return BITCOIN;
    }

    @Override
    public int getItemCount() {
        return mPayModels == null ? 0 : mPayModels.size();
    }

    public void setPayModels(List<PayModel> payModels) {
        mPayModels = payModels;
        notifyDataSetChanged();
    }

    public void addPayModelToBeginning(PayModel payModels) {
        mPayModels.add(0, payModels);
        notifyItemInserted(0);
    }

    public void addPayModel(int pos, PayModel payModels) {
        mPayModels.add(pos, payModels);
        notifyItemInserted(pos);
    }

    @Override
    public void onClick(BuyBitcoinViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() >= 0 &&
                viewHolder.getAdapterPosition() < mPayModels.size()) {
            PayModel payModel = mPayModels.get(viewHolder.getAdapterPosition());

            callback.onGoToSite(payModel.url);
        }
    }

    @Override
    public void onClick(PayCustomOfferViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() >= 0 &&
                viewHolder.getAdapterPosition() < mPayModels.size()) {
            PayModel payModel = mPayModels.get(viewHolder.getAdapterPosition());

            mPayModels.remove(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());

            callback.onGoToSite(payModel.url);
        }
    }
}


