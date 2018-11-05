package com.zifangdt.amapdemo.net

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.util.*

/**
 * @auther xuzijian
 * @date 2017/6/21 10:48.
 */

object NetUtils {
    /**
     * 通过键值对获取请求体
     */
    fun getRequestBody(params: Map<String, Any>): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONObject(params).toString())
    }

    fun getRequestBody(jsonObject: JSONObject): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString())
    }

    fun getRequestBody(json: String): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
    }

    /**
     * 通过对象获取请求体
     */
    fun getRequestBody(`object`: Any): RequestBody {
        val json = Gson().toJson(`object`)
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
    }

    fun pathsToMultipartBodyParts(pasth: List<String>): List<MultipartBody.Part>? {
        val files = ArrayList<File>()
        for (s in pasth) {
            val f = File(s)
            if (f.exists()) {
                files.add(f)
            }
        }
        return filesToMultipartBodyParts(files)
    }

    fun filesToMultipartBodyParts(files: List<File>?): List<MultipartBody.Part>? {
        if (files == null) return null
        val parts = ArrayList<MultipartBody.Part>(files.size)
        for (file in files) {
            val requestBody = RequestBody.create(MultipartBody.FORM, file)
            val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
            parts.add(part)
        }
        return parts
    }
}
