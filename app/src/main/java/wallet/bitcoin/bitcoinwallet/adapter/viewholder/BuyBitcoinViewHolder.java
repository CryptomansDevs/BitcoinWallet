package wallet.bitcoin.bitcoinwallet.adapter.viewholder;

import android.view.View;

import com.squareup.picasso.Picasso;

import wallet.bitcoin.bitcoinwallet.adapter.PayAdapter;

public class BuyBitcoinViewHolder extends BaseViewHolder {

    private BuyBitcoinViewHolderCallback callback;

    public interface BuyBitcoinViewHolderCallback{
        void onClick(BuyBitcoinViewHolder viewHolder);
    }

    public BuyBitcoinViewHolder(View itemView, BuyBitcoinViewHolderCallback callback) {
        super(itemView);
        this.callback = callback;
    }

    public void bind(final PayAdapter.PayModel miner) {
        if (miner == null){
            return;
        }

        sponsorred.setVisibility(View.GONE);
        title.setText(miner.titleId);
        tvDescr.setText(miner.descrId);

//        Picasso.with(ivIcon.getContext()).cancelRequest(ivIcon);
        ivIcon.setImageResource(miner.icon);
//        Picasso.with(ivIcon.getContext())
//                .load(miner.iconUrl)
//                .into(ivIcon);

        installButton.setText(miner.btnTextId);
        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(BuyBitcoinViewHolder.this);
            }
        });
    }
}


