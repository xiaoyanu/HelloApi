package ee.zxz.helloapi.mapper;

import ee.zxz.helloapi.domain.DTO.ApiTodayArray;
import ee.zxz.helloapi.domain.DTO.ApiWeekArray;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatMapper {
    /**
     * 增加ApiId调用次数
     *
     * @param apiId API ID
     */
    @Insert("INSERT INTO `helloapi_api_views` (`api_id`, `count`) VALUES (#{apiId}, 1) ON DUPLICATE KEY UPDATE `count` = `count` + 1")
    void addApiCount(int apiId);

    /**
     * 删除API调用次数
     *
     * @param apiId API ID
     */
    @Delete("delete from helloapi_api_views where api_id = #{apiId}")
    void deleteApiCount(int apiId);

    /**
     * 删除API日志
     *
     * @param apiId API ID
     */
    @Delete("delete from helloapi_api_request_logs where api_id = #{apiId}")
    void deleteApiLog(int apiId);

    /**
     * 插入API日志
     *
     * @param apiId  API ID
     * @param ip     IP地址
     * @param header 请求头
     * @param body   请求体
     */
    @Insert("insert into helloapi_api_request_logs (`api_id`,`ip`, `header`, `body`,`api_key`,`user_id`) values (#{apiId}, #{ip}, #{header}, #{body},#{apiKey},#{userId})")
    void insertApiLog(int apiId, String ip, Object header, Object body, String apiKey, int userId);

    /**
     * GetApiAllCount - 获取所有API调用次数的总和
     *
     * @return 返回数量，若无数据则返回0
     */
    @Select("select COALESCE(SUM(`count`), 0) from helloapi_api_views")
    int getApiAllCount();

    /**
     * GetUserCount - 获取用户数量
     *
     * @return 返回数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_users`")
    int getUserCount();

    /**
     * GetApiTodayCount - 获取今日API调用总量
     *
     * @return 返回数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_api_request_logs` " +
            "WHERE `time` >= CURDATE() " +
            "AND `time` < CURDATE() + INTERVAL 1 DAY")
    int getApiTodayCount();

    /**
     * GetApiTodayCountChange - 获取今日API调用总量，较昨天增加/减少了多少
     *
     * @return 返回数量
     */
    @Select("SELECT IFNULL(SUM(IF(`time` >= CURDATE(), 1, -1)), 0) " +
            "FROM `helloapi_api_request_logs` " +
            "WHERE `time` >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) " +
            "AND `time` < CURDATE() + INTERVAL 1 DAY")
    int getApiTodayCountChange();

    /**
     * GetApiWeekCount - 获取近7天API调用总量
     *
     * @return 返回数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_api_request_logs` " +
            "WHERE `time` >= CURDATE() - INTERVAL 6 DAY " +
            "AND `time` < CURDATE() + INTERVAL 1 DAY")
    int getApiWeekCount();

    /**
     * GetApiWeekCountChange - 获取近7天API调用总量，较上一个7天增加/减少了多少
     *
     * @return 返回数量
     */
    @Select("SELECT IFNULL(SUM(IF(`time` >= CURDATE() - INTERVAL 6 DAY, 1, -1)), 0) AS count_change " +
            "FROM `helloapi_api_request_logs` " +
            "WHERE `time` >= CURDATE() - INTERVAL 13 DAY " +
            "AND `time` < CURDATE() + INTERVAL 1 DAY")
    int getApiWeekCountChange();

    /**
     * 获取近7天API调用统计（数组）
     *
     * @return 数组
     */
    @Select("SELECT CAST(Days.Date AS DATETIME) AS date, " +
            "       IFNULL(Stats.cnt, 0) AS count " +
            "FROM (" +
            "    SELECT CURDATE() - INTERVAL 6 DAY AS Date UNION ALL " +
            "    SELECT CURDATE() - INTERVAL 5 DAY UNION ALL " +
            "    SELECT CURDATE() - INTERVAL 4 DAY UNION ALL " +
            "    SELECT CURDATE() - INTERVAL 3 DAY UNION ALL " +
            "    SELECT CURDATE() - INTERVAL 2 DAY UNION ALL " +
            "    SELECT CURDATE() - INTERVAL 1 DAY UNION ALL " +
            "    SELECT CURDATE()" +
            ") AS Days " +
            "LEFT JOIN (" +
            "    SELECT DATE(`time`) AS stat_date, COUNT(*) AS cnt " +
            "    FROM `helloapi_api_request_logs` " +
            "    WHERE `time` >= CURDATE() - INTERVAL 6 DAY " +
            "    AND `time` < CURDATE() + INTERVAL 1 DAY " + // <-- 这里是关键补充
            "    GROUP BY DATE(`time`) " +
            ") AS Stats ON Days.Date = Stats.stat_date")
    List<ApiWeekArray> getApiWeekCountArray();

    /**
     * 获取今天API调用数量前7的接口（包含今天调用次数为0的接口）
     *
     * @return 数组
     */
    @Select("SELECT a.title AS name, IFNULL(t.today_count, 0) AS count " +
            "FROM helloapi_api_apps a " +
            "LEFT JOIN ( " +
            "    SELECT api_id, COUNT(log_id) AS today_count " +
            "    FROM helloapi_api_request_logs " +
            "    WHERE time >= CURDATE() AND time < DATE_ADD(CURDATE(), INTERVAL 1 DAY) " +
            "    GROUP BY api_id " +
            ") t ON a.id = t.api_id " +
            "ORDER BY count DESC " +
            "LIMIT 7")
    List<ApiTodayArray> getApiTodayCountArray();

    /**
     * GetApiMonthCount - 获取近30天API调用总量
     *
     * @return 返回数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_api_request_logs` " +
            "WHERE `time` >= CURDATE() - INTERVAL 29 DAY " +
            "AND `time` < CURDATE() + INTERVAL 1 DAY")
    int getApiMonthCount();

    /**
     * GetApiMonthCountChange - 获取近30天API调用总量，较上一个30天增加/减少了多少
     *
     * @return 返回数量
     */
    @Select("SELECT IFNULL(SUM(IF(`time` >= CURDATE() - INTERVAL 29 DAY, 1, -1)), 0) AS count_change " +
            "FROM `helloapi_api_request_logs` " +
            "WHERE `time` >= CURDATE() - INTERVAL 59 DAY " +
            "AND `time` < CURDATE() + INTERVAL 1 DAY")
    int getApiMonthCountChange();
}
