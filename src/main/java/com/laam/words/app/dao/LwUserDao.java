package com.laam.words.app.dao;

import org.apache.ibatis.annotations.Mapper;

import com.laam.words.cmn.model.LwUser;

import java.util.List;

@Mapper
public interface LwUserDao {
    List<LwUser> findAll();
    LwUser findById(Long id);
    LwUser findByEmail(String email);
    void insertUser(LwUser user);
}
