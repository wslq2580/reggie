package com.cjb.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjb.demo.common.R;
import com.cjb.demo.entity.Employee;
import com.cjb.demo.service.EmployeeService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cjb
 * @since 2023-07-10
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login") //员工登录
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        String password = employee.getPassword();//将页面提交的密码MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();//根据username查数据库
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //如果没有查询到则返回登陆失败结果
        if(emp == null){
            return R.error("用户名不存在");
        }

        //如果查询到,但密码不一致则登陆失败
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //如果用户是禁用状态，则拒绝登录
       if(emp.getStatus() == 0){
            return R.error("该账号已禁用");
        }

        //登陆成功,则将员工id存入session并返回登陆成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout") //员工退出登录
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping //新增员工
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工信息：{}",employee.toString());

        //设置初始密码123456，并进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long empId = (Long)request.getSession().getAttribute("employee"); //谁注册的
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee); //mp新增
        return R.success("新增员工成功");
    }

    @GetMapping("/page") //分页显示列表
    public R<Page> list(int page,int pageSize,String name){
        Page p = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(p,lambdaQueryWrapper);
        return R.success(p);
    }

    /*
    * 根据id修改员工信息*/
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        Long empId = (Long)request.getSession().getAttribute("employee");
        if (employee.getId() != 1){
//            employee.setUpdateTime(LocalDateTime.now());
//            employee.setUpdateUser(empId);
            employeeService.updateById(employee);

            return R.success("员工信息修改成功");
        }
        else{
            return R.error("管理员不能被禁用");
        }
    }

    @GetMapping("/{id}")
    public R<Employee> update(@PathVariable String id){
        log.info("查询员工信息");
        Employee emp = employeeService.getById(id);
        if(emp !=null){
            return R.success(emp);
        }else
        return R.error("未查询到员工信息");
    }
}

