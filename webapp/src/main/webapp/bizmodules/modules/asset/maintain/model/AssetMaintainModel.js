Ext.define("Maintain.model.AssetMaintainModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid','maintainApplycode','cardUuid','state','officeId','cdmBillStateCombo','cardCode','assetCode','assetName','content','operater','operateTime','companyCode','companyName','departmentCode','createBy','createOffice','createTime','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','scheduledTime','projectedExpenditure','remark','malfunction']
    }
);