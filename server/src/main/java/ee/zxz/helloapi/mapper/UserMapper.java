package ee.zxz.helloapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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


}
