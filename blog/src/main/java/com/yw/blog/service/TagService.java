package com.yw.blog.service;

import com.yw.blog.po.Tag;
import com.yw.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    List<Tag> listTag(Integer size);

    List<Tag> listTagPublishNumber();

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);
}
