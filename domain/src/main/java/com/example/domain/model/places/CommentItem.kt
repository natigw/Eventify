package com.example.domain.model.places

data class CommentItem(
    val ownerId : Int,
    val commentId: Int,
    val username: String,
    val content: String,
    val date: String,
    val isPending : Boolean
)
