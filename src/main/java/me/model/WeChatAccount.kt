package me.model


/**
 * 一个微信公众号
 */
data class WeChatAccount(
        //昵称
        val nick: String,

        //公众号
        val weChatId: String,

        //抓取到的最新的文章id
        val lastArticleTitle: String? = null
) {
    companion object {
        val all: List<WeChatAccount> = arrayListOf<WeChatAccount>().apply {
            add(WeChatAccount("创意DIY", "ideadiy"))

            add(WeChatAccount("XY500W", "xy500wjd"))

            add(WeChatAccount("hellodd", "123"))

            add(WeChatAccount("玖拾度智能衣柜橱柜", "jiushidujiaju"))

            add(WeChatAccount("师兄网", "vipshixiong"))

            add(WeChatAccount("芙蓉王", "fu-rongwang"))

            add(WeChatAccount("长沙里手", "changshatong"))

            add(WeChatAccount("长沙吃货", "cschwx"))
        }

        val new: List<WeChatAccount> = arrayListOf<WeChatAccount>().apply {
            getAccountPair().forEach {
                add(WeChatAccount(it.first , it.second))
            }
        }

        private fun getAccountPair(): List<Pair<String, String>> {
            return listOf(
                    "学生安全教育平台" to "xueanquan123",
                    "夜听" to "yetingfm",
                    "十点读书" to "duhaoshu",
                    "中国移动" to "cmccguanfang",
                    "球球大作战" to "bobsuperjuice",
                    "微店" to "vdian_niall",
                    "卡娃微卡" to "kawa01",
                    "开心消消乐" to "happyxiaoxiaole",
                    "二更" to "ergengshipin",
                    "一条" to "yitiaotv",
                    "中国工商银行" to "icbc601398",
                    "龙卡信用卡" to "CCB_4008200588",
                    "有书" to "youshucc",
                    "罗辑思维" to "luojisw",
                    "林清玄读书会" to "linqingxuanOl",
                    "教育百师通" to "safeOl",
                    "中国电信" to "chinatelecom-189",
                    "微路况" to "weixinlukuang",
                    "汽车之家" to "autohomeweixin",
                    "MINISO名创优品" to "minisohome"
            )
        }
    }
}