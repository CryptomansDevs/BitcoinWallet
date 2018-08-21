package wallet.bitcoin.bitcoinwallet.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;

public class TutorialFirstItem extends TutorialItemFragment {

    public static final String INDEX = "index";

    public static final int ICON_SEND = 1;
    public static final int ICON_TRANSACT = 2;
    public static final int ICON_PIN = 3;

    @BindView(R.id.tvTextDescr)
    protected TextView tvTextDescr;

    @BindView(R.id.tvTextTitle)
    protected TextView tvTextTitle;

    @BindView(R.id.parent)
    protected LinearLayout parent;


    public static TutorialFirstItem newInstance(int index){
        TutorialFirstItem instance = new TutorialFirstItem();

        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) LayoutInflater.from(App.getContext()).inflate(R.layout.tutorial_fisrt, container, false);
        ButterKnife.bind(this, mRootView);

        int val = this.getArguments().getInt(INDEX);
        switch (val){
            case ICON_SEND:
                parent.setBackgroundResource(R.drawable.prevone);
                tvTextTitle.setText(R.string.onboarding_first_title);
                tvTextDescr.setText(R.string.onboarding_first_descr);
                break;
            case ICON_TRANSACT:
                parent.setBackgroundResource(R.drawable.prevthree);
                tvTextTitle.setText(R.string.onboarding_second_title);
                tvTextDescr.setText(R.string.onboarding_second_descr);
                break;
            case ICON_PIN:
                parent.setBackgroundResource(R.drawable.prevtwo);
                tvTextTitle.setText(R.string.onboarding_third_title);
                tvTextDescr.setText(R.string.onboarding_third_descr);
                break;
        }

        return mRootView;
    }

}
