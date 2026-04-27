package com.xxh.summary.download.data.remote

import com.xxh.summary.download.data.remote.config.ConfigManager
import java.security.MessageDigest
import java.util.Arrays

object NetworkUtil {

    fun getCommonParams(map: MutableMap<String,String>?=null): Map<String, String> {
        val data = map ?: mutableMapOf()
        data["app_id"] = ConfigManager.getAppId()
        data["request_time"] = System.currentTimeMillis().toString()
        data["system_version"] = "GEN_CN_NX789JV1.0.0B04"
        data["apk_version"] = "V8.0.7.2026030420a"
        data["platform"] = "1"
        data["oaid"] = "09b93f69ad09bc941cf07b64a82eeb3e16047570c6509f7ae5c7b22dfeb70cc2"
        data["vaid"] = "dd0ecc5b0f59b3b2698b729c3b540841fdcf8042bd3bdeec585ea46318b468ad"
        val sign = convertToMD5(data)
        data["sign"] = sign
        return data
    }

    private fun convertToMD5(map: Map<String, String>): String {
        val paramBuf = StringBuilder()
        val keys = map.keys.toTypedArray()
        Arrays.sort(keys)
        for (key in keys) {
            paramBuf.append(key).append("=").append(map[key])
        }
        paramBuf.append(ConfigManager.getSecretKey())
        return md5(paramBuf.toString())
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        md.update(input.toByteArray(charset("UTF-8")))
        val resBytes = md.digest()
        val hexValue = StringBuffer()
        for (i in resBytes.indices) {
            val `val` = resBytes[i].toInt() and 0xFF
            if (`val` < 16) {
                hexValue.append("0")
            }
            hexValue.append(Integer.toHexString(`val`))
        }
        return hexValue.toString()
    }

}