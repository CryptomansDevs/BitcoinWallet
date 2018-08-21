package wallet.bitcoin.bitcoinwallet.rest.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import wallet.bitcoin.bitcoinwallet.rest.request.AuthRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.FacebookLoginRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.AuthResponse;

public interface AuthService {

    @POST("/api/v1.0/SignUp")
    public Call<AuthResponse> SignUp(@Body AuthRequest request);

    @POST("/api/v1.0/SignIn")
    public Call<AuthResponse> SignIn(@Body AuthRequest request);

    @POST("/api/v1.0/SignInWithFacebook")
    public Call<AuthResponse> loginViaFacebook(@Body FacebookLoginRequest request);
}
