package com.example.eventify.test

import com.example.common.base.BaseActivity
import com.example.eventify.databinding.ActivityTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : BaseActivity<ActivityTestBinding>(ActivityTestBinding::inflate) {
    override fun onCreateLight() {

    }
}