Ext.define("Scmcontract.model.ScmContractModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'scmContractCode', 'electricCommercialStore', 'jdName', 'jdPassword', 'jdOrderNo', 'jdOrderDate', 'jdLastDate', 'fadSubjectCode', 'contractNature', 'supplierCode', 'supplierName', 'amount', 'quantity', 'purchaseTypes', 'contractPayType', 'otherDes', 'debter', 'debterDepartment', 'bank', 'bankId', 'totalValue', 'payType', 'payTypeCombo', 'isUrgent', 'debtDate', 'state', 'effectiveDate', 'stampDate', 'contractState', 'isVirtual', 'isVirtualCombo', 'paid', 'invoiceAmount', 'remark', 'companyCode', 'projectId', 'projectName', 'departmentCode', 'createBy', 'createOffice', 'officeId', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'subjectCode', 'tblVersion', 'purchasePackageId', {
            name: 'isVoid',
            defaultValue: 0
        }, 'payeeName', 'operatorId', 'operatorName', 'bugdetId', 'taxTypes', 'levelTypes', 'isProject', 'supplierGenre', 'eta',
            'contractNatureCombo', 'purchaseTypesCombo', 'contractPayTypeCombo', 'contractStateCombo', 'isUrgentCombo', 'debterDepartmentDesc', 'stampProjectCode']
    }
);