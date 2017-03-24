Ext.define("Prmprojectmainc.model.ProjectMainCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'prmContractId', 'projectName', {
            name: 'isPreProject',
            defaultValue: 0
        }, 'projectMoney', 'contractSignMoney', 'costControlMoney', 'projectSerialNo', 'customerId', 'contractorOffice', 'projectManager', 'contractDuration', 'scheduledBeginDate', 'scheduledEndDate', 'seriesAmount', 'taxType', 'fundsExplanation', 'remark', 'state', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'projectCode', 'auditTime', 'examDate',
            , {name: 'detail_type', defaultValue: '*'}, {name: 'isMajorProject', defaultValue: 0}]
    }
);