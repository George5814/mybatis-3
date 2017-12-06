package org.apache.ibatis.base;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**.
 * @author jingzz
 * @since 2016年10月18日 下午3:01:35
 */
@Mapper
public interface CodeUserMapper {

  @Select("select * from user where id = #{id}")
  User getUserById(Long id);
}
