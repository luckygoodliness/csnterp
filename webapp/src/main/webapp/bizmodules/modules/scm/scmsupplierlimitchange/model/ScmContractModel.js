Ext.define("Scmsupplierlimitchange.model.ScmContractModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'scmContractCode', 'electricCommercialStore', 'jdName', 'jdPassword', 'jdOrderNo', 'jdOrderDate', 'jdLastDate', 'fadSubjectCode', 'contractNature', 'supplierCode', 'supplierName', 'amount', 'quantity', 'purchaseTypes', 'contractPayType', 'otherDes', 'debter', 'debterDepartment', 'bank', 'bankId', 'totalValue', 'payType', 'payTypeCombo', 'isUrgent', 'debtDate', 'state', 'effectiveDate', 'stampDate', 'contractState', 'isVirtual', 'isVirtualCombo', 'paid', 'invoiceAmount', 'remark', 'companyCode', 'projectId', 'projectName', 'departmentCode', 'createBy', 'createOffice', 'officeId', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'subjectCode', 'projectCode','tblVersion', 'purchasePackageId', {
            name: 'isVoid',
            defaultValue: 0
        }, 'payeeName', 'operatorId', 'operatorName', 'bugdetId', 'taxTypes', 'levelTypes','isProject',
            'contractNatureCombo', 'purchaseTypesCombo', 'contractPayTypeCombo', 'contractStateCombo', 'isUrgentCombo', 'debterDepartmentDesc']
    }
);