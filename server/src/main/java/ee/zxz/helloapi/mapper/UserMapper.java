package ee.zxz.helloapi.mapper;

import ee.zxz.helloapi.domain.Api_app;
import ee.zxz.helloapi.domain.User;
import ee.zxz.helloapi.domain.User_key;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
     * @param name 用户名
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
     * CheckUserAdmin - 检查用户是否为管理员
     *
     * @param id 用户ID
     * @return 返回条数大于0则为管理员
     */
    @Select("SELECT COUNT(*) FROM `users` WHERE `id` = #{id} AND `mode` = 1")
    int checkUserAdmin(int id);

    /**
     * GetUserKey - 获取用户密钥
     *
     * @param userId 用户ID
     * @return 返回用户密钥，否则返回null
     */
    @Select("SELECT * FROM `user_keys` WHERE `user_id` = #{userId}")
    User_key getUserKey(int userId);

    /**
     * CheckUserKey - 检查用户密钥是否正确
     *
     * @param userId 用户ID
     * @return 返回条数大于0则为存在
     */
    @Select("SELECT COUNT(*) FROM `user_keys` WHERE `user_id` = #{userId}")
    int checkUserKey(int userId);

    /**
     * DeleteUser - 删除用户
     *
     * @param userId 用户ID
     * @return 返回删除条数大于0则为成功
     */
    @Delete("DELETE FROM `users` WHERE `id` = #{userId}")
    int deleteUser(int userId);

     /**
      * DeleteUserKey - 删除用户密钥
      *
      * @param userId 用户ID
      * @return 返回删除条数大于0则为成功
      */
    @Delete("DELETE FROM `user_keys` WHERE `user_id` = #{userId}")
    int deleteUserKey(int userId);

    /**
     * CheckUserIdExists - 检查用户ID是否存在
     *
     * @param userId 用户ID
     * @return 返回条数大于0则为存在
     */
    @Select("SELECT COUNT(*) FROM `users` WHERE `id` = #{userId}")
    int checkUserIdExists(int userId);

     /**
      * GetUserApiList - 获取用户API列表
      *
      * @param userId 用户ID
      * @return 返回用户API列表，否则返回null
      */
    @Select("SELECT * FROM `api_apps` WHERE `user_id` = #{userId}")
    List<Api_app> getUserApiList(int userId);

}
