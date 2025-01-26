package com.example.eventify.presentation.adapters

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.example.common.base.BaseAdapter
import com.example.common.utils.functions.getLocalTime
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.eventify.databinding.SampleCommentBinding

class CommentAdapter : BaseAdapter<SampleCommentBinding>(SampleCommentBinding::inflate) {

    var comments: MutableList<CommentItem> = mutableListOf()

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindLight(binding: SampleCommentBinding, position: Int) {
        val currentComment = comments[position]
        with(binding) {
            textUsernameComments.text = currentComment.username
            textContentComments.text = currentComment.content
            textCommentDateComments.text = currentComment.date
            if(currentComment.isPending){
                isPendingTextView.isVisible = true
            }
            else{
                isPendingTextView.visibility = View.GONE
            }
        }

    }

    fun updateAdapter(newComments: List<CommentItem>) {
        comments = newComments.toMutableList()
        notifyDataSetChanged()
    }

    fun addComment(commentItem: CommentItem){
        comments.add(0,commentItem)
        Log.e("comments",comments.toString())
        notifyItemInserted(0)
    }

    fun updateComment(){
        val oldCommentItem = comments[0]
        oldCommentItem.let{
            val newCommentItem = it.copy(isPending = !it.isPending)
            comments[0] = newCommentItem
        }
        notifyItemChanged(0)
    }

}