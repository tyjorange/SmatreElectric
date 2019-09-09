package com.rejuvee.smartelectric.family.model.bean;

import java.io.Serializable;

/**
 * Created by liuchengran on 2017/12/20.
 */
public class SwitchSignalItem implements Serializable {
    private String signalsTypeID;
    private String typeName;
    private String unit;
    private float value;

    public String getSignalsTypeID() {
        return signalsTypeID;
    }

    public void setSignalsTypeID(String signalsTypeID) {
        this.signalsTypeID = signalsTypeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
//    public static class SignalsType implements Serializable {
//        private String signalsTypeID;
//        private String typeCode;
//        private String typeName;
//        private String unit;
//
//        public String getSignalsTypeID() {
//            return signalsTypeID;
//        }
//
//        public void setSignalsTypeID(String signalsTypeID) {
//            this.signalsTypeID = signalsTypeID;
//        }
//
//        public String getTypeCode() {
//            return typeCode;
//        }
//
//        public void setTypeCode(String typeCode) {
//            this.typeCode = typeCode;
//        }
//
//        public String getTypeName() {
//            return typeName;
//        }
//
//        public void setTypeName(String typeName) {
//            this.typeName = typeName;
//        }
//
//        public String getUnit() {
//            return unit;
//        }
//
//        public void setUnit(String unit) {
//            this.unit = unit;
//        }
//    }
}
