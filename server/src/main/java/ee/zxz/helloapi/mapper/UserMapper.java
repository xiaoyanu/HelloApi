package ee.zxz.helloapi.mapper;

import ee.zxz.helloapi.domain.ApiApp;
import ee.zxz.helloapi.domain.User;
import ee.zxz.helloapi.domain.UserKey;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * UserMapper - 用户Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * CheckUsernameExists - 检查用户名是否存在
     *
     * @param name 用户名
     * @return 返回条数大于0则为存在
     */
    @Select("SELECT COUNT(*) FROM `users` WHERE `name` = #{name}")
    int checkUsernameExists(String name);

    /**
     * Register - 注册账号
     *
     * @param name     用户名
     * @param password 密码
     * @param nick     昵称
     * @param mode     模式 0-普通用户 1-管理员
     * @return 返回插入条数大于0则为成功
     */
    @Insert("INSERT INTO `users` (`name`, `password`,`nick`,`mode`) VALUES (#{name}, #{password},#{nick},#{mode})")
    int register(String name, String password, String nick, int mode);


    /**
     * Login - 登录账号
     *
     * @param name     用户名
     * @param password 密码，需要先转为MD5再提交
     * @return 登录成功后返回用户信息，否则返回null
     */
    @Select("SELECT * FROM `users` WHERE `name` = #{name} AND `password` = #{password}")
    User login(String name, String password);

    /**
     * UserInfo - 获取用户信息
     *
     * @param id 用户ID
     * @return 返回用户信息，否则返回null
     */
    @Select("SELECT * FROM `users` WHERE `id` = #{id}")
    User getUserInfo(int id);

    /**
     * GetUserKey - 获取用户密钥
     *
     * @param userId 用户ID
     * @return 返回用户密钥，否则返回null
     */
    @Select("SELECT * FROM `user_keys` WHERE `user_id` = #{userId}")
    UserKey getUserKey(int userId);

    /**
     * CheckUserKeyExists - 检查用户密钥是否存在
     *
     * @param key    密钥
     * @return 返回条数大于0则为密钥存在
     */
    @Select("SELECT COUNT(*) FROM `user_keys` WHERE `key` = #{key}")
    int checkUserKeyExists(String key);

    /**
     * DeleteUser - 删除用户
     *
     * @param userId 用户ID
     */
    @Delete("DELETE FROM `users` WHERE `id` = #{userId}")
    void deleteUser(int userId);

    /**
     * DeleteUserKey - 删除用户密钥
     *
     * @param userId 用户ID
     */
    @Delete("DELETE FROM `user_keys` WHERE `user_id` = #{userId}")
    void deleteUserKey(int userId);

    /**
     * CheckUserIdExists - 检查用户ID是否存在
     *
     * @param userId 用户ID
     * @return 返回条数大于0则为存在
     */
    @Select("SELECT COUNT(*) FROM `users` WHERE `id` = #{userId}")
    int checkUserIdExists(int userId);

    /**
     * GetUserApiList - 获取用户API列表(分页)
     *
     * @param userId   用户ID
     * @param pageSize 每页数量
     * @param offset   偏移量
     * @return 返回用户API列表，否则返回null
     */
    @Select("SELECT * FROM `api_apps` WHERE `user_id` = #{userId} LIMIT #{pageSize} OFFSET #{offset}")
    List<ApiApp> getUserApiList(int userId, int pageSize, int offset);

    /**
     * GetUserApiListAll - 获取用户API列表(全部)
     *
     * @param userId 用户ID
     * @return 返回用户API列表，否则返回null
     */
    @Select("SELECT * FROM `api_apps` WHERE `user_id` = #{userId}")
    List<ApiApp> getUserApiListAll(int userId);

    /**
     * ResetUserKey - 重置用户密钥
     *
     * @param userId 用户ID
     * @param key    密钥
     * @param created 创建时间
     */
    @Insert("INSERT INTO `user_keys` (`user_id`, `key`,`created`) VALUES (#{userId}, #{key},#{created}) ON DUPLICATE KEY UPDATE `key` = #{key},`created` = #{created}")
    void resetUserKey(int userId, String key, long created);
}
