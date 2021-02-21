package app.adam.bossku;

import app.adam.bossku.helper.Route;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AppConfig {
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://server.bossku.id/systemcall/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
}