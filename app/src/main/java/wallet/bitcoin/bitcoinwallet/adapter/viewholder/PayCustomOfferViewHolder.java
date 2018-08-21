package wallet.bitcoin.bitcoinwallet.adapter.viewholder;

import android.view.View;

import com.squareup.picasso.Picasso;

import wallet.bitcoin.bitcoinwallet.adapter.PayAdapter;

public class PayCustomOfferViewHolder extends BaseViewHolder {

    private PayCustomAdViewHolderCallback callback;

    public interface PayCustomAdViewHolderCallback{
        void onClick(PayCustomOfferViewHolder viewHolder);
    }

    public PayCustomOfferViewHolder(View itemView, PayCustomAdViewHolderCallback callback) {
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

        Picasso.with(ivIcon.getContext()).cancelRequest(ivIcon);
        Picasso.with(ivIcon.getContext())
                .load(miner.iconUrl)
                .into(ivIcon);

        installButton.setText(miner.btnTextId);
        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(PayCustomOfferViewHolder.this);
            }
        });
    }
}

