package me.crawl.utils

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

/**
 * 通过选择器，往input中写入词语
 */
fun WebDriver.inputAndSubmit(selector: String, word: String) {
    val textInput = findElement(By.cssSelector(selector))
    if (textInput != null) {
        for(char in word) {
            textInput.sendKeys(char.toString())
            Sleep.random(500 , 1000)
        }
        textInput.submit()
    } else {
        Logger.log("currentPageUrl :$currentUrl")
        Logger.log("elementNotFound :$selector")
        System.exit(0)
    }
}

/**
 * 通过选择器找到元素并点击
 */
fun WebDriver.clickBySelector(selector: String) {
    val we = findElement(By.cssSelector(selector))
    if (we != null) {
        we.click()
    } else {
        Logger.log("currentPageUrl :$currentUrl")
        Logger.log("elementNotFound: $selector")
        System.exit(0)
    }
}

//根据标题切换窗口
fun WebDriver.switchToWindow(windowTitle: String): Boolean {
    var flag = false
    val initHandle = windowHandle
    val handles = windowHandles
    for (s in handles) {
        if (s == initHandle) continue
        switchTo().window(s)
        if (!title.contains(windowTitle)) continue
        flag = true
        Logger.log("Switch to window: $windowTitle successfully!")
        break
    }
    return flag
}

fun WebDriver.closeWindow(windowTitle: String): Boolean {
    var closed = false
    val initHandle = windowHandle
    val handles = windowHandles
    for (s in handles) {
        if (s == initHandle) continue
        switchTo().window(s)
        if (!title.contains(windowTitle)) continue
        closed = true
        close()
        break
    }
    switchTo().window(initHandle)
    return closed
}

/** 判断元素是否存在*/
fun WebDriver.isElementExist(locator: By): Boolean {
    return try {
        findElement(locator)
        true
    } catch (ex: org.openqa.selenium.NoSuchElementException) {
        false
    }

}