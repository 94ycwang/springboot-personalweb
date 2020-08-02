package com.yw.blog.web.admin;

import com.yw.blog.po.Comment;
import com.yw.blog.po.Tag;
import com.yw.blog.service.CommentService;
import com.yw.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/comments")
    public String tags(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                               Pageable pageable, Model model) {
        model.addAttribute("page", commentService.listComment(pageable));
        return "admin/comments";
    }

    @GetMapping("comments/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("comment", commentService.getComment(id));
        return "admin/comments-input";
    }

    @PostMapping("/comments/{id}")
    public String editPost(Comment comment, @PathVariable Long id,
                           RedirectAttributes attributes){

        Comment c = commentService.updateComment(id, comment);
        if (c == null){
            attributes.addFlashAttribute("message","Failed to update.");
        }else{
            attributes.addFlashAttribute("message","Updated successfully!");
        }
        return "redirect:/admin/comments";
    }

    @GetMapping("/comments/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        commentService.deleteComment(id);
        attributes.addFlashAttribute("message","Deleted successfully!");
        return  "redirect:/admin/comments";
    }
}

