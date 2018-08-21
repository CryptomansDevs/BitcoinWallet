package wallet.bitcoin.bitcoinwallet.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Constants;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.model.WalletAddress;
import wallet.bitcoin.bitcoinwallet.rest.response.BalanceResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.GetNewAddressResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.GetTxsResponse;

public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 1;
    private static final int FOOTER = 2;

    private List<WalletAddress> addresses;

    private AddressAdapterCallback callback;

    private boolean showFooter = false;

    public interface AddressAdapterCallback{
        void onAddressClicked(String id, float balance, String cur);
    }

    public AddressAdapter(AddressAdapterCallback callback){
        this.callback = callback;
        this.addresses = new ArrayList<>();
    }

    public List<WalletAddress> getAddresses(){
        return addresses;
    }

    public void setItems(List<BalanceResponse.Result> results) {
        addresses = new ArrayList<>();

        for (BalanceResponse.Result result : results){
            addresses.add(WalletAddress.create(result));
        }
        notifyDataSetChanged();
    }

    public void addItems(List<BalanceResponse.Result> results) {
        for (BalanceResponse.Result result : results){
            addresses.add(WalletAddress.create(result));
        }

        notifyDataSetChanged();
    }

    public void addItem(GetNewAddressResponse.Result newAdress){
        addresses.add(0, WalletAddress.create(newAdress));
        notifyItemInserted(0);
    }

    public void showFooter(){
        if (!showFooter) {
            showFooter = true;
            notifyItemInserted(addresses.size());
        }
    }

    public void hideFooter(){
        if (showFooter) {
            showFooter = false;
            notifyItemRemoved(addresses.size());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM: {
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
                AddressViewHolder addressViewHolder = new AddressViewHolder(parentView);
                return addressViewHolder;
            }
            case FOOTER:
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.txs_footer, parent, false);
                HeaderViewHolder addressViewHolder = new HeaderViewHolder(parentView);
                return addressViewHolder;
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
        switch (type) {
            case ITEM:
                final AddressViewHolder holder = (AddressViewHolder) mHolder;
                holder.bind(addresses.get(pos), pos);
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
        if (position == addresses.size()){
            return FOOTER;
        }

        return ITEM;
    }

    protected class HeaderViewHolder extends RecyclerView.ViewHolder {

        private HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected class AddressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        protected TextView tvTitle;

        @BindView(R.id.tvDescr)
        protected TextView tvDescr;

        @BindView(R.id.ivIcon)
        protected ImageView ivIcon;

        private AddressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

//            ivIcon.setImageResource(R.drawable.circle_10dp);
        }

        public void bind(final WalletAddress address, int pos) {
            Utility.setDrawableColor(ivIcon, App.getContext().getResources().getColor(R.color.colorAccent));

            tvTitle.setText(address.id);
            tvDescr.setText(Utility.getDoubleStringFormatNoSign(address.balance) + " " + address.currency);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onAddressClicked(address.id, address.balance, address.currency);
                }
            });
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
