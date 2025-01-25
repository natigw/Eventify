package com.example.eventify.presentation.adapters

import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseAdapter
import com.example.domain.model.places.event.EventItem
import com.example.eventify.R
import com.example.eventify.databinding.SampleEventBinding

class EventAdapter(
    val onLike: (id: Int) -> Unit,
    val onClick: (id: Int) -> Unit
) : BaseAdapter<SampleEventBinding>(SampleEventBinding::inflate) {

    var events: List<EventItem> = emptyList()

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindLight(binding: SampleEventBinding, position: Int) {
        val event = events[position]
        with(binding) {
            textEventName.text = event.name
            textEventDateTime.text = event.eventDateTime
            Glide.with(imageEvent)
                .load(event.imageLink)
                .placeholder(R.drawable.placeholder_event)
                .error(R.drawable.placeholder_event)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageEvent)

            if(event.isLiked){
                buttonLikeEvent.setIconResource(R.drawable.like_fav)
            }

            if (position == events.size - 1)
                dividerEvent.visibility = View.INVISIBLE

            root.setOnClickListener {
                onClick(event.eventId)
            }
            buttonEventAllDetails.setOnClickListener {
                onClick(event.eventId)
            }
            buttonLikeEvent.setOnClickListener {
                onLike(event.eventId)
                event.isLiked = !event.isLiked
                notifyItemChanged(position)
            }
        }
    }

    fun updateAdapter(newEvents: List<EventItem>) {
        events = newEvents
        notifyDataSetChanged()
    }
}