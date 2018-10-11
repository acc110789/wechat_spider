package me.run

import me.model.Article
import me.model.WeChatAccount
import me.mysql.dao.ArticleDao
import me.spider.ArticleSpider
import me.spider.WhenArticleLoaded

/**
 * Created by Administrator on 2015/9/22.
 */
object Main {

    private val articleLoadCallback: WhenArticleLoaded =  { article ->
        ArticleDao.save(article)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val accounts = WeChatAccount.new
        for (account in accounts) {
            crawlAccount(account)
        }
    }

    private fun crawlAccount(account: WeChatAccount): List<Article> {
        val sp = ArticleSpider(account , articleLoadCallback)
        return sp.crawlArticles()
    }

}
