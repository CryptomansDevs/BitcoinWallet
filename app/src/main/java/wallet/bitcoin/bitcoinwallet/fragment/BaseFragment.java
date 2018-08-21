package wallet.bitcoin.bitcoinwallet.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import wallet.bitcoin.bitcoinwallet.activity.MainActivity;

public abstract class BaseFragment extends Fragment {

    protected ViewGroup mRootView;

    public View getRootView() {
        return mRootView;
    }

    public void showProgress(boolean isProgress){
        if (getActivity() != null){
            ((MainActivity) getActivity()).showProgress(isProgress);
        }
    }

    public void onVisibleOnScreen(){}

    public void onInvisibleOnScreen(){}

}
