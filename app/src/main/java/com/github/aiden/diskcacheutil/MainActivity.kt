package com.github.aiden.diskcacheutil

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.aiden.diskcacheutil.cache.DiskCacheUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val diskCacheUtil = DiskCacheUtil.getInstance(this)
        diskCacheUtil.put("str","gogogo")
       val user =  User()
        user.age = "26"
        user.name = "aiden@9"
        diskCacheUtil.put("obj",user)
        //show
        val user2 = diskCacheUtil.get<User>("obj")
        val str = diskCacheUtil.get<String>("str")
        tv_content.setText(str+"\n"+user2)
    }
}
