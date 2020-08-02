package com.yw.blog.web.admin;

import com.yw.blog.dao.UserRepository;
import com.yw.blog.po.User;
import com.yw.blog.service.UserService;
import com.yw.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username, password);
        if(user != null){
            if(user.getType() != 1) return "redirect:/admin";
            user.setPassword(null);
            session.setAttribute("user", user);
            session.setAttribute("type", user.getType());
            return "admin/adminindex";
        }
        else{
            attributes.addFlashAttribute("message","Incorrect email/password – please check and retry");
            return "redirect:/";
        }
    }

}
