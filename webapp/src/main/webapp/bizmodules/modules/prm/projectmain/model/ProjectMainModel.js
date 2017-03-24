Ext.define("Projectmain.model.ProjectMainModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmContractId', 'projectName', {
            name: 'isPreProject',
            defaultValue: 0
        }, 'projectMoney', 'costControlMoney', 'projectSerialNo', 'customerId', 'contractorOffice', 'projectManager', 'contractDuration', 'scheduledBeginDate', 'scheduledEndDate', 'seriesAmount', 'taxType', 'fundsExplanation', 'remark', 'state', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },{name: 'isMajorProject',defaultValue: 0}]
	}
);