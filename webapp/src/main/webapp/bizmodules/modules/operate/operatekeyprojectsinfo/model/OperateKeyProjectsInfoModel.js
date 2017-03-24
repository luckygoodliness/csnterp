Ext.define("Operatekeyprojectsinfo.model.OperateKeyProjectsInfoModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'projectMoney', 'companyCode', 'projectShowName', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'period', 'recordDate', 'projectName', {
            name: 'isKey',
            defaultValue: 0
        }, 'proprietorUnit', 'area', 'decisionMaker', 'projectProfile', 'bidTime', 'signedTime', 'competitor', 'projectOc', 'questions', 'plannedCa', 'operateStateCombo', 'operationSummary', 'contractStatus', 'remark', 'isWin', 'comBidUnit', 'comBidNumber', 'bidingDocStart', 'bidingDocEnd', 'bidingDocPrice', 'bidBond', 'eotm', 'bod', 'seqNo', 'fid', 'officeId', 'officeIdDesc']
    }
);