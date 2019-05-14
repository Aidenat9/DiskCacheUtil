package com.github.aiden.diskcacheutil.cache

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author sunwei
 * email：tianmu19@gmail.com
 * date：2019/5/14 11:18
 * package：com.github.aiden.diskcacheutil.cache
 * version：1.0
 * <p>description：    缓存工具          </p>
 */
class CacheUtil {
    companion object {
        /**
         * 获取应用版本
         * @param context
         * @return
         */
        fun getAppVersion(context: Context): Int {
            try {
                val info = context.packageManager.getPackageInfo(context.packageName, 0)
                return info.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return 1
        }

        /**
         * 获取缓存路径
         * @param context
         * @return
         */
        fun getCacheDirectory(context: Context): File? {
            val cacheDir = File(context.externalCacheDir, "diskcache")
            return if (!cacheDir.mkdirs() && (!cacheDir.exists() || !cacheDir.isDirectory)) {
                null
            } else cacheDir
        }

        /**
         * 把key用MD5加密
         * @param key
         * @return
         */
        fun toMd5Key(key: String): String {
            var cacheKey: String
            try {
                val mDigest = MessageDigest.getInstance("MD5")
                mDigest.update(key.toByteArray())
                cacheKey = bytesToHexString(mDigest.digest())
            } catch (e: NoSuchAlgorithmException) {
                cacheKey = key.hashCode().toString()
            }

            return cacheKey
        }

        fun bytesToHexString(bytes: ByteArray): String {
            val sb = StringBuilder()
            for (i in bytes.indices) {
                val hex = Integer.toHexString(0xFF and bytes[i].toInt())
                if (hex.length == 1) {
                    sb.append('0')
                }
                sb.append(hex)
            }
            return sb.toString()
        }

        fun writeObject(fos: OutputStream, `object`: Any): Boolean {
            var oos: ObjectOutputStream? = null
            try {
                oos = ObjectOutputStream(fos)
                oos.writeObject(`object`)
                oos.flush()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (oos != null) {
                    try {
                        oos.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }

                }
            }
            return false
        }

        fun readObject(inputStream: InputStream): Any? {
            var ois: ObjectInputStream? = null
            try {
                ois = ObjectInputStream(inputStream)
                return ois.readObject()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (ois != null) {
                    try {
                        ois.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            return null
        }

        /**
         * Bitmap -> byte[]
         * @param bm
         * @return
         */
        fun bitmap2Bytes(bm: Bitmap?): ByteArray? {
            if (bm == null) {
                return null
            }
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return baos.toByteArray()
        }

        /**
         * byte[] -> Bitmap
         * @param bytes
         * @return
         */
        fun bytes2Bitmap(bytes: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        /**
         * Drawable -> Bitmap
         * @param drawable
         * @return
         */
        fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
            if (drawable == null) {
                return null
            }
            val w = drawable.intrinsicWidth
            val h = drawable.intrinsicHeight
            val config = if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
            val bitmap = Bitmap.createBitmap(w, h, config)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, w, h)
            drawable.draw(canvas)
            return bitmap
        }

        /**
         * Bitmap -> Drawable
         * @param bm
         * @return
         */
        fun bitmap2Drawable(context: Context, bm: Bitmap?): Drawable? {
            if (bm == null) {
                return null
            }
            val bd = BitmapDrawable(bm)
            bd.setTargetDensity(bm.density)
            return BitmapDrawable(context.resources, bm)
        }
    }
}