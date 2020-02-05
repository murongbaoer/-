package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

@Service (interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuDao menuDao;

    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 1：初始化分页参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：执行查询
        Page<Menu> page = menuDao.findPage(queryString);
        // 3：封装结果
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }

    @Override
    public void delete(Integer id) {
        //提示“当前检查项和检查组存在关联关系，不能删除”
        // 先查询检查组和检查项的中间表，判断是否可以删除，使用sql：selete count(*)  from t_checkgroup_checkitem where checkitem_id = #{id}
        Integer count = menuDao.findRoleAndMenuCountByMenuId(id);
        if (count>0){
            throw new RuntimeException(MessageConstant.CHECK_ROLE_MENU_FAIL );
        }else {
            menuDao.delete(id);
        }
    }
}
