package com.androsgames.vksdktest.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKUser(
    val id: Int = 0,
    val sex : Int,
    val relation : Int,
    val city : String = "",
    val firstName: String = "",
    var lastName: String = "",
    val photo: String = "",
    val deactivated: Boolean = false)
     : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
      )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(sex)
        parcel.writeInt(relation)
        parcel.writeString(city)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(photo)
        parcel.writeByte(if (deactivated) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VKUser> {
        override fun createFromParcel(parcel: Parcel): VKUser {
            return VKUser(parcel)
        }

        override fun newArray(size: Int): Array<VKUser?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject)
                = VKUser(id = json.optInt("id", 0),
            sex = json.optInt("sex", 0),
            relation = json.optInt("relation", 0),
            city = json.optString("city", "0"),
            firstName = json.optString("first_name", ""),
            lastName = json.optString("last_name", ""),
            photo = json.optString("photo_200", ""),
            deactivated = json.optBoolean("deactivated", false))
    }
}