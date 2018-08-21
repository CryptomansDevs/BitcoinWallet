package wallet.bitcoin.bitcoinwallet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.adapter.PayAdapter;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Utility;

public class BuyBitcoinActivity extends AppCompatActivity implements PayAdapter.PayAdapterCallback {

    @BindView(R.id.abBack)
    protected ImageView abBack;

    @BindView(R.id.rvOptions)
    protected RecyclerView rvOptions;

    private PayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarColor(this);
        setContentView(R.layout.buy_btc_activity);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Utility.setDrawableColor(abBack, App.getContext().getResources().getColor(R.color.white));

        LinearLayoutManager quickLinkLayoutManager = new LinearLayoutManager(rvOptions.getContext(), LinearLayoutManager.VERTICAL, false);
        rvOptions.setLayoutManager(quickLinkLayoutManager);
        rvOptions.setHasFixedSize(true);

        adapter = new PayAdapter(this);
        rvOptions.setAdapter(adapter);

        List<PayAdapter.PayModel> payModels = new ArrayList<>();

        payModels.add(PayAdapter.PayModel.createBitcoin("https://exmo.me",
                getString(R.string.exmo_title),
                getString(R.string.exmo_title_descr),
                getString(R.string.str_goto),
                R.drawable.exmo));

        payModels.add(PayAdapter.PayModel.createBitcoin("https://www.binance.com",
                getString(R.string.binance_title),
                getString(R.string.binance_title_descr),
                getString(R.string.str_goto),
                R.drawable.binance));

        payModels.add(PayAdapter.PayModel.createBitcoin("https://yobit.io",
                getString(R.string.youbit_title),
                getString(R.string.youbit_title_descr),
                getString(R.string.str_goto),
                R.drawable.yobit));

        adapter.setPayModels(payModels);
    }

    @OnClick(R.id.abBack)
    public void onBackClick() {
        onBackPressed();
    }

    @Override
    public void onGoToSite(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        try {
            startActivity(i);
        } catch (Throwable t) {
        }
    }
}
