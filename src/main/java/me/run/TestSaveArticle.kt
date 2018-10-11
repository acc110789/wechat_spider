package me.run

import me.model.Article
import me.mysql.dao.ArticleDao

object TestSaveArticle {

    @JvmStatic
    fun main(args: Array<String>) {
        val article = Article(
                weChatId = "ideadiy",
                weChatNickName = "创意DIY",
                title = "你想都想不到，机场安检没收的打火机都长啥样",
                url = "https://mp.weixin.qq.com/s?timestamp=1539444031&src=3&ver=1&signature=xdRgi76YzUE1ie7lL7EP3K0LdomnCTaFny00J4fpmKdP7qIylSlhUii4WdhqwCawyo1quLDASvmapdNMYaURPK9G6nw0M*RB0WqN360F3PtWdm-efSAqrLRvL13iWdWOHElw9qAryq8bvTv06WObbB3YE76vYcIAP*AMrErZCXI=",
                imgLink = "url(\"http://mmbiz.qpic.cn/mmbiz_jpg/BS2Niaiba56dibAILdACzhXmQO7HZxDwUxopicpQDyShwiayXElzTumF86XQKHYc7v2zTbo5CtdNickkib5QnXA8UMFvA/0?wx_fmt=jpeg\")",
                description = "",
                content = "i am content",
                lastModifiedTime = "2018年7月6日"
        )
        ArticleDao.save(article)
    }

}