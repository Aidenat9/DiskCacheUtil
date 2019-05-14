package com.github.aiden.diskcacheutil.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.jakewharton.disklrucache.DiskLruCache
import java.io.File
import java.io.IOException

/**
 * @author sunwei
 * email：tianmu19@gmail.com
 * date：2019/5/14 11:07
 * package：com.github.aiden.diskcacheutil.cache
 * version：1.0
 * <p>description：    DiskLruCache的封装          </p>
 */
class DiskCacheUtil {

    private var diskLruCache: DiskLruCache? = null
    private var context: Context? = null

    constructor(context: Context?) {
        this.context = context
        if (diskLruCache == null) {
            try {
                val directory = CacheUtil.getCacheDirectory(context!!.applicationContext)
                val appVersion = CacheUtil.getAppVersion(context.applicationContext)
                diskLruCache = DiskLruCache.open(directory, appVersion, 1, (maxSize * 1024 * 1024).toLong())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    companion object {
        private var instance: DiskCacheUtil? = null
        private val maxSize = 50//单位M
        fun getInstance(context: Context): DiskCacheUtil {
            if (null == instance) {
                synchronized(DiskCacheUtil::class.java) {
                    if (null == instance) {
                        instance = DiskCacheUtil(context.applicationContext)
                    }
                }
            }
            if (instance == null) {
                synchronized(DiskCacheUtil::class.java) {
                    if (instance == null) {
                        instance = DiskCacheUtil(context.applicationContext)
                    }
                }
            }
            return instance!!
        }
    }

    /**
     * 保存Object对象，Object要实现Serializable
     * @param key
     * @param value
     */
    fun put(key: String, value: Any) {
        var key = key
        try {
            key = CacheUtil.toMd5Key(key)
            val editor = diskLruCache!!.edit(key)
            if (editor != null) {
                val os = editor.newOutputStream(0)
                if (CacheUtil.writeObject(os, value)) {
                    editor.commit()
                } else {
                    editor.abort()
                }
                diskLruCache!!.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /**
     * 保存Bitmap
     * @param key
     * @param bitmap
     */
    fun putBitmap(key: String, bitmap: Bitmap) {
        put(key, CacheUtil.bitmap2Bytes(bitmap)!!)
    }

    /**
     * 保存Drawable
     * @param key
     * @param value
     */
    fun putDrawable(key: String, value: Drawable) {
        putBitmap(key, CacheUtil.drawable2Bitmap(value)!!)
    }

    /**
     * 根据key获取保存对象
     * @param key
     * @param <T>
     * @return
    </T> */
    operator fun <T> get(key: String): T? {
        var key = key
        try {
            key = CacheUtil.toMd5Key(key)
            val snapshot = diskLruCache!!.get(key)

            if (snapshot != null) {
                val inputStream = snapshot.getInputStream(0)
                return CacheUtil.readObject(inputStream) as T?
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取Bitmap
     * @param key
     * @return
     */
    fun getBitmap(key: String): Bitmap? {
        val bytes = get<Any>(key) as ByteArray? ?: return null
        return CacheUtil.bytes2Bitmap(bytes)
    }

    /**
     * 获取Drawable
     * @param key
     * @return
     */
    fun getDrawable(key: String): Drawable? {
        val bytes = get<Any>(key) as ByteArray? ?: return null
        return CacheUtil.bitmap2Drawable(context!!, CacheUtil.bytes2Bitmap(bytes))
    }

    fun size(): Long {
        return diskLruCache!!.size()
    }

    fun setMaxSize(maxSize: Int) {
        diskLruCache!!.setMaxSize((maxSize * 1024 * 1024).toLong())
    }

    fun getDirectory(): File {
        return diskLruCache!!.getDirectory()
    }

    fun getMaxSize(): Long {
        return diskLruCache!!.getMaxSize()
    }

    fun remove(key: String): Boolean {
        var key = key
        try {
            key = CacheUtil.toMd5Key(key)
            return diskLruCache!!.remove(key)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    fun delete() {
        try {
            diskLruCache!!.delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun flush() {
        try {
            diskLruCache!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun close() {
        try {
            diskLruCache!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun isClosed(): Boolean {
        return diskLruCache!!.isClosed()
    }


}