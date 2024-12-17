package com.example.eventify.presentation.adapters

import android.view.View
import com.example.common.base.BaseAdapter
import com.example.eventify.databinding.BottomsheetPlacesCommentsBinding
import com.example.eventify.databinding.SampleCommentBinding
import com.example.domain.model.CommentItem

class CommentAdapter (
//    val bindinqa: BottomsheetPlacesCommentsBinding   //TODO -> daha yaxsi usul??
) : BaseAdapter<SampleCommentBinding>(SampleCommentBinding::inflate) {

    var comments: List<com.example.domain.model.CommentItem> = emptyList()

    override fun getItemCount(): Int {
//        if (comments.isEmpty()) bindinqa.NoCommentsText.visibility = View.GONE
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

    fun updateAdapter(newComments: List<com.example.domain.model.CommentItem>) {
        comments = newComments
        notifyDataSetChanged()
    }
}