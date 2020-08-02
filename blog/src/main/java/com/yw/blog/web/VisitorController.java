package com.yw.blog.web;

import com.yw.blog.po.User;
import com.yw.blog.service.UserService;
import com.yw.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class VisitorController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String vLoginPage() throws RuntimeException {
        return "users";
    }

    @PostMapping("/users")
    public String vLogin(@RequestParam String username,
                         @RequestParam String password,
                         HttpSession session,
                         RedirectAttributes attributes){
        User user = userService.checkUser(username, password);
        if(user != null){
            user.setPassword(null);
            session.setAttribute("user", user);
            session.setAttribute("type", user.getType());
            return "redirect:/";
        }
        else{
            attributes.addFlashAttribute("message","Incorrect email/password – please check and retry");
            return "redirect:/users";
        }
    }

    @GetMapping("/signup")
    public String signUpPage() throws RuntimeException {
        return "signup";
    }

    @PostMapping("/signup")
    public String addUser(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String nickname,
                          @RequestParam String email,
                          RedirectAttributes attributes){

        if(userService.checkUsername(username)!=null){
            attributes.addFlashAttribute("message","This username is already taken!");
            return "redirect:/signup";
        }

        if(userService.checkEmail(email)!=null){
            attributes.addFlashAttribute("message","This email address has been registered already!");
            return "redirect:/signup";
        }

        if(userService.checkNickname(nickname)!=null){
            attributes.addFlashAttribute("message","This nickname is already taken!");
            return "redirect:/signup";
        }

        User user = new User();
        user.setType(0);
        user.setUsername(username);
        user.setPassword(MD5Utils.code(password));
        user.setNickname(nickname);
        user.setEmail(email);
        Date time = new Date();
        user.setCreateTime(time);
        user.setUpdateTime(time);
        user.setAvatar("/images/avatar_user.jpg");
        userService.saveUser(user);
        attributes.addFlashAttribute("sucessmessage","Registered successfully!");
        return "redirect:/users";
    }

}
