package com.rejuvee.smartelectric.family.model.bean;


import com.rejuvee.smartelectric.family.common.NativeLine;

/**
 * Created by SH on 2018/1/2.
 */

public class SignalType {

    private String signalsTypeID;
    private String typeCode;
    private String typeName;
    private String unit;


    public String getSignalsTypeID() {
        return signalsTypeID;
    }

    public void setSignalsTypeID(String signalsTypeID) {
        this.signalsTypeID = signalsTypeID;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getTabTitle() {
        return NativeLine.TabTitleSignalType[Integer.parseInt(typeCode, 16) - 1];
    }
}
