package com.yw.blog.web.admin;

import com.yw.blog.po.Tag;
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
public class TagsController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    @GetMapping("tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(Tag tag,
                       RedirectAttributes attributes, Model model){
        if(tagService.getTagByName(tag.getName())!=null){
            model.addAttribute("message", "The tag name has already existed!");
            return "admin/tags-input";
        }

        Tag t = tagService.saveTag(tag);
        if (t == null){
            attributes.addFlashAttribute("message","Failed to add.");
        }else{
            attributes.addFlashAttribute("message","Added successfully!");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editpost(Tag tag, @PathVariable Long id,
                       RedirectAttributes attributes, Model model){
        if(tagService.getTagByName(tag.getName())!=null){
            model.addAttribute("message", "The tag name has already existed!");
            return "admin/tags-input";
        }

        Tag t = tagService.updateTag(id, tag);
        if (t == null){
            attributes.addFlashAttribute("message","Failed to update.");
        }else{
            attributes.addFlashAttribute("message","Updated successfully!");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","Deleted successfully!");
        return  "redirect:/admin/tags";
    }
}

