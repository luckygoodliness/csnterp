Ext.define("Fadinterestrate.model.FadInterestRateModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'rate', 'depositRate', 'validityDateFrom', 'validityDateTo', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion', 'seqNo', 'rateName']
    }
);