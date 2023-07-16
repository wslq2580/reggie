package com.cjb.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjb.demo.entity.AddressBook;
import com.cjb.demo.mapper.AddressBookMapper;
import com.cjb.demo.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
