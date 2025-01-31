package com.example.eventify.presentation.adapters

import android.view.View
import androidx.core.view.isInvisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseAdapter
import com.example.domain.model.places.FavoriteItem
import com.example.eventify.databinding.SampleFavoriteBinding

class FavoriteAdapter(
    val onClick: (id: Int) -> Unit
) : BaseAdapter<SampleFavoriteBinding>(SampleFavoriteBinding::inflate) {

    private var favorites: List<FavoriteItem> = emptyList()

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindLight(binding: SampleFavoriteBinding, position: Int) {

        val favItem = favorites[position]

        with(binding) {
            textFavoriteName.text = favItem.title
            textFavoriteDescription.text = favItem.description
            textFavoriteDate.text = favItem.date

            Glide.with(imageFavorite)
                .load(favItem.imageLink)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageFavorite)

            if (position == favorites.size - 1)
                materialDividerFavorite.isInvisible = true

            buttonFavoriteAllDetails.setOnClickListener {
                onClick(favItem.id)
            }
            root.setOnClickListener {
                onClick(favItem.id)
            }
            cardFavorite.setOnClickListener {
                onClick(favItem.id)
            }
        }
    }

    fun updateAdapter(newFavorites: List<FavoriteItem>) {
        favorites = newFavorites.reversed()
        notifyDataSetChanged()
    }
}