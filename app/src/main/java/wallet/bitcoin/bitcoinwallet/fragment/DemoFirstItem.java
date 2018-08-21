package wallet.bitcoin.bitcoinwallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.auth.TutorialItemFragment;
import wallet.bitcoin.bitcoinwallet.helper.App;

public class DemoFirstItem extends BaseFragment {

    public static DemoFirstItem newInstance(){
        DemoFirstItem instance = new DemoFirstItem();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) LayoutInflater.from(App.getContext()).inflate(R.layout.tutorial_fisrt, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void showProgress(boolean progressShown) {

    }
}
