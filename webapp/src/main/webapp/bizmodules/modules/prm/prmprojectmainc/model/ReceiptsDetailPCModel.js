Ext.define("Prmprojectmainc.model.ReceiptsDetailPCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainCId', 'projectStage', {
            name: 'schemingReceiptsDate',
            type: 'date'
        }, 'schemingReceiptsMoney', 'explanation', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'lastUuid', 'isRevised', 'changedFields', 'changeStatus']
	}
);