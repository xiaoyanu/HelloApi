package ee.zxz.helloapi.mapper;

import ee.zxz.helloapi.domain.Setting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SettingMapper {
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
}
