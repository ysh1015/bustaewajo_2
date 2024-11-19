package com.hk.transportProject.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.hk.transportProject.member.mapper.UserMapper;
import com.hk.transportProject.member.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        System.out.println("로그인 성공시에 자기 아이디 찍어보기 : " + userId);

        return new org.springframework.security.core.userdetails.User(
            user.getUserId(),
            user.getUserPwd(),
            authorities
        );
    }

    public void registerUser(User user) {
        User existingUser = userMapper.findByUserId(user.getUserId());
        if (existingUser != null) {
            throw new IllegalStateException("이미 사용중인 아이디입니다.");
        }

        System.out.println(user.getUserId());
        System.out.println(user.getUserPwd());
        System.out.println(user.getUserEmail());

        String userNo = generateUserNo();
        user.setUserNo(userNo);
        user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
        userMapper.save(user);
    }

    private String generateUserNo() {
        return "U" + System.currentTimeMillis();
    }

    public User findByUserId(String userId) {
        return userMapper.findByUserId(userId);
    }

    public boolean updatePassword(String userId, String newPassword) {
        User user = userMapper.findByUserId(userId);
        if (user != null) {
            user.setUserPwd(passwordEncoder.encode(newPassword));
            userMapper.updatePassword(user);
            return true;
        }
        return false;
    }

    public boolean changePassword(String userId, String currentPassword, String newPassword) {
        User user = userMapper.findByUserId(userId);
        System.out.println(user.getUserNo());
        System.out.println(user.getUserId());
        System.out.println(user.getUserPwd());
        System.out.println(user.getUserEmail());
        System.out.println(userId);
        System.out.println(newPassword);
        if (user != null && passwordEncoder.matches(currentPassword, user.getUserPwd())) {
            user.setUserPwd(passwordEncoder.encode(newPassword));
            userMapper.updatePassword(user);
            return true;
        }
        return false;
    }

    public boolean updateUserInfo(User user, boolean updatePassword) {
        User existingUser = userMapper.findByUserId(user.getUserId());
        if (existingUser != null) {
            existingUser.setUserEmail(user.getUserEmail());
            if (updatePassword) {
                existingUser.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
            }
            userMapper.updateUserInfo(existingUser);
            return true;
        }
        return false;
    }

    public boolean checkPassword(String userId, String password) {
        User user = userMapper.findByUserId(userId);
        return user != null && passwordEncoder.matches(password, user.getUserPwd());
    }

    public boolean updateEmail(String userId, String userEmail) {
        User user = userMapper.findByUserId(userId);
        if (user != null) {
            user.setUserEmail(userEmail);
            userMapper.updateEmail(user);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String userId) {
        User user = userMapper.findByUserId(userId);
        if (user != null) {
            userMapper.deleteUser(user);
            return true;
        }
        return false;
    }

    public boolean isUserIdDuplicate(String userId) {
        User user = userMapper.findByUserId(userId);
        return user != null;
    }
}
