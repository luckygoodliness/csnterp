Ext.define("Subjectsubject.model.NonProjectSubjectSubjectModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'officeId', 'financialSubject', 'isActive', 'financialSubjectCode', 'needPurchase', 'enabledTime', 'disabledTime', 'descp', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', {
            name: 'isVoid',
            defaultValue: 0
        }, 'locTimezone', 'tblVersion', 'officeIdDesc']
    }
);