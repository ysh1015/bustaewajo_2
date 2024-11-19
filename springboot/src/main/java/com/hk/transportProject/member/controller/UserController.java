package com.hk.transportProject.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import com.hk.transportProject.member.service.UserService;
import com.hk.transportProject.member.model.User;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm() {
        return "user/register";
    }

    @PostMapping("/register")
    public String register(User user, RedirectAttributes redirectAttributes) {
        try {
            if (userService.isUserIdDuplicate(user.getUserId())) {
                redirectAttributes.addFlashAttribute("error", "이미 사용중인 아이디입니다.");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/user/register";
            }

            userService.registerUser(user);
            return "redirect:/login?register=success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원가입 중 오류가 발생했습니다.");
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/user/register";
        }
    }

    @PostMapping("/check-userid")
    @ResponseBody
    public Map<String, Boolean> checkUserId(@RequestParam String userId) {
        boolean isDuplicate = userService.isUserIdDuplicate(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("duplicate", isDuplicate);
        return response;
    }

    @GetMapping("/login")
    public String loginForm() {
        System.out.println();
        return "login";
    }

    @GetMapping("/change-userInfo")
    public String changeUserInfoForm(Model model, Principal principal) {
        User user = userService.findByUserId(principal.getName());
        model.addAttribute("user", user);
        return "user/change-userInfo";
    }

    @PostMapping("/change-userInfo")
    public String changeUserInfo(@RequestParam String userEmail,
                           @RequestParam String currentPassword,
                           @RequestParam(required = false) String newPassword,
                           @RequestParam(required = false) String confirmPassword,
                           Principal principal,
                           Model model) {
        try {
            User existingUser = userService.findByUserId(principal.getName());
            if (existingUser == null) {
                model.addAttribute("error", true);
                model.addAttribute("message", "사용자 정보를 찾을 수 없습니다.");
                return "user/change-userInfo";
            }

            if (!userService.checkPassword(principal.getName(), currentPassword)) {
                model.addAttribute("error", true);
                model.addAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
                model.addAttribute("user", existingUser);
                return "user/change-userInfo";
            }

            existingUser.setUserEmail(userEmail);

            if (newPassword != null && !newPassword.isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    model.addAttribute("error", true);
                    model.addAttribute("message", "새 비밀번호가 일치하지 않습니다.");
                    model.addAttribute("user", existingUser);
                    return "user/change-userInfo";
                }
                existingUser.setUserPwd(newPassword);
                boolean isUpdated = userService.updateUserInfo(existingUser, true);
                if (isUpdated) {
                    model.addAttribute("message", "회원정보가 성공적으로 수정되었습니다.");
                }
            } else {
                boolean isUpdated = userService.updateEmail(existingUser.getUserId(), userEmail);
                if (isUpdated) {
                    model.addAttribute("message", "이메일이 성공적으로 수정되었습니다.");
                }
            }

            User updatedUser = userService.findByUserId(principal.getName());
            model.addAttribute("user", updatedUser);
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "회원정보 수정 중 오류가 발생했습니다.");
            User currentUser = userService.findByUserId(principal.getName());
            model.addAttribute("user", currentUser);
        }
        
        return "user/change-userInfo";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "user/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("userId") String userId,
                                      @RequestParam(value = "newPassword", required = false) String newPassword,
                                      @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
                                      Model model) {
        // 첫 단계: 아이디 확인
        if (newPassword == null || confirmPassword == null) {
            User user = userService.findByUserId(userId);
            if (user != null) {
                model.addAttribute("userFound", true);
                model.addAttribute("userId", userId);
                return "user/forgot-password";
            } else {
                model.addAttribute("error", true);
                model.addAttribute("message", "일치하는 사용자 정보를 찾을 수 없습니다.");
                return "user/forgot-password";
            }
        }
        
        // 두 번째 단계: 새 비밀번호 설정
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("userFound", true);
            model.addAttribute("userId", userId);
            model.addAttribute("error", true);
            model.addAttribute("message", "새 비밀번호가 일치하지 않습니다.");
            return "user/forgot-password";
        }

        try {
            boolean isChanged = userService.updatePassword(userId, newPassword);
            if (isChanged) {
                return "redirect:/main?passwordChanged=true";
            } else {
                model.addAttribute("error", true);
                model.addAttribute("message", "비밀번호 변경에 실패했습니다.");
                return "user/forgot-password";
            }
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "비밀번호 변경 중 오류가 발생했습니다.");
            return "user/forgot-password";
        }
    }

    @GetMapping("/change-password")
    public String changePasswordForm() {
        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                           @RequestParam String newPassword,
                           @RequestParam String confirmPassword,
                           Model model,
                           Principal principal) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", true);
            model.addAttribute("message", "새 비밀번호가 일치하지 않습니다.");
            return "user/change-password";
        }

        try {
            boolean isChanged = userService.changePassword(principal.getName(), currentPassword, newPassword);
            if (isChanged) {
                return "redirect:/main?passwordChanged=true";
            } else {
                model.addAttribute("error", true);
                model.addAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
                return "user/change-password";
            }
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "비밀번호 변경 중 오류가 발생했습니다.");
            return "user/change-password";
        }
    }

    @GetMapping("/delete")
    public String deleteAccountForm(Model model, Principal principal) {
        User user = userService.findByUserId(principal.getName());
        model.addAttribute("user", user);
        return "user/delete-account";
    }

    @PostMapping("/delete")
    public String deleteAccount(@RequestParam String password, Principal principal, Model model) {
        try {
            if (userService.checkPassword(principal.getName(), password)) {
                boolean isDeleted = userService.deleteUser(principal.getName());
                if (isDeleted) {
                    return "redirect:/login?deleted=true";
                }
            } else {
                model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
                User user = userService.findByUserId(principal.getName());
                model.addAttribute("user", user);
                return "user/delete-account";
            }
        } catch (Exception e) {
            model.addAttribute("message", "회원 탈퇴 처리 중 오류가 발생했습니다.");
            User user = userService.findByUserId(principal.getName());
            model.addAttribute("user", user);
            return "user/delete-account";
        }
        return "redirect:/main";
    }
}

