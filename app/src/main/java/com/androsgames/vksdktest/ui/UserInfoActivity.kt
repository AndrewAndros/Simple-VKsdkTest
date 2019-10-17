package com.androsgames.vksdktest.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.androsgames.vksdktest.R
import com.androsgames.vksdktest.models.VKUser
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_info.*
import org.json.JSONObject

class UserInfoActivity : AppCompatActivity() {

    private lateinit var user : VKUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        getIncomingIntent()
    }


    private fun getIncomingIntent() {
        if (intent.hasExtra("the_user")) {
            user = intent.getParcelableExtra("the_user")
            updateUI()
        }
    }

    private fun updateUI() {

        Glide.with(this)
            .load(user.photo)
            .into(user_picture)


        user_first_name.text = user.firstName
        user_last_name.text = user.lastName
        user_city.text = getCity()
        user_relation.text = getRelation()
        user_sex.text = getSex()
    }

    private fun getSex() : String {
          return when(user.sex) {
                  1 -> "не мужик"
                  2 -> "мужик"
              else -> "не указан"
          }
    }

    private fun getRelation() : String {
        if(user.sex == 1) {
            return when (user.relation) {
                1 -> "не замужем"
                2 -> "есть друг"
                3 -> "помолвлена"
                4 -> "замужем"
                5 -> "всё сложно"
                6 -> "в активном поиске"
                7 -> "влюблена"
                8 -> "в гражданском браке"
                else -> "не указано"
            }
        } else {
            return when (user.relation) {
                1 -> "не женат"
                2 -> "есть подруга"
                3 -> "помолвлен"
                4 -> "женат"
                5 -> "всё сложно"
                6 -> "в активном поиске"
                7 -> "влюблён"
                8 -> "в гражданском браке"
                else -> "не указано"
            }
        }
    }

    private fun getCity() : String {
        return if (!user.city.contains("0")) {
            val json =  JSONObject(user.city)
            json.getString("title")
        } else {
            "не указан"
        }

    }
}
