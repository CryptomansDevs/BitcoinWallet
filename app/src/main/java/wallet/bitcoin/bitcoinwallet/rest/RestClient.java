package wallet.bitcoin.bitcoinwallet.rest;

import android.util.Log;

import java.io.IOException;

import code.junker.JCoder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wallet.bitcoin.bitcoinwallet.rest.service.ApiService;
import wallet.bitcoin.bitcoinwallet.rest.service.AuthService;

public class RestClient {

    private static final String BASE_URL = JCoder.decode("��oicjernrjvekbgktrhbkjtnrjgbtrn屵芶꦳꧐㫣뗉떾妅姤ꍎ犃狯簢ᣔᢸ�❤✁抋얏엻儠䎄䏦윖ᙽᘘ丮ጦ輾轊ﵹﵗ쒇搏摬�畐甿碠䒭䓀䜵");

    private AuthService authService;
    private ApiService apiService;

    private Retrofit retrofit;

    public RestClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", "Basic IHNhbhcebufijreiknrejngkjrwbjf=")
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        authService = retrofit.create(AuthService.class);
        apiService = retrofit.create(ApiService.class);

    }

    public AuthService getAuthService() {
        return authService;
    }

    public ApiService getApiService() {
        return apiService;
    }

}
