package cc.seektao.codegenerator.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    public static String convertObjectToJson(Object obj){
        if (null == obj){
            return null;
        }
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }
}
