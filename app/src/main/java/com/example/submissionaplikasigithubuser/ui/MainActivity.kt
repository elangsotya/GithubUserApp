package com.example.submissionaplikasigithubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.adapter.MainAdapter
import com.example.submissionaplikasigithubuser.databinding.ActivityMainBinding
import com.example.submissionaplikasigithubuser.preferences.themePreferences
import com.example.submissionaplikasigithubuser.response.ItemsItem
import com.example.submissionaplikasigithubuser.viewModel.MainViewModel
import com.example.submissionaplikasigithubuser.viewModel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapters: MainAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val barAction = supportActionBar
        barAction!!.title = "Github Users"
        val pref = themePreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        ).get(MainViewModel::class.java)

        mainViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.githubUser.observe(this, {
            setUserData(it)
        })
        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_favorite -> {
                Intent(this@MainActivity,FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.action_settings -> {
                Intent(this@MainActivity,ThemeActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData(list: ArrayList<ItemsItem>) {
        adapters = MainAdapter(list)
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapters
        }
        adapters.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUserList(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showSelectedUser(data: ItemsItem) {
        val userSelected = data.login
        val userId = data.id
        val userAvatar = data.avatarUrl
        val detailUserActivity = Intent(this@MainActivity, DetailUserActivity::class.java)
        detailUserActivity.apply {
            putExtra(DetailUserActivity.EXTRA_USER, userSelected)
            putExtra(DetailUserActivity.EXTRA_ID, userId)
            putExtra(DetailUserActivity.EXTRA_URL, userAvatar)
        }
        startActivity(detailUserActivity)
    }
}