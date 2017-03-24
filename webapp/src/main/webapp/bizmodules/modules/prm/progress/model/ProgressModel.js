Ext.define("Progress.model.ProgressModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'prmProgressCombo', 'prmWeeklyId', 'projectProgress', 'progressDeviationReason', 'receiptsDeviationReason', 'management', 'state', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },'departmentCodeDesc']
    }
);