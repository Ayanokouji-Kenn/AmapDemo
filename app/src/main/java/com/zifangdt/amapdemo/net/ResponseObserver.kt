package com.zifangdt.amapdemo.net

import android.support.annotation.NonNull
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException

/**
 * @auther xuzijian
 * @date 2017/7/28 11:15.
 */

abstract class ResponseObserver<T> : DisposableObserver<T>() {


    override fun onNext(@NonNull t: T) {
        //考虑到可能有一些特殊需求没有遵循到json格式规范，这里做一个判断
        val data: BaseDto
        if (t is BaseDto) {
            data = t
//            if (data.code == Codes.SUCCESS) {             //服务端返回成功
//                success(t)
//            } else {
//                //服务端返回失败信息
//                if (!operationError(t, data.code, data.message)) {    //可以复写此方法，返回true，就用户自己处理，返回false，走下面的代码
//                    ToastUtils.showShort("${data.code?.desc}: ${data.message}")
//                }
//            }
        } else {
            success(t)
        }
    }

    /**
     * 默认的处理方式，如果需要自定义则重写error方法
     */
//    override fun onError(@NonNull e: Throwable) {
//        if (!error(e)) {
//            if (e is ConnectException) {
//                //网络异常
//                ToastUtils.showShort("连接异常")
//            } else if (e is HttpException) {
//                if (!httpError(e)) {
//                    val errorBody = e.response().errorBody()?.string()
//                    if (errorBody != null) {
//                        try {
//                            LogUtils.json(LogUtils.E, errorBody)
//                            val errorDTO = Gson().fromJson<BaseDto>(errorBody, BaseDto::class.java)
//                            when (errorDTO.code) {
////                            token不合法 和  刷新token也失效了，就重新登录吧
//                                Codes.REFRESH_TOKEN_EXPIRED -> {
//                                    ToastUtils.showShort("请重新登录")
//                                    val loginIntent = Intent("com.cosyhome.construct.login").apply {
//                                        flags = FLAG_ACTIVITY_NEW_TASK
//                                    }
//                                    Utils.getApp().startActivity(loginIntent);
//                                }
//////                                token过期
////                                Codes.TOKEN_EXPIRED -> {
////                                    RetrofitFactory.refreshInstance.create(CustomerApi::class.java).refreshToken()
////                                            .compose(ResponseTransformer())
////                                            .subscribe(object:ResponseObserver<LoginDTO>(){
////                                                override fun success(t: LoginDTO) {
////                                                    SPUtils.getInstance().put(SpConst.TOKEN,t.data.token)
////                                                    SPUtils.getInstance().put(SpConst.REFRESH_TOKEN,t.data.refreshToken)
////                                                    ToastUtils.showShort("token已刷新，请重试")
////                                                }
////                                            })
////
////                                }
//                                else -> ToastUtils.showShort("${errorDTO.code?.desc} -- ${errorDTO.message}")
//                            }
//                        } catch (e: Exception) {
//                            ToastUtils.showShort(e.localizedMessage);
//                        }
//                    }
////                    try {
////                        BaseDTO baseDTO = new Gson().fromJson(((HttpException) e).response().errorBody().string(), BaseDTO.class);
////                        switch(baseDTO.code) {
////                            //                            0：成功
////                            //                            1：未知错误
////                            //                            10：登录失败
////                            //                            11：token失效或不合法
////                            //                            12：没有访问权限
////                            case 1:
////                            ToastUtils.showShort(baseDTO.message);
////                            break;
////                            case 11:
////                            ToastUtils.showShort("请重新登录");
////                            Intent loginIntent = new Intent("login");
////                            loginIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
////                            loginIntent.putExtra(Constants.RC, LoginActivity.RC_RELOAD);
////
////                            Utils.getApp().startActivity(loginIntent);
////                            break;
////                            default:
////                            ToastUtils.showShort(baseDTO.message);
////                            break;
////                        }
////                    } catch (NullPointerException | IOException e1) {
////                        ToastUtils.showShort(e1.getLocalizedMessage());
////                    }
//                }
//
//            } else if (e is SocketTimeoutException) {
//                //网络超时
//                ToastUtils.showShort("网络超时")
//            } else {
//                ToastUtils.showShort(e.localizedMessage)
//                LogUtils.e(e.message)
//            }
//        }
//    }

    /**
     * 请求成功同时业务成功的情况下会调用此函数
     */
    abstract fun success(t: T)

    /**
     * @description 如果错误是正常返回的，在success里通过错误码来判断，走这个方法
     * 请求成功但业务失败的情况下会调用此函数.
     * @return 空实现，默认返回false，执行父类方法。 用户可以复写此方法，返回true来自己处理
     */
//    fun operationError(t: T, codes: Codes?, message: String?): Boolean {
//        return false
//    }

    /**
     * @description  如果错误码不是正常返回的，而是直接返回例如403这种错误，在这个方法里处理，可以复写自己处理
     * @param e
     * @return 自己处理返回true
     */
    fun httpError(e: HttpException): Boolean {
        return false
    }

    /**
     * 请求失败的情况下会调用此函数
     * @return 空实现，默认返回false，执行父类方法。 用户可以复写此方法，返回true来自己处理
     */
    fun error(e: Throwable): Boolean {
        return false
    }

    override fun onComplete() {

    }
}
