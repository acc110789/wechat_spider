package me.db


import me.crawl.utils.Logger
import me.model.Article

import java.util.ArrayList


private const val DB_NAME_ARTICLE = "tg_collect_article_temp"

private const val WE_CHAT_ID = "we_chat_id"

private const val WE_CHAT_NICK_NAME = "we_chat_nick_name"

private const val TITLE = "title"

private const val URL = "url"

private const val IMG_LINK = "img_link"

private const val DES = "description"

private const val CONTENT = "content"

private const val LAST_MODIFY_TIME = "last_modified_time"

private const val CREATE_TABLE_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS `$DB_NAME_ARTICLE`(" +
        "`$WE_CHAT_ID` VARCHAR(1000)," +
        "`$WE_CHAT_NICK_NAME` VARCHAR(1000)," +
        "`$TITLE` TEXT," +
        "`$URL` TEXT," +
        "`$IMG_LINK` TEXT," +
        "`$DES` TEXT," +
        "`$CONTENT` TEXT," +
        "`$LAST_MODIFY_TIME` TEXT" +
        ") "/*+
        "DEFAULT CHARSET=$ENCODING"*/

private const val SQL_FIND_ARTICLE = "select count(1) from $DB_NAME_ARTICLE where $WE_CHAT_ID = ? and $WE_CHAT_NICK_NAME = ? and $TITLE = ?"

private const val SQL_INSERT_ARTICLE = "insert into $DB_NAME_ARTICLE " +
        "($WE_CHAT_ID,$WE_CHAT_NICK_NAME,$TITLE,$URL,$IMG_LINK,$DES,$CONTENT,$LAST_MODIFY_TIME) " +
        "values(?,?,?,?,?,?,?,?)"

object ArticleDao {

    init {
        val result = MysqlDB.executeSql(CREATE_TABLE_IF_NOT_EXIST)
        Logger.log("init ArticleDao: $result")
    }

    /** 本地数据库是否存在对应的article */
    fun isArticleExist(article: Article): Boolean {
        //判断文章在数据库中是否已经存在
        val findParam = ArrayList<String>()
        findParam.add(article.weChatId)
        findParam.add(article.weChatNickName)
        findParam.add(article.title)
        val obj = MysqlDB.executeQuerySingle(SQL_FIND_ARTICLE, findParam.toTypedArray())
        val count = if (obj == null)  0 else Integer.parseInt(obj.toString())
        return count > 0
    }

    /** 把文章保存在数据库中 */
    fun save(article: Article): Int {
        if(isArticleExist(article)) return 0

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

        val i = MysqlDB.executeUpdate(SQL_INSERT_ARTICLE, insertValue.toTypedArray())
        Logger.log("insert article : ${article.title} , result : ${i > 0}")
        return i
    }

}
