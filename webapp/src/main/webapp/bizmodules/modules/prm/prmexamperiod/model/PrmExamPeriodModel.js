Ext.define("PrmExamPeriod.model.PrmExamPeriodModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'examDate', 'beginDate', 'endDate', 'state',
            'companyCode', 'projectId', 'departmentCode', 'createBy', 'createTime', 'seqNo', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
                name: 'isVoid',
                defaultValue: 0
            }]
    }
);