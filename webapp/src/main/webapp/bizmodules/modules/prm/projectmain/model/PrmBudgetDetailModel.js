Ext.define("Projectmain.model.PrmBudgetDetailModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmBudgetMainId', 'budgetCode', 'contractMoney', 'jointDesignMoney', 'costControlMoney', 'explanation', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'prmProjectMainId', 'serialNumber', 'latestUpdateTime', 'changeStatus', 'changeVersionDesc', 'latestUpdateBy', 'latestUpdateByDesc', 'vatAmount', 'excludingVatAmount']
    }
);