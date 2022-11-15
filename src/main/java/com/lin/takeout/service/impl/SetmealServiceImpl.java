package com.lin.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.takeout.common.Result;
import com.lin.takeout.dto.SetmealDto;
import com.lin.takeout.entity.Category;
import com.lin.takeout.entity.Setmeal;
import com.lin.takeout.entity.SetmealDish;
import com.lin.takeout.mapper.CategoryMapper;
import com.lin.takeout.mapper.DishMapper;
import com.lin.takeout.mapper.SetmealDishMapper;
import com.lin.takeout.mapper.SetmealMapper;
import com.lin.takeout.service.CategoryService;
import com.lin.takeout.service.CommonService;
import com.lin.takeout.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CommonService commonService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishMapper dishMapper;

    @Override
    public Result<SetmealDto> getSetmealById(long id) {
        //查询setmeal
        Setmeal setmeal = setmealMapper.selectById(id);
        //查询setmeal所有的setmealDish存入ArrayList
        List<SetmealDish> list = setmealDishMapper.selectByDishId(setmeal.getId());
        //新建setmealDto拷贝并返回
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(list);
        return Result.success(setmealDto);
    }

    @Override
    public Result<Page> getSetmealPageList(int page, int pageSize,String name) {
        Page pageInfo = new Page<Setmeal>(page,pageSize);
        Page dtoPage = new Page<SetmealDto>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealMapper.selectPage(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryMapper.selectById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return Result.success(dtoPage);
    }

    @Override
    public Result<String> setSetmeal(SetmealDto setmealDto, long userId) {
        //设置创建人创建时间
        setmealDto.setCreateTime(LocalDateTime.now());
        setmealDto.setUpdateTime(LocalDateTime.now());
        setmealDto.setCreateUser(userId);
        setmealDto.setUpdateUser(userId);
        Setmeal setmeal = setmealDto;
        //将setmeal从setmealDto中取出来，存入setmeal库（name不能重复）
        if (setmealMapper.selectByName(setmealDto.getName()) != null){
            return Result.error("套餐名字重复，添加失败");
        }
        setmealMapper.insert(setmeal);
        //从setmeal库中取出setmealId
        setmeal = setmealMapper.selectByName(setmeal.getName());
        //将setmealDish从setmealDto中取出来(ArrayList),取的是dishId
        ArrayList<SetmealDish> list = (ArrayList<SetmealDish>) setmealDto.getSetmealDishes();
        for (int i = 0;i < list.size();i++){
            SetmealDish setmealDish = list.get(i);
            setmealDish.setSetmealId(setmeal.getId());
            setmealDish.setCreateTime(LocalDateTime.now());
            setmealDish.setUpdateTime(LocalDateTime.now());
            setmealDish.setCreateUser(userId);
            setmealDish.setUpdateUser(userId);
            setmealDishMapper.insert(setmealDish);
        }
        return Result.success("添加成功");
    }

    @Override
    public Result<String> changeSetmealStatus(int status, String id, long userId) {
        String[] ids = id.split(",");
        for (int i = 0;i < ids.length;i++){
            if (setmealMapper.updateStatusById(status,Long.parseLong(ids[i]),LocalDateTime.now(),userId) == -1){
                return Result.error("修改失败");
            }
        }
        return Result.success("修改成功");
    }

    @Override
    public Result<List<Setmeal>> getCategoryList(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        List<Setmeal> list = setmealMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @Override
    public Result<String> deleteSetmealById(long id) throws Exception{
        String img = setmealMapper.selectById(id).getImage();
        if (setmealDishMapper.deleteBySetmealId(id) == -1) return Result.error("删除套餐菜品失败");
        if (setmealMapper.deleteById(id) == -1) return Result.error("删除套餐品失败");
        commonService.deleteImg(img);
        return Result.success("删除成功");
    }

    @Override
    public Result<String> changeSetmealById(SetmealDto setmealDto,long userId) throws Exception{
        Setmeal setmeal = setmealDto;
        String oldImg = setmealMapper.selectById(setmeal.getId()).getImage();
        setmeal.setUpdateUser(userId);
        setmeal.setUpdateTime(LocalDateTime.now());
        //根据setmealDto中的setmealId将setmealDish表中数据全部删除
        ArrayList<SetmealDish> newList = (ArrayList<SetmealDish>) setmealDto.getSetmealDishes();
        ArrayList<SetmealDish> oldList = (ArrayList<SetmealDish>) setmealDishMapper.selectByDishId(setmeal.getId());
        if (oldList.size() >= newList.size()) {
            for (int i =0;i < oldList.size();i++){
                for (int j = 0;j < newList.size();j++){
                    if (oldList.get(i).getName().equals(newList.get(j).getName())){
                        newList.get(j).setCreateTime(oldList.get(i).getCreateTime());
                        newList.get(j).setCreateUser(oldList.get(i).getCreateUser());
                    }
                }
            }
        } else {
            for (int i =0;i < newList.size();i++){
                for (int j = 0;j < oldList.size();j++){
                    if (newList.get(i).getName().equals(oldList.get(j).getName())){
                        newList.get(i).setCreateTime(oldList.get(j).getCreateTime());
                        newList.get(i).setCreateUser(oldList.get(j).getCreateUser());
                    }
                }
            }
        }
        setmealDishMapper.deleteBySetmealId(setmeal.getId());
        //将setmealDto中的setmealDish取出并插入表
        for (int i =0;i < newList.size();i++){
            if (newList.get(i).getCreateTime() == null) newList.get(i).setCreateTime(LocalDateTime.now());
            if (newList.get(i).getCreateUser() == null) newList.get(i).setCreateUser(userId);
            newList.get(i).setUpdateTime(LocalDateTime.now());
            newList.get(i).setUpdateUser(userId);
            newList.get(i).setSetmealId(setmeal.getId());
            setmealDishMapper.insert(newList.get(i));
        }
        //根据setmealDto中的setmealId查出之前图片的name
        String img = setmeal.getImage();
        //将setmeal数据进行修改（用setmealDto）
        setmealMapper.updateById(setmeal);
        //删除之前图片
        if (!oldImg.equals(img)){
            commonService.deleteImg(img);
        }
        return Result.success("更改成功");
    }
}
