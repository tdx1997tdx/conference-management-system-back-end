package com.sustech.conferenceSystem.util;
import com.alibaba.fastjson.serializer.ValueFilter;
import java.lang.reflect.Field;

public class JsonFilter {

    //fastjson用的过滤器，如果对象属性值为空，用字符串"null"代替
    private static ValueFilter filter = new ValueFilter() {
        @Override
        public Object process(Object obj, String s, Object v) {
            if (v == null)
                return "null";
            return v;
        }
    };

    public static ValueFilter getFilter(){
        return filter;
    }

    /**
     * 将属性中null字符串转成null
     * @param obj
     */
    public static void attributeFilter(Object obj){
        try {
            // 获取obj类的字节文件对象
            Class c = obj.getClass();
            // 获取该类的成员变量
            Field[] fs = c.getDeclaredFields();
            for(Field f:fs){
                // 取消语言访问检查
                f.setAccessible(true);
                // 给变量赋值
                Object o=f.get(obj);
                if(o!=null&&o.toString().equals("null"))
                    f.set(obj, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
