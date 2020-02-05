package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface RoleDao {

    public Set<Role> findRolesByUserId(Integer userId);

    Page<Role> findPage(String queryString);

    void add(Role role);

    void addRoleAndPermission(@Param(value = "roleId") Integer roleId, @Param(value = "permissionId") Integer permissionId);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    void edit(Role role);

    void deleteRoleAndPermissionByRoleId(Integer id);

    Role findById(Integer id);

    Integer findRoleAndPermissionCountByPermissionId(Integer id);

    void delete(Integer id);

    List<Role> findAll();

}
