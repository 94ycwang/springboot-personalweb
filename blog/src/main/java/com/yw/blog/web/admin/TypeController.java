package com.yw.blog.web.admin;

import com.yw.blog.po.Type;
import com.yw.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    @GetMapping("types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    @PostMapping("/types")
    public String post(Type type,
                       RedirectAttributes attributes, Model model){
        if(typeService.getTypeByName(type.getName())!=null){
            model.addAttribute("message", "The category name has already existed!");
            return "admin/types-input";
        }

        Type t = typeService.saveType(type);
        if (t == null){
            attributes.addFlashAttribute("message","Failed to add.");
        }else{
            attributes.addFlashAttribute("message","Added successfully!");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editpost(Type type, @PathVariable Long id,
                       RedirectAttributes attributes, Model model){
        if(typeService.getTypeByName(type.getName())!=null){
            model.addAttribute("message", "The category name has already existed!");
            return "admin/types-input";
        }

        Type t = typeService.updateType(id, type);
        if (t == null){
            attributes.addFlashAttribute("message","Failed to update.");
        }else{
            attributes.addFlashAttribute("message","Updated successfully!");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        if (typeService.getType(id).getBlogs().size()!= 0) {
            attributes.addFlashAttribute("message", "Can't delete the type with existing blogs!");
        } else {
            typeService.deleteType(id);
            attributes.addFlashAttribute("message", "Deleted successfully!");
        }
        return  "redirect:/admin/types";
    }
}

