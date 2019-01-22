package com.example.beardwulf.reva.injection.module

import com.example.beardwulf.reva.Endpoint
import com.example.beardwulf.reva.StringConverterFactory
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
object NetworkModule {

    private val BASE_URL = "http://10.0.2.2:3000"
    /**
     * Provides the Metar Service implemenation
     * @param retrofit the retrofit object used to instantiate the service
     */
    @Provides
    internal fun provideMetarApi(retrofit: Retrofit): Endpoint {
        return retrofit.create(Endpoint::class.java)
    }


    /**
     * Return the Retrofit object.
     */
    @Provides
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient): Retrofit {

            val gson = GsonBuilder()
                    .create()
            return  retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()
    }
    /**
     * Returns the OkHttpClient
     */
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        //To debug Retrofit/OkHttp we can intercept the calls and log them.
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()
    }

}