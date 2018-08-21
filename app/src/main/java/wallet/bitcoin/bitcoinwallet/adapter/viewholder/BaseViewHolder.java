package wallet.bitcoin.bitcoinwallet.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wallet.bitcoin.bitcoinwallet.R;

public class BaseViewHolder  extends RecyclerView.ViewHolder {

    @BindView(R.id.tvTitle)
    protected TextView title;

    @BindView(R.id.tvDescr)
    protected TextView tvDescr;

    @BindView(R.id.ivIcon)
    protected ImageView ivIcon;

    @BindView(R.id.tvSponsored)
    protected TextView sponsorred;

    @BindView(R.id.btn_install)
    protected Button installButton;

    @BindView(R.id.flContainerInteraction)
    protected FrameLayout flContainerInteraction;

    @BindView(R.id.parent)
    protected ViewGroup parent;

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setVisible(boolean visible){
        if (visible) {
            parent.setVisibility(View.VISIBLE);
        } else {
            parent.setVisibility(View.GONE);
        }
    }

    public void setEnabled(boolean enabled){
        if (enabled){
            installButton.setEnabled(true);
            installButton.setSelected(false);
            installButton.setClickable(true);
        } else {
            installButton.setEnabled(false);
            installButton.setSelected(true);
            installButton.setClickable(false);
        }
    }

}
