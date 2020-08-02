package com.yw.blog.web;

import com.yw.blog.po.Comment;
import com.yw.blog.po.User;
import com.yw.blog.service.BlogService;
import com.yw.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;


@Controller
public class CommentShowController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @PostMapping("/comments")
    public String post(Comment comment, Model model, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        if(user!=null && user.getType() == 1){
            comment.setAvatar(user.getAvatar());
            comment.setNickname(user.getNickname());
            comment.setEmail(user.getEmail());
            comment.setAdminComment(true);
        } else if(user!=null && user.getType() == 0){
            comment.setAvatar(user.getAvatar());
            comment.setNickname(user.getNickname());
            comment.setEmail(user.getEmail());
        } else{
            comment.setAvatar("/images/avatar_visitor.png");
        }
        commentService.saveComment(comment);
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog::commentList";
    }

}
