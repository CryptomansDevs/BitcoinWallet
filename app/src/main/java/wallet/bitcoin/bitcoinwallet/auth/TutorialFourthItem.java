package wallet.bitcoin.bitcoinwallet.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.activity.AuthActivity;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Utility;

public class TutorialFourthItem extends TutorialItemFragment {

    @BindView(R.id.etPass)
    protected EditText etPass;

    @BindView(R.id.etName)
    protected EditText etName;

    @BindView(R.id.tvNameHints)
    protected TextView tvNameHints;

    @BindView(R.id.tvPassHints)
    protected TextView tvPassHints;

    @BindView(R.id.change)
    protected TextView change;

    @BindView(R.id.ivIcon)
    protected ImageView ivIcon;

    @BindView(R.id.ivIcon1)
    protected ImageView ivIcon1;

    @BindView(R.id.fb_register)
    protected Button fbRegister;

    @BindView(R.id.login)
    protected Button loginRegisterBtn;

    public boolean isRegister = true;

    public static TutorialFourthItem newInstance() {
        TutorialFourthItem instance = new TutorialFourthItem();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) LayoutInflater.from(App.getContext()).inflate(R.layout.tutorial_fourth, container, false);
        ButterKnife.bind(this, mRootView);

        init();

        return mRootView;
    }

    private void init() {
        Utility.setDrawableColor(ivIcon, App.getContext().getResources().getColor(R.color.white));
        Utility.setDrawableColor(ivIcon1, App.getContext().getResources().getColor(R.color.white));

        setTexts();
    }

    @OnClick(R.id.change)
    public void onChangeClicked() {
        isRegister = !isRegister;
        setTexts();
    }

    private void setTexts(){
        if (!isRegister) {
            change.setText(R.string.or_reg);
            loginRegisterBtn.setText(R.string.login);
            fbRegister.setText(R.string.login_fb);
        } else {
            change.setText(R.string.or_login);
            loginRegisterBtn.setText(R.string.reg);
            fbRegister.setText(R.string.reg_fb);
        }
    }

    @OnClick(R.id.fb_register)
    public void onFbClicked() {
        if (getActivity() != null) {
            ((AuthActivity) getActivity()).facebookLoginClicked();
        }
    }

    @OnClick(R.id.login)
    public void onLoginClicked() {
        if (getActivity() != null) {
            if (isRegister) {
                ((AuthActivity) getActivity()).onUserRegistered(etName.getText().toString(), etPass.getText().toString());
            } else {
                ((AuthActivity) getActivity()).onUserlogginIn(etName.getText().toString(), etPass.getText().toString());
            }
        }
    }
}

