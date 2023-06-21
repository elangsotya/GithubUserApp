package com.example.submissionaplikasigithubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.adapter.MainAdapter
import com.example.submissionaplikasigithubuser.database.entity.favoriteUser
import com.example.submissionaplikasigithubuser.databinding.ActivityFavoriteBinding
import com.example.submissionaplikasigithubuser.response.ItemsItem
import com.example.submissionaplikasigithubuser.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private var _favoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _favoriteBinding!!
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapterMain: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Favorite User"

        favoriteViewModel = ViewModelProvider(this)
            .get(FavoriteViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getFavUser().observe(this){
                allFav: List<favoriteUser> ->
            val items = arrayListOf<ItemsItem>()
            allFav.map {
                val item = ItemsItem(id = it.id, login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            showFavoriteUser(items)
            adapterMain = MainAdapter(items)
            adapterMain.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        super.onPause()
        favoriteViewModel.getFavUser().observe(this){
                allFav: List<favoriteUser> ->
            val items = arrayListOf<ItemsItem>()
            allFav.map {
                val item = ItemsItem(id = it.id, login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            showFavoriteUser(items)
            adapterMain = MainAdapter(items)
            adapterMain.notifyDataSetChanged()
            if (allFav.size == 1){
                items.clear()
                adapterMain.notifyDataSetChanged()
            }
        }
    }

    private fun showFavoriteUser(listUser: ArrayList<ItemsItem>){
        if (listUser.size > 0){
            binding.tvEmpty.visibility = View.GONE
            adapterMain = MainAdapter(listUser)
            adapterMain.notifyDataSetChanged()
            binding.rvReview.apply {
                layoutManager = LinearLayoutManager(this@FavoriteActivity)
                adapter = adapterMain
                setHasFixedSize(true)
            }
            adapterMain.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
                override fun onItemClicked(user: ItemsItem) {
                    Intent(this@FavoriteActivity, DetailUserActivity::class.java).apply {
                        putExtra(DetailUserActivity.EXTRA_USER, user.login)
                        putExtra(DetailUserActivity.EXTRA_ID, user.id)
                        putExtra(DetailUserActivity.EXTRA_URL, user.avatarUrl)
                    }.also {
                        startActivity(it)
                    }
                }
            })
        } else{
            binding.tvEmpty.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                Intent(this@FavoriteActivity, ThemeActivity::class.java).also {
                    startActivity(it)
                }
        }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        _favoriteBinding = null
        super.onDestroy()
    }
}