package com.example.eventify.presentation.adapters

import android.util.Log
import com.example.common.base.BaseAdapter
import com.example.domain.model.places.SearchItem
import com.example.eventify.databinding.SearchItemBinding

class MapSearchAdapter(val onClick : (SearchItem)->Unit) : BaseAdapter<SearchItemBinding>(SearchItemBinding::inflate){
    var searchItems = listOf<SearchItem>()

    override fun onBindLight(binding: SearchItemBinding, position: Int) {
        val currentItem = searchItems[position]
        binding.titleTextView.text = currentItem.name
        binding.root.setOnClickListener{
            onClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    fun updateAdapter(newData : List<SearchItem>){
        Log.e("adapterDatam",newData.toString())
        searchItems = newData
        notifyDataSetChanged()
    }


}