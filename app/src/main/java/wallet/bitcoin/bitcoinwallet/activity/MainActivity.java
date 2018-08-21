package wallet.bitcoin.bitcoinwallet.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.dialog.GameDialog;
import wallet.bitcoin.bitcoinwallet.events.RatesUpdateEvent;
import wallet.bitcoin.bitcoinwallet.fragment.BaseFragment;
import wallet.bitcoin.bitcoinwallet.fragment.MainFragment;
import wallet.bitcoin.bitcoinwallet.fragment.ReceiveFragment;
import wallet.bitcoin.bitcoinwallet.fragment.SendFragment;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.CustomViewPager;
import wallet.bitcoin.bitcoinwallet.helper.OnFinishCallback;
import wallet.bitcoin.bitcoinwallet.helper.RatesHelper;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.main.BottomMenuControl;
import wallet.bitcoin.bitcoinwallet.model.WalletAddress;

public class MainActivity extends AppCompatActivity implements BottomMenuControl.MenuCallback {

    public static final int FIRST_TAB_NUM = 0;
    public static final int SECOND_TAB_NUM = 1;
    //    public static final int THIRD_TAB_NUM = 2;
    public static final int FOURTH_TAB_NUM = 2;
    private static final int INITIAL_TAB_INDEX = SECOND_TAB_NUM;

    private static final int CODE_SETTINGS_ACTIVITY = 32;


    @BindView(R.id.llSend)
    public LinearLayout llSend;

    @BindView(R.id.llMain)
    public LinearLayout llMain;

    @BindView(R.id.llReceive)
    public LinearLayout llReceive;

    @BindView(R.id.viewpager)
    public CustomViewPager mViewPager;

    @BindView(R.id.bottomBar)
    public LinearLayout bottomBar;

    @BindView(R.id.abSettings)
    public ImageView abSettings;

    @BindView(R.id.ivSendBtn)
    public ImageView ivSendBtn;

    @BindView(R.id.swiperefresh)
    public SwipeRefreshLayout mPullToRefreshView;

    @BindView(R.id.abName)
    public TextView abName;

    @BindView(R.id.rlProgress)
    public SmoothProgressBar rlProgress;

    private float lastBalance = 0;

    private BottomMenuControl bottomMenuControl;

    private MainAdapter adapter;

    private BaseFragment lastFragment;

    private List<WalletAddress> results = new ArrayList<>();

    private boolean onRateGoneToGP = false;
    private long gpTime;
    private GameDialog gameDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarColor(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();

//        if (System.currentTimeMillis() - AppPreferenceManager.getInstance().getTimeInstalled() > 1 * 60 * 60 * 1000 &&
//                (AppPreferenceManager.getInstance().getTicket() == null || AppPreferenceManager.getInstance().getTicket().length() == 0) &&
//                AppPreferenceManager.getInstance().getGameDialogAmount() < 3) {
//            AppPreferenceManager.getInstance().increaseGameDialogAmount();
//            showGameDialog();
//        }
    }

    public void showProgress(boolean progressShown) {
        if (rlProgress == null) {
            return;
        }

        if (progressShown) {
            rlProgress.setVisibility(View.VISIBLE);
        } else {
            rlProgress.setVisibility(View.GONE);
        }
    }

    private void showGameDialog() {
        gameDialog = GameDialog.show(this, new GameDialog.GameDialogCallback() {
            @Override
            public void onOpenDescrLink() {
                Utility.openSite(MainActivity.this, App.getContext().getResources().getString(R.string.game_rules_url));
            }

            @Override
            public void onRateOnGP() {
                onRateGoneToGP = true;
                gpTime = System.currentTimeMillis();

                Uri uri = Uri.parse("market://details?id=" + App.getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + App.getContext().getPackageName())));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (onRateGoneToGP && System.currentTimeMillis() - gpTime > 10 * 1000) {
            if (gameDialog != null) {
                gameDialog.onReceiveTicket();
            }
            onRateGoneToGP = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void setBalance(float balance) {
        lastBalance = balance;
    }

    public float getBalance() {
        return lastBalance;
    }

    private void init() {
        Utility.setDrawableColor(abSettings, App.getContext().getResources().getColor(R.color.white));
        Utility.setDrawableColor(ivSendBtn, App.getContext().getResources().getColor(R.color.white));

        RatesHelper.getRates();

        mPullToRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RatesHelper.getRates();

                if (mViewPager.getCurrentItem() == SECOND_TAB_NUM) {
                    ((MainFragment) adapter.getItem(SECOND_TAB_NUM)).getAllTxs(new OnFinishCallback() {
                        @Override
                        public void onFinished() {
                            mPullToRefreshView.setRefreshing(false);
                        }
                    });

                    ((MainFragment) adapter.getItem(SECOND_TAB_NUM)).resetOffset();
                    ((MainFragment) adapter.getItem(SECOND_TAB_NUM)).getBalance(new OnFinishCallback() {
                        @Override
                        public void onFinished() {
                            mPullToRefreshView.setRefreshing(false);
                        }
                    });

                } else {
                    mPullToRefreshView.setRefreshing(false);
                }
            }
        });
        mPullToRefreshView.setColorSchemeResources(R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent);


        bottomMenuControl = new BottomMenuControl(bottomBar, this);

        adapter = new MainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                bottomMenuControl.onPageScrolled(positionOffset, position);
            }

            @Override
            public void onPageSelected(int position) {
                onOpenedFragment(position);

                switch (position) {
                    case FIRST_TAB_NUM:
                        abName.setText(R.string.send);
                        ivSendBtn.setVisibility(View.VISIBLE);
                        abSettings.setVisibility(View.GONE);
                        break;
                    case SECOND_TAB_NUM:
                        abName.setText(R.string.wallet_name);
                        ivSendBtn.setVisibility(View.GONE);
                        abSettings.setVisibility(View.VISIBLE);

//                        App.getHandler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                                ((MainFragment) adapter.getItem(SECOND_TAB_NUM)).onRequestAllTxs();
//                            }
//                        }, 1000);
                        break;
                    case FOURTH_TAB_NUM:
                        abName.setText(R.string.receive);
                        ivSendBtn.setVisibility(View.GONE);
                        abSettings.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });

        mViewPager.setCurrentItem(INITIAL_TAB_INDEX);
        ivSendBtn.setVisibility(View.GONE);
        abSettings.setVisibility(View.VISIBLE);
    }

    private void onOpenedFragment(int pos) {
        if (lastFragment != adapter.getItem(pos)) {
            if (lastFragment != null) {
                lastFragment.onInvisibleOnScreen();
            }
            lastFragment = adapter.getItem(pos);
            lastFragment.onVisibleOnScreen();
        }
    }

    public void getBalance(final OnFinishCallback callback) {
        ((MainFragment) adapter.getItem(SECOND_TAB_NUM)).getBalance(callback);
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (mPullToRefreshView != null) {
            mPullToRefreshView.setEnabled(enable);
        }
    }

    @OnClick(R.id.abSettings)
    public void onSettingsClicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, CODE_SETTINGS_ACTIVITY);
    }

    public void setAddresses(List<WalletAddress> results, float balance) {
        this.results = results;

        if (results != null && results.size() > 0) {
            ((ReceiveFragment) adapter.getItem(FOURTH_TAB_NUM)).setCurrentAddress(this.results);
            ((SendFragment) adapter.getItem(FIRST_TAB_NUM)).setCurrentAddress(this.results, balance);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_SETTINGS_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    ((MainFragment) adapter.getItem(SECOND_TAB_NUM)).onRatesUpdated();

                    if (data != null && data.getBooleanExtra(SettingsActivity.LOGOUT, false)) {
                        Intent intent = new Intent(MainActivity.this, RootActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in_at_once, R.anim.fade_out_at_once);
                        finish();
                    }

                    if (data != null && data.getBooleanExtra(SettingsActivity.UPDATE, false)) {
                        ((MainFragment) adapter.getItem(SECOND_TAB_NUM)).getBalance(null);
                    }
                }
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRatesUpdated(RatesUpdateEvent event) {
        ((MainFragment) adapter.getItem(SECOND_TAB_NUM)).onRatesUpdated();
    }

    @Override
    public void onSetCurrentViewPagerItem(int pos) {
        mViewPager.setCurrentItem(pos, true);
    }

    public static class MainAdapter extends FragmentStatePagerAdapter {

        private static final int SIZE = 3;

        private SparseArray<BaseFragment> list = new SparseArray<>(SIZE);


        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseFragment fragment = (BaseFragment) super.instantiateItem(container, position);
            list.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            list.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return SIZE;
        }

        @Override
        public BaseFragment getItem(int position) {
            switch (position) {
                case 0:
                    if (list.get(0) == null) {
                        list.put(0, SendFragment.newInstance());
                    }
                    return list.get(0);

                case 1:
                    if (list.get(1) == null) {
                        list.put(1, MainFragment.newInstance());
                    }
                    return list.get(1);

//                case 2:
//                    if (list.get(2) == null) {
//                        list.put(2, TransactionFragment.newInstance());
//                    }
//                    return list.get(2);

                case 2:
                    if (list.get(2) == null) {
                        list.put(2, ReceiveFragment.newInstance());
                    }
                    return list.get(2);
            }

            return null;
        }
    }

    @OnClick(R.id.ivSendBtn)
    public void ivSendBtnCLicked() {
        showConfirmSendDialog();
    }

    private void showConfirmSendDialog() {
        ((SendFragment) adapter.getItem(FIRST_TAB_NUM)).showConfirmSendDialog();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
