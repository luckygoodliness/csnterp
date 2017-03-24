Ext.define("Projectmain.model.PrmBudgetRunModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmBudgetMainId', 'financialSubjectCode', 'unit', 'amount', 'price', 'totalValue', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'prmProjectMainId', 'serialNumber', 'latestUpdateTime', 'changeStatus', 'changeVersionDesc', 'latestUpdateBy', 'latestUpdateByDesc']
    }
);