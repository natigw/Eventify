package com.example.eventify.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eventify.R
import com.example.eventify.common.base.BaseAdapter
import com.example.eventify.databinding.SampleEventBinding
import com.example.eventify.domain.model.EventItem

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
            textEventDescription.text = event.description
            textEventType.text = event.eventType
            textEventOrganizer.text = if (event.organizerId == 1) "Eventify Group" else event.organizerId.toString()
            textEventDate.text = event.eventDate
            textEventPublished.text = event.publishingDate
            textEventDurationHours.text = "${event.startHour.substring(0, 5)} - ${event.endHour.substring(0, 5)}"
            textEventLikeCount.text = event.likeCount.toString()
            Glide.with(imageEvent)
                .load(event.imageLink)
                .placeholder(R.drawable.placeholder_event)
                .error(R.drawable.ic_launcher_foreground)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageEvent)

            var flag = true
            buttonLikeEvent.setOnClickListener {
                if (buttonLikeEvent.tag == "not_liked") {
                    buttonLikeEvent.setIconResource(R.drawable.like_fav)
                    textEventLikeCount.text = (event.likeCount + 1).toString()
                    buttonLikeEvent.tag = "liked"
                }
                else {
                    buttonLikeEvent.setIconResource(R.drawable.like_fav_border)
                    textEventLikeCount.text = event.likeCount.toString()
                    buttonLikeEvent.tag = "not_liked"
                }
//                buttonLikeEvent.setIconResource(if (flag) R.drawable.like_fav else R.drawable.like_fav_border)
//                flag = !flag
//                buttonLikeEvent.setIconTintResource(R.color.purple_light_eventify)
//                textEventLikeCount.text = if (flag) (event.likeCount + 1).toString() else event.likeCount.toString()
                textEventName.text = "Like olundu ama bunu duzzeltt"
                //TODO -> like olsun request atsin, icon qirmizi urek olsun, like count text bir dene artsin
            }
            buttonReadMoreEvents.setOnClickListener {
                //TODO -> button more duzelt next 5 lines, show less
            }
            buttonEventShowLocation.setOnClickListener {
                //TODO -> button more duzelt next 5 lines, show less
                onClick(event)
            }
        }
    }

//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }

    fun updateAdapter(newEvents: List<EventItem>) {
        events = newEvents
        notifyDataSetChanged()
    }
}