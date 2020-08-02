package com.yw.blog.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="t_vote")
public class Vote {

    @Id
    @GeneratedValue
    private Long id;
    private Integer ne;
    private Integer e;

    public Vote() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNe() {
        return ne;
    }

    public void setNe(Integer ne) {
        this.ne = ne;
    }

    public Integer getE() {
        return e;
    }

    public void setE(Integer e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", ne=" + ne +
                ", e=" + e +
                '}';
    }
}
