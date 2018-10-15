package me.run

import me.model.Article
import me.model.WeChatAccount
import me.mysql.dao.ArticleDao
import me.spider.ArticleSpider
import me.spider.OnArticleLoaded

/**
 * Created by Administrator on 2015/9/22.
 */
object Main {

    private val articleLoadCallback: OnArticleLoaded =  { article -> ArticleDao.save(article) }

    private val articleFilter:(Article)->Boolean = { article -> !ArticleDao.isArticleExist(article) }

    @JvmStatic
    fun main(args: Array<String>) {
        WeChatAccount.accounts.forEach { account -> crawlAccount(account) }
    }

    private fun crawlAccount(account: WeChatAccount): List<Article> {
        val sp = ArticleSpider(account, articleFilter, articleLoadCallback)
        return sp.crawlArticles()
    }

}
