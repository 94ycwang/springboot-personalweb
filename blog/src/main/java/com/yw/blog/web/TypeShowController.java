package com.yw.blog.web;

import com.yw.blog.po.Type;
import com.yw.blog.service.BlogService;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;
    @GetMapping("/types/{id}")
    public String types(@PathVariable Long id,
                        @PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        List<Type> types = typeService.listTypePublishNumber();
        model.addAttribute("types", types);
        if(id == -1 && types.size() != 0) id = types.get(0).getId();
        if(types.size() != 0){
            model.addAttribute("activeTypeId", id);
            model.addAttribute("page", blogService.listBlogByType(typeService.getType(id),pageable));
        }
        return "types";
    }
}
