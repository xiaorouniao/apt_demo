package cn.gd.snm.annotation;

import java.lang.reflect.Method;

/**
 * @author by Low_Power on 2023/6/26
 * 功能：
 */
public class CustomButterKnife {

    public static void bind(Object object) {
        String simpleName = object.getClass().getName();
        String newClassName = simpleName + "$$ViewBind";
        try {
            Class<?> newClass = Class.forName(newClassName);
            Object obj = newClass.newInstance();
            Method bind = newClass.getMethod("bind", object.getClass());
            bind.invoke(obj, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
