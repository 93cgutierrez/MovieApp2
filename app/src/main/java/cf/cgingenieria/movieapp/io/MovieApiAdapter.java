package cf.cgingenieria.movieapp.io;


import cf.cgingenieria.movieapp.utils.Parameters;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CGIngenieria on 11/02/2021.
 */
public class MovieApiAdapter {
    private static final String BASE_URL = Parameters.API_SERVER_BASE_URL;
    private static MovieApiService API_SERVICE;

    public static MovieApiService getApiService() {

        // Creamos un interceptor y le indicamos el log level a usar
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);


        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.addInterceptor(new ServiceInterceptor()
                    ).build()) // <-- usamos el log level
                    .build();
            API_SERVICE = retrofit.create(MovieApiService.class);
        }

        return API_SERVICE;
    }
}
