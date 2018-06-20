package kelijun.com.sqlite.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kelijun.com.sqlite.annotation.sqlite.Id;
import kelijun.com.sqlite.annotation.sqlite.Table;
import kelijun.com.sqlite.db.sqlite.ManyToOneLazyLoader;
import kelijun.com.sqlite.db.table.ManyToOne;
import kelijun.com.sqlite.db.table.OneToMany;
import kelijun.com.sqlite.db.table.Property;
import kelijun.com.sqlite.exception.DbException;

/**
 * Created by ${kelijun} on 2018/6/20.
 */

public class ClassUtils {
    /**
     * 根据实体类 获得 实体类对应的表名
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        Table table=clazz.getAnnotation(Table.class);
        if(table==null ||table.name().trim().length()==0){
            //当没有注解的时候默认用类的名称作为表名,并把点（.）替换为下划线(
            return clazz.getName().replace(',','_');
        }
        return table.name();
    }
    /**
     * 根据实体类 获得 实体类对应的表名
     * @return
     */
    public static Field getPrimaryKeyField(Class<?> clazz) {
        Field primaryKeyField = null ;
        Field[] fields = clazz.getDeclaredFields();
        if(fields!=null){
            for(Field field : fields){ //获取ID注解
                if(field.getAnnotation(Id.class) != null){
                    primaryKeyField = field;
                    break;
                }
            }

            if(primaryKeyField == null){ //没有ID注解
                for(Field field : fields){
                    if("_id".equals(field.getName())){
                        primaryKeyField = field;
                        break;
                    }
                }
            }

            if(primaryKeyField == null){ // 如果没有_id的字段
                for(Field field : fields){
                    if("id".equals(field.getName())){
                        primaryKeyField = field;
                        break;
                    }
                }
            }
        }else{
            throw new RuntimeException("this model["+clazz+"] has no field");
        }
        return primaryKeyField;
    }
    /**
     * 将对象转换为ContentValues
     * @return
     */
    public static List<Property> getPropertyList(Class<?> clazz) {

        List<Property> plist = new ArrayList<Property>();
        try {
            Field[] fs = clazz.getDeclaredFields();
            String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
            for (Field f : fs) {
                //必须是基本数据类型和没有标瞬时态的字段
                if(!FieldUtils.isTransient(f)){
                    if (FieldUtils.isBaseDateType(f)) {

                        if(f.getName().equals(primaryKeyFieldName)) //过滤主键
                            continue;

                        Property property = new Property();

                        property.setColumn(FieldUtils.getColumnByField(f));
                        property.setFieldName(f.getName());
                        property.setDataType(f.getType());
                        property.setDefaultValue(FieldUtils.getPropertyDefaultValue(f));
                        property.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                        property.setGet(FieldUtils.getFieldGetMethod(clazz, f));
                        property.setField(f);

                        plist.add(property);
                    }
                }
            }
            return plist;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    /**
     * 将对象转换为ContentValues
     *
     * @return
     */
    public static List<OneToMany> getOneToManyList(Class<?> clazz) {

        List<OneToMany> oList = new ArrayList<OneToMany>();
        try {
            Field[] fs = clazz.getDeclaredFields();
            for (Field f : fs) {
                if (!FieldUtils.isTransient(f) && FieldUtils.isOneToMany(f)) {

                    OneToMany otm = new OneToMany();

                    otm.setColumn(FieldUtils.getColumnByField(f));
                    otm.setFieldName(f.getName());

                    Type type = f.getGenericType();

                    if(type instanceof ParameterizedType){
                        ParameterizedType pType = (ParameterizedType) f.getGenericType();
                        //如果类型参数为2则认为是LazyLoader 2013-7-25
                        if(pType.getActualTypeArguments().length==1){
                            Class<?> pClazz = (Class<?>)pType.getActualTypeArguments()[0];
                            if(pClazz!=null)
                                otm.setOneClass(pClazz);
                        }else{
                            Class<?> pClazz = (Class<?>)pType.getActualTypeArguments()[1];
                            if(pClazz!=null)
                                otm.setOneClass(pClazz);
                        }
                    }else{
                        throw new DbException("getOneToManyList Exception:"+f.getName()+"'s type is null");
                    }
					/*修正类型赋值错误的bug，f.getClass返回的是Filed*/
                    otm.setDataType(f.getType());
                    otm.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                    otm.setGet(FieldUtils.getFieldGetMethod(clazz, f));

                    oList.add(otm);
                }
            }
            return oList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将对象转换为ContentValues
     *
     * @return
     */
    public static List<ManyToOne> getManyToOneList(Class<?> clazz) {

        List<ManyToOne> mList = new ArrayList<ManyToOne>();
        try {
            Field[] fs = clazz.getDeclaredFields();
            for (Field f : fs) {
                if (!FieldUtils.isTransient(f) && FieldUtils.isManyToOne(f)) {

                    ManyToOne mto = new ManyToOne();
                    //如果类型为ManyToOneLazyLoader则取第二个参数作为manyClass（一方实体） 2013-7-26
                    if(f.getType()==ManyToOneLazyLoader.class){
                        Class<?> pClazz = (Class<?>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[1];
                        if(pClazz!=null)
                            mto.setManyClass(pClazz);
                    }else {
                        mto.setManyClass(f.getType());
                    }
                    mto.setColumn(FieldUtils.getColumnByField(f));
                    mto.setFieldName(f.getName());
                    mto.setDataType(f.getType());
                    mto.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                    mto.setGet(FieldUtils.getFieldGetMethod(clazz, f));

                    mList.add(mto);
                }
            }
            return mList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    /**
     * 根据实体类 获得 实体类对应的表名
     * @return
     */
    public static String getPrimaryKeyFieldName(Class<?> clazz) {
        Field f = getPrimaryKeyField(clazz);
        return f==null ? null:f.getName();
    }
}
