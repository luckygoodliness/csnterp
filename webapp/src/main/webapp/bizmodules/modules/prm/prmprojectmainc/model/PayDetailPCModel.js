Ext.define("Prmprojectmainc.model.PayDetailPCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainCId', 'projectStage', 'payContent', 'payMoney', {
            name: 'beginDate',
            type: 'date'
        }, {
            name: 'endDate',
            type: 'date'
        }, 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'lastUuid', 'isRevised', 'changedFields', 'changeStatus']
	}
);