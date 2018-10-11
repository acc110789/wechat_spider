package me.model

data class Article(
        val weChatId: String,
        val weChatNickName: String,
        var title: String,
        var url: String? = null,
        var imgLink: String? = null,
        var description: String? = null,
        var content: String? = null,
        var lastModifiedTime: String? = null
        )