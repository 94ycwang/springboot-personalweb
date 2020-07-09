package com.yw.blog.web;

import com.yw.blog.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {
    @GetMapping("/{id}/{name}")
    public String index(@PathVariable Integer id, String name) throws RuntimeException {
//        int i =9/0;
//        String blog = null;
//        if(blog == null){
//            throw new NotFoundException("Not Found!");
//        }
        System.out.println("------------index--------------");
        return "index";
    }
}
