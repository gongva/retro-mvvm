package com.hik.core.android.api.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.hik.core.java.security.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * SharePreferences工具库
 *
 * @date 2019/7/25
 */
public class SharePreferencesUtil {
    /**
     * 保存到手机的文件
     */
    public final static String FILE_NAME = "share_data";
    private static SharedPreferences mSharedPreferences;

    public static void init(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getString(key, "");
    }

    /**
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, @NonNull String defValue) {
        return (String) get(key, defValue);
    }

    /**
     * SharedPreferences保存String,由系统commit
     *
     * @param key
     * @param value
     */
    public static void setString(String key, String value) {
        put(key, value, false);
    }

    /**
     * SharedPreferences保存String
     *
     * @param key
     * @param value
     * @param commitImmediately true 立即commit
     */
    public static void setString(String key, String value, boolean commitImmediately) {
        put(key, value, commitImmediately);
    }

    public static int getInt(String key, int defValue) {
        return (int) get(key, defValue);
    }

    /**
     * SharedPreferences保存Int,由系统commit
     *
     * @param key
     * @param value
     */
    public static void setInt(String key, int value) {
        put(key, value, false);
    }

    /**
     * SharedPreferences保存Int
     *
     * @param key
     * @param value
     * @param commitImmediately true 立即commit
     */
    public static void setInt(String key, int value, boolean commitImmediately) {
        put(key, value, commitImmediately);
    }

    public static long getLong(String key, long defValue) {
        return (long) get(key, defValue);
    }

    /**
     * SharedPreferences保存Long,由系统commit
     *
     * @param key
     * @param value
     */
    public static void setLong(String key, long value) {
        put(key, value, false);
    }

    /**
     * SharedPreferences保存Long
     *
     * @param key
     * @param value
     * @param commitImmediately true 立即commit
     */
    public static void setLong(String key, long value, boolean commitImmediately) {
        put(key, value, commitImmediately);
    }

    public static float getFloat(String key, float defValue) {
        return (float) get(key, defValue);
    }

    /**
     * SharedPreferences保存Float,由系统commit
     *
     * @param key
     * @param value
     */
    public static void setFloat(String key, float value) {
        put(key, value, false);
    }

    /**
     * SharedPreferences保存Float
     *
     * @param key
     * @param value
     * @param commitImmediately true 立即commit
     */
    public static void setFloat(String key, float value, boolean commitImmediately) {
        put(key, value, commitImmediately);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return (boolean) get(key, defValue);
    }

    /**
     * SharedPreferences保存boolean,由系统commit
     *
     * @param key
     * @param value
     */
    public static void setBoolean(String key, boolean value) {
        put(key, value, false);
    }

    /**
     * SharedPreferences保存boolean
     *
     * @param key
     * @param value
     * @param commitImmediately true 立即commit
     */
    public static void setBoolean(String key, boolean value, boolean commitImmediately) {
        put(key, value, commitImmediately);
    }

    /**
     * 获取保存的序列化的实体
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getSerializableEntity(Context context, String key) {
        SharedPreferences sharePre = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            String wordBase64 = sharePre.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (wordBase64 == null || wordBase64.equals("")) { // 不可少，否则在下面会报java.io
                // .StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存序列号的实体类
     *
     * @param context
     * @param key
     * @param object
     * @param commitImmediately true 立即commit
     */
    public static void putSerializableEntity(Context context, String key, Serializable object, boolean commitImmediately) {
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(context);
        if (object == null) {
            SharedPreferences.Editor editor = share.edit().remove(key);
            SharedPreferencesCompat.apply(editor, commitImmediately);
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        String objectStr = "";
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            // 将对象放到OutputStream中
            // 将对象转换成byte数组，并将其进行base64编码
            objectStr = new String(Base64.encode(baos.toByteArray()));
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        SharedPreferences.Editor editor = share.edit();
        // 将编码后的字符串写到base64.xml文件中
        editor.putString(key, objectStr);
        SharedPreferencesCompat.apply(editor, commitImmediately);
    }

    /**
     * 保存序列号的实体类，由系统commit
     *
     * @param context
     * @param key
     * @param object
     */
    public static void putSerializableEntity(Context context, String key, Serializable object) {
        putSerializableEntity(context, key, object, false);
    }

    /**
     * 得到保存数据的方法，根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp;
        if (context != null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        } else {
            sp = mSharedPreferences;
        }
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    public static Object get(String key, Object defaultObject) {
        return get(null, key, defaultObject);
    }

    /**
     * 保存数据的方法，将不同类型的数据保存到文件中
     *
     * @param context
     * @param key
     * @param object
     * @param commitImmediately true 立即commit
     */
    public static void put(Context context, String key, Object object, boolean commitImmediately) {
        SharedPreferences sp;
        if (context != null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        } else {
            sp = mSharedPreferences;
        }
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor, commitImmediately);
    }

    /**
     * 保存数据的方法，将不同类型的数据保存到文件中
     *
     * @param key
     * @param object
     * @param commitImmediately true 立即commit
     */
    public static void put(String key, Object object, boolean commitImmediately) {
        put(null, key, object, commitImmediately);
    }

    /**
     * 保存数据的方法，将不同类型的数据保存到文件中，由系统commit
     *
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {
        put(null, key, object, false);
    }

    /**
     * 清除SharedPreferences对应key的数据
     *
     * @param context
     * @param key
     * @param commitImmediately true 立即commit
     */
    public static void remove(Context context, String key, boolean commitImmediately) {
        SharedPreferences sp;
        if (context != null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        } else {
            sp = mSharedPreferences;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor, commitImmediately);
    }

    /**
     * 清除SharedPreferences对应key的数据
     *
     * @param key
     * @param commitImmediately true 立即commit
     */
    public static void remove(String key, boolean commitImmediately) {
        remove(null, key, commitImmediately);
    }

    /**
     * 清除SharedPreferences对应key的数据，由系统commit
     *
     * @param key
     */
    public static void remove(String key) {
        remove(null, key, false);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context, boolean commitImmediately) {
        SharedPreferences sp;
        if (context != null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        } else {
            sp = mSharedPreferences;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor, commitImmediately);
    }

    /**
     * 清理SharedPreferences
     *
     * @param commitImmediately true 立即commit
     */
    public static void clear(boolean commitImmediately) {
        clear(null, commitImmediately);
    }

    /**
     * 清理SharedPreferences，由系统commit
     */
    public static void clear() {
        clear(false);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp;
        if (context != null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        } else {
            sp = mSharedPreferences;
        }
        return sp.contains(key);
    }

    public static boolean contains(String key) {
        return contains(null, key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor, boolean commitImmediately) {
            if (commitImmediately) {
                editor.commit();
            } else {
                try {
                    if (sApplyMethod != null) {
                        sApplyMethod.invoke(editor);
                        return;
                    }
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
                editor.commit();
            }
        }
    }
}
