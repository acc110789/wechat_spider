package me.run

import me.model.WeChatAccount
import me.mysql.dao.ArticleDao
import me.spider.ArticleSpider
import me.spider.WhenArticleLoaded

/**
 * Created by Administrator on 2015/9/22.
 */
object TestCrawlOneAccount {

    private val articleLoadCallback: WhenArticleLoaded =  { article ->
        ArticleDao.save(article)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val account = WeChatAccount("创意DIY", "ideadiy")
        val spider = ArticleSpider(account, null, articleLoadCallback)
        spider.crawlArticles()
    }
}
