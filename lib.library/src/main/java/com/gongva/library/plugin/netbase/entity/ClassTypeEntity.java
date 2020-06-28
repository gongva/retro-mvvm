package com.gongva.library.plugin.netbase.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 类型实体
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class ClassTypeEntity implements IEmptyContent {
    private List<Type> classType;
    private ClassTypeEntity childTypeEntity;

    public List<Type> getClassType() {
        return classType;
    }

    public void setClassType(List<Type> classType) {
        this.classType = classType;
    }

    public void addClassType(Type type) {
        if (classType == null) {
            classType = new ArrayList<>();
        }
        classType.add(type);
    }

    public ClassTypeEntity getChildTypeEntity() {
        return childTypeEntity;
    }

    public void setChildTypeEntity(ClassTypeEntity childTypeEntity) {
        this.childTypeEntity = childTypeEntity;
    }

    public Type getLastClassType() {
        if (classType != null && classType.size() > 0) {
            return classType.get(classType.size() - 1);
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        ClassTypeEntity entity = (ClassTypeEntity) obj;

        return super.equals(obj);
    }

    @Override
    public boolean isEmpty() {
        if (classType != null && classType.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ClassTypeEntity{" +
                "classType=" + classType +
                ", childTypeEntity=" + childTypeEntity +
                '}';
    }
}
