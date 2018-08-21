package wallet.bitcoin.bitcoinwallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.auth.IndicatorController;
import wallet.bitcoin.bitcoinwallet.auth.TutorialFirstItem;
import wallet.bitcoin.bitcoinwallet.auth.TutorialFourthItem;
import wallet.bitcoin.bitcoinwallet.auth.TutorialItemFragment;
import wallet.bitcoin.bitcoinwallet.auth.facebook.FacebookHelper;
import wallet.bitcoin.bitcoinwallet.auth.facebook.FacebookResponse;
import wallet.bitcoin.bitcoinwallet.auth.facebook.FacebookUser;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.AppPreferenceManager;
import wallet.bitcoin.bitcoinwallet.helper.CustomViewPager;
import wallet.bitcoin.bitcoinwallet.helper.MagicDrawable;
import wallet.bitcoin.bitcoinwallet.helper.UIHelper;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.rest.request.AuthRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.FacebookLoginRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.AuthResponse;

public class AuthActivity extends AppCompatActivity {


    private static final int FIRST_TAB_NUM = 0;
    private static final int SECOND_TAB_NUM = 1;
    private static final int THIRD_TAB_NUM = 2;
    private static final int FOURTH_TAB_NUM = 3;

    public static final String EXTRA = "EXTRA";
    private static final int INITIAL_TAB_INDEX = FIRST_TAB_NUM;

    @BindView(R.id.viewpager)
    public CustomViewPager mViewPager;

    @BindView(R.id.activity_tutorial)
    public View activity_tutorial;

    @BindView(R.id.ivLeft)
    public ImageView ivLeft;

    @BindView(R.id.ivRight)
    public ImageView ivRight;

    @BindView(R.id.tvDone)
    public TextView tvDone;

    private IndicatorController indicatorController;

    private MyAdapter adapter;

    private MagicDrawable rightArrow;
    private boolean refCodeApplyed = false;

    private FacebookHelper facebookHelper;

    @BindView(R.id.pbProgress)
    public ProgressBar pbProgress;

    @BindView(R.id.rlProgress)
    public RelativeLayout rlProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarColorAuth(this);
        setContentView(R.layout.auth_activity);
        ButterKnife.bind(this);

        indicatorController = new IndicatorController(activity_tutorial);

        ivLeft.setImageDrawable(MagicDrawable.createEnabled(App.getContext().getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp),
                getResources().getColor(R.color.disabled_color),
                getResources().getColor(R.color.white)));

        rightArrow = MagicDrawable.createEnabled(App.getContext().getResources().getDrawable(R.drawable.ic_arrow_forward_black_24dp),
                getResources().getColor(R.color.disabled_color),
                getResources().getColor(R.color.white));
        ivRight.setImageDrawable(rightArrow);

        adapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicatorController.onPageScrolled(positionOffset, position);

                switch (position) {
                    case FIRST_TAB_NUM:
                        UIHelper.hideKeyboard(AuthActivity.this);

                        tvDone.setVisibility(View.GONE);
                        ivRight.setVisibility(View.VISIBLE);
                        ivLeft.setVisibility(View.GONE);
                        break;
                    case THIRD_TAB_NUM:

                        tvDone.setVisibility(View.GONE);
                        ivRight.setVisibility(View.VISIBLE);
                        ivLeft.setVisibility(View.VISIBLE);

                        break;
                    case SECOND_TAB_NUM:
                        UIHelper.hideKeyboard(AuthActivity.this);

                        tvDone.setVisibility(View.GONE);
                        ivRight.setVisibility(View.VISIBLE);
                        ivLeft.setVisibility(View.VISIBLE);

                        break;

                    case FOURTH_TAB_NUM:
                        UIHelper.hideKeyboard(AuthActivity.this);

                        tvDone.setVisibility(View.GONE);
                        ivRight.setVisibility(View.GONE);
                        ivLeft.setVisibility(View.VISIBLE);

                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        initFB();

        mViewPager.setCurrentItem(INITIAL_TAB_INDEX);
    }

    public void showProgress(boolean isVisible) {
        if (isVisible) {
            rlProgress.setVisibility(View.VISIBLE);
        } else {
            rlProgress.setVisibility(View.GONE);
        }
    }

    private void initFB() {
        facebookHelper = new FacebookHelper(new FacebookResponse() {
            @Override
            public void onFbSignInFail() {

            }

            @Override
            public void onFbSignInSuccess() {

            }

            @Override
            public void onFbProfileReceived(FacebookUser facebookUser) {
                String name = facebookUser.name;
                String email = facebookUser.email;
                String userId = facebookUser.facebookID;
                String token = AccessToken.getCurrentAccessToken().getToken();

                sendFacebookRegCall(token, userId, email, name);
            }

            @Override
            public void onFBSignOut() {
            }
        }, "name,email", this);
    }

    private void sendFacebookRegCall(String token, String userId, String email, final String name) {
        FacebookLoginRequest facebookLoginRequest = new FacebookLoginRequest();
        facebookLoginRequest.email = email;
        facebookLoginRequest.fbtoken = token;
        facebookLoginRequest.userid = userId;

        Call<AuthResponse> call = App.getRestClient().getAuthService().loginViaFacebook(facebookLoginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse responseBody = response.body();
                    if (responseBody.success && responseBody.result != null && responseBody.result.access_token != null && responseBody.result.access_token.length() > 0) {

                        App.getCurrentUser().accessToken = responseBody.result.access_token;
                        App.getCurrentUser().username = name;
                        App.updateUser();

                        AppPreferenceManager.getInstance().setNotFirstLaunch();

                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in_at_once, R.anim.fade_out_at_once);
                        finish();
                        showProgress(false);
                    } else {
                        showError(responseBody);
                    }
                } else {
                    showError(response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                showError(t.toString());
            }
        });
    }

    private boolean checkNameAndPass(String name, String pass) {
        if (name.length() < 5) {
            Toasty.error(this, getResources().getString(R.string.short_name), Toast.LENGTH_SHORT, true).show();
            showProgress(false);
            return false;
        }

        if (pass.length() < 6) {
            Toasty.error(this, getResources().getString(R.string.short_pass), Toast.LENGTH_SHORT, true).show();
            showProgress(false);
            return false;
        }

        return true;
    }

    public void onUserlogginIn(String name, String pass) {
        onUserRegistered(name, pass, false);
    }

    public void onUserRegistered(String name, String pass) {
        onUserRegistered(name, pass, true);
    }

    public void onUserRegistered(final String name, String pass, final boolean isRegister) {
        if (!checkNameAndPass(name, pass)) {
            return;
        }
        showProgress(true);

        AuthRequest authRequest = new AuthRequest();
        authRequest.login = name;
        authRequest.password = pass;

        Call<AuthResponse> call;
        if (isRegister) {
            call = App.getRestClient().getAuthService().SignUp(authRequest);
        } else {
            call = App.getRestClient().getAuthService().SignIn(authRequest);
        }

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse responseBody = response.body();

                    if (responseBody.success && responseBody.result != null && responseBody.result.access_token != null && responseBody.result.access_token.length() > 0) {
                        App.getCurrentUser().accessToken = responseBody.result.access_token;
                        App.getCurrentUser().username = name;
                        App.getCurrentUser().justRegistered = isRegister;
                        App.updateUser();

                        AppPreferenceManager.getInstance().setNotFirstLaunch();

                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in_at_once, R.anim.fade_out_at_once);
                        finish();
                        showProgress(false);
                    } else {
                        showError(responseBody);
                    }
                } else {
                    showError(response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                showError(t.toString());
            }
        });
    }

    private void showError(AuthResponse responseBody){
//        if (responseBody.error.equalsIgnoreCase("AUTH_ERROR")) {
//            Toasty.error(AuthActivity.this, "User already exists", Toast.LENGTH_LONG, true).show();
//        } else {
            Toasty.error(AuthActivity.this, responseBody.error_msg, Toast.LENGTH_LONG, true).show();
//        }

        showProgress(false);
    }

    private void showError(String cause){
        Toasty.error(AuthActivity.this, cause, Toast.LENGTH_LONG, true).show();
        showProgress(false);
    }

    public void facebookLoginClicked() {
        showProgress(true);
        facebookHelper.performSignIn(AuthActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onActivatePaging(boolean activate) {
        ivRight.setEnabled(activate);
        mViewPager.setPagingEnabled(activate);
    }

    @OnClick(R.id.ivLeft)
    public void onLeftClicked() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);

        onActivatePaging(true);
    }

    @OnClick(R.id.ivRight)
    public void onRightClicked() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
    }

    public static class MyAdapter extends FragmentStatePagerAdapter {

        private static final int SIZE = 4;

        private SparseArray<TutorialItemFragment> list = new SparseArray<>(SIZE);


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TutorialItemFragment fragment = (TutorialItemFragment) super.instantiateItem(container, position);
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
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (list.get(0) == null) {
                        list.put(0, TutorialFirstItem.newInstance(TutorialFirstItem.ICON_SEND));
                    }
                    return list.get(0);

                case 2:
                    if (list.get(2) == null) {
                        list.put(2, TutorialFirstItem.newInstance(TutorialFirstItem.ICON_TRANSACT));
                    }
                    return list.get(2);

                case 1:
                    if (list.get(1) == null) {
                        list.put(1, TutorialFirstItem.newInstance(TutorialFirstItem.ICON_PIN));
                    }
                    return list.get(1);

                case 3:
                    if (list.get(3) == null) {
                        list.put(3, TutorialFourthItem.newInstance());
                    }
                    return list.get(3);
            }

            return null;
        }
    }
}
