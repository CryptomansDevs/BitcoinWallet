package wallet.bitcoin.bitcoinwallet.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.auth.PinLockActivity;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.model.User;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = App.getCurrentUser();
        if (Utility.isEmpty(user.accessToken)) {
            Intent intent = new Intent(this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in_at_once, R.anim.fade_out_at_once);
            finish();
        } else {
            //normal flow
            if (user.pin != User.DEFAULT_PIN){
                Intent intent = new Intent(RootActivity.this, PinLockActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_at_once, R.anim.fade_out_at_once);
                finish();
            } else {
                Intent intent = new Intent(RootActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_at_once, R.anim.fade_out_at_once);
                finish();
            }

        }
    }
}
