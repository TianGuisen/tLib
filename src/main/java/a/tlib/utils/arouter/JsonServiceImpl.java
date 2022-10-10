package a.tlib.utils.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;

import java.lang.reflect.Type;

import a.tlib.utils.gson.GsonUtil;

/**
 * @author 田桂森 2021/7/21 0021
 */
@Route(path = "/service/json")
public class JsonServiceImpl implements SerializationService {
    @Override
    public void init(Context context) {
        
    }
    
    @Override
    public <T> T json2Object(String text, Class<T> clazz) {
        return GsonUtil.toBean(text, clazz);
    }
    
    @Override
    public String object2Json(Object instance) {
        return GsonUtil.toJson(instance);
    }
    
    @Override
    public <T> T parseObject(String input, Type clazz) {
        return GsonUtil.toBean(input, clazz);
    }
}