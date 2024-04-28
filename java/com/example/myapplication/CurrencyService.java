package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CurrencyService {

    //@GET("/latest?app_id={4c7d83bce0b64811bade4625e9d2024e}&base={base}")
    //@GET("latest.json?app_id=4c7d83bce0b64811bade4625e9d2024e&base={base}")
    //Call<ExchangeRateResponse> getExchangeRate(@Path("base") String base);
    @GET("latest.json")
    Call<ExchangeRateResponse> getExchangeRate(@Query("app_id") String appId, @Query("base") String fromCurrency);
}
