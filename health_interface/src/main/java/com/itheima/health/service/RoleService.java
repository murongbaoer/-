package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Role;

import java.util.List;

public interface RoleService {

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void add(Role role, Integer[] permissionIds);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    void edit(Role role, Integer[] permissionIds);

    Role findById(Integer id);

    void delete(Integer id);

    List<Role> findAll();

}
