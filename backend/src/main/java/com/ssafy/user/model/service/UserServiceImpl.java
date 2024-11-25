package com.ssafy.user.model.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.user.mapper.UserMapper;
import com.ssafy.user.model.UserDto;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public UserServiceImpl(UserMapper mapper, @Autowired(required = false) JavaMailSender mailSender) {
        this.mapper = mapper;
        this.mailSender = mailSender;
    }

    @Override
    public int registerUser(UserDto userDto) throws Exception {
        return mapper.registerUser(userDto);
    }

    @Override
    public List<UserDto> searchAllUser() throws Exception {
        return mapper.searchAllUser();
    }

    @Override
    public UserDto searchUser(UserDto userDto) throws Exception {
        return mapper.searchUser(userDto);
    }

    @Override
    public void editUser(UserDto userDto) throws Exception {
        mapper.editUser(userDto);
    }

    @Override
    @Transactional
    public int deleteUser(String userId) throws Exception {
        return mapper.deleteUser(userId);
    }

    @Override
    public UserDto searchUserById(String userId) throws Exception {
        return mapper.searchUserById(userId);
    }

    @Override
    public boolean changePassword(String userId, String currentPassword, String newPassword) throws Exception {
        int result = mapper.changePassword(userId, currentPassword, newPassword);
        return result > 0;
    }

    @Override
    public void updateProfileImage(String userId, String profileImage) throws Exception {
        mapper.updateProfileImage(userId, profileImage);
    }

    @Override
    public boolean resetPassword(String email) throws Exception {
        try {
            if (mailSender == null) {
                throw new RuntimeException("메일 서비스가 구성되지 않았습니다.");
            }
            
            UserDto user = mapper.findUserByEmail(email);
            if (user == null) {
                return false;
            }
            
            String tempPassword = generateTempPassword();
            
            mapper.updatePassword(user.getId(), tempPassword);
            
            try {
                sendPasswordResetEmail(email, user.getName(), tempPassword);
            } catch (Exception e) {
                System.out.println("이메일 발송 실패: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("임시 비밀번호가 생성되었으나 이메일 발송에 실패했습니다.");
            }
            
            return true;
        } catch (Exception e) {
            System.out.println("비밀번호 재설정 실패: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    
    private void sendPasswordResetEmail(String email, String name, String tempPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("[SWEET HOME] 임시 비밀번호 발급");
            message.setText(String.format(
                "안녕하세요 %s님,\n\n"
                + "요청하신 임시 비밀번호입니다: %s\n\n"
                + "보안을 위해 로그인 후 반드시 비밀번호를 변경해주세요.\n\n"
                + "감사합니다.",
                name, tempPassword
            ));
            
            System.out.println("Sending email to: " + email);
            System.out.println("From: " + fromEmail);
            mailSender.send(message);
            System.out.println("Email sent successfully");
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("이메일 발송 실패: " + e.getMessage());
        }
    }
}
