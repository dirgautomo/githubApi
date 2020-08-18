package com.example.dreamofyou.main

import ListUsers
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamofyou.R
import com.example.dreamofyou.main.adapter.UserListAdapter
import com.squareup.seismic.ShakeDetector
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainContract.View, ShakeDetector.Listener {

    private var pageLoad = 1

    private var presenter = MainPresenter()

    private var adapter: UserListAdapter? = null

    private val githubUsers: MutableList<ListUsers> = arrayListOf()

    private var urlGithub = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onAttach(this)
        showProgressbar(false)
        initListener()
        initAdapter()
        adapterLoadmore()
        initShakeListener()
    }

    private fun initAdapter() {
        adapter = UserListAdapter()
        rv_user_lis.adapter = adapter
    }

    private fun initListener() {
        ed_search_name.setOnEditorActionListener { textview, actionId, event ->
            showProgressbar(true)
            presenter.onSearchUserGithub(textview.text.toString(), 1)
            pageLoad = 1
            true
        }


        ed_search_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable) {
                if (text.isEmpty()) {
                    showProgressbar(false)
                    adapter?.setData(arrayListOf())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                //Do nothing
            }
        })
    }

    private fun showProgressbar(status: Boolean) {
        if (status) pb_loading.visibility = View.VISIBLE else pb_loading.visibility = View.GONE
    }

    private fun initShakeListener() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sd = ShakeDetector(this)
        sd.start(sensorManager)
    }

    private fun addLoadmore(listUser: List<ListUsers>): List<ListUsers> {
        val list = arrayListOf<ListUsers>()
        if (listUser.size >= 15) {
            list.addAll(listUser)
            list.add(ListUsers("", 101, "", ""))
            return list
        }
        return listUser
    }

    private fun adapterLoadmore() {
        rv_user_lis.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                linearLayoutManager?.let {
                    val lastPos = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    val calculateSize = (pageLoad * (githubUsers.size - 1))
                    if (lastPos == calculateSize) {
                        pageLoad++
                        presenter.onSearchUserGithub(ed_search_name.text.toString(), pageLoad)
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    override fun onSuccessGetUser(listUser: List<ListUsers>?) {
        listUser?.let {
            if (it.isNotEmpty()) {
                showProgressbar(false)
                githubUsers.clear()
                if (pageLoad == 1) {
                    githubUsers.addAll(addLoadmore(listUser))
                    adapter?.setData(githubUsers)
                } else {
                    removeLoadMore()
                    adapter?.removeLoadMore()
                    githubUsers.addAll(addLoadmore(listUser))
                    adapter?.updateData(githubUsers)
                }
                Toast.makeText(this, "page $pageLoad", Toast.LENGTH_SHORT).show()
            } else {
                showProgressbar(false)
                Toast.makeText(this, "No User found", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun removeLoadMore() {
        githubUsers.map { user ->
            if (user.id == 101) {
                githubUsers.remove(user)
            }
        }
    }

    override fun onFailedGetUser(errorMessage: String) {
        showProgressbar(false)
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onHideProgressBar(status: Boolean) {
        showProgressbar(status)
    }

    override fun verifyUrl(url: String) {
        Log.d("url ", url)
        urlGithub = url
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun hearShake() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlGithub))
        startActivity(browserIntent)
    }
}