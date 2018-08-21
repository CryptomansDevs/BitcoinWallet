package wallet.bitcoin.bitcoinwallet.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.activity.MainActivity;
import wallet.bitcoin.bitcoinwallet.activity.RootActivity;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.model.User;

public class PinLockActivity extends AppCompatActivity {

    public static final String CHANGE = "CHANGE";

    @BindView(R.id.pin_lock_view)
    protected PinLockView pinLockView;

    @BindView(R.id.indicator_dots)
    protected IndicatorDots indicatorDots;

    @BindView(R.id.profile_name)
    protected TextView profile_name;


    private boolean isLogging = true;//if false - we are in change pin flow
    private int newPinCounter = 0;
    private int newPin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarColorPin(this);
        setContentView(R.layout.pin_activity);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().getBooleanExtra(CHANGE, false)){
            isLogging = false;

            if (App.getCurrentUser().pin != User.DEFAULT_PIN) {
                profile_name.setText(R.string.enter_old_pin);
            } else {
                profile_name.setText(R.string.enter_new_pin);
                newPinCounter = 1;
            }
        }

        pinLockView.attachIndicatorDots(indicatorDots);

        pinLockView.setPinLength(4);
        pinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));

        indicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL);

        pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                if (isLogging) {
                    goToMainActivity(pin);
                } else {
                    if (newPinCounter == 0) {
                        launchResetPin(pin);
                    } else if (newPinCounter == 1){
                        launchNewPin(pin);
                    } else if (newPinCounter == 2){
                        confirmNewPin(pin);
                    }
                }
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });
    }

    private void confirmNewPin(String pin){
        if (newPin == Integer.parseInt(pin)){
            App.getCurrentUser().pin = newPin;
            App.updateUser();

            Toasty.success(this, getResources().getString(R.string.pin_changed)).show();
            onBackPressed();
        } else {
            Toasty.error(this, getResources().getString(R.string.not_matching_pins)).show();

            profile_name.setText(R.string.enter_new_pin);
            newPinCounter = 1;
            pinLockView.resetPinLockView();
        }
    }

    private void launchNewPin(String pin){
        newPin = Integer.parseInt(pin);

        profile_name.setText(R.string.confirm_new_pin);
        newPinCounter = 2;

        pinLockView.resetPinLockView();
    }

    private void launchResetPin(String pin){
        if (App.getCurrentUser().pin == Integer.parseInt(pin)) {
            profile_name.setText(R.string.enter_new_pin);
            newPinCounter = 1;

            pinLockView.resetPinLockView();
        } else {
            Toasty.error(this, getResources().getString(R.string.error_pin)).show();
            pinLockView.resetPinLockView();
        }
    }

    private void goToMainActivity(String pin){
        if (App.getCurrentUser().pin == Integer.parseInt(pin)) {
            Intent intent = new Intent(PinLockActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in_at_once, R.anim.fade_out_at_once);
            finish();
        } else {
            Toasty.error(this, getResources().getString(R.string.error_pin)).show();
            pinLockView.resetPinLockView();
        }
    }
}
