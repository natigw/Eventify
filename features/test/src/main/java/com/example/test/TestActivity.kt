package com.example.test

import com.example.common.base.BaseActivity
import com.example.test.databinding.ActivityTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : BaseActivity<ActivityTestBinding>(ActivityTestBinding::inflate) {
    override fun onCreateLight() {

    }
}