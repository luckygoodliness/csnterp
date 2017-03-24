Ext.define("ContractC.model.ContractCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'operateBusinessBidInfoId', 'countryName', 'provinceName', 'remark', 'auditTime', 'examDate',
            'projectName', 'projectManager', 'generalEngineer', 'contractorOffice', 'contractLastMoney',
            'contractName', 'contractNo', 'state', 'customerId', 'designerId', 'prmCodeType',
            'managementId', 'contractSignMoney', 'contractNowMoney', 'contractSignDate', 'contractStartDate',
            'contractEndDate', 'contractDuration', 'defectsLiabilityPeriods', 'preoperation', 'countryCode',
            'buildRegion', 'projectSourceType', 'taxRegion', 'taxType', 'successBidDate', 'projectScale',
            'prmProjectMainId', 'companyCode', 'projectId', 'departmentCode', 'affiliatedInstitutions', 'createBy', 'createOffice',
            'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
                name: 'isVoid',
                defaultValue: 0
            }, {name: 'isMajorProject', defaultValue: 0}]
    }
);