Ext.define("Finalestimate.model.FinalEstimateModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'cdmBillStateCombo', 'squareType', 'squareDate', 'squareProjectMoney', 'squareGrossProfit', 'manageMoney', 'rax', 'state', {
            name: 'isArchiving',
            defaultValue: 0
        }, 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'examDate', 'examRTaxDate', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'squareCost', 'departmentCodeDesc']
    }
);