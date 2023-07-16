package com.cjb.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjb.demo.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
