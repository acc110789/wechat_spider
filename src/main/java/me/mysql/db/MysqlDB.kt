package me.mysql.db

import java.sql.*
import java.util.ArrayList
import java.util.HashMap

object MysqlDB {

    private const val DRIVER = "com.mysql.jdbc.Driver"

    private const val HOST = "localhost:3306"

    private const val DB_NAME = "db_we_chat_article"

    private const val USER = "zhangxiaolong"

    private const val PASSWORD = "12345678"


    init {
        println("加载数据库驱动程序...")
        Class.forName(DRIVER)
    }

    private val connection: Connection by lazy {
        println("connecting to mysql ...")
        val conStr = "jdbc:mysql://$HOST/$DB_NAME?user=$USER&password=$PASSWORD&useUnicode=true&characterEncoding=utf-8"
        DriverManager.getConnection(conStr)
    }

    /**
     * insert update delete SQL语句的执行的统一方法
     * @param sql SQL语句
     * @param params 参数数组，若没有参数则为null
     * @return 受影响的行数
     */
    fun executeUpdate(sql: String, params: Array<Any?>?): Int {
        val preparedStatement = connection.prepareStatement(sql)

        // 参数赋值
        if (params != null) {
            for (i in params.indices) {
                preparedStatement.setObject(i + 1, params[i])
            }
        }

        // 执行
        return preparedStatement.executeUpdate()
    }

    /**
     * SQL 查询将查询结果直接放入ResultSet中
     * @param sql SQL语句
     * @param params 参数数组，若没有参数则为null
     * @return 结果集
     */
    private fun executeQueryRS(sql: String, params: Array<Any>?): ResultSet? {
        // 调用SQL
        val preparedStatement = connection.prepareStatement(sql)

        // 参数赋值
        if (params != null) {
            for (i in params.indices) {
                preparedStatement.setObject(i + 1, params[i])
            }
        }

        // 执行
        return preparedStatement.executeQuery()
    }

    /**
     * SQL 查询将查询结果：一行一列
     * @param sql SQL语句
     * @param params 参数数组，若没有参数则为null
     * @return 结果集
     */
    fun executeQuerySingle(sql: String, params: Array<Any>?): Any? {
        // 调用SQL
        val preparedStatement = connection.prepareStatement(sql)
        // 参数赋值
        if (params != null) {
            for (i in params.indices) {
                preparedStatement.setObject(i + 1, params[i])
            }
        }

        // 执行
        val resultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
            return resultSet.getObject(1)
        }

        return null
    }

    /**
     * 获取结果集，并将结果放在List中
     *
     * @param sql
     * SQL语句
     * @return List
     * 结果集
     */
    fun executeQuery(sql: String, params: Array<Any>): List<Any> {
        // 执行SQL获得结果集
        val rs = executeQueryRS(sql, params)

        rs ?: return emptyList()

        // 创建ResultSetMetaData对象
        val rsMetaData = rs.metaData

        // 获得结果集列数
        val columnCount = rsMetaData.columnCount

        // 创建List
        val list = ArrayList<Any>()

        // 将ResultSet的结果保存到List中
        while (rs.next()) {
            val map = HashMap<String, Any>()
            for (i in 1..columnCount) {
                map[rsMetaData.getColumnLabel(i)] = rs.getObject(i)
            }
            list.add(map)
        }

        return list
    }
}
