package ee.zxz.helloapi.mapper;

import ee.zxz.helloapi.domain.Link;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LinkMapper {

    /**
     * 获取所有友链列表
     *
     * @param pageSize 每页数量
     * @param offset   偏移量 = (page - 1) * pageSize
     * @param desc     True或False，true为创建时间最新的在上面
     * @return LinkList
     */
    @Select("select * from helloapi_links " +
            "ORDER BY " +
            "CASE WHEN #{desc} THEN created END DESC, " +
            "CASE WHEN NOT #{desc} THEN created END " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<Link> getLinkList(int pageSize, int offset, boolean desc);

    /**
     * 获取所有友链列表（总数）
     *
     * @return 所有友链总数
     */
    @Select("select count(*) from helloapi_links")
    int getLinkListCount();

    /**
     * GetLinkListSearch - 获取用户列表(搜索)
     *
     * @param keyword  搜索关键词 username/nick/id
     * @param pageSize 每页数量
     * @param offset   偏移量
     * @return 返回用户列表，否则返回null
     */
    @Select("SELECT * FROM `helloapi_links` " +
            "WHERE (#{keyword} IS NULL OR #{keyword} = '' " +
            "OR `name` LIKE CONCAT('%', #{keyword}, '%') " +
            "OR `link_id` LIKE CONCAT('%', #{keyword}, '%') " +
            "OR `desc` = #{keyword}) " +
            "ORDER BY `created` DESC " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<Link> getLinkListSearch(String keyword, int pageSize, int offset);

    /**
     * GetLinkListSearchCount - 获取用户列表(搜索总数量)
     *
     * @param keyword 搜索关键词 username/nick/id
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM `helloapi_links` " +
            "WHERE (#{keyword} IS NULL OR #{keyword} = '' " +
            "OR `name` LIKE CONCAT('%', #{keyword}, '%') " +
            "OR `link_id` LIKE CONCAT('%', #{keyword}, '%') " +
            "OR `desc` = #{keyword})")
    int getLinkListSearchCount(String keyword);

    /**
     * 新增友链
     *
     * @param link 友链对象
     * @return 插入行数
     */
    @Insert("INSERT INTO `helloapi_links` (`name`, `url`, `desc`, `avatar`) VALUES (#{name}, #{url}, #{desc}, #{avatar})")
    @Options(useGeneratedKeys = true, keyProperty = "link_id")
    int insertLink(Link link);

    /**
     * 更新友链
     *
     * @param link 友链对象
     * @return 更新行数
     */
    @Update("UPDATE `helloapi_links` SET `name` = #{name}, `url` = #{url}, `desc` = #{desc}, `avatar` = #{avatar} WHERE `link_id` = #{link_id}")
    int updateLink(Link link);

    /**
     * 删除友链
     *
     * @param linkId 友链ID
     * @return 删除行数
     */
    @Delete("DELETE FROM `helloapi_links` WHERE `link_id` = #{linkId}")
    int deleteLink(int linkId);

    /**
     * 根据ID获取友链
     *
     * @param linkId 友链ID
     * @return 友链对象
     */
    @Select("SELECT * FROM `helloapi_links` WHERE `link_id` = #{linkId}")
    Link getLinkById(int linkId);


}
