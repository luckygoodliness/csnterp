Ext.define("Budget.model.NonProjectBudgetCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'hid', 'financialSubjectCode', 'lastYearAssigned', 'lastYearChanged',
            'lastYearOccured', 'thisYearApplyed', 'thisYearFirstInstance', 'thisYearAssigned','thisYearPreappropriation',
            'thisYearOccured', 'thisYearChanged', 'thisYearAppropriation', 'descp', 'companyCode', 'thisYearLocked',
            'companyName', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo',
            'updateBy', 'updateOffice', 'updateTime', {name: 'isVoid', defaultValue: 0},
            'locTimezone', 'tblVersion', 'nonProjectBudgetCDDto',
            'nonProjectBudgetCD2Dto', 'subjectName', 'subjectCode']
    }
);