package com.yw.blog.web;

import com.yw.blog.po.Blog;
import com.yw.blog.po.Vote;
import com.yw.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VoteService voteService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {

        typeService.listTypePublishNumber();
        tagService.listTagPublishNumber();
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listType(6));
        model.addAttribute("tags", tagService.listTag(15));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(4));

        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size=5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {

        model.addAttribute("page", blogService.listBlog(query,pageable));
        model.addAttribute("query", query);
        return "search";
    }

    private boolean likedState(HttpServletRequest request, Long id) {
        boolean liked = false;
        Cookie[] cookies =  request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("session"+id) && cookie.getValue()!= null) {
                    liked = true;
                    break;
                }
            }
        }
        return liked;
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model, HttpServletRequest request) {

        boolean liked = this.likedState(request,id);
        model.addAttribute("liked", liked);
        model.addAttribute("blog", blogService.getAndConvert(id));
        model.addAttribute("comments",commentService.listCommentByBlogId(id));
        return "blog";
    }

    @PostMapping(value = "/blog/{id}/like")
    public  String likeRefresh(@PathVariable Long id, Model model,
                              HttpServletRequest request, HttpServletResponse response) {
        Blog blog = blogService.getBlog(id);
        boolean liked = this.likedState(request,id);
        int likes;

        if(!liked) {
            Cookie c = new Cookie("session"+id,"CookieInfo");
            c.setMaxAge(100 * 24 * 60 * 60);
            response.addCookie(c);

            blogService.updateBlogLikes(id, blog.getLikes() + 1);
            model.addAttribute("liked", true);

        } else {
            Cookie c = new Cookie("session"+id,null);
            c.setMaxAge(0);
            response.addCookie(c);

            blogService.updateBlogLikes(id, blog.getLikes() - 1);
            model.addAttribute("liked", false);
        }

        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog::likeRefresh";
    }

    private boolean voteState(HttpServletRequest request) {
        boolean voted = false;
        Cookie[] cookies =  request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("votefornp") && cookie.getValue()!= null){
                    voted = true;
                    break;
                }
            }
        }
        return voted;
    }
    @PostMapping("/vote")
    public  String voteRefresh(@RequestParam String vote, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<Vote> v = voteService.listVote();
        Vote v0 =  v.get(0);
        Integer e =v0.getE();
        Integer ne = v0.getNe();

        if (!voteState(request)){
            Cookie c;
            if(vote.equals("0")){
                e++;
                c = new Cookie("votefornp","e");
                model.addAttribute("voteE", true);
            }
            else if(vote.equals("1")){
                ne++;
                c = new Cookie("votefornp","ne");
                model.addAttribute("voteNe", true);
            }
            else c = new Cookie("votefornp","none");
            v0.setE(e);
            v0.setNe(ne);
            voteService.updateVote(v0.getId(),v0);
            c.setMaxAge(100*24*60*60);
            response.addCookie(c);
            model.addAttribute("voted", true);
        } else{
            Cookie[] cookies =  request.getCookies();
            for(Cookie cookie : cookies) {
                if (cookie.getName().equals("votefornp")) {
                    if(cookie.getValue().equals("e")){
                        e--;
                    } else if(cookie.getValue().equals("ne")){
                        ne--;
                    }
                }
            }
            v0.setE(e);
            v0.setNe(ne);
            voteService.updateVote(v0.getId(),v0);
            Cookie c = new Cookie("votefornp",null);
            c.setMaxAge(0);
            response.addCookie(c);
            model.addAttribute("voted", false);
        }
        model.addAttribute("e",e);
        model.addAttribute("ne",ne);
        return "_fragment::voteRefresh";
    }

    @PostMapping("/voteload")
    public  String voteLoad(Model model, HttpServletRequest request){
        model.addAttribute("voted", voteState(request));
        List<Vote> v = voteService.listVote();
        Vote v0 =  v.get(0);
        Integer e =v0.getE();
        Integer ne = v0.getNe();
        model.addAttribute("e",e);
        model.addAttribute("ne",ne);
        if (voteState(request)){
            Cookie[] cookies =  request.getCookies();
            for(Cookie cookie : cookies) {
                if (cookie.getName().equals("votefornp")) {
                    if(cookie.getValue().equals("e")){
                        model.addAttribute("voteE", true);
                    } else if(cookie.getValue().equals("ne")){
                        model.addAttribute("voteNe", true);
                    }
                }
            }
        }
        return "_fragment::voteRefresh";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}
