package com.sustech.conferenceSystem.util;

import com.alibaba.fastjson.serializer.ValueFilter;

public class Filter {

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
}
