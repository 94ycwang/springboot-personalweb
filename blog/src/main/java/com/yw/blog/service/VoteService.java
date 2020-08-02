package com.yw.blog.service;

import com.yw.blog.po.Tag;
import com.yw.blog.po.Vote;

import java.util.List;

public interface VoteService {
    List<Vote> listVote();
    void updateVote(Long id, Vote vote);
}
