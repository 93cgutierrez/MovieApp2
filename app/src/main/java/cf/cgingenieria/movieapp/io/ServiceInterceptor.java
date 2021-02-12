package cf.cgingenieria.movieapp.io;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

import cf.cgingenieria.movieapp.utils.Parameters;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CGIngenieria on 11/02/2021.
 */
public class ServiceInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        HttpUrl originalHttpUrl = chain.request().url();
        HttpUrl newHttpUrl = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", Parameters.API_KEY)
                .build();
        newRequest.url(newHttpUrl);
    return chain.proceed(newRequest.build());
    }

}
