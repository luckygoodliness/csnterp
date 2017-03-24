Ext.define("Prmprojectmainc.model.MemberDetailPCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainCId', 'staffId', 'post', 'jobShare', {
            name: 'enterDate',
            type: 'date'
        }, {
            name: 'outDate',
            type: 'date'
        }, 'percent', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }
            , 'staffIdDesc', 'lastUuid', 'isRevised', 'changedFields', 'changeStatus']
    }
);