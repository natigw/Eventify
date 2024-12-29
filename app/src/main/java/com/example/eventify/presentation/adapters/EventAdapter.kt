package com.example.eventify.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseAdapter
import com.example.domain.model.places.event.EventItem
import com.example.eventify.R
import com.example.eventify.databinding.SampleEventBinding

class EventAdapter(
    val onClick: (EventItem) -> Unit
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

            var flag = true
            buttonLikeEvent.setOnClickListener {
                if (buttonLikeEvent.tag == "not_liked") {
                    buttonLikeEvent.setIconResource(R.drawable.like_fav)
                    buttonLikeEvent.tag = "liked"
                }
                else {
                    buttonLikeEvent.setIconResource(R.drawable.like_fav_border)
                    buttonLikeEvent.tag = "not_liked"
                }
//                buttonLikeEvent.setIconResource(if (flag) R.drawable.like_fav else R.drawable.like_fav_border)
//                flag = !flag
//                buttonLikeEvent.setIconTintResource(R.color.purple_light_eventify)
//                textEventLikeCount.text = if (flag) (event.likeCount + 1).toString() else event.likeCount.toString()
                textEventName.text = "Like olundu ama bunu duzzeltt"
                //TODO -> like olsun request atsin, icon qirmizi urek olsun, like count text bir dene artsin
            }

            root.setOnClickListener {
                onClick(event)
            }
            buttonEventAllDetails.setOnClickListener {
                onClick(event)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateAdapter(newEvents: List<EventItem>) {
        events = newEvents
        notifyDataSetChanged()
    }
}