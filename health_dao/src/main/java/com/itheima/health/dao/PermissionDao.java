package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
     Set<Permission> findPermissionsByRoleId(Integer id);

    List<Permission> findAll();

    void add(Permission permission);

    Page<Permission> findPage(String queryString);


    Integer findPermissionAndPermissionCountByRoleId(Integer id);

    void delete(Integer id);

    Permission findById(Integer id);

    void edit(Permission permission);
}
