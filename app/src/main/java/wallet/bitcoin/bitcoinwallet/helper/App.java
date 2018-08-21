package wallet.bitcoin.bitcoinwallet.helper;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.greenrobot.greendao.database.Database;

import io.fabric.sdk.android.Fabric;
import rateusdialoghelper.RateDialogHelper;
import wallet.bitcoin.bitcoinwallet.model.DaoMaster;
import wallet.bitcoin.bitcoinwallet.model.DaoSession;
import wallet.bitcoin.bitcoinwallet.model.User;
import wallet.bitcoin.bitcoinwallet.rest.RestClient;

public class App extends Application {

    private static Handler handler;

    private static DaoSession daoSession;

    private static Context appContext;

    private static User currentUser;

    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        handler = new Handler();
        appContext = getApplicationContext();

        initGreenDao();

        UIHelper.init(appContext);

        RateDialogHelper.onNewSession(appContext);

        restClient = new RestClient();

        if (AppPreferenceManager.getInstance().getTimeInstalled() == 0){
            AppPreferenceManager.getInstance().setTimeInstalled();
        }
    }

    private static void initGreenDao() {
        DaoMaster.OpenHelper helper = new UpgradeDb(appContext, "wallet-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return appContext;
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            currentUser = new User();
            currentUser.load();
        }

        return currentUser;
    }

    public static User forceLoadCurrentUser() {
        currentUser = new User();
        currentUser.load();

        return currentUser;
    }

    public static void setCurrentUser(User _currentUser) {
        currentUser = _currentUser;
        currentUser.save();
    }

    public static void updateUser() {
        currentUser.save();
    }

    public static RestClient getRestClient() {
        return restClient;
    }

}
