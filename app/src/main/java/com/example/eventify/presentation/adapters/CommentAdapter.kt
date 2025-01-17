package com.example.eventify.presentation.adapters

import com.example.common.base.BaseAdapter
import com.example.common.utils.functions.getLocalTime
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.eventify.databinding.SampleCommentBinding

class CommentAdapter (
) : BaseAdapter<SampleCommentBinding>(SampleCommentBinding::inflate) {

    var comments: MutableList<CommentItem> = mutableListOf()

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindLight(binding: SampleCommentBinding, position: Int) {
        val comment = comments[position]
        with(binding) {
            textUsernameComments.text = comment.username
            textContentComments.text = comment.content
            textCommentDateComments.text = comment.date
        }

    }

    fun updateAdapter(newComments: List<CommentItem>) {
        comments = newComments.toMutableList()
        notifyDataSetChanged()
    }
}