package com.androsgames.vksdktest.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androsgames.vksdktest.R
import com.androsgames.vksdktest.models.VKUser
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.user_item_layout.view.*


class UsersAdapter(private val interaction: Interaction) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var mUsers : ArrayList<VKUser>? = ArrayList()

    companion object {
        private const val USER_TYPE = 1
        private const val LOADING_TYPE = 2
        private const val EXHAUSTED_TYPE = 3
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            USER_TYPE -> {
                UserViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.user_item_layout,
                        parent,
                        false
                    ),
                    interaction
                )
            }
            LOADING_TYPE -> {
                LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_loading_list_item, parent, false))

            } EXHAUSTED_TYPE -> {
                SearchExhaustedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_search_exhausted, parent, false))

            }
            else -> {
                UserViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.user_item_layout,
                        parent,
                        false
                    ),
                    interaction
                )
            }


        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mUsers!!.get(position).lastName == "LOADING...") {
            LOADING_TYPE
        } else if (mUsers!!.get(position).lastName == "EXHAUSTED...") {
            EXHAUSTED_TYPE
        }
        else {
            USER_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType : Int = getItemViewType(position)
        if (itemViewType == USER_TYPE) {
            when (holder) {
                is UserViewHolder -> {
                    holder.bind(mUsers!!.get(position))
                }
        }
        }
    }

    override fun getItemCount(): Int {
        if (mUsers != null) {
            return mUsers!!.size
        }
        return 0
    }

    fun submitList(list: List<VKUser>) {
        clearUsersList()
        mUsers!!.addAll(list as ArrayList<VKUser>)
        notifyDataSetChanged()
    }


    fun hideLoading() {
        if(isLoading()) {
            if (mUsers!!.get(0).lastName.equals("LOADING...")) {
                Log.d("HAO", "${mUsers!!.size}")
                mUsers!!.removeAt(0)
                Log.d("HAO", "${mUsers!!.size}")

            }
            else if (mUsers!!.get(mUsers!!.size - 1).lastName.equals("LOADING...")) {
                Log.d("HAO", "${mUsers!!.size}")
                mUsers!!.removeAt(mUsers!!.size - 1)
            }
        }
        notifyDataSetChanged()
    }

    // pagination loading
    fun displayLoading() {
        if (mUsers == null) {
            mUsers = ArrayList()
        }
        if (!isLoading()) {
            val user = VKUser(0, 0, 0, "", "", "", "", false)
            user.lastName = "LOADING..."
            Log.d("HAO", "display loading ordered/")
            mUsers!!.add(user)
            notifyDataSetChanged()
        }
    }


    // display loading during search
    fun displayOnlyLoading() {
        clearUsersList()
        val user = VKUser(0, 0, 0, "", "", "", "", false)
        user.lastName = "LOADING..."
        mUsers!!.add(user)
        notifyDataSetChanged()
    }

    fun clearUsersList() {
        if (mUsers == null) {
            mUsers = ArrayList()
        } else {
            mUsers?.clear()
        }
        notifyDataSetChanged()
    }


     fun isLoading() : Boolean {
        if(mUsers != null && mUsers!!.size > 0) {
            if (mUsers!!.get(mUsers!!.size - 1).lastName.equals("LOADING...")) {
                return true
            }
        }
        return false
    }

    fun setQueryExhausted() {
        hideLoading()
        val exhaustedUser : VKUser = VKUser(0, 0, 0, "", "", "", "", false)
        exhaustedUser.lastName = "EXHAUSTED..."
        mUsers!!.add(exhaustedUser)
        notifyDataSetChanged()
    }



    class UserViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView){
        fun bind(item: VKUser) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            user_name.text = "${item.firstName} ${item.lastName}"
            Glide.with(itemView.context)
                .load(item.photo)
                .into(user_image)
        }
    }

    class LoadingViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView)

    class SearchExhaustedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface Interaction {
        fun onItemSelected(position: Int, item: VKUser)
    }
}