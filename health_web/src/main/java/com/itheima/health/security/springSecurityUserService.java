package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserServcie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class springSecurityUserService implements UserDetailsService {

    @Reference
    UserServcie userServcie; //注意：此处要通过dubbo远程调用用户服务
    // 认证：执行/login.do的表单认证的时候，就会执行该代码，传递username（用户名），返回认证封装的的信息UserDetails对象
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查询
        // 1：使用登录名查询用户信息（包含角色集合，权限集合）
        User user = userServcie.findUserByUsername(username);
        // user==null表示用户名输入有误，不能登录，跳转到登录页面
        if (user==null){
            //异常信息：org.springframework.security.authentication.InternalAuthenticationServiceException
            // 用户名输入有误，抛出异常：
            return null;
        }
        // 2：使用登录名查询角色、权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 获取用户的角色
        Set<Role> roles = user.getRoles();
        if (roles!=null && roles.size()>0) {
            for (Role role : roles) {
                // authorities.add(new SimpleGrantedAuthority(role.getKeyword())); // 角色
                // 获取角色中的权限
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null && permissions.size() > 0) {
                    for (Permission permission : permissions) {
                        authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));// 权限
                    }
                }
            }
        }
        /** 如果认证成功，返回User对象，User对象封装：
         *  1：存放当前登录名
         *  2：存放当前数据库中的密码（123）
         *      SpringSecurity会使用数据库的密码{noop}123和页面输入的密码123：会自动进行比对，如果2个密码一致，登录成功；如果不一致，抛出异常，会回退到登录页面
         *      异常信息：
         *      org.springframework.security.authentication.BadCredentialsException: Bad credentials
         *  3：授予的角色、权限的集合
         */
        //String password = "{noop}"+user.getPassword(); // 明文
        String password = user.getPassword(); // 使用BCryptPasswordEncoder
        return new org.springframework.security.core.userdetails.User(user.getUsername(),password,authorities);
    }
}
