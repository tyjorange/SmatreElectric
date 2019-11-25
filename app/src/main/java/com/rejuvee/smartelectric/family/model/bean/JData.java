package com.rejuvee.smartelectric.family.model.bean;

import java.util.List;

/**
 * 查询参数:{"switchCode":1,"paramIDs":[1,2,3,4]}
 * <p>
 * 设置参数:{"switchCode":1,"params":[{"id":1,"value":1},{"id":2,"value":4},{"id":3,"value":9},{"id":4,"value":16}]}
 */
public class JData {
    public String switchCode;
    public List<String> paramIDs;
    public List<PP> params;
}
