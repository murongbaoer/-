package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//开启service注解
@Service(interfaceClass = CheckItemService.class)// interfaceClass目的也是为了解决事务问题
//开启事务
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    //调用dao层
    @Autowired
    CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
    // 当前页、当前页最多显示的记录数，查询条件
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 没有学习mybatis的分页插件PageHelper（问题：1：开发繁琐；2：不能使用查询语句通用，如果变更数据，改动较大，因为需要改sql）
        // 1：使用查询条件查询总记录数（sql）：SELECT COUNT(*) FROM t_checkitem WHERE CODE = '传智身高' OR NAME = '传智身高'
        // String total;
        // 2：使用查询条件查询当前页对应的数据集合（sql）：SELECT * FROM t_checkitem WHERE CODE = '传智身高' OR NAME = '传智身高' LIMIT ?,? # 第一个问号，表示当前页从第几条开始检索（currentPage-1）*PageSize；第二个参数，表示当前页最多显示的记录数，pageSize
        // List<CheckItem> rows;
        // 3：封装PageResult
        // PageResult pageResult = new PageResult(total,rows);
        // return pageResult;

        // 使用mybatis的分页插件PageHelper（解决问题），配置：添加mybatis的分页插件
        // 方案一：
//        // 1：完成分页数据的初始化工作
//        PageHelper.startPage(currentPage,pageSize);
//        // 2：完成查询
//        List<CheckItem> list = checkItemDao.findPage(queryString);
//        // 3：对PageResult的封装
//        PageInfo<CheckItem> pageInfo = new PageInfo<>(list);
//        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        // 方案二：
        // 1：完成分页数据的初始化工作
        PageHelper.startPage(currentPage,pageSize);
        //完成查询
        Page<CheckItem> page= checkItemDao.findPage1(queryString);
        // 3：对PageResult的封装
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        //提示“当前检查项和检查组存在关联关系，不能删除”
        // 先查询检查组和检查项的中间表，判断是否可以删除，使用sql：selete count(*)  from t_checkgroup_checkitem where checkitem_id = #{id}
       Integer count = checkItemDao.findCheckGroupAndCheckItemCountByCheckItemId(id);
       if (count>0){
            throw new RuntimeException(MessageConstant.CHECK_CHECKGROUP_CHECKITEM_FAIL);
       }else {
           checkItemDao.delete(id);
       }
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);

    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);

    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
