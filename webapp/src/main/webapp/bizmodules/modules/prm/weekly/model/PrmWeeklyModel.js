Ext.define("Weekly.model.PrmWeeklyModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'contractorOffice', 'weekleDate', 'state', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },'departmentCodeDesc']
    }
);