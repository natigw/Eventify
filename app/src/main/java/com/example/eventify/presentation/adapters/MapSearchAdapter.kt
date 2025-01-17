package com.example.eventify.presentation.adapters

import com.example.common.base.BaseAdapter
import com.example.eventify.databinding.SearchItemBinding

class MapSearchAdapter : BaseAdapter<SearchItemBinding>(SearchItemBinding::inflate){
    override fun onBindLight(binding: SearchItemBinding, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }
}