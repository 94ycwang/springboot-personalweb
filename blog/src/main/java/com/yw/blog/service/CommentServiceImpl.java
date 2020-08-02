package com.yw.blog.service;

import com.yw.blog.NotFoundException;
import com.yw.blog.dao.CommentRepository;
import com.yw.blog.po.Comment;
import com.yw.blog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndAndParentCommentNull(blogId, sort);
        return eachComment(comments);
    }

    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments) {
        for(Comment comment : comments) {
            List<Comment> replys = comment.getReplyComment();
            for(Comment reply : replys){
                recursively(reply);
            }
            comment.setReplyComment(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }

    private List<Comment> tempReplys = new ArrayList<>();
    private void recursively(Comment comment) {
        tempReplys.add(comment);
        List<Comment> replys = comment.getReplyComment();
        for (Comment reply : replys) recursively(reply);
    }

    @Override
    public Page<Comment> listComment(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public Comment getComment(Long id) {
        return commentRepository.getOne(id);
    }


    @Transactional
    @Override
    public Comment updateComment(Long id, Comment comment) {
        Comment c = commentRepository.getOne(id);
        if(c==null){
            throw new NotFoundException("Not found comment!");
        }
        c.setContent(comment.getContent());
        return c;
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        } else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }


    @Transactional
    @Override
    public void deleteComment(Long id) {
        recur(commentRepository.getOne(id));
    }

    private void recur(Comment comment) {
        List<Comment> replys = comment.getReplyComment();
        for (Comment reply : replys) recur(reply);
        commentRepository.delete(comment);
    }

}
