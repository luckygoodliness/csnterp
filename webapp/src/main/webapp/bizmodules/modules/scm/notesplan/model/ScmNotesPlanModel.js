Ext.define("Notesplan.model.ScmNotesPlanModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'issueNumber', 'islock', 'remark', 'companyCode', 'companyName', 'createBy', 'createTime', 'updateBy', 'updateTime', {
            name: 'isVoid',
            defaultValue: 0
        }, 'locTimezone', 'tblVersion', 'seqNo']
    }
);