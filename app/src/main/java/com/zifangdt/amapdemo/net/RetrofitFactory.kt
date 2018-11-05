package com.zifangdt.ch.pad2.utils.net

/**
 * @auther xuzijian
 * @date 2017/7/28 12:43.
 */

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.zifangdt.ch.pad2.utils.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    private val baseUrl: String = if(AppUtils.isAppDebug()) Const.TEST_URL else Const.URL
//   private val baseUrl: String =  Const.URL //科希家现场调试，直接连接阿里云获取信息  记得改回来

    /**
     * 获取配置好的retrofit对象来生产Manager对象
     */
    // 参考RxJava
    // 参考与GSON的结合
    // 参考自定义Log输出
    //添加一个请求头
    // Buffer buffer = new Buffer();       //不依赖下面的Interceptor，用这三行也能打印出请求体
    // request.body().writeTo(buffer);
    // LogUtils.d("ZFDT", "intercept: " + buffer.readUtf8());
    //这个拦截器是用来打印日志的，不稳定
    val instance: Retrofit
        get() {
//            if (baseUrl == null || baseUrl.length <= 0) throw IllegalStateException("请先调用setBaseUrl再创建retrofit实例")

            val builder = Retrofit.Builder()
            builder.baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
            val client = OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .addInterceptor { chain ->
//                        val requestBuild = chain.request().newBuilder()
//                        val token = SPUtils.getInstance().getString(SpConst.TOKEN)
//
//                        if (!TextUtils.isEmpty(token)) {
//                            requestBuild.addHeader("Authorization", token)
//                        }
//                        val request = requestBuild.build()
//
//                        chain.proceed(request)
//                    }
                    .addNetworkInterceptor(HttpLoggingInterceptor(LogInterceptor()).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            builder.client(client)
            return builder.build()
        }
}

class LogInterceptor : HttpLoggingInterceptor.Logger {
    override fun log(message: String?) {
        if (message == null) return
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            LogUtils.json(message)
        } else {
            HttpLoggingInterceptor.Logger.DEFAULT.log(message)
//            LogUtils.d(message)
        }

    }

}
