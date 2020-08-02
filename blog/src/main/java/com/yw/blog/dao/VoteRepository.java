package com.yw.blog.dao;

import com.yw.blog.po.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
