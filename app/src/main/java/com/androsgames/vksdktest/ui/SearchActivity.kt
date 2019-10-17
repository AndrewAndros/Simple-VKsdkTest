package com.androsgames.vksdktest.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.androsgames.vksdktest.R
import com.androsgames.vksdktest.adapters.UsersAdapter
import com.androsgames.vksdktest.models.VKUser
import com.androsgames.vksdktest.utils.VerticalSpacingItemDecorator
import com.androsgames.vksdktest.viemodels.UsersViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity(), UsersAdapter.Interaction {

    private lateinit var usersViewModel : UsersViewModel
    private lateinit var searchView : SearchView
    private lateinit var adapter : UsersAdapter
    companion object {

        private val FRIENDS_SCOPE = arrayListOf(VKScope.FRIENDS)
        private const val TAG = "Search Activity Test: "

        fun startFrom(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)

//        Для работы с ВК SDK необходима авторизация
        if (!usersViewModel.loggedIn) {
            VK.login(this, FRIENDS_SCOPE)
        }

        searchView = findViewById(R.id.search_view)
        initViews()
        initScrollListener()
        subscribeObservers()
    }



    private fun initScrollListener() {
        users_list.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(!users_list.canScrollVertically(1)) {
                   if(!adapter.isLoading()) {
                       adapter.displayLoading()
                       Log.d(TAG, "display loading ordered")
                   }
                    launchSearch(searchView.query.toString())

                }
            }
        })
    }

    private fun launchSearch(query: String?) {
        if (query == null || query.isBlank()) {
            val toast = Toast.makeText(this, getString(R.string.fill_search_warning), Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            return
        }
        usersViewModel.requestUsers(query)
    }

    private fun initViews() {
        adapter = UsersAdapter(this)
        val itemDecorator = VerticalSpacingItemDecorator(5)
        users_list.addItemDecoration(itemDecorator)
        users_list.adapter = adapter
        users_list.layoutManager = LinearLayoutManager(this)

        searchView.onActionViewExpanded()
        searchView.isIconified = false


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {
                adapter.displayOnlyLoading()
                launchSearch(q)
                return true
            }

            override fun onQueryTextChange(q: String?): Boolean {
              return false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                usersViewModel.loggedIn = true
            }

            override fun onLoginFailed(errorCode: Int) {
                Toast.makeText(applicationContext, getString(R.string.authentication_fail), Toast.LENGTH_SHORT).show()
                Log.e(TAG, errorCode.toString())
            }
        }
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun subscribeObservers() {
                   usersViewModel.getUsersSearchList().observe(this, Observer {
                    if(it != null) {
                        showUsers(it)
                    }
                   })
    }


    private fun showUsers(foundUsers: List<VKUser>) {
        instructions_view.visibility = View.GONE


//        ТОЛЬКО ДЛЯ ДЕМОНСТРАЦИИ pagination
        Thread.sleep(400)


        adapter.hideLoading()
        adapter.submitList(foundUsers)
        if(foundUsers.size == 0) {
            adapter.setQueryExhausted()
        }
    }

    override fun onItemSelected(position: Int, item: VKUser) {
        val intent = Intent(this, UserInfoActivity::class.java)
        intent.putExtra("the_user", item)
        startActivity(intent)
        Log.d(TAG, item.lastName + "/ " + item.relation + "/ " + item.sex + "/ " + item.city + "/ " + position)
    }


}
