package com.yw.blog.dao;

import com.yw.blog.po.Blog;
import com.yw.blog.po.Tag;
import com.yw.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    @Query("select b from Blog b where b.recommend = True and b.published = True")
    List<Blog> findTopByPublished(Pageable pageable);

    @Query("select b from Blog b where (b.published = True) and (b.title like ?1 or b.content like ?1)")
    Page<Blog> findByQueryAndPublished(String query, Pageable pageable);

    @Query("select b from Blog b where b.published = True")
    Page<Blog> findAllByPublished(Pageable pageable);

    @Query("select b from Blog b where b.published = True and b.type = ?1")
    Page<Blog> findByTypeAndPublished(Type queryType, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupYear();
;

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1 and b.published = True")
    List<Blog> findByYear(String year);
}
