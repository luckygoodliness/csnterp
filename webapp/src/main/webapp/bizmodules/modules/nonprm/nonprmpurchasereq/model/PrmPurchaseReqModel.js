Ext.define("Nonprmpurchasereq.model.PrmPurchaseReqModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'purchaseReqNo', 'bugdetId', 'subjectCode', 'state', 'remark', 'companyCode', 'hidNeedFlg', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'seqNo', 'officeId', 'remainAmount', 'remainPrice', 'isProject', 'departmentCodeDesc']
    }
);