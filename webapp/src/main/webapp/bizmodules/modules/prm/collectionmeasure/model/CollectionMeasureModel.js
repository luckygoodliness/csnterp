Ext.define("Collectionmeasure.model.CollectionMeasureModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'prmMeasureSignCombo', 'prmMeasureReportCombo', 'prmWeeklyId', 'periods', 'report', 'principal', 'measureMoney', 'sign', 'actualReceiptsDate', 'remark', 'state', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'departmentCodeDesc']
    }
);