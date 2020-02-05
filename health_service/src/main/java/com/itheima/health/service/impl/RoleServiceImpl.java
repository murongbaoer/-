package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//开启service注解
@Service(interfaceClass = RoleService.class)// interfaceClass目的也是为了解决事务问题
//开启事务
@Transactional
public class RoleServiceImpl implements RoleService {
    //调用dao层
    @Autowired
    RoleDao roleDao;


    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：完成分页数据的初始化工作
        PageHelper.startPage(currentPage,pageSize);
        //完成查询
        Page<Role> page= roleDao.findPage(queryString);
        // 3：对PageResult的封装
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Role role, Integer[] permissionIds) {
        // 1：新增检查组，向t_role表中添加1条数据，同时返回检查组id
        roleDao.add(role);
        // 2：向检查组和检查项的中间表添加数据t_role_permission，多条数据，与检查项的数组有关
            this.setRoleAndPermission(role.getId(),permissionIds);
    }

    @Override
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        return roleDao.findPermissionIdsByRoleId(id);
    }

    @Override
    public void edit(Role role, Integer[] permissionIds) {
        // 1：使用CheckGroup，更新检查组（动态sql更新）
        roleDao.edit(role);
        // 2：删除检查项和检查组的中间表，使用检查组的id作为条件删除
        roleDao.deleteRoleAndPermissionByRoleId(role.getId());
        // 3：向检查组和检查项的中间表添加数据t_checkgroup_checkitem，多条数据，与检查项的数组有关
        this.setRoleAndPermission(role.getId(),permissionIds);
    }

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public void delete(Integer id) {
        Integer count = roleDao.findRoleAndPermissionCountByPermissionId(id);
        if (count>0){
            throw new RuntimeException(MessageConstant.CHECK_ROLE_PERMISSION_FAIL);
        }else {
            roleDao.delete(id);
        }
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    private void setRoleAndPermission(Integer roleId, Integer[] permissionIds) {
        if (permissionIds!=null && permissionIds.length>0){
            for (Integer permissionId : permissionIds) {
                //使用@Param
                roleDao.addRoleAndPermission(roleId,permissionId);
            }
        }
    }
}
