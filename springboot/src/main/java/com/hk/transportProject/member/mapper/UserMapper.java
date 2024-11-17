package com.hk.transportProject.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.hk.transportProject.member.model.User;

@Mapper
public interface UserMapper {
    User findByUserId(String userId);
    void save(User user);
    User findByUserIdAndEmail(String userId, String userEmail);
    void updatePassword(User user);
    void updateUserInfo(User user);
    void updateEmail(User user);
    void deleteUser(User user);
}
