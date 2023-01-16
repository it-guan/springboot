package com.itguan.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itguan.reggie.common.BaseContext;
import com.itguan.reggie.common.R;
import com.itguan.reggie.pojo.AddressBook;
import com.itguan.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){

        Long userLogger = BaseContext.getLoggerId();
        QueryWrapper<AddressBook> addressBookQueryWrapper = new QueryWrapper<>();
        addressBookQueryWrapper.eq("user_id",userLogger);
        long count = addressBookService.count(addressBookQueryWrapper);
        if (count == 0) addressBook.setIsDefault(1);

//        System.out.println(addressBook);
        addressBook.setUserId(BaseContext.getLoggerId());
        addressBookService.save(addressBook);



        return R.success("添加成功！");
    }

    @GetMapping("/list")
    public R<List> list(){

        Long userLogger = BaseContext.getLoggerId();
        QueryWrapper<AddressBook> addressBookQueryWrapper = new QueryWrapper<>();
        addressBookQueryWrapper.eq("user_id",userLogger);
        List<AddressBook> addressBookList = addressBookService.list(addressBookQueryWrapper);

        return R.success(addressBookList);
    }

    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){

//        先把该账号对应的地址全部重置为非默认
        Long userLogger = BaseContext.getLoggerId();
        UpdateWrapper<AddressBook> addressBookUpdateWrapper = new UpdateWrapper<>();
        addressBookUpdateWrapper.eq("user_id",userLogger);
        addressBookUpdateWrapper.set("is_default",0);
        addressBookService.update(addressBookUpdateWrapper);
//        再把传进来的地址id所对应的地址设置为默认
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(){

        Long userLogger = BaseContext.getLoggerId();
        QueryWrapper<AddressBook> addressBookQueryWrapper = new QueryWrapper<>();
        addressBookQueryWrapper.eq("user_id",userLogger);
        addressBookQueryWrapper.eq("is_default",1);
        AddressBook defaultAddressBook = addressBookService.getOne(addressBookQueryWrapper);



        return R.success(defaultAddressBook);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getInfo(@PathVariable("id") Long id){

        AddressBook addressBook = addressBookService.getById(id);

        return R.success(addressBook);
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){

        addressBookService.updateById(addressBook);

        return R.success("修改成功！");
    }
}
