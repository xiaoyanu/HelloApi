package ee.zxz.helloapi.mapper;

import ee.zxz.helloapi.domain.ApiApp;
import ee.zxz.helloapi.domain.ApiKey;
import ee.zxz.helloapi.domain.ApiParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ApiMapper {

    /**
     * 获取所有API列表
     *
     * @param pageSize 每页数量
     * @param offset   偏移量 = (page - 1) * pageSize
     * @return ApiApp列表
     */
    @Select("select * from helloapi_api_apps ORDER BY created DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<ApiApp> getApiList(int pageSize, int offset);

    /**
     * 获取API参数列表
     *
     * @param apiId API ID
     * @return ApiParam列表
     */
    @Select("select * from helloapi_api_params where api_id = #{apiId}")
    List<ApiParam> getApiParam(int apiId);


    /**
     * 创建API应用
     *
     * @param apiApp ApiApp对象
     * @return 插入成功的行数
     */
    @Insert("insert into helloapi_api_apps (title,smallTitle,status,type,url,sendtype,returnType,returnContent,created,user_id) values (#{title}, #{smallTitle}, #{status}, #{type}, #{url}, #{sendType}, #{returnType}, #{returnContent}, #{created}, #{user_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createApiApp(ApiApp apiApp);

    /**
     * 创建API参数
     *
     * @param apiParam ApiParam对象
     * @return 插入成功的行数
     */
    @Insert("insert into helloapi_api_params (api_id, name, required, type, msg) values (#{api_id}, #{name}, #{required}, #{type}, #{msg})")
    int createApiParam(ApiParam apiParam);

    /**
     * 检测API是否存在
     *
     * @param apiId API ID
     * @return 大于0表示存在
     */
    @Select("select count(*) from helloapi_api_apps where id = #{apiId}")
    int checkApiExist(int apiId);

    /**
     * 检测userID是不是Api的创建人
     *
     * @param apiId  API ID
     * @param userId 用户ID
     * @return 大于0表示是创建人
     */
    @Select("select count(*) from helloapi_api_apps where id = #{apiId} and user_id = #{userId}")
    int checkApiCreator(int apiId, int userId);

    /**
     * 更新API应用
     *
     * @param apiApp ApiApp对象
     * @return 更新成功的行数
     */
    @Update("update helloapi_api_apps set title = #{title}, smallTitle = #{smallTitle}, status = #{status}, type = #{type}, url = #{url}, sendType = #{sendType}, returnType = #{returnType}, returnContent = #{returnContent} where id = #{id}")
    int updateApiApp(ApiApp apiApp);

    /**
     * 更新API参数
     *
     * @param apiParam ApiParam对象
     * @return 更新成功的行数
     */
    @Update("update helloapi_api_params set name = #{name}, required = #{required}, type = #{type}, msg = #{msg} where api_id = #{api_id}")
    int updateApiParam(ApiParam apiParam);

    /**
     * API参数是否存在（通过ApiParam对象）
     *
     * @param apiParam ApiParam对象
     * @return 大于0表示存在
     */
    @Select("select count(*) from helloapi_api_params where api_id = #{api_id} and name = #{name}")
    int checkApiParamExistObj(ApiParam apiParam);

    /**
     * API参数是否存在
     *
     * @param apiId API ID
     * @param name  参数名
     * @return 大于0表示存在
     */
    @Select("select count(*) from helloapi_api_params where api_id = #{apiId} and name = #{name}")
    int checkApiParamExist(int apiId, String name);

    /**
     * 删除API参数
     *
     * @param apiId API ID
     * @param name  参数名
     */
    @Delete("delete from helloapi_api_params where api_id = #{apiId} and name = #{name}")
    void deleteApiParam(int apiId, String name);

    /**
     * 删除API所有参数
     *
     * @param apiId API ID
     */
    @Delete("delete from helloapi_api_params where api_id = #{apiId}")
    void deleteApiParamAll(int apiId);

    /**
     * 删除API应用
     *
     * @param apiId API ID
     */
    @Delete("delete from helloapi_api_apps where id = #{apiId}")
    void deleteApiApp(int apiId);

    /**
     * 搜索Api接口
     *
     * @param keyword  搜索关键词
     * @param pageSize 每页数量
     * @param offset   偏移量
     * @return ApiApp列表
     */
    @Select("select * from helloapi_api_apps where  (`title` LIKE CONCAT('%',#{keyword},'%') OR `smalltitle` LIKE CONCAT('%',#{keyword},'%')) ORDER BY created DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<ApiApp> searchApiList(String keyword, int pageSize, int offset);

    /**
     * 获取Api接口详情
     *
     * @param apiId API ID
     * @return ApiApp对象
     */
    @Select("select * from helloapi_api_apps where id = #{apiId}")
    ApiApp getApiApp(int apiId);

    /**
     * 创建API密钥
     *
     * @param apiId   API ID
     * @param key     密钥
     * @param created 创建时间
     * @param type    密钥类型
     * @param started 开始时间
     * @param expired 过期时间
     * @param count   调用次数
     */
    @Insert("insert into helloapi_api_keys (`api_id`, `key`, `created` , `type`, `started`,`expired`,`count`) values (#{apiId}, #{key}, #{created}, #{type}, #{started}, #{expired}, #{count})")
    void createApiKey(int apiId, String key, long created, int type, long started, long expired, int count);

    /**
     * 查询API密钥是否存在
     *
     * @param key 密钥
     * @return 大于0表示存在
     */
    @Select("select count(*) from helloapi_api_keys where `key` = #{key}")
    int checkApiKeyExist(String key);

    /**
     * 减少API密钥调用次数
     *
     * @param key 密钥
     */
    @Update("update helloapi_api_keys set count = count - 1 where `key` = #{key}")
    void reduceApiKeyCount(String key);

    /**
     * 获取API密钥信息
     *
     * @param key 密钥
     * @return ApiKey对象
     */
    @Select("select * from helloapi_api_keys where `key` = #{key}")
    ApiKey getApiKey(String key);

    /**
     * 获取API密钥列表
     *
     * @param userId 用户ID
     * @param pageSize 每页数量
     * @param offset   偏移量
     * @return ApiKey列表
     */
    @Select("select * from helloapi_api_keys where api_id in (select id from api_apps where user_id = #{userId}) ORDER BY created DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<ApiKey> getApiKeyList(int userId, int pageSize, int offset);

    /**
     * 通过API密钥查询API应用ID
     *
     * @param key 密钥
     * @return API应用ID
     */
    @Select("select api_id from helloapi_api_keys where `key` = #{key}")
    int getApiIdByKey(String key);

    /**
     * 更新API密钥
     *
     * @param key     密钥
     * @param type    密钥类型
     * @param started 开始时间
     * @param expired 过期时间
     * @param count   调用次数
     */
    @Update("update helloapi_api_keys set `type` = #{type}, `started` = #{started}, `expired` = #{expired}, `count` = #{count} where `key` = #{key}")
    void updateApiKey(String key, int type, long started, long expired, int count);

    /**
     * 删除API密钥
     *
     * @param key 密钥
     */
    @Delete("delete from helloapi_api_keys where `key` = #{key}")
    void deleteApiKey(String key);

    /**
     * 重置API密钥
     *
     * @param key 密钥
     */
    @Update("update helloapi_api_keys set `key` = #{newKey} where `key` = #{key}")
    void resetApiKey(String key, String newKey);

    /**
     * 插入API日志
     *
     * @param apiId  API ID
     * @param ip     IP地址
     * @param time   时间
     * @param header 请求头
     * @param body   请求体
     */
    @Insert("insert into helloapi_api_request_logs (`api_id`,`ip`, `time`,`header`, `body`) values (#{apiId}, #{ip}, #{time}, #{header}, #{body})")
    void insertApiLog(int apiId, String ip, String time, Object header, Object body);

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
}
