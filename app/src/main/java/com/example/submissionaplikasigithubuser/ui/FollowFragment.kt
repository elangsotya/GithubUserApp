package com.example.submissionaplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.text.style.TtsSpan.ARG_USERNAME
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionaplikasigithubuser.R
import com.example.submissionaplikasigithubuser.adapter.MainAdapter
import com.example.submissionaplikasigithubuser.databinding.FragmentFollowBinding
import com.example.submissionaplikasigithubuser.response.ItemsItem
import com.example.submissionaplikasigithubuser.viewModel.FollowViewModel


class FollowFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    lateinit var followViewModel: FollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        followViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)

        _binding = FragmentFollowBinding.inflate(layoutInflater, container, false)


        followViewModel.getFollow().observe(viewLifecycleOwner) {
            if (it != null) {
                setFollowData(it)
            } else {
                val username = arguments?.getString(ARG_USERNAME) ?: ""
                followViewModel.setFollow(username)
            }
        }

        followViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
        return binding.root
    }

    private fun setFollowData(list: ArrayList<ItemsItem>) {
        if (list != null) {
            binding.rvFragment.visibility = View.VISIBLE
            val adapters = MainAdapter(list)
            binding.rvFragment.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = adapters
                setHasFixedSize(true)
            }
            adapters.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ItemsItem) {
                    showSelectedUser(data)
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFragment.visibility = View.GONE

        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFragment.visibility = View.VISIBLE
        }
    }

    private fun showSelectedUser(data: ItemsItem) {
        val userSelected = data.login
        val userId = data.id
        val userAvatar = data.avatarUrl
        val detailUserActivity = Intent(activity, DetailUserActivity::class.java)
        detailUserActivity.apply {
            putExtra(DetailUserActivity.EXTRA_USER, userSelected)
            putExtra(DetailUserActivity.EXTRA_ID, userId)
            putExtra(DetailUserActivity.EXTRA_URL, userAvatar)
        }
        startActivity(detailUserActivity)
    }
}


