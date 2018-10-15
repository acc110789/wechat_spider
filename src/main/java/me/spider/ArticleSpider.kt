package me.spider


import me.crawl.utils.*
import me.model.Article
import org.openqa.selenium.*
import me.model.WeChatAccount

import java.net.URL

private const val SELECTOR_ARTICLE_CONTAINER = ".weui_msg_card .weui_msg_card_bd .weui_media_box"

private const val SELECTOR_ARTICLE_TITLE = ".weui_media_title"

private const val SELECTOR_ARTICLE_DES = ".weui_media_desc"

private const val SELECTOR_ARTICLE_MODIFY_TIME = ".weui_media_extra_info"

private const val SELECTOR_ARTICLE_THUMB = "span.weui_media_hd"

typealias OnArticleLoaded = (Article) -> Unit

/**
 * 通过公众号昵称 公众号号 从搜索页进入进行抓取
 */

class ArticleSpider(private val account: WeChatAccount,
                    private val articleFilter: ((Article) -> Boolean)? = null,
                    private val articleLoadCallback: OnArticleLoaded? = null) {

    private val driver: WebDriver = Browser.obtainWebDriver()

    private var findLastArticle: Boolean? = false

    /**
     * 根据公众号爬取文章
     */
    fun crawlArticles(): List<Article> {
        Sleep.random()

        if (!navigateToHomePage()) return emptyList()

        Sleep.random()

        //切换到公众号列表页
        driver.switchToWindow(account.nick)

        Sleep.random()

        //关闭其它窗口
        driver.closeWindow(String.format(RELATED_ACCOUNT , account.nick))

        Sleep.random()

        val articleElements = driver.findElements(By.cssSelector(SELECTOR_ARTICLE_CONTAINER))

        var articles = articleElements.map { articleElementToArticle(it) }

        if(articleFilter != null) articles = articles.filter(articleFilter)

        articles.forEach { article ->
            Sleep.random()

            fetchArticleContent(article)

            articleLoadCallback?.invoke(article)
        }

        driver.quit()

        return articles
    }

    /**
     * @return success or not
     * */
    private fun navigateToHomePage(): Boolean {
        //搜公众号，定位到公众号页面
        val navigator = HomePageNavigator(account, driver)
        val success = navigator.navigateToHomePage()
        if (!success) {
            driver.quit()
        }
        return success
    }


    private fun fetchArticleContent(article: Article) {
        Logger.log("fetch article content: ${driver.title}")

        driver.navigate().to(article.url)

        Sleep.random()

        val body = driver.findElement(By.cssSelector(BODY)).text
        Logger.log("find article body : $body")

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
