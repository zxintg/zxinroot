package com.zxin.root.sharedfref;

import java.util.Set;

/**
 * Created by alex.liu on 2017/5/17.
 */

public interface IData {
    enum DataType {NONE, INT, LONG, STRING, FLOAT, BOOLEAN, STRING_SET}
    int DEFAULT_VALUE_INTEGER = Integer.MIN_VALUE;
    float DEFAULT_VALUE_FLOAT = Float.MIN_VALUE;
    long DEFAULT_VALUE_LONG = Long.MIN_VALUE;
    boolean DEFAULT_VALUE_BOOLEAN = false;
    String DEFAULT_VALUE_STRING = "";
    Set DEFAULT_VALUE_STRING_SET = null;
}
