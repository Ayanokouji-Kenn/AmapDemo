@file:Suppress("UNCHECKED_CAST")

package com.zifangdt.ch.pad2.utils.net

/**
 * @author xzj
 * @date 2016/8/25 09:38.
 * 通过定义好的api接口以及Retrofit来生成具体的实例.
 */
object ApiFactory {
    private val serviceMap = mutableMapOf<String, Any>()
    fun <T> create(clz: Class<T>): T {
        var service: Any? = serviceMap[clz.name]
        if (service == null) {
            service = RetrofitFactory.instance.create(clz)
            serviceMap.put(clz.name, service!!)
        }
        return service as T
    }

    fun clearCache() {
        serviceMap.clear()
    }

}
