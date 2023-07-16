package com.cjb.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjb.demo.entity.Category;
import com.cjb.demo.entity.Employee;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cjb
 * @since 2023-07-10
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
