package wallet.bitcoin.bitcoinwallet.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import wallet.bitcoin.bitcoinwallet.helper.App;

public class User {

    final public static int         USD_CURRENCY = 0;
    final public static int         EUR_CURRENCY = 1;
    final public static int         RUR_CURRENCY = 2;

    final public static String      USD_CURRENCY_STR = "USD";
    final public static String      EUR_CURRENCY_STR = "EUR";
    final public static String      RUR_CURRENCY_STR = "RUB";

    final private static String      USER_NAME = "name";
    final private static String      USER_PASS = "password";
    final private static String      USER_ACCESS_TOKEN = "access_token";
    final private static String      USER_PIN = "pin";
    final private static String      RATE_USD = "RATE_USD";
    final private static String      RATE_EURO = "RATE_EURO";
    final private static String      RATE_RUR = "RATE_RUR";
    final private static String      LOCAL_CURRENCY = "LOCAL_CURRENCY";

    final private static String      FAST_FEE = "FAST_FEE";
    final private static String      SLOW_FEE = "SLOW_FEE";
    final private static String      OPTIMAL_FEE = "OPTIMAL_FEE";

    final private static String      TIP_1 = "TIP_1";
    final private static String      JUST_GERA = "TIPJUST_GERA_1";

    final public static int         DEFAULT_PIN = -1;

    public String username;
    public String password;
    public String accessToken;

    public float rateUsd;
    public float rateEuro;
    public float rateRur;

    public int fastFee;
    public int optimalFee;
    public int slowFee;

    public int localCurrency = 0;

    public int pin = -1;

    public boolean tip1Shown;
    public boolean justRegistered;


    public void save() {
        final SharedPreferences.Editor storage = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
        storage.putString(USER_NAME, username);
        storage.putString(USER_PASS, password);
        storage.putString(USER_ACCESS_TOKEN, accessToken);
        storage.putInt(USER_PIN, pin);

        storage.putBoolean(TIP_1, tip1Shown);
        storage.putBoolean(JUST_GERA, justRegistered);

        storage.putInt(FAST_FEE, fastFee);
        storage.putInt(SLOW_FEE, slowFee);
        storage.putInt(OPTIMAL_FEE, optimalFee);

        storage.putFloat(RATE_USD, rateUsd);
        storage.putFloat(RATE_EURO, rateEuro);
        storage.putFloat(RATE_RUR, rateRur);
        storage.putInt(LOCAL_CURRENCY, localCurrency);

        storage.apply();
    }

    public void reset() {
        final SharedPreferences.Editor storage = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
        storage.putString(USER_NAME, "");
        storage.putString(USER_PASS, "");
        storage.putString(USER_ACCESS_TOKEN, "");
        storage.putInt(USER_PIN, DEFAULT_PIN);
        storage.putInt(LOCAL_CURRENCY, USD_CURRENCY);
        storage.putBoolean(TIP_1, false);

        storage.apply();
    }

    public void load() {
        final SharedPreferences storage = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        username = storage.getString(USER_NAME, "");
        password = storage.getString(USER_PASS, "");
        accessToken = storage.getString(USER_ACCESS_TOKEN, "");
        pin = storage.getInt(USER_PIN, DEFAULT_PIN);

        tip1Shown = storage.getBoolean(TIP_1, false);
        justRegistered = storage.getBoolean(JUST_GERA, false);

        fastFee = storage.getInt(FAST_FEE, 0);
        slowFee = storage.getInt(SLOW_FEE, 0);
        optimalFee = storage.getInt(OPTIMAL_FEE, 0);

        rateUsd = storage.getFloat(RATE_USD, 0);
        rateEuro = storage.getFloat(RATE_EURO, 0);
        rateRur = storage.getFloat(RATE_RUR, 0);

        localCurrency = storage.getInt(LOCAL_CURRENCY, USD_CURRENCY);
    }

    public float getRate(){
        switch (localCurrency){
            case USD_CURRENCY:
                return rateUsd;
            case EUR_CURRENCY:
                return rateEuro;
            case RUR_CURRENCY:
                return rateRur;
        }

        return rateUsd;
    }

    public String getRateName(){
        switch (localCurrency){
            case USD_CURRENCY:
                return USD_CURRENCY_STR;
            case EUR_CURRENCY:
                return EUR_CURRENCY_STR;
            case RUR_CURRENCY:
                return RUR_CURRENCY_STR;
        }

        return "USD";
    }

}
