Ext.define("Brief.model.BriefModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'prmWeeklyId', 'prmBriefTypeCombo', 'content', 'state', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'departmentCodeDesc']
    }
);