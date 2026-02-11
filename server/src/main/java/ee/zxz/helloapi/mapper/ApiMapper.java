package ee.zxz.helloapi.mapper;

import ee.zxz.helloapi.domain.ApiApp;
import ee.zxz.helloapi.domain.ApiParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ApiMapper {

    /**
     * 获取所有API列表
     *
     * @param pageSize 每页数量
     * @param offset 偏移量 = (page - 1) * pageSize
     * @return ApiApp列表
     */
    @Select("select * from api_apps ORDER BY created DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<ApiApp> getApiList(int pageSize, int offset);

    /**
     * 获取API参数列表
     *
     * @param apiId API ID
     * @return ApiParam列表
     */
    @Select("select * from api_params where api_id = #{apiId}")
    List<ApiParam> getApiParam(int apiId);


    /**
     * 创建API应用
     *
     * @param apiApp ApiApp对象
     * @return 插入成功的行数
     */
    @Insert("insert into api_apps (title,smallTitle,status,type,url,sendtype,returnType,returnContent,created,user_id) values (#{title}, #{smallTitle}, #{status}, #{type}, #{url}, #{sendType}, #{returnType}, #{returnContent}, #{created}, #{user_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createApiApp(ApiApp apiApp);

    /**
     * 创建API参数
     *
     * @param apiParam ApiParam对象
     * @return 插入成功的行数
     */
    @Insert("insert into api_params (api_id, name, required, type, msg) values (#{api_id}, #{name}, #{required}, #{type}, #{msg})")
    int createApiParam(ApiParam apiParam);

    /**
     * 检测API是否存在
     *
     * @param apiId API ID
     * @return 大于0表示存在
     */
    @Select("select count(*) from api_apps where id = #{apiId}")
    int checkApiExist(int apiId);

    /**
     * 检测userID是不是Api的创建人
     *
     * @param apiId  API ID
     * @param userId 用户ID
     * @return 大于0表示是创建人
     */
    @Select("select count(*) from api_apps where id = #{apiId} and user_id = #{userId}")
    int checkApiCreator(int apiId, int userId);

    /**
     * 更新API应用
     *
     * @param apiApp ApiApp对象
     * @return 更新成功的行数
     */
    @Update("update api_apps set title = #{title}, smallTitle = #{smallTitle}, status = #{status}, type = #{type}, url = #{url}, sendType = #{sendType}, returnType = #{returnType}, returnContent = #{returnContent} where id = #{id}")
    int updateApiApp(ApiApp apiApp);

    /**
     * 更新API参数
     *
     * @param apiParam ApiParam对象
     * @return 更新成功的行数
     */
    @Update("update api_params set name = #{name}, required = #{required}, type = #{type}, msg = #{msg} where api_id = #{api_id}")
    int updateApiParam(ApiParam apiParam);

    /**
     * API参数是否存在
     *
     * @param apiParam ApiParam对象
     * @return 大于0表示存在
     */
    @Select("select count(*) from api_params where api_id = #{api_id} and name = #{name}")
    int checkApiParamExist(ApiParam apiParam);

    /**
     * 删除API参数
     *
     * @param apiId API ID
     * @param name  参数名
     */
    @Delete("delete from api_params where api_id = #{api_id} and name = #{name}")
    void deleteApiParam(int apiId, String name);

    /**
     * 删除API所有参数
     *
     * @param apiId API ID
     */
    @Delete("delete from api_params where api_id = #{apiId}")
    void deleteApiParamAll(int apiId);

    /**
     * 删除API应用
     *
     * @param apiId API ID
     */
    @Delete("delete from api_apps where id = #{apiId}")
    void deleteApiApp(int apiId);
}
