package com.gongva.library.plugin.netbase;

import com.gongva.library.plugin.netbase.entity.ABaseResult;
import com.gongva.library.plugin.netbase.entity.ClassTypeEntity;
import com.hik.core.android.api.LogCat;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 泛型类型Utils
 *
 * @author gongwei
 * @data 2019/3/8
 * @email shmily__vivi@163.com
 */
public class GenericityTypeUtils {

    private static final String TAG = "GenericsUtils";

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
    public static Class getSuperClassGenericType(Class clazz, ClassTypeEntity entity) {
        return getSuperClassGenericType(clazz, entity, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    public static Class getSuperClassGenericType(Class clazz, ClassTypeEntity entity, int index) throws IndexOutOfBoundsException {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type，然后将其转换ParameterizedType。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            if (ABaseResult.class.isAssignableFrom(clazz)) {
                if (entity == null) {
                    entity = new ClassTypeEntity();
                }
                entity.addClassType(ABaseResult.class);
            }
            return Object.class;
        }

        if (entity == null) {
            entity = new ClassTypeEntity();
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。[0]就是这个数组中第一个了。简而言之就是获得超类的泛型参数的实际类型。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            LogCat.d(TAG, "getSuperClassGenricType type:" + params[index]);
            if (params[index] instanceof ParameterizedType) {
                createClassTypeEntity(entity, params[index]);
                LogCat.d(TAG, "getSuperClassGenricType entity:" + entity);
                return (Class) ((ParameterizedType) params[index]).getRawType();
            }
            entity.addClassType(Object.class);
            return Object.class;
        }
        entity.addClassType(params[index]);
        return (Class) params[index];
    }

    /**
     * 创建类型实例，用于收纳复合类型
     *
     * @param entity
     * @param type
     */
    private static void createClassTypeEntity(ClassTypeEntity entity, Type type) {
        if (type instanceof ParameterizedType) {
            if (Collection.class.isAssignableFrom((Class) ((ParameterizedType) type).getRawType())) {
                entity.addClassType(Collection.class);
                Type[] tparams = ((ParameterizedType) type).getActualTypeArguments();
                if (tparams != null) {
                    for (Type ttparam : tparams) {
                        ClassTypeEntity tentity = new ClassTypeEntity();
                        entity.setChildTypeEntity(tentity);
                        tentity.addClassType(ttparam);
                        createClassTypeEntity(tentity, ttparam);
                    }
                }
            } else if (ABaseResult.class.isAssignableFrom((Class) ((ParameterizedType) type).getRawType())) {
                entity.addClassType(ABaseResult.class);
                Type[] tparams = ((ParameterizedType) type).getActualTypeArguments();
                if (tparams != null) {
                    for (Type ttparam : tparams) {
                        ClassTypeEntity tentity = new ClassTypeEntity();
                        entity.setChildTypeEntity(tentity);
                        tentity.addClassType(ttparam);
                        createClassTypeEntity(tentity, ttparam);
                    }
                }
            }
        }
    }

    /**
     * class类别比较
     *
     * @param obj    class类型实例
     * @param entity 类型entity
     * @return
     */
    public static boolean classTypeCompare(Object obj, ClassTypeEntity entity) {
        if (obj != null && entity != null) {
            if (obj instanceof List) {
                LogCat.d(TAG, "classTypeCompare obj is list:" + obj.getClass());
                if (entity.getClassType() != null && entity.getClassType().size() == 1
                        && List.class.isAssignableFrom((Class) (entity.getClassType().get(0)))) {
                    if (((List) obj).size() > 0) {
                        return classTypeCompare(((List) obj).get(0), entity.getChildTypeEntity());
                    }
                }
            } else {
                LogCat.d(TAG, "classTypeCompare obj is object:" + obj.getClass());
                if (entity.getClassType() != null && entity.getClassType().size() == 1
                        && obj.getClass().isAssignableFrom((Class) (entity.getClassType().get(0)))) {
                    return true;
                }
            }
        }
        return false;
    }
}
