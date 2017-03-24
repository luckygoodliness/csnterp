Ext.define("Apply.model.AssetDiscardApplyModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'applyCode', 'applyOfficeId','applyOfficeIdDesc', 'state', 'applyUserCode', 'applyUserName', 'applyDate', 'cardUuid', 'assetCode', 'discardReason', 'residualHandle', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion']
    }
);