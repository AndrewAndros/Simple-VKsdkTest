package com.androsgames.vksdktest.viemodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.androsgames.vksdktest.models.VKUser
import com.androsgames.vksdktest.requests.VKUsersSearch
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

class UsersViewModel : ViewModel() {

    private val usersSearchList : MutableLiveData<List<VKUser>> = MutableLiveData()
    private val TAG = "SearchingTest: "
    private var offset : Int = 0
    private var lastQuery : String? = ""
    public var loggedIn : Boolean = false

    fun requestUsers(query : String) {
        prepareSearch(query)
        VK.execute(VKUsersSearch(query, offset), object: VKApiCallback<List<VKUser>> {
            override fun success(result: List<VKUser>) {
                    val userList : ArrayList<VKUser> = ArrayList()
                    if (usersSearchList.value != null) {
                        userList.addAll(getUsersSearchList().value!!)
                    }
                    userList.addAll(result)
                    usersSearchList.postValue(userList)
                    lastQuery = query
            }
            override fun fail(error: VKApiExecutionException) {
                Log.e(TAG, error.toString())
            }
        })
    }

    private fun prepareSearch(query : String) {
        if (!query.equals(lastQuery)) {
            offset = 0
            usersSearchList.value = null
        } else {
            offset += 50
        }
    }

    fun getUsersSearchList() : LiveData<List<VKUser>> {
        return usersSearchList
    }


}