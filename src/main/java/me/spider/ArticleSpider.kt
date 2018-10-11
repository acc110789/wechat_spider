package me.spider


import me.crawl.utils.*
import me.model.Article
import org.openqa.selenium.*
import me.model.WeChatAccount

import java.net.URL
import java.util.ArrayList

private const val SELECTOR_ARTICLE_CONTAINER = ".weui_msg_card .weui_msg_card_bd .weui_media_box"

private const val SELECTOR_ARTICLE_TITLE = ".weui_media_title"

private const val SELECTOR_ARTICLE_DES = ".weui_media_desc"

private const val SELECTOR_ARTICLE_MODIFY_TIME = ".weui_media_extra_info"

private const val SELECTOR_ARTICLE_THUMB = "span.weui_media_hd"

typealias WhenArticleLoaded = (Article) -> Unit

/**
 * 通过公众号昵称 公众号号 从搜索页进入进行抓取
 */

class ArticleSpider(private val account: WeChatAccount , private val articleLoadCallback: WhenArticleLoaded? = null) {

    private val driver: WebDriver = Browser.obtainWebDriver()

    var findLastArticle: Boolean? = false

    private val lastArticleChecker: (String?)->Boolean = checker@ { title ->
        if(title.isNullOrEmpty()) return@checker false
        if(title != account.lastArticleTitle) return@checker false
        findLastArticle = true
        println("last article encounter")
        true
    }

    /**
     * 根据公众号爬取文章
     */
    fun crawlArticles(): List<Article> {
        val navigator = HomePageNavigator(account, driver)

        //搜公众号，定位到公众号页面
        if (!navigator.navigateToHomePage()) return emptyList()

        //切换到公众号列表页
        driver.switchToWindow(account.nick)

        //关闭其它窗口
        driver.closeWindowByTitle(String.format(RELATED_ACCOUNT , account.nick))

        Sleep.random()

        //解析第一页文章的元素
        val allElements = getArticleElements()
        println("find ${allElements.size} article element")

        val articles = allElements.map { articleElementToArticle(it) }

        articles.forEach {
            fetchArticleContent(it)
            articleLoadCallback?.invoke(it)
        }

        driver.quit()

        return articles
    }


    private fun getArticleElements(): List<WebElement> {
        Sleep.random()

        val articles = driver.findElements(By.cssSelector(SELECTOR_ARTICLE_CONTAINER))

        val articlesElements = ArrayList<WebElement>()
        for (article in articles) {
            val title = article.findElement(By.cssSelector(SELECTOR_ARTICLE_TITLE)).text
            if(lastArticleChecker(title)) break
            articlesElements.add(article)
        }
        return articlesElements
    }

    private fun fetchArticleContent(article: Article) {
        driver.navigate().to(article.url)
        println("fetch article content: ${driver.title}")

        Sleep.random()

        val currentUrl = driver.currentUrl
        article.url = currentUrl

        val body = driver.findElement(By.cssSelector(BODY)).text
        println("find article body : $body")

        article.content = body
    }

    /** 把Article Web元素转化成Article entity */
    private fun articleElementToArticle(articleElement: WebElement): Article {
        val titleElement = articleElement.findElement(By.cssSelector(SELECTOR_ARTICLE_TITLE))
        val title = titleElement.text
        println("title :$title")

        val path = titleElement.getAttribute(HREFS)//获取跳转前的url
        val currentUrl = driver.currentUrl
        val url = URL(currentUrl)
        val protocol = url.protocol
        val host = url.host
        val articleUrl = "$protocol://$host$path"
        println("link :$articleUrl")

        val desElement = articleElement.findElement(By.cssSelector(SELECTOR_ARTICLE_DES))
        val description = desElement.text
        println("description :$description")

        val modifyTimeElement = articleElement.findElement(By.cssSelector(SELECTOR_ARTICLE_MODIFY_TIME))
        val modifyTime = modifyTimeElement.text
        println("last modify time :$modifyTime")

        val imgElement = articleElement.findElement(By.cssSelector(SELECTOR_ARTICLE_THUMB))
        val imgSrc = imgElement.getCssValue("background-image")
        println("img src :$imgSrc")

        return Article(
                weChatId = account.weChatId,
                weChatNickName = account.nick,
                url = articleUrl,
                title = title,
                description = description,
                imgLink = imgSrc,
                lastModifiedTime = modifyTime
        )
    }
}
