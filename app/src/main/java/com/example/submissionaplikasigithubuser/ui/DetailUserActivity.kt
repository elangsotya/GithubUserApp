package com.example.submissionaplikasigithubuser.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.adapter.SectionsPagerAdapter
import com.example.submissionaplikasigithubuser.databinding.ActivityDetailUserBinding
import com.example.submissionaplikasigithubuser.viewModel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()


    companion object {
        const val EXTRA_USER = "key_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower_fragment,
            R.string.following_fragment
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val barAction = supportActionBar
        barAction!!.title = "User Detail"

        val getIntentLogin = intent.getStringExtra(EXTRA_USER)
        val getIntentId = intent.getIntExtra(EXTRA_ID, 0)
        val getIntentAvatar = intent.getStringExtra(EXTRA_URL)

        getIntentLogin?.let { detailViewModel.setDetailUser(it) }

        val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailUserActivity, getIntentLogin!!)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.getDetailUser().observe(this, {
            binding.tvDetail.text = it.name
            binding.tvDetailName.text = it.login
            binding.tvFollower.text = it.followers.toString() + " Followers"
            binding.tvFollowing.text = it.following.toString() + " Following"
            Glide.with(this@DetailUserActivity)
                .load(it.avatarUrl)
                .into(binding.imgDetailPhoto)
        })

        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        val fabFav = binding.actionFavorite
        var isBookmarkedFav = false
        CoroutineScope(Dispatchers.IO).launch {
            val checky = detailViewModel.checkFavorite(getIntentId)
            withContext(Dispatchers.Main){
                if (checky != null){
                    if (checky > 0){
                        fabFav.setImageDrawable(
                            ContextCompat.getDrawable(
                                fabFav.context,
                                R.drawable.ic_favorite_24
                            )
                        )
                        isBookmarkedFav = true
                    } else {
                        fabFav.setImageDrawable(
                            ContextCompat.getDrawable(
                                fabFav.context, R.drawable.ic_favorite_border_24
                            )
                        )
                        isBookmarkedFav = false
                    }
                }
            }
        }

        fabFav.setOnClickListener {
            isBookmarkedFav =  !isBookmarkedFav
            if(isBookmarkedFav){
                if (getIntentAvatar != null) {
                    detailViewModel.insertFavorite(getIntentId, getIntentLogin, getIntentAvatar)
                    fabFav.setImageDrawable(
                        ContextCompat.getDrawable(
                            fabFav.context, R.drawable.ic_favorite_24
                        )
                    )
                }
            } else{
                detailViewModel.deleteFavorite(getIntentId)
                fabFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        fabFav.context, R.drawable.ic_favorite_border_24
                    )
                )

            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvDetail.visibility = View.GONE
            binding.tabs.visibility = View.GONE
            binding.tvFollowing.visibility = View.GONE
            binding.tvFollower.visibility = View.GONE
            binding.tvDetailName.visibility = View.GONE
            binding.imgDetailPhoto.visibility = View.GONE
            binding.viewPager.visibility = View.GONE
            binding.actionFavorite.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.tvDetail.visibility = View.VISIBLE
            binding.tabs.visibility = View.VISIBLE
            binding.tvFollowing.visibility = View.VISIBLE
            binding.tvFollower.visibility = View.VISIBLE
            binding.tvDetailName.visibility = View.VISIBLE
            binding.imgDetailPhoto.visibility = View.VISIBLE
            binding.viewPager.visibility = View.VISIBLE
            binding.actionFavorite.visibility = View.VISIBLE
        }
    }
}
