package com.yw.blog.service;


import com.yw.blog.NotFoundException;
import com.yw.blog.dao.VoteRepository;

import com.yw.blog.po.Tag;
import com.yw.blog.po.Vote;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VoteServiceImpl implements VoteService{
    @Autowired
    private VoteRepository voteRepository;

    @Override
    public List<Vote> listVote() {
        return voteRepository.findAll();
    }

    @Transactional
    @Override
    public void updateVote(Long id, Vote vote) {
        Vote v = voteRepository.getOne(id);
        BeanUtils.copyProperties(vote, v);
    }
}
