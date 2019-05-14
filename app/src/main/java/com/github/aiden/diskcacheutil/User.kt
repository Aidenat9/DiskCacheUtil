package com.github.aiden.diskcacheutil
import java.io.Serializable

 class User :Serializable{
    var age:String= ""
    var name: String= ""
     override fun toString(): String {
         return "User(age='$age', name='$name')"
     }

 }