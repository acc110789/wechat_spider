package me.crawl.utils

import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver

/**
 * Created by Administrator on 2015/9/23.
 */
object Browser {
    private fun obtainChromeWebDriver(): WebDriver {
        System.setProperty(CHROME_DRIVER_NAME, CHROME_DRIVER_PATH)
        return ChromeDriver()
    }

    private fun obtainFirefoxDriver(): WebDriver {
        return FirefoxDriver()
    }

    fun obtainWebDriver(): WebDriver {
        val os = System.getProperty(OS_NAME)
        return if (os.contains(WINDOW)) {
            obtainChromeWebDriver()
        } else {
            obtainFirefoxDriver()
        }
    }
}
