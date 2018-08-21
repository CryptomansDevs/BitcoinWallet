package wallet.bitcoin.bitcoinwallet.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.activity.MainActivity;
import wallet.bitcoin.bitcoinwallet.adapter.AddressAdapter;
import wallet.bitcoin.bitcoinwallet.adapter.SpinnerAdapter;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.UIHelper;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.model.WalletAddress;

public class ReceiveFragment extends BaseFragment {

    @BindView(R.id.ivQr)
    protected ImageView ivQr;

    @BindView(R.id.ivCopy)
    protected ImageView ivCopy;

    @BindView(R.id.tvQr)
    protected TextView tvQr;

    @BindView(R.id.spinnerPresets)
    public Spinner spinnerPresets;

    private List<WalletAddress> address = null;

    private int position = 0;

    public static ReceiveFragment newInstance(){
        ReceiveFragment instance = new ReceiveFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) LayoutInflater.from(App.getContext()).inflate(R.layout.fragment_receive, container, false);
        ButterKnife.bind(this, mRootView);

        showProgress(true);

        ivQr.getLayoutParams().width = UIHelper.getW() - UIHelper.getPixel(60);
        ivQr.getLayoutParams().height = ivQr.getLayoutParams().width;
        ivQr.forceLayout();

        return mRootView;
    }

    public void setCurrentAddress(List<WalletAddress> addresses){
        this.address = addresses;
        init();
        createQr();
    }

    private void init(){
        showProgress(false);

        Utility.setDrawableColor(ivCopy, getResources().getColor(R.color.colorPrimary));

        SpinnerAdapter adapter = createAdapter();
        spinnerPresets.setAdapter(adapter);
        spinnerPresets.requestFocus();
        spinnerPresets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, final int position, long id) {
                ReceiveFragment.this.position = position;
                createQr();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }


    @Override
    public void showProgress(boolean progressShown) {
        super.showProgress(progressShown);

        if (progressShown) {
            ivCopy.setVisibility(View.GONE);
            ivQr.setVisibility(View.GONE);
            tvQr.setVisibility(View.GONE);
            spinnerPresets.setVisibility(View.GONE);
        } else {
            ivCopy.setVisibility(View.VISIBLE);
            ivQr.setVisibility(View.VISIBLE);
            tvQr.setVisibility(View.VISIBLE);
            spinnerPresets.setVisibility(View.VISIBLE);
        }
    }

    public SpinnerAdapter createAdapter() {
        return new SpinnerAdapter(false, 0, getActivity(),
                R.layout.spinner_item,
                address);
    }

    private void createQr(){
        if (address == null || address.size() <= position){
            return;
        }

        Bitmap myBitmap = QRCode.from(address.get(position).id)
                .withSize(UIHelper.getW() - UIHelper.getPixel(60), UIHelper.getW() - UIHelper.getPixel(60))
                .withColor(App.getContext().getResources().getColor(R.color.black),
                Color.parseColor("#f7f7f7")).bitmap();
        ivQr.setImageBitmap(myBitmap);
        tvQr.setText(R.string.copy);
    }

    @OnClick(R.id.tvQr)
    public void onTextViewCopyClicked(){
        onCopyClicked();
    }

    @OnClick(R.id.ivCopy)
    public void onCopyClicked(){
        if (address != null && address.size() > position) {
            Utility.copy(address.get(position).id);

            if (getActivity() != null) {
                Toasty.success(getActivity(), getResources().getString(R.string.copied)).show();
            }
        }
    }
}
