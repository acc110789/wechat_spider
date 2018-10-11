package me.mysql.dao


import me.model.Article
import me.mysql.db.MysqlDB

import java.util.ArrayList

private const val DB_NAME_ARTICLE = "tg_collect_article_temp"

private const val SQL_FIND_ARTICLE = "select count(1) from $DB_NAME_ARTICLE where title = ?"

private const val SQL_INSERT_ARTICLE = "insert into $DB_NAME_ARTICLE " +
        "(weChatId,weChatNickName,title,url,imgLink,description,content,lastModifiedTime) " +
        "values(?,?,?,?,?,?,?,?)"

object ArticleDao {

    /** 把文章保存在数据库中 */
    fun save(article: Article): Int {
        val db = MysqlDB

        //判断文章在数据库中是否已经存在
        val findParam = ArrayList<String>()
        findParam.add(article.title)
        val obj = db.executeQuerySingle(SQL_FIND_ARTICLE, findParam.toTypedArray())
        val count = if (obj == null)  0 else Integer.parseInt(obj.toString())
        val exist = count > 0
        if(exist) {
            println("article: " + article.title + " already exist!")
            return 0
        }


        //插入数据库
        val insertValue = ArrayList<Any?>()
        insertValue.add(article.weChatId)
        insertValue.add(article.weChatNickName)
        insertValue.add(article.title)
        insertValue.add(article.url)
        insertValue.add(article.imgLink)
        insertValue.add(article.description)
        insertValue.add(article.content)
        insertValue.add(article.lastModifiedTime)

        val i = db.executeUpdate(SQL_INSERT_ARTICLE, insertValue.toTypedArray())
        if (i == 0) {
            println("insert article fail: ${article.title}")
        }
        return i
    }

}
