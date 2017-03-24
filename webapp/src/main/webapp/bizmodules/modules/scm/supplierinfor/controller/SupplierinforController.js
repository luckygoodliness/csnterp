Ext.define("Supplierinfor.controller.SupplierinforController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Supplierinfor.view.SupplierinforView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'scmSupplierDto->supplierGenre', name: 'change', fn: 'supplierGenreChange'},
        {cid: 'scmSupplierDto->completeName', name: 'blur', fn: 'supplierNameValidate'},
        {cid: 'scmSupplierDto->simpleName', name: 'blur', fn: 'supplierNameValidate'},
        {cid: 'cancelEffect_edit', name: 'click', fn: 'cancelEffectEditBtn'},
        {cid: 'cancelEffect_query', name: 'click', fn: 'cancelEffectQueryBtn'},
        {cid: 'updateNcCode_edit', name: 'click', fn: 'updateNcCodeBtn'},
        {cid: 'supplierEvaluation', name: 'click', fn: 'supplierEvaluation'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.supplierinfor.dto.ScmSupplierDto',
    queryAction: 'supplierinfor-query',
    loadAction: 'supplierinfor-load',
    addAction: 'supplierinfor-add',
    modifyAction: 'supplierinfor-modify',
    deleteAction: 'supplierinfor-delete',
    exportXlsAction: "supplierinfor-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        me.controlFileButton();
        //me.doQuery();
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.view.getCmp("scmSupplierDto->taxTypes").sotValue("0");
        me.view.getCmp("scmSupplierDto->supplierGenre").sotValue("1");
        me.view.getCmp("scmSupplierDto->state").sotValue("0");
        me.setNcCodeButtonState(false);
        me.controlFileButton();
        me.view.getCmp("supplierEvaluation").setDisabled(true);
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.setNcCodeButtonState(false);
        me.controlFileButton();
        me.view.getCmp("supplierEvaluation").setDisabled(true);
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        //var supplierGenre=me.view.getCmp("scmSupplierDto->supplierGenre");
        //if (Scdp.ObjUtil.isEmpty(supplierGenre.gotValue())) {
        //    supplierGenre.sotValue("1");
        //}
        me.view.getCmp("supplierEvaluation").setDisabled(true);
        me.setNcCodeButtonState(false);
        me.controlFileButton();
    },
    beforeSave: function () {
        var me = this;
        me.view.getCmp("supplierEvaluation").setDisabled(false);
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        me.setNcCodeButtonState(true);
        me.controlEditButton();
        me.controlFileButton();
        me.view.getCmp("supplierEvaluation").setDisabled(false);
        if (Scdp.ObjUtil.isNotEmpty(retData.supplierCode)) {
            me.view.getCmp("scmSupplierDto->supplierCode").sotValue(retData.supplierCode);
        }
        //me.view.getCmp("query\x26edit").setActiveTab(me.view.getQueryPanel());
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        me.controlEditButton();
        me.controlFileButton();
        me.setNcCodeButtonState(true);
        me.fiveStat(view, "price");
        me.fiveStat(view, "business");
        me.fiveStat(view, "personQuality");
        me.fiveStat(view, "organizingCapability");
        me.fiveStat(view, "compliance");
        me.fiveStat(view, "securityManagement");
        me.fiveStat(view, "finalEstimate");
        me.fiveStat(view, "arrivalTime");
        me.fiveStat(view, "equipmentQuality");
        me.fiveStat(view, "service");
        me.fiveStat(view, "comprehensive");
        me.view.getCmp("supplierEvaluation").setDisabled(false);

    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        me.controlEditButton();
        me.controlFileButton();
        me.setNcCodeButtonState(true);
        me.view.getCmp("supplierEvaluation").setDisabled(false);
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
        //me.view.getCmp("query\x26edit").setActiveTab(me.view.getQueryPanel());
        me.setNcCodeButtonState(true);
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
    //文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "CLASSIFY_FOR_SCM_SUPPLIER";
        Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initFileUploadData);
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
    controlEditButton: function () {
        var role = Erp.Util.getCurrentUserRoleName();
        var isScmVp = false;
        var isKJ = false;
        if (Scdp.ObjUtil.isNotEmpty(role)) {
            isScmVp = role.ROLE.indexOf("供应链部供应商管理专员") > -1;
            //isKJ=role.ROLE.indexOf("会计") > -1;
        }
        var me = this;
        var view = me.view;
        if (isScmVp || isKJ) {
            view.getCmp("editPanel->modifyBtn").setDisabled(false);
        }
    },
    controlButton: function () {
        //var me = this;
        //var uistatus = me.view.getUIStatus();
        //if (uistatus == Scdp.Const.UI_INFO_STATUS_NEW
        //    || uistatus == Scdp.Const.UI_INFO_STATUS_NULL
        //    || uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY) {
        //    me.view.getCmp("cancelEffect_edit").setDisabled(false);
        //} else {
        //    me.view.getCmp("cancelEffect_edit").setDisabled(true);
        //}
    },
    refreshUIStatusBasedOnWorkFlow: function (returnData) {
        var me = this;
        me.controlEditButton();
    },
    cancelEffectEditBtn: function () {
        var me = this;
        var uuid = me.view.getCmp('scmSupplierDto->uuid').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据！");
            return false;
        }
        Scdp.MsgUtil.confirm("是否使该供方失效？", function (e) {
            if ("yes" == e) {
                var postData = {uuid: uuid};
                Scdp.doAction("supplierinfor-canceleffect", postData, function () {
                    //me.view.getCmp("scmContractDto->contractState").sotValue("5");
                    Scdp.MsgUtil.info("供方失效成功");
                });
            }
        })
    },
    cancelEffectQueryBtn: function () {
        var me = this;
        var selectedSubject = me.view.getResultPanel().getCurRecord();
        if (selectedSubject == null) {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        var uuid = selectedSubject.data.uuid;
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据！");
            return false;
        }
        Scdp.MsgUtil.confirm("是否使该供方失效？", function (e) {
            if ("yes" == e) {
                var postData = {uuid: uuid};
                Scdp.doAction("supplierinfor-canceleffect", postData, function () {
                    me.doQuery();
                    Scdp.MsgUtil.info("供方失效成功");
                });
            }
        })
    },

    updateNcCodeBtn: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("scmSupplierDto->uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据！");
            return false;
        }
        var callBack = function (subView) {
            if (!subView.validate()) {
                return false;
            }
            var ncCode = subView.getCmp("ncCode").gotValue();
            var postData = {uuid: uuid, ncCode: ncCode};
            Scdp.doAction("supplierinfor-updatenccode", postData, function (result) {
                win.close();
                if (result.success) {
                    Scdp.MsgUtil.info("NC编号设置成功!");
                    view.getCmp("scmSupplierDto->ncCode").sotValue(ncCode);
                }
            }, null, null, null, subView.getForm());
        };
        var form = Ext.widget("JForm", {
            height: 80,
            width: 400,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            items: [
                {
                    xtype: 'JText',
                    allowBlank: false,
                    fieldLabel: 'NC编号',
                    fullInfo: true,
                    cid: 'ncCode'
                }
            ]
        });
        var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', 'NC编号设置');
        form.getCmp("ncCode").focus();
    },
    //beforeWorkFlowCommit: function () {
    //    var me = this;
    //    var view = me.view;
    //    var uuid = view.getCmp("uuid").gotValue();
    //    var postData = {uuid: uuid};
    //    Scdp.doAction("supplierinfor-checkifhaveeffectbank", postData, function (result) {
    //        var errorMsg = "";
    //        if (result.result == "0") {
    //            errorMsg = "至少需要一个生效的银行账号！";
    //            Erp.Util.showLogView(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
    //        } else {
    //            me.executeTask();
    //        }
    //    });
    //},
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        if (!me.workFlowTaskId) {
            var errorMsg = "";
            var scmSupplierContactsItems = view.getCmp("scmSupplierContactsDto").store.data.items;
            var scmSupplierBankItems = view.getCmp("scmSupplierBankDto").store.data.items;
            var supplierGenre = view.getCmp("scmSupplierDto->supplierGenre").gotValue();
            if (Scdp.ObjUtil.isEmpty(scmSupplierContactsItems) && (supplierGenre == "0" || supplierGenre == "1")) {
                errorMsg += Erp.Const.BREAK_LINE + "至少需要一个联系人！";
            }
            var haveEffectBankId = false;
            if (Scdp.ObjUtil.isNotEmpty(scmSupplierBankItems)) {
                for (var i = 0; i < scmSupplierBankItems.length; i++) {
                    if ("1" == scmSupplierBankItems[i].get("isEffect")) {
                        haveEffectBankId = true;
                    }
                }
            }
            if (!haveEffectBankId) {
                errorMsg += Erp.Const.BREAK_LINE + "至少需要一个生效的银行账号！";
            }
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
    setNcCodeButtonState: function (flag) {
        var me = this;
        var updateNcCodeButton = me.view.getCmp("updateNcCode_edit");
        updateNcCodeButton.setDisabled(!flag);
    },
    supplierNameValidate: function () {
        var me = this;
        var completeName = me.view.getCmp('scmSupplierDto->completeName').gotValue();
        var simpleName = me.view.getCmp('scmSupplierDto->simpleName').gotValue();
        var supplierCode = me.view.getCmp('scmSupplierDto->supplierCode').gotValue();
        var postData = {completeName: completeName, simpleName: simpleName, supplierCode: supplierCode};
        Scdp.doAction("supplierinfor-supplierNameValidate", postData, function () {
        });
    },

    supplierEvaluation: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp('scmSupplierDto->uuid').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据！");
            return false;
        }
        var params = {items: ["市场价格", "经营商务", "质量（外协）", "组织能力", "服从管理", "安全管理", "结算", "到货时间", "设备质量", "服务", "综合评价"]}
        var callBack = function (result) {
            var price = result.scoreArray[0];
            var business = result.scoreArray[1];
            var personQuality = result.scoreArray[2];
            var organizingCapability = result.scoreArray[3];
            var compliance = result.scoreArray[4];
            var securityManagement = result.scoreArray[5];
            var finalEstimate = result.scoreArray[6];
            var arrivalTime = result.scoreArray[7];
            var equipmentQuality = result.scoreArray[8];
            var service = result.scoreArray[9];
            var comprehensive = result.scoreArray[10];
            var remark = result.suggest;
            if (comprehensive == "0") {
                Scdp.MsgUtil.info("综合评价不能为空！");
                return false;
            }
            var postData = {
                uuid: uuid,
                price: price,
                business: business,
                personQuality: personQuality,
                organizingCapability: organizingCapability,
                compliance: compliance,
                securityManagement: securityManagement,
                finalEstimate: finalEstimate,
                arrivalTime: arrivalTime,
                equipmentQuality: equipmentQuality,
                service: service,
                comprehensive: comprehensive,
                remark: remark,
                evaluateFrom: "0"
            };
            Scdp.doAction("supplierinfor-supplierevaluation", postData, function (result) {

            });
            me.loadItem(uuid);
        };

        Scdp.FiveStarWin.show(params, callBack, true, true)
    },
    fiveStat: function (view, name) {
        var scmSupplierEvaluationGrid = view.getCmp('scmSupplierEvaluationDto');
        var scmSupplierEvaluationGridColumns = scmSupplierEvaluationGrid.getColumnBydataIndex(name);
        scmSupplierEvaluationGridColumns.renderer = function (value, p, record) {
            if (value == '1') {
                return "<a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a>"
            } else if (value == '2') {
                return "<a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a>"
            } else if (value == '3') {
                return "<a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a>"
            } else if (value == '4') {
                return "<a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a>"
            } else if (value == '5') {
                return "<a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a><a class='star_red_icon'>&nbsp;&nbsp;&nbsp; </a>"
            }
        };
    },
    supplierGenreChange: function () {
        var me = this;
        var view = me.view;
        var supplierGenreCmp = view.getCmp("scmSupplierDto->supplierGenre");
        var supplierGenre = view.getCmp("scmSupplierDto->supplierGenre").gotValue();
        if(supplierGenre == "0"){
            Scdp.MsgUtil.info("合格供方只能通过合格供方审批产生！");
            supplierGenreCmp.sotValue("");
            return;
        }
        if (supplierGenre == "0" || supplierGenre == "1") {//如果供方类型为合格供方、普通供方，税务登记号与纳税人类型为必填
            view.getCmp("scmSupplierDto->taxRegistrationNo").allowBlank = false;
            view.getCmp("scmSupplierDto->taxTypes").allowBlank = false;
        } else {
            view.getCmp("scmSupplierDto->taxRegistrationNo").allowBlank = true;
            view.getCmp("scmSupplierDto->taxTypes").allowBlank = true;
        }
    }
})
;