Ext.define("Prmprojectmainc.model.ProgressDetailPCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainCId', 'projectStage', {
            name: 'schemingBeginDate',
            type: 'date'
        }, {name: 'schemingEndDate', type: 'date'},
            {name: 'actualBeginDate', type: 'date'}, {name: 'actualEndDate', type: 'date'},
            'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
            }, 'lastUuid', 'isRevised', 'changedFields', 'changeStatus']
	}
);