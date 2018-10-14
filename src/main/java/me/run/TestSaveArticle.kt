package me.run

import me.model.Article
import me.mysql.dao.ArticleDao

object TestSaveArticle {

    @JvmStatic
    fun main(args: Array<String>) {
        val article = Article(
                weChatId = "test_we_chat_id",
                weChatNickName = "test_we_chat_nick_name",
                title = "test title",
                url = "test url",
                imgLink = "test image link",
                description = "test description",
                content = "请尊重孩子的磨蹭，90%的父母不知道的秘密\n" +
                        "点击蓝字关注\uD83D\uDC49  十点读书  前天\n" +
                        "\n" +
                        "\n" +
                        "请尊重孩子的磨蹭\n" +
                        "来自十点读书\n" +
                        "00:0016:26\n" +
                        "\n" +
                        "文 | 若桃 · 主播 | 文倩\n" +
                        "\n" +
                        "来源：亲宝宝育儿（ID：qbaobao6）\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "最近，发生在儿子身上的一件小事，改变了我对“磨蹭”的偏见！\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "那天有事不得不早起，6：30叫醒儿子，没睡醒的他有点烦躁和赖皮。",
                lastModifiedTime = "2018年7月6日"
        )
        ArticleDao.save(article)
    }

}