/**
 * Created by lijx on 2016/8/18.
 */
Ext.define("Prmbilling.controller.PrmInvalidController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Prmbilling.view.PrmInvalidView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'prmBillingDto->repealUuid', name: 'change', fn: 'changeLoadFn'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmbilling.dto.PrmBillingDto',
    queryAction: 'prmbilling-query',
    loadAction: 'prmbilling-load',
    addAction: 'prmbilling-add',
    modifyAction: 'prmbilling-modify',
    deleteAction: 'prmbilling-delete',
    fillAction: 'prmbilling-fill',
    exportXlsAction: "prmbilling-exportxls",

    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        var uuid = me.view.getCmp("uuid").gotValue();
        me.invalidBtnFromBillingFn();
        me.controlFileButton();
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        Ext.Array.each(view.getCmp("toolbar").items.items, function (a) {
            a.setDisabled(true);
        });
        Ext.Array.each(me.view.getCmp("prmBillingDto").items.items, function (a) {
            if(a.cid!="dataSelect"){
                a.sotValue("");
                a.setReadOnly(true);
            }
        });
        me.controlFileButton();
        me.view.getCmp("prmBillingDto->remark").sotEditable(true);
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        Ext.Array.each(view.getCmp("toolbar").items.items, function (a) {
            a.setDisabled(true);
        });
        var repealUuid = me.view.getCmp("prmBillingDto->repealUuid");
        Scdp.doAction("prmbilling-getinvoiceno",{uuid:repealUuid.gotValue()},function(result){
            if(result.invoiceNo=="NULL"){
                me.view.getCmp("prmBillingDto->invoiceNo").sotEditable(true);
            }else{
                me.view.getCmp("prmBillingDto->invoiceNo").sotEditable(false);
            }
        },false,false);
        me.controlFileButton();
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        me.controlFileButton();
        //me.doQuery();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.controlFileButton();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("prmBillingDto").reset();
        me.view.getCmp("prmBillingDetailDto").clearData();
        me.controlFileButton();
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeBatchDel: function () {
        var me = this;
        return true;
    },
    afterBatchDel: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeExport: function () {
        var me = this;
        return true;
    },
    afterExport: function () {
        var me = this;
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var isPreProject = view.getCmp("prmBillingDto->isPreProject").gotValue();
        var prmContractId = view.getCmp("prmBillingDto->prmContractId").gotValue();
        var customerName = view.getCmp("prmBillingDto->customerName").gotValue();
        var errorMsg = "";
        if (isPreProject != "1") {
            if (Scdp.ObjUtil.isEmpty(prmContractId)) {
                errorMsg += Erp.Const.BREAK_LINE + "非预立项项目“合同名称”" + Erp.I18N.CAN_NOT_EMPTY;
            }
            if (Scdp.ObjUtil.isEmpty(customerName)) {
                errorMsg += Erp.Const.BREAK_LINE + "非预立项项目“业主单位”" + Erp.I18N.CAN_NOT_EMPTY;
            }
        }
        if (!me.workFlowTaskId) {
            if (Scdp.ObjUtil.isEmpty(errorMsg)) {
                return true;
            } else {
                Erp.Util.showLogView(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
                return false;
            }
        } else {
            return true;
        }
    },
    afterCompelteTask: function(a){
        var me = this;
       if(a.wf_fixed){
           var record = me.view.getCmp('resultPanel').getCurRecord();
           if(Scdp.ObjUtil.isNotEmpty(record)) {
               record.set("state", "3");
               record.set("cdmBillStateCombo", "作废");
           } else {
               var stateCmp = me.view.getCmp("prmBillingDto->state");
               stateCmp.sotValue("3");
           }
           var uuid = me.view.getCmp("prmBillingDto->uuid").gotValue();
           Scdp.doAction("prm_invalid-after-fixed",{businessKey:uuid},function(result){
               me.view.getCmp('prmBillingDto->state').sotValue("3");
           });
       }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('prmBillingDto->reqOffice').gotValue();
        return processDeptCode;
    },
    //文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "";
        var fileObjConfig = {};
        fileObjConfig.regex = /(.)+((\.pdf)|(\.doc)|(\.docx)|(\.xls)|(\.xlsx)|(\.ppt)|(\.pptx)|(\.txt)(\w)?)$/i;
        fileObjConfig.regexText = '支持的文件格式：pdf,doc,docx,xls,xlsx,ppt,pptx,txt';
        Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initFileUploadData, null, fileObjConfig);
    },
    //文件下载
    fileDownloadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    },
    //文件预览
    filePreviewBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFilePreview(grid);
    },
    //文件删除
    fileDeleteBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDelete(grid);
    },
    //文件按钮的禁用启用
    controlFileButton: function () {
        var me = this;
        var status = Ext.getCmp("editStatus").getValue();
        if (Scdp.ObjUtil.isEmpty(status)) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(true);
            me.view.getCmp("fileDelete").setDisabled(true);
            return false;
        }
        if (status == Scdp.I18N.VIEW_STATUS) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDelete").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(false);
        } else if (status == Scdp.I18N.MODIFY_STATUS || status == Scdp.I18N.ADD_STATUS) {
            me.view.getCmp("fileUpload").setDisabled(false);
            me.view.getCmp("fileDownload").setDisabled(false);
            me.view.getCmp("fileDelete").setDisabled(false);
        }
    },
    changeLoadFn:function(a,uuid){
        if(!Scdp.ObjUtil.isEmpty(uuid)){
            var me = this;
            Scdp.doAction("prmbilling-load",{uuid:uuid,dtoClass:this.dtoClass},function(result){
                var data = result.prmBillingDto
                //me.view.getCmp("prmBillingDto").sotValue(data);
                me.view.getCmp("prmBillingDto->repealUuid").sotValue(uuid);
                me.view.getCmp("prmBillingDto->billType").sotValue(1);
                me.view.getCmp("prmBillingDto->prmProjectMainId").sotValue(data.prmProjectMainId);
                me.view.getCmp("prmBillingDto->projectName").sotValue(data.projectName);
                me.view.getCmp("prmBillingDto->projectCode").sotValue(data.projectCode);
                me.view.getCmp("prmBillingDto->customerId").sotValue(data.customerId);
                me.view.getCmp("prmBillingDto->prmContractIdDesc").sotValue(data.prmContractIdDesc);
                me.view.getCmp("prmBillingDto->isPreProject").sotValue(data.isPreProject);
                me.view.getCmp("prmBillingDto->customerInvoiceId").sotValue(data.customerInvoiceId);
                me.view.getCmp("prmBillingDto->prmContractId").sotValue(data.prmContractId);
                me.view.getCmp("prmBillingDto->customerName").sotValue(data.customerName);
                me.view.getCmp("prmBillingDto->customerInvoiceName").sotValue(data.customerInvoiceName);
                me.view.getCmp("prmBillingDto->customerInvoiceNameEn").sotValue(data.customerInvoiceNameEn);
                me.view.getCmp("prmBillingDto->invoiceType").sotValue(data.invoiceType);
                me.view.getCmp("prmBillingDto->taxNo").sotValue(data.taxNo);
                me.view.getCmp("prmBillingDto->bankName").sotValue(data.bankName);
                me.view.getCmp("prmBillingDto->bankAccount").sotValue(data.bankAccount);
                me.view.getCmp("prmBillingDto->customerLocation").sotValue(data.customerLocation);
                me.view.getCmp("prmBillingDto->invoiceCurrencyName").sotValue(data.invoiceCurrencyName);
                me.view.getCmp("prmBillingDto->invoiceCurrency").sotValue(data.invoiceCurrency);
                me.view.getCmp("prmBillingDto->originalCurrencyName").sotValue(data.originalCurrencyName);
                me.view.getCmp("prmBillingDto->originalCurrency").sotValue(data.originalCurrency);
                me.view.getCmp("prmBillingDto->phone").sotValue(data.phone);
                me.view.getCmp("prmBillingDto->invoiceMoney").sotValue(data.invoiceMoney);
                me.view.getCmp("prmBillingDto->originalMoney").sotValue(data.originalMoney);
                me.view.getCmp("prmBillingDto->exchangeRate").sotValue(data.exchangeRate);
                me.view.getCmp("prmBillingDto->taxRateName").sotValue(data.taxRateName);
                me.view.getCmp("prmBillingDto->netMoney").sotValue(data.netMoney);
                me.view.getCmp("prmBillingDto->taxMoney").sotValue(data.taxMoney);
                me.view.getCmp("prmBillingDto->taxRate").sotValue(data.taxRate);
                me.view.getCmp("prmBillingDto->reqPersonName").sotValue(data.reqPersonName);
                me.view.getCmp("prmBillingDto->reqPerson").sotValue(data.reqPerson);
                me.view.getCmp("prmBillingDto->reqOfficeDesc").sotValue(data.reqOfficeDesc);
                me.view.getCmp("prmBillingDto->reqOffice").sotValue(data.reqOffice);
                me.view.getCmp("prmBillingDto->state").sotValue("0");//data.state);
                me.view.getCmp("prmBillingDto->locationType").sotValue(data.locationType);
                me.view.getCmp("prmBillingDto->paymentType").sotValue(data.paymentType);
                me.view.getCmp("prmBillingDto->expectReceiveDate").sotValue(data.expectReceiveDate);
                me.view.getCmp("prmBillingDto->invoiceNo").sotValue(data.invoiceNo);
                me.view.getCmp("prmBillingDto->invoiceDate").sotValue(data.invoiceDate);
                me.view.getCmp("prmBillingDto->remark").sotValue(data.remark);

                me.view.getCmp("prmBillingDetailDto").sotValue(result.prmBillingDto.prmBillingDetailDto);
                me.view.getCmp("cdmFileRelationDto").sotValue(result.prmBillingDto.cdmFileRelationDto);
                if(Scdp.ObjUtil.isEmpty(data.invoiceNo)){
                    me.view.getCmp("prmBillingDto->invoiceNo").sotEditable(true);
                }
            },false,false);
        }
    },
    invalidBtnFromBillingFn:function(){
        var me = this;
        if(Scdp.ObjUtil.isNotEmpty(me.actionParams) && me.actionParams.flag=="1"){
            me.doAdd();
            me.view.getCmp("prmBillingDto->projectName").setReadOnly(true)
            new Ext.util.DelayedTask(function(){
                me.view.getCmp("prmBillingDto->repealUuid").sotValue( me.actionParams.uuid);
            }).delay(1500)
        }
    }

});

//自定义方法
//去除空格方法開始
function Trim(value) {
    return value.toString().replace(/(^\s*)|(\s*$)/g, "");
}
function LTrim(value) {
    return value.toString().replace(/(^\s*)/g, "");
}
function RTrim(value) {
    return value.toString().replace(/(\s*$)/g, "");
}
//去除空格方法結束

//去除null方法开始
function isEmptyReturnZero(value) {
    if (Trim(value).length == 0) {
        return 0;
    }
    else {
        return Trim(value);
    }
}
function isZeroReturnEmpty(value) {
    if (value == 0) {
        return "";
    } else {
        return Trim(value);
    }
}
function isNullReturnEmpty(value) {
    if (value == null || value == "null") {
        return "";
    }
    else {
        return Trim(value);
    }
}
//去除null方法结束

//返回yyyy/MM/dd日期
function getDateForStandard(dateStr) {
    try {
        var date;
        if (isNullReturnEmpty(dateStr).length > 0) {
            date = new Date(dateStr);
        }
        else {
            date = new Date();
        }
        if (date.getMonth() < 9) {
            if (date.getDate() < 10) {
                return (date.getFullYear().toString() + "/" + "0" + (date.getMonth() + 1) + "/" + "0" + date.getDate()).toString();
            }
            else {
                return (date.getFullYear().toString() + "/" + "0" + (date.getMonth() + 1) + "/" + date.getDate()).toString();
            }
        }
        else {
            if (date.getDate() < 10) {
                return (date.getFullYear().toString() + "/" + (date.getMonth() + 1) + "/" + "0" + date.getDate()).toString();
            }
            else {
                return (date.getFullYear().toString() + "/" + (date.getMonth() + 1) + "/" + date.getDate()).toString();
            }
        }
    }
    catch (e) {
        return "";
    }
}