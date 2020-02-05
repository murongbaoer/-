package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MainDao;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.MainServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service(interfaceClass = MainServcie.class)
@Transactional
public class MainServiceImpl implements MainServcie {
    @Autowired
    MainDao mainDao;


    @Override
    public List<Menu> findMenu(String username) {
        User user = mainDao.findRoleAndMenu(username);
        Role role = mainDao.findRoleByUser(user.getId());
        List<Menu> list  = mainDao.findMenuByRole(role.getId());
        List<Menu> menuList = new ArrayList<>();
        for (Menu menu : list) {
            if (menu.getParentMenuId()==null){
                menuList.add(menu);
            }
        }
        for (Menu menu : menuList) {
            menu.setChildren(getChild(menu.getId(),list));
        }
        return menuList ;
    }

    private List<Menu> getChild(Integer id, List<Menu> root) {
        List<Menu> childList =new ArrayList<>();
        for (Menu menu : root) {
            if (menu.getParentMenuId()!=null && menu.getParentMenuId()==id){
                childList.add(menu);
            }
        }
        if (childList.size()==0){
            return null;
        }
        return childList;
    }
}
