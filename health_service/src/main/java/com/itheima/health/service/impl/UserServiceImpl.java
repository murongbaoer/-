package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = UserServcie.class)
@Transactional
public class UserServiceImpl implements UserServcie {
    @Autowired
    UserDao userDao;


    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public void add(User user, Integer[] checkroleIds) {
        // 1：新增用户，向t_user表中添加1条数据，同时返回检查组id
        userDao.add(user);
        // 2：向用户和角色的中间表添加数据t_user_role，多条数据，与角色的数组有关
        this.setUserAndRole(user.getId(),checkroleIds);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化分页参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：执行查询
        Page<User> page = userDao.findPage(queryString);
        // 3：封装结果
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<Integer> findCheckroleIdsByUserId(Integer id) {
        return userDao.findCheckroleIdsByUserId(id);
    }

    @Override
    public void edit(User user, Integer[] checkroleIds) {
        // 1：使用User，更新用户（动态sql更新）
        userDao.edit(user);
        // 2：删除用户和角色的中间表，使用用户的id作为条件删除
        userDao.deleteUserAndRoleByUserId(user.getId());
        // 3：向用户和角色的中间表添加数据t_user_role，多条数据，与角色的数组有关
        this.setUserAndRole(user.getId(),checkroleIds);
    }

    @Override
    public void delete(Integer id) {
        //提示“当前用户和角色存在关联关系，不能删除”
        // 先查询用户和角色的中间表，判断是否可以删除，使用sql：selete count(*)  from t_user_role where role_id = #{id}
        Integer count = userDao.findUserAndRoleCountByRoleId(id);
        if (count>0){
            throw new RuntimeException(MessageConstant.CHECK_CHECKGROUP_CHECKITEM_FAIL);
        }else {
            userDao.delete(id);
        }

    }

    private void setUserAndRole(Integer userid, Integer[] checkroleIds) {
        if (checkroleIds!=null && checkroleIds.length>0){
            for (Integer checkroleId : checkroleIds) {
                userDao.addUserAndRole(userid,checkroleId);
            }
        }
    }

}
