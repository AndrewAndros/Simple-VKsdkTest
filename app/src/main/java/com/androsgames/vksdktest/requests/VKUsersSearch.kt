package com.androsgames.vksdktest.requests
import com.androsgames.vksdktest.models.VKUser
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject




class VKUsersSearch(q: String = "", offset : Int): VKRequest<List<VKUser>>("users.search") {


    init {
        if (q.isNotEmpty()) {
            addParam("q", q)
        }
        addParam("sort", "0")
        addParam("offset", offset.toString())
        addParam("count", "50")
        addParam("fields", "photo_200, sex, relation, city")

    }

    override fun parse(r: JSONObject): List<VKUser> {
        val users = r.getJSONObject("response").getJSONArray("items")
        val result = ArrayList<VKUser>()
        for (i in 0 until users.length()) {
            result.add(VKUser.parse(users.getJSONObject(i)))
        }
        return result
    }
}