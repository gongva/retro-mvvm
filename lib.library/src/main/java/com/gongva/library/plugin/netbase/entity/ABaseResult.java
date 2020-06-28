package com.gongva.library.plugin.netbase.entity;

import java.util.Collection;
import java.util.Map;

/**
 * 相应结果接口
 *
 * @author
 * @date 2019/11/15
 * @email
 */
public abstract class ABaseResult<T> implements IEmptyContent {

    private ClassTypeEntity mClassTypeEntity;

    public ClassTypeEntity getClassTypeEntity() {
        return mClassTypeEntity;
    }

    public void setClassTypeEntity(ClassTypeEntity classTypeEntity) {
        mClassTypeEntity = classTypeEntity;
    }

    protected boolean isDataEmpty(T data) {
        if (data != null && getClassTypeEntity() != null && !getClassTypeEntity().isEmpty()) {
            return isDataEmpty(data, getClassTypeEntity());
        } else {
            if (data != null) {
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean isDataEmpty(T data, ClassTypeEntity typeEntity) {
        if (typeEntity.getClassType().size() >= 1) {
            if (Collection.class == typeEntity.getLastClassType()) {
                Collection collection = (Collection) data;
                return collection.isEmpty();
            } else if (Map.class == typeEntity.getLastClassType()) {
                Map map = (Map) data;
                return map.isEmpty();
            } else if (ABaseResult.class == typeEntity.getLastClassType()) {
                ABaseResult baseResult = (ABaseResult) data;
                ((ABaseResult) data).setClassTypeEntity(typeEntity.getChildTypeEntity());
                return baseResult.isEmpty();
            } else if (data != null) {
                return false;
            }
        }
        return true;
    }
}
