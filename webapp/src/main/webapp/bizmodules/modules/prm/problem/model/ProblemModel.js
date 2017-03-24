Ext.define("Problem.model.ProblemModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmWeeklyId', 'cdmBillStateCombo', 'prmProjectMainId', 'prmProblemTypeCombo', 'postDate', 'problemType', 'description', 'postPerson', 'proposalSolution', 'proposalDate', 'provider', 'remark', 'state', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },'departmentCodeDesc']
    }
);