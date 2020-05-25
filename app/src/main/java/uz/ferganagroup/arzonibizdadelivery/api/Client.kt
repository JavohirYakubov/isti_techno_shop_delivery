package uz.ferganagroup.arzonibizdadelivery.api

import android.content.Context
import android.os.Build
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uz.ferganagroup.arzonibizdadelivery.utils.Constants
import uz.ferganagroup.arzonibizdadelivery.BuildConfig
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.util.*
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


object Client {

    internal lateinit var retrofit: Retrofit

    fun initClient(context: Context, host: String) {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(host)
            .client(getOkHttpClient(context))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getOkHttpClient(context: Context): OkHttpClient {
        var builder = OkHttpClient().newBuilder()
        builder.retryOnConnectionFailure(false)
        builder.connectTimeout(60, TimeUnit.SECONDS)
        builder.writeTimeout(60, TimeUnit.SECONDS)
        builder.readTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(ChuckInterceptor(context))
        }
        builder.addInterceptor(AppInterceptor())
        builder = enableTls12OnPreLollipop(builder)

        return builder.build()
    }

    private fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT in 16..21) {
            try {
                val trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.getTrustManagers()
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                    throw IllegalStateException(
                        "Unexpected default trust managers:" + Arrays.toString(
                            trustManagers
                        )
                    )
                }
                val trustManager = trustManagers[0] as X509TrustManager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, arrayOf(trustManager), null)
                val sslSocketFactory = sslContext.socketFactory
                return client.sslSocketFactory(sslSocketFactory, trustManager)
            } catch (e: Exception) {
                return client
            }
        } else {
            return client

        }

    }

    class AppInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            return chain.proceed(getRequest(original))
        }

        fun getRequest(original: Request): Request {
            var builder = original.newBuilder()
            builder.addHeader("Content-Type", "application/json")
            builder.header("Connection", "close")
            builder.addHeader("Authorization", Credentials.basic("mobiles", "123"))
//            if (Prefs.getToken() != null && !Prefs.getToken().isEmpty()) {
//                builder.addHeader("token", Prefs.getToken())
//            }
            builder.method(original.method(), original.body())
            return builder.build()
        }
    }
}