package com.rickandmorty.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.rickandmorty.databinding.ActivityMainBinding
import com.rickandmorty.extentions.visible
import com.rickandmorty.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservable()
        setSupportActionBar(binding.toolbar)

    }

    private fun setupObservable() {
        mainViewModel.displayLoading.observe(this){ showLoading ->
            binding.progressIndicator.visible(showLoading, View.INVISIBLE)
        }


    }

    override fun getViewBinding() =
        ActivityMainBinding.inflate(layoutInflater)

}
