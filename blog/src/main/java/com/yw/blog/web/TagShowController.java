package com.yw.blog.web;

import com.yw.blog.po.Tag;
import com.yw.blog.po.Type;
import com.yw.blog.service.BlogService;
import com.yw.blog.service.TagService;
import com.yw.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PathVariable Long id,
                        @PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        List<Tag> tags = tagService.listTagPublishNumber();
        model.addAttribute("tags", tags);
        if(id == -1 && tags.size() != 0) id = tags.get(0).getId();
        if (tags.size() != 0){
            model.addAttribute("activeTagId", id);
            model.addAttribute("page", blogService.listBlogByTag(id,pageable));
        }
        return "tags";
    }
}
