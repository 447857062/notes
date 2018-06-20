package kelijun.com.sqlite.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kelijun.com.sqlite.annotation.sqlite.Id;
import kelijun.com.sqlite.annotation.sqlite.ManyToOne;
import kelijun.com.sqlite.annotation.sqlite.OneToMany;
import kelijun.com.sqlite.annotation.sqlite.Property;
import kelijun.com.sqlite.annotation.sqlite.Transient;

/**
 * Created by ${kelijun} on 2018/6/20.
 */

public class FieldUtils {
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Method getFieldGetMethod(Class<?>clazz, Field f){
        String fn=f.getName();
        Method m=null;
        if(f.getType()==boolean.class){
            m=getBooleanFieldGetMethod(clazz,fn);
        }
        if(m==null){
            m=getFieldGetMethod(clazz,fn);
        }
        return m;
    }

    private static Method getFieldGetMethod(Class<?> clazz, String fieldName) {
        String mn = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return clazz.getDeclaredMethod(fieldName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取boolean类型的get开头的方法
     * @param clazz
     * @param fn
     * @return
     */
    private static Method getBooleanFieldGetMethod(Class<?> clazz, String fn) {
        String mn="is"+fn.substring(0,1).toUpperCase()+fn.substring(1);
        if(isISStart(fn)){
            mn=fn;
        }
        try {
            return clazz.getDeclaredMethod(mn);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Boolean 类型的set方法
     * @param clazz
     * @param f
     * @return
     */
    public static Method getBooleanFieldSetMethod(Class<?> clazz, Field f) {
        String fn=f.getName();
        String mn="set"+fn.substring(0,1).toUpperCase()+fn.substring(1);
        if(isISStart(fn)){
            mn="set"+fn.substring(2,3).toUpperCase()+fn.substring(3);
        }
        try {
            return clazz.getDeclaredMethod(mn,f.getType());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断is开头的方法
     * @param fn
     * @return
     */
    private static boolean isISStart(String fn) {
        if(fn==null || fn.trim().length()==0){
            return false;
        }
        //is开头，并且is之后第一个字母是大写 比如 isAdmin
        return fn.startsWith("is")&& !Character.isLowerCase(fn.charAt(2));
    }
    public static boolean isManyToOne(Field field){
        return field.getAnnotation(ManyToOne.class)!=null;
    }
    public static boolean isOneToMany(Field field){
        return field.getAnnotation(OneToMany.class)!=null;
    }
    public static String getPropertyDefaultValue(Field field){
        Property property = field.getAnnotation(Property.class);
        if(property != null && property.defaultValue().trim().length() != 0){
            return property.defaultValue();
        }
        return null ;
    }
    public static Method getFieldSetMethod(Class<?> clazz, Field f) {
        String fn = f.getName();
        String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
        try {
            return clazz.getDeclaredMethod(mn, f.getType());
        } catch (NoSuchMethodException e) {
            if(f.getType() == boolean.class){
                return getBooleanFieldSetMethod(clazz, f);
            }
        }
        return null;
    }
    /**
     * 获取某个属性对应的 表的列
     * @return
     */
    public static String getColumnByField(Field field){
        Property property = field.getAnnotation(Property.class);
        if(property != null && property.column().trim().length() != 0){
            return property.column();
        }

        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        if(manyToOne!=null && manyToOne.column().trim().length()!=0){
            return manyToOne.column();
        }

        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if(oneToMany!=null && oneToMany.manyColumn()!=null &&oneToMany.manyColumn().trim().length()!=0){
            return oneToMany.manyColumn();
        }

        Id id = field.getAnnotation(Id.class);
        if(id!=null && id.column().trim().length()!=0)
            return id.column();

        return field.getName();
    }
    /**
     * 检测 字段是否已经被标注为 非数据库字段
     * @param f
     * @return
     */
    public static boolean isTransient(Field f) {
        return f.getAnnotation(Transient.class) != null;
    }

    /**
     * 是否基本的数据类型(数据库支持的类型)
     * @param field
     * @return
     */
    public static boolean isBaseDateType(Field field){
        Class <?>clazz=field.getType();
        return clazz.equals(String.class)||
                clazz.equals(Integer.class)||
                clazz.equals(Boolean.class)||
                clazz.equals(Byte.class)||
                clazz.equals(Long.class)||
                clazz.equals(Double.class)||
                clazz.equals(Float.class)||
                clazz.equals(Character.class)||
                clazz.equals(Short.class)||
                clazz.equals(Date.class)||
                clazz.equals(java.sql.Date.class)||
                clazz.isPrimitive();
    }
    /**
     * 字符串转时间类型
     * @param strData
     * @return
     */
    public static Date stringToDateTime(String strData){
        if(strData!=null){
            try {
                return SDF.parse(strData);
            } catch (ParseException e) {
                e.printStackTrace();

            }
        }
        return null;
    }
}
