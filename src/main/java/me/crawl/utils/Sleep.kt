package me.crawl.utils

/**
 * 通用工具类
 */
object Sleep {

    /**随机时间睡眠 1秒到10秒之间*/
    fun random() {
        val start = 2000
        val end = 1000 * 5
        random(start, end)
    }

    /**
     * 随机时间睡眠 多少毫秒到多少毫秒之间
     */
    fun random(start: Int, end: Int) {
        val ms = Math.round(Math.random() * (end - start) + start).toInt()
        try {
            println("等待:" + ms + "毫秒")
            Thread.sleep(ms.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
