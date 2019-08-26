package com.zxin.root.sharedfref;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex.liu on 2017/5/17.
 */

public class DataTypeHelper implements IData{

    public static final char GROUP_ITEM_SPLIT = '@';

    private static Map<String, DataType> mDataTypeMap = new HashMap<>();

    public static DataType getDataType(String key) {
        DataType type =  mDataTypeMap.get(key);
        return type == null ? DataType.NONE : type;
    }

    public static void setDataType(String key, DataType dataType) {
        mDataTypeMap.put(key, dataType);
    }

    public static String generateDataTypeKey(SharedPreferencesWrapper wrapper, String key) {
        return wrapper.getSharedPreferencesName() + GROUP_ITEM_SPLIT + key;
    }
}
