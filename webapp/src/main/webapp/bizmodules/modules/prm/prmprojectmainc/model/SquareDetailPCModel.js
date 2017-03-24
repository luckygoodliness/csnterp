Ext.define("Prmprojectmainc.model.SquareDetailPCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainCId', {
            name: 'schemingSquareDate',
            type: 'date'
        }, 'schemingSquareMoney', 'schemingSquareCost', 'schemingSquareProfits', 'explanation', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'lastUuid', 'isRevised', 'changedFields', 'changeStatus']
	}
);