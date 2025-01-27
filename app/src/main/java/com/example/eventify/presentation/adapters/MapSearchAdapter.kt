package com.example.eventify.presentation.adapters

import com.example.common.base.BaseAdapter
import com.example.domain.model.places.SearchItem
import com.example.eventify.R
import com.example.eventify.databinding.SampleSearchBinding

class MapSearchAdapter(
    val onClick: (SearchItem) -> Unit
) : BaseAdapter<SampleSearchBinding>(SampleSearchBinding::inflate) {

    private var searchItems = listOf<SearchItem>()

    override fun getItemCount(): Int {
        return searchItems.size
    }

    override fun onBindLight(binding: SampleSearchBinding, position: Int) {

        val currentItem = searchItems[position]

        if (currentItem.placeType == "event")
            binding.imageSearchIcon.setImageResource(R.drawable.ic_ticket)

        binding.titleTextViewSearch.text = currentItem.name

        binding.cardSearchResult.setOnClickListener {
            onClick(currentItem)
        }
    }

    fun updateAdapter(newData: List<SearchItem>) {
        searchItems = newData
        notifyDataSetChanged()
    }
}