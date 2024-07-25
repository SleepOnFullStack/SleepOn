package bitc.fullstack.sleepon.controller;

import bitc.fullstack.sleepon.model.SleepOnUser;
import bitc.fullstack.sleepon.service.SleepOnUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class SleepOnUserController {

    @Autowired
    private SleepOnUserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody SleepOnUser user, HttpServletRequest request) {
        try {
            if (user.isUnderage()) {
                return "만 18세 미만은 가입할 수 없습니다.";
            }
            userService.registerUser(user);

            // 회원가입 후 자동 로그인 처리
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            return "회원가입이 성공적으로 완료되었습니다.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody SleepOnUser loginUser, HttpServletRequest request) {
        SleepOnUser user = userService.authenticate(loginUser.getId(), loginUser.getPass());
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return "로그인 성공";
        } else {
            return "아이디 또는 비밀번호가 잘못되었습니다.";
        }
    }

    @GetMapping("/logout")
    public String logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "로그아웃 성공";
    }
}