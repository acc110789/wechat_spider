package me.spider

import me.crawl.utils.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import me.model.WeChatAccount
import org.openqa.selenium.NoSuchElementException

//搜索微信搜索首页
private const val URL_PRIMARY_PAGE = "http://weixin.sogou.com/"

//搜索公众号按钮
private const val SELECTOR_BTN_SEARCH_ACCOUNT = ".querybox .swz2"

//搜索内容输入框
private const val SELECTOR_INPUT_SEARCH_BOX = "#query"

//下一页
private const val SELECTOR_NEXT_PAGE = "#sogou_next"

private const val ID_CAPTCHAR = "seccodeInput"

private const val UNUSUAL_ID_1 = "noresult_frontbl_container"

private const val UNUSUAL_ID_2 = "noresult_part1_container"

private const val ACCOUNT_ID_SELECTOR = ".news-box li .txt-box .info label"

private const val ACCOUNT_LINK_SELECTOR = ".news-box li .txt-box .tit a"

/**
 * 导航到微信公众号的个人主页
 */
class HomePageNavigator(private val account: WeChatAccount, private val driver: WebDriver = Browser.obtainWebDriver()) {

    var findTarget: Boolean = false

    var hasNext: Boolean = true

    private val resultHandler: (Result) -> Boolean?  = { result ->
        when (result) {
            is Result.Success -> {
                result.element.click()
                true
            }

            is Result.Error -> {
                driver.quit()
                false
            }

            else -> null
        }
    }

    private sealed class Result(val code: Int , val msg: String) {

        //404、需要验证码等一场页面
        class Error(code:Int, msg: String): Result(code , msg)

        //解析出正常结果
        class Success(val element: WebElement): Result(CODE_SUCCESS , MSG_SUCCESS)

        //当前页面没有找到目标公众号
        class NotFound: Result(CODE_NOT_FOUND , "")
    }

    /**
     * @return 是否成功定位到个人主页
     * */
    fun navigateToHomePage(): Boolean {
        //搜狗首页
        driver.get(URL_PRIMARY_PAGE)

        Sleep.random()

        //切换搜公众号按钮
        driver.clickBySelector(SELECTOR_BTN_SEARCH_ACCOUNT)

        Sleep.random()

        //在搜索框中写入搜索词并且提交
        driver.inputAndSubmit(SELECTOR_INPUT_SEARCH_BOX, account.nick)

        Sleep.random()

        return recurSearchTarget()
    }

    /**
     * @return find target or not
     * */
    private fun recurSearchTarget(): Boolean {
        do {
            val result = searchTarget()

            val hasTarget = resultHandler(result)

            if (hasTarget != null) return hasTarget

        } while (hasNext && !findTarget && navigateToNext())

        return false
    }

    /**
     * 通过appCode找公众号
     */
    private fun searchTarget(): Result {
        Sleep.random()

        getErrorResult()?.let { return it }

        getOkResult()?.let { return it }

        updateHasNext()

        return Result.NotFound()
    }

    /** 检查异常页面 */
    private fun getErrorResult(): Result.Error? {
        //法律法规 or 找不到公众号
        if (driver.isElementExist(By.id(UNUSUAL_ID_1)) || driver.isElementExist(By.id(UNUSUAL_ID_2))) {
            val msg = "$LAW or $ACCOUNT_NOT_FOUND : ${driver.currentUrl}"
            println(msg)
            return Result.Error(CODE_UNUSUAL, msg)
        }

        //出现了验证码
        if (driver.isElementExist(By.id(ID_CAPTCHAR))) {
            val msg = "$FOUND_CAPTCHAR : ${driver.currentUrl}"
            println(msg)
            return Result.Error(CODE_NEED_CAPTCHAR , msg)
        }

        return null
    }

    private val matchTargetWeChatId: (String)->Boolean = checker@{
        if(it != account.weChatId) return@checker false
        println("WeChatId :$it match target ...")
        findTarget = true
        true
    }

    private fun getOkResult(): Result.Success? {
        val accountIds = driver.findElements(By.cssSelector(ACCOUNT_ID_SELECTOR))
        val accountLinks = driver.findElements(By.cssSelector(ACCOUNT_LINK_SELECTOR))
        for (i in accountIds.indices) {
            val accountEle = accountIds[i] as WebElement
            val weChatId = accountEle.text
            println("find WeChatId :$weChatId")
            if (!matchTargetWeChatId(weChatId)) continue
            val h3 = accountLinks[i] as WebElement
            return Result.Success(h3)
        }
        return null
    }

    private fun updateHasNext() {
        hasNext = try { driver.findElement(By.cssSelector(SELECTOR_NEXT_PAGE)) != null }
        catch (ex: NoSuchElementException) { false }
    }

    /**
     * @return 是否进入下一页
     * */
    private fun navigateToNext(): Boolean {
        return try {
            //如果出现没有结果 获取到的页面数量为0
            Sleep.random()
            val nextButton = driver.findElement(By.cssSelector(SELECTOR_NEXT_PAGE))
            val hasNextBtn = nextButton != null
            hasNext = hasNextBtn
            nextButton?.click()
            Sleep.random()
            hasNextBtn
        } catch (ex: NoSuchElementException) {
            Logger.log("no next page...")
            hasNext = false
            false
        }
    }
}
