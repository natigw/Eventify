package com.example.eventify.presentation.adapters

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eventify.R
import com.example.eventify.common.base.BaseAdapter
import com.example.eventify.databinding.SampleEventBinding
import com.example.eventify.domain.model.EventItem

class EventAdapter(
    val onClickSeeComments: (EventItem) -> Unit,
    val onClickShowInMap: (EventItem) -> Unit,
    val onClickBuyTicket: (EventItem) -> Unit
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
            textEventDate.text = "${event.eventDate.substring(0,10)}"
            textEventPublished.text = "${event.publishingDate.substring(0,10)}, ${event.publishingDate.substring(12)}"
            textEventDurationHours.text = "${event.startHour.substring(0, 5)} - ${event.endHour.substring(0, 5)}"
            textEventLikeCount.text = event.likeCount.toString()
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

            buttonReadMoreEvents.post {
                val layout = textEventDescription.layout
                val lines = layout.lineCount
                if(lines > 0) {
                    if(layout.getEllipsisCount(lines - 1) > 0){
                        buttonReadMoreEvents.visibility = View.VISIBLE
                    }
                    else{
                        buttonReadMoreEvents.visibility = View.GONE
                    }
                }
            }
            var flagRead = true
            buttonReadMoreEvents.setOnClickListener {
                if(flagRead){
                    textEventDescription.maxLines = Integer.MAX_VALUE
                    buttonReadMoreEvents.text = "Read less"
                    flagRead = false
                }
                else{
                    buttonReadMoreEvents.text = "Read more"
                    textEventDescription.maxLines = 3
                    flagRead = true
                }
            }
            buttonEventSeeComments.setOnClickListener {
                onClickSeeComments(event)
            }
            buttonEventShowLocation.setOnClickListener {
                onClickShowInMap(event)
            }
            buttonBuyTicketEvents.setOnClickListener {
                onClickBuyTicket(event)
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