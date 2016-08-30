package com.example.shuangxiang.qdkelenqdforphone.bean;

import java.util.List;

/**
 * Created by shuang.xiang on 2016/8/19.
 */
public class AlarmInfo {

    /**
     * createBy : null
     * createDate : 1471582665000
     * delFlag : 0
     * deviceId : deviceId0026
     * deviceName : 设备26
     * displayName : 温度下限报警
     * elementId : xjf_t_2_e_4
     * id : 237764f8-dd68-4b27-b18b-4875805e3ce8
     * labelName : null
     * level : 1
     * orgId : null
     * status : 0
     * tenantId : xjfkhd1a93a12208d4fc5a4d81
     * uint :
     * updateBy : null
     * updateDate : null
     * userId : null
     * userName : null
     * value : true
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private Object createBy;
        private String createDate;
        private String delFlag;
        private String deviceId;
        private String deviceName;
        private String displayName;
        private String elementId;
        private String id;
        private Object labelName;
        private String level;
        private Object orgId;
        private String status;
        private String tenantId;
        private String uint;
        private Object updateBy;
        private Object updateDate;
        private Object userId;
        private Object userName;
        private String value;

        public Object getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Object createBy) {
            this.createBy = createBy;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getElementId() {
            return elementId;
        }

        public void setElementId(String elementId) {
            this.elementId = elementId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getLabelName() {
            return labelName;
        }

        public void setLabelName(Object labelName) {
            this.labelName = labelName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public Object getOrgId() {
            return orgId;
        }

        public void setOrgId(Object orgId) {
            this.orgId = orgId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public String getUint() {
            return uint;
        }

        public void setUint(String uint) {
            this.uint = uint;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public Object getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Object updateDate) {
            this.updateDate = updateDate;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public Object getUserName() {
            return userName;
        }

        public void setUserName(Object userName) {
            this.userName = userName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
