package ee.zxz.helloapi.mapper;

import ee.zxz.helloapi.domain.ApiApp;
import ee.zxz.helloapi.domain.Setting;
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
     * @param username 用户名
     * @return 返回条数大于0则为存在
     */
    @Select("SELECT COUNT(*) FROM `helloapi_users` WHERE `username` = #{username}")
    int checkUsernameExists(String username);

    /**
     * Register - 注册账号
     *
     * @param username 用户名
     * @param password 密码
     * @param nick     昵称
     * @param mode     模式 0-普通用户 1-管理员
     * @return 返回插入条数大于0则为成功
     */
    @Insert("INSERT INTO `helloapi_users` (`username`, `password`,`nick`,`mode`,`mail`) VALUES (#{username}, #{password},#{nick},#{mode},#{mail})")
    int register(String username, String password, String nick, int mode, String mail);


    /**
     * Login - 登录账号
     *
     * @param username 用户名
     * @param password 密码，需要先转为MD5再提交
     * @return 登录成功后返回用户信息，否则返回null
     */
    @Select("SELECT * FROM `helloapi_users` WHERE `username` = #{username} AND `password` = #{password}")
    User login(String username, String password);

    /**
     * UserInfo - 获取用户信息
     *
     * @param id 用户ID
     * @return 返回用户信息，否则返回null
     */
    @Select("SELECT * FROM `helloapi_users` WHERE `id` = #{id}")
    User getUserInfo(int id);

    /**
     * GetUserKey - 获取用户密钥
     *
     * @param userId 用户ID
     * @return 返回用户密钥，否则返回null
     */
    @Select("SELECT * FROM `helloapi_user_keys` WHERE `user_id` = #{userId}")
    UserKey getUserKey(int userId);

    /**
     * CheckUserKeyExists - 检查用户密钥是否存在
     *
     * @param key 密钥
     * @return 返回条数大于0则为密钥存在
     */
    @Select("SELECT COUNT(*) FROM `helloapi_user_keys` WHERE `key` = #{key}")
    int checkUserKeyExists(String key);

    /**
     * DeleteUser - 删除用户
     *
     * @param userId 用户ID
     */
    @Delete("DELETE FROM `helloapi_users` WHERE `id` = #{userId}")
    void deleteUser(int userId);

    /**
     * DeleteUserKey - 删除用户密钥
     *
     * @param userId 用户ID
     */
    @Delete("DELETE FROM `helloapi_user_keys` WHERE `user_id` = #{userId}")
    void deleteUserKey(int userId);

    /**
     * CheckUserIdExists - 检查用户ID是否存在
     *
     * @param userId 用户ID
     * @return 返回条数大于0则为存在
     */
    @Select("SELECT COUNT(*) FROM `helloapi_users` WHERE `id` = #{userId}")
    int checkUserIdExists(int userId);

    /**
     * GetUserApiList - 获取用户API列表(分页)
     *
     * @param userId   用户ID
     * @param pageSize 每页数量
     * @param offset   偏移量
     * @return 返回用户API列表，否则返回null
     */
    @Select("SELECT * FROM `helloapi_api_apps` WHERE `user_id` = #{userId} ORDER BY created DESC LIMIT #{pageSize} OFFSET #{offset} ")
    List<ApiApp> getUserApiList(int userId, int pageSize, int offset);

    /**
     * GetUserApiListAll - 获取用户API列表(全部)
     *
     * @param userId 用户ID
     * @return 返回用户API列表，否则返回null
     */
    @Select("SELECT * FROM `helloapi_api_apps` WHERE `user_id` = #{userId}")
    List<ApiApp> getUserApiListAll(int userId);

    /**
     * GetUserApiListAllCount - 获取用户API总数量
     *
     * @param userId 用户ID
     * @return 返回用户API总数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_api_apps` WHERE `user_id` = #{userId}")
    int getUserApiListAllCount(int userId);

    /**
     * ResetUserKey - 重置用户密钥
     *
     * @param userId 用户ID
     * @param key    密钥
     */
    @Insert("INSERT INTO `helloapi_user_keys` (`user_id`, `key`) VALUES (#{userId}, #{key}) ON DUPLICATE KEY UPDATE `key` = #{key}")
    void resetUserKey(int userId, String key);

    /**
     * GetUserCount - 获取用户数量
     *
     * @return 返回用户数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_users`")
    int getUserCount();

    /**
     * GetUserApiListSearch - 获取用户API列表(搜索)
     *
     * @param userId      用户ID
     * @param keyword     搜索关键词
     * @param type        类型 0免费 1付费 -1表示不筛选
     * @param status      状态 0正常 1异常 2维护 -1表示不筛选
     * @param view_status 视图状态 0通过 1拒绝 2审核中 -1表示不筛选
     * @param pageSize    每页数量
     * @param offset      偏移量
     * @return 返回用户列表，否则返回null
     */
    @Select("SELECT * FROM `helloapi_api_apps` WHERE `user_id` = #{userId} AND (#{keyword} IS NULL OR #{keyword} = '' OR `title` LIKE CONCAT('%', #{keyword}, '%') OR `id` = #{keyword} OR `smallTitle` LIKE CONCAT('%', #{keyword}, '%')) AND (#{type} = -1 OR `type` = #{type}) AND (#{status} = -1 OR `status` = #{status}) AND (#{view_status} = -1 OR `view_status` = #{view_status}) ORDER BY created DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<ApiApp> getUserApiListSearch(int userId, String keyword, int type, int status, int view_status, int pageSize, int offset);

    /**
     * GetUserApiListSearchCount - 获取用户API列表(搜索)总数量
     *
     * @param userId      用户ID
     * @param keyword     搜索关键词
     * @param type        类型 0免费 1付费 -1表示不筛选
     * @param status      状态 0正常 1异常 2维护 -1表示不筛选
     * @param view_status 视图状态 0通过 1拒绝 2审核中 -1表示不筛选
     * @return 返回用户列表(搜索)总数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_api_apps` WHERE `user_id` = #{userId} AND (#{keyword} IS NULL OR #{keyword} = '' OR `title` LIKE CONCAT('%', #{keyword}, '%') OR `id` = #{keyword} OR `smallTitle` LIKE CONCAT('%', #{keyword}, '%')) AND (#{type} = -1 OR `type` = #{type}) AND (#{status} = -1 OR `status` = #{status}) AND (#{view_status} = -1 OR `view_status` = #{view_status})")
    int getUserApiListSearchCount(int userId, String keyword, int type, int status, int view_status);

    /**
     * UpdateUserNick - 修改用户昵称
     *
     * @param userId 用户ID
     * @param nick   昵称
     */
    @Update("UPDATE `helloapi_users` SET `nick` = #{nick} WHERE `id` = #{userId}")
    void updateUserNick(int userId, String nick);

    /**
     * UpdateUserMail - 修改用户邮箱
     *
     * @param userId 用户ID
     * @param mail   昵称
     */
    @Update("UPDATE `helloapi_users` SET `mail` = #{mail} WHERE `id` = #{userId}")
    void updateUserMail(int userId, String mail);

    /**
     * updateUserPassword - 修改用户密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Update("UPDATE `helloapi_users` SET `password` = #{newPassword} WHERE `id` = #{userId} AND `password`=#{oldPassword}")
    void updateUserPassword(int userId, String oldPassword, String newPassword);

    /**
     * setUserPassword - 设置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     */
    @Update("UPDATE `helloapi_users` SET `password` = #{password} WHERE `id` = #{userId} ")
    void setUserPassword(int userId, String password);

    /**
     * GetUserList - 获取用户列表(分页)
     *
     * @param pageSize 每页数量
     * @param offset   偏移量
     * @return 返回用户列表，否则返回null
     */
    @Select("SELECT id, username, nick, mode, mail, created, updated FROM `helloapi_users` ORDER BY mode DESC, created LIMIT #{pageSize} OFFSET #{offset} ")
    List<User> getUserList(int pageSize, int offset);

    /**
     * GetUserListAllCount - 获取用户列表(总数量）
     *
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_users`")
    int getUserListAllCount();

    /**
     * GetUserListSearch - 获取用户列表(搜索)
     *
     * @param keyword  搜索关键词 username/nick/id
     * @param mode     类型 0普通 1管理员 -1表示不筛选
     * @param pageSize 每页数量
     * @param offset   偏移量
     * @return 返回用户列表，否则返回null
     */
    @Select("SELECT id, username, nick, mode, mail, created, updated FROM `helloapi_users` " +
            "WHERE (#{keyword} IS NULL OR #{keyword} = '' " +
            "OR `username` LIKE CONCAT('%', #{keyword}, '%') " +
            "OR `nick` LIKE CONCAT('%', #{keyword}, '%') " +
            "OR `id` = #{keyword}) " +
            "AND (#{mode} = -1 OR `mode` = #{mode}) " +
            "ORDER BY `mode` DESC, `created`  " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<User> getUserListSearch(String keyword, int mode, int pageSize, int offset);

    /**
     * GetUserListSearchCount - 获取用户列表(搜索总数量)
     *
     * @param keyword 搜索关键词 username/nick/id
     * @param mode    类型 0普通 1管理员 -1表示不筛选
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_users` " +
            "WHERE (#{keyword} IS NULL OR #{keyword} = '' " +
            "OR `username` LIKE CONCAT('%', #{keyword}, '%') " +
            "OR `nick` LIKE CONCAT('%', #{keyword}, '%') " +
            "OR `id` = #{keyword}) " +
            "AND (#{mode} = -1 OR `mode` = #{mode})")
    int getUserListSearchCount(String keyword, int mode);


    /**
     * SetUserMode - 设置用户权限
     *
     * @param userId 用户ID
     * @param mode   权限0普通1管理
     */
    @Select("UPDATE `helloapi_users` SET `mode` = #{mode} WHERE `id` = #{userId}")
    void setUserMode(int userId, int mode);

    /**
     * GetSettingValue - 获取设置的参数值
     *
     * @param key 键值
     * @return 键值内容
     */
    @Select("SELECT `value` FROM `helloapi_settings` WHERE `key` = #{key}")
    String getSettingValue(String key);

    /**
     * UpdateSettingValue - 更新设置的参数值
     *
     * @param key   键值
     * @param value 键值内容
     */
    @Insert("INSERT INTO `helloapi_settings` (`key`, `value`) VALUES (#{key}, #{value}) ON DUPLICATE KEY UPDATE `value` = #{value}")
    void updateSettingValue(String key, String value);

    /**
     * GetSettingValueAll - 获取设置的参数值全部
     *
     * @return Setting数组
     */
    @Select("SELECT * FROM `helloapi_settings`")
    List<Setting> getSettingValueAll();

    /**
     * CheckUserIsAdmin - 检查用户是否为管理员
     *
     * @param userId 用户ID
     * @return 返回条数大于0则为管理员
     */
    @Select("SELECT COUNT(*) FROM `helloapi_users` WHERE `id` = #{userId} AND `mode` = 1")
    int checkUserIsAdmin(int userId);

}
