Ext.define("Check.model.AssetAnnualCheckModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'cardCode', 'cardUuid','alreadyUse', 'annualCheckTime','annualCheckCompany','annualCheckFee', 'companyDeviceManager', 'deptDeviceManager', 'descp', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion']
    }
);