package com.yw.blog.service;

import com.yw.blog.NotFoundException;
import com.yw.blog.dao.TypeRepository;
import com.yw.blog.po.Blog;
import com.yw.blog.po.Type;
import org.hibernate.type.ListType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.*;
import java.util.*;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService{
    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        typeRepository.save(type);
        return type;
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.getOne(id);
    }

    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }


    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Transactional
    @Override
    public List<Type> listTypePublishNumber() {
       List<Type> types = listType();
       for(Type type : types){
           int i = 0;
           List<Blog> blogs = type.getBlogs();
           for(Blog blog: blogs){
               if(blog.isPublished()) i++;
           }
           type.setPublishedBlogsN(i);
       }
        return types;
    }

    @Transactional
    @Override
    public List<Type> listType(Integer size) {
        Sort sort =  Sort.by(Sort.Order.desc("blogs.size"));
        Pageable pageable = PageRequest.of(0, size, sort);
        return typeRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.getOne(id);
        if(t==null){
            throw new NotFoundException("Not found type!");
        }
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);

    }
}
