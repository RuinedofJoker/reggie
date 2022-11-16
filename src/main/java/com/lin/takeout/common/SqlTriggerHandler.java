package com.lin.takeout.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
    mybatis提供的元数据处理器
    在实体类中的字段上设置：
        @TableField(fill = FieldFill.INSERT)
        @TableField(fill = FieldFill.INSERT_UPDATE)
    指定在插入/或插入修改时需要对该字段进行操作
    然后在本类中指定操作
*/
@Component
public class SqlTriggerHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",GetIdByThreadLocal.getId());
        metaObject.setValue("updateUser",GetIdByThreadLocal.getId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",GetIdByThreadLocal.getId());
    }
}
