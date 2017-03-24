Ext.define("Projectmain.model.ProgressDetailPModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'prmProgressMainPId', 'projectStage', 'schemingBeginDate', 'schemingEndDate', 'actualBeginDate', 'actualEndDate', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'latestUpdateTime', 'changeStatus', 'changeVersionDesc', 'latestUpdateBy', 'latestUpdateByDesc']
    }
);