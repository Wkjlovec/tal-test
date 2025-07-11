package cn.diinj.userservice.mapper;

import cn.diinj.userservice.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * User mapper interface
 */
@Mapper
public interface UserMapper {

/**
 * Find user by username
 *
 * @param username username
 * @return User
 */
@Select("SELECT * FROM user WHERE username = #{username}")
User findByUsername(@Param("username") String username);

/**
 * Find user by username and password
 *
 * @param username username
 * @param password password
 * @return User
 */
@Select("SELECT * FROM user WHERE username = #{username} AND password = #{password}")
User findByUsernameAndPassword(@Param("username") String username,
        @Param("password") String password);

/**
 * Update last login time
 *
 * @param id            user id
 * @param lastLoginTime last login time
 * @return affected rows
 */
@Update("UPDATE user SET last_login_time = #{lastLoginTime} WHERE id = #{id}")
int updateLastLoginTime(@Param("id") Long id, @Param("lastLoginTime") java.util.Date lastLoginTime);

/**
 * Find user by ID
 *
 * @param id user id
 * @return User
 */
@Select("SELECT * FROM user WHERE id = #{id}")
User findById(@Param("id") Long id);
}