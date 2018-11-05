package com.zifangdt.ch.pad2.utils.net

import android.arch.lifecycle.LifecycleOwner
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.schedulers.Schedulers

/**
 * @auther xuzijian
 * @date 2017/7/28 11:39.
 */

class ResponseTransformer<T> : ObservableTransformer<T, T> {
    private var mTransformer: ObservableTransformer<T, T>? = null
    private var mLifecycleOwner: LifecycleOwner? = null

    constructor(lifecycleOwner: LifecycleOwner?=null, transformer: ObservableTransformer<T, T>?=null) {
        mLifecycleOwner = lifecycleOwner
        mTransformer = transformer
    }


    override fun apply(@NonNull upstream: Observable<T>): ObservableSource<T> {
        var result: Observable<T> = upstream
        if (mTransformer != null) result = result.compose(mTransformer)
        return result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }
}
