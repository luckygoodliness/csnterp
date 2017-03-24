Ext.define("Prmprojectmainc.controller.Prmprojectmainc2Controller", {
    extend: 'Prmprojectmainc.controller.PrmprojectmaincController',
    viewClass: 'Prmprojectmainc.view.Prmprojectmainc2View',
    uniqueValidateFields: [],
    prmDetailType: "*",
    extraEvents: [
        {cid: "editPanel->fileUpLoad", name: "click", fn: "doFileUpload"},
        {cid: "prmBudgetOutsourceCForm->amount", name: "blur", fn: "changeOutsourceAmount"},
        {cid: "prmBudgetOutsourceCForm->price", name: "blur", fn: "changeOutsourceAmount"},
        {cid: "prmBudgetPrincipalCForm->contractAmount", name: "blur", fn: "changePrincipalContractAmount"},
        {cid: "prmBudgetPrincipalCForm->contractPrice", name: "blur", fn: "changePrincipalContractAmount"},
        {cid: "prmBudgetPrincipalCForm->amount", name: "blur", fn: "changePrincipalAmount"},
        {cid: "prmBudgetPrincipalCForm->schemingPrice", name: "blur", fn: "changePrincipalAmount"},
        {cid: "prmProjectMainCDto->taxType", name: "change", fn: "changeTaxType"},
        {cid: "prmProjectMainCDto->prmCodeType", name: "change", fn: "changePrmCodeType"},
        {cid: "prmProjectMainCDto->isPreProject", name: "change", fn: "changeIsPreFlag"},
        {cid: "editPanel->showChangeChk", name: "change", fn: "showDifference"},
        {cid: 'editPanel->fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'editPanel->fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'editPanel->filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'editPanel->fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'btnExamDate', name: 'click', fn: 'fnExamDate'},
        {cid: 'btnMark', name: 'click', fn: 'fnMark'},
        {cid: 'btnUnMark', name: 'click', fn: 'fnUnMark'}
    ],
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
    },
    doFileUpload: function () {
        var me = this;
        var view = me.view;
        var formView = Ext.widget("JForm", {
            height: 150,
            width: 500,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            cid: 'uploadForm',
            items: [
                {
                    xtype: 'JFile',
                    fieldLabel: Erp.I18N.UPLOAD_FILE_NAME,
                    cid: 'uploadFile'
                }
            ]
        });
        var callBack = function (subView) {
            var uploadFile = subView.getCmp("uploadFile");
            var uploadData = [];
            if (Scdp.ObjUtil.isEmpty(uploadFile.gotValue())) {
                Scdp.MsgUtil.info("Please select file!");
                return;
            }
            var projectMainCUuid = view.getCmp("prmProjectMainCDto->uuid").value;
            uploadData.push(projectMainCUuid);
            uploadData.push("modify");
            var postData = {uploadMeta: uploadData};
            Scdp.doAction("requirement-header-fileupload", postData, function (result) {
                if (result.saveFlag) {
                    Scdp.MsgUtil.info(Scdp.I18N.DATA_NOT_CHANGE);
                } else {
                    Erp.Util.showLogView(Erp.I18N.EXCEL_UPLOAD_FAILURE + Erp.Const.BREAK_LINE + result.errorMsgLog);
                }
                win.close();
            }, null, null, null, subView.getForm());
        };
        var win = Scdp.openNewWinByView(formView, callBack, 'temp_icon_16', Erp.I18N.FILE_UPLOAD, Erp.I18N.UPLOAD_FILE);
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    doAdd: function () {
        var me = this;
        var view = me.view;
        var callBack = function (subView) {
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length >= 1) {
                var uuid = selectedRecords[0].get("uuid");
                var postData = {};
                postData.uuid = uuid;
                postData.prmDetailType = me.prmDetailType;
                Scdp.doAction("wrapper-projectmain-to-change", postData, function (result) {
                    view.setUIStatus(Scdp.Const.UI_INFO_STATUS_NEW);
                    view.getCmp("query\x26edit").setActiveTab(me.view.getEditPanel());
                    view.mainContractUuids = result.mainContractUuids;
                    view.sotValue(result, true);
                    view.calBudgetOutsource();
                    view.calBudgetPrincipal();
                    view.calBudgetAccessory();
                    view.calBudgetRun();
                    view.totalBudgetDetail();
                    var isPreProject = view.getCmp("prmProjectMainCDto->isPreProject").gotValue();
                    if (isPreProject === 1) {
                        Scdp.MsgUtil.confirm("是否进行预立项转立项变更？", function (btn) {
                            if (btn == 'yes') {
                                view.getCmp("prmProjectMainCDto->isPreProject").sotValue(0);
                            }
                        });
                    }
                }, null, true);
                return true;
            } else if (selectedRecords.length == 0) {
                Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
                return false;
            } else {
                return true;
            }
        };
        var queryController = Ext.create("Projectmain.controller.PickProjectmainQueryController");
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');
        var queryExtraParams = {detailType: me.prmDetailType};
        queryController.view.getConditionPanel().queryExtraParams = queryExtraParams;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        me.view.getCmp("editPanel->btnUnMark").setDisabled(false);
        me.view.getCmp("editPanel->btnMark").setDisabled(false);
        //var errorMsg = me.validateHeader() + me.validateDetailAmount() + me.validateDetailDate() + me.validateSerialNumber();
        //if (Scdp.ObjUtil.isEmpty(errorMsg)) {
        //    return true;
        //} else {
        //    Erp.Util.showLogView(Erp.I18N.BEFORE_SAVE_VALIDATE_FAILURE + errorMsg);
        //    return false;
        //}
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.view.mainContractUuids = null;
        if (arguments.length > 0) {
            var tmp = arguments[0][0];
            if (Scdp.ObjUtil.isNotEmpty(tmp)) {
                me.view.mainContractUuids = tmp.mainContractUuids;
            }
        }
        me.view.afterChangeUIStatus();
    },
    afterCancel: function () {
        var me = this;
        me.view.getCmp("editPanel->btnUnMark").setDisabled(false);
        me.view.getCmp("editPanel->btnMark").setDisabled(false);
        me.callParent(arguments);
    },
    beforeModify: function () {
        var me = this;
        me.view.getCmp("editPanel->btnUnMark").setDisabled(true);
        me.view.getCmp("editPanel->btnMark").setDisabled(true);
        return true;
    },
    afterModify: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var showChangeCmp = view.getCmp("editPanel->showChangeChk");
        if (showChangeCmp.gotValue()) {
            me.filterRevisedRecords(false);
            showChangeCmp.sotValue(0);
        }

        var state = view.getCmp("prmProjectMainCDto->state").gotValue();
        var uuid = view.getCmp("prmProjectMainCDto->uuid").gotValue();
        var prmProjectMainId = view.getCmp("prmProjectMainCDto->prmProjectMainId").gotValue();
        if (state != '1' && state != '2' && state != '4' && state != '9') {
            var errorMsg = me.validateContract() + me.validateHeader() + me.validateDetailSize() + me.validateEmpty() +
                me.validateDetailAmount() + me.validateDetailDate() + me.validateSerialNumber();
            if (Scdp.ObjUtil.isEmpty(errorMsg)) {
                var postData = {};
                postData.uuid = uuid;
                postData.prmProjectMainId = prmProjectMainId;
                Scdp.doAction("prmprojectmainc2-validate-before-submit", postData, function (result) {
                    if (result && result.root && result.root.length) {
                        var errorMsg = "";
                        Ext.Array.each(result.root, function (message) {
                            errorMsg += Erp.Const.BREAK_LINE + message;
                        });
                        Erp.Util.showLogView(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
                    } else {
                        me.executeTask();
                    }
                });
                return false;
            } else {
                Erp.Util.showLogView(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
                return false;
            }
        } else {
            return true;
        }
    },

    //编辑区复选框显示变化
    showDifference: function () {
        var me = this;
        var view = me.view;
        var isEnable = view.getCmp("editPanel->showChangeChk").gotValue();
        if (isEnable) {
            var loaded = view.getCmp("prmProjectMainCDto->changesInfoLoaded").gotValue();
            if (!loaded) {
                var postData = {};
                postData.uuid = view.getCmp("prmProjectMainCDto->uuid").gotValue();
                postData.prmProjectMainId = view.getCmp("prmProjectMainCDto->prmProjectMainId").gotValue();
                Scdp.doAction("prmprojectmainc-load-difference", postData, function (result) {
                    me.headerChangedValues = result.prmProjectMainCDto.headerChangedValues;
                    view.sotValue(result);
                    view.getCmp("prmProjectMainCDto->changesInfoLoaded").sotValue("loaded");
                    me.filterRevisedRecords(true);
                }, null, true);
            } else {
                me.filterRevisedRecords(true);
            }
        } else {
            me.filterRevisedRecords(false);
        }
    },
    filterRevisedRecords: function (isRevised) {
        var me = this;
        var view = me.view;
        var bindings = view.bindings;
        Ext.Array.each(bindings, function (binding) {
            var path = binding.split(".");
            if (path.length == 2) {
                var grid = view.getCmp(path[1]);
                grid.store.filterBy(function (record) {
                    return isRevised ? Scdp.ObjUtil.isNotEmpty(record.get("isRevised")) : Scdp.ObjUtil.isEmpty(record.get("isRevised"));
                });

                var bindCompArr = view.getCmp(grid.cid + ".bind", true);
                if (Scdp.ObjUtil.isNotEmpty(bindCompArr)) {
                    Ext.Array.each(bindCompArr, function (item) {
                        item.clearData();
                    });
                }
            }
        });

        var changedFields = view.getCmp("prmProjectMainCDto->changedFields").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(changedFields)) {
            var fields = changedFields.split('|');
            Ext.Array.each(fields, function (cid) {
                var cmp = view.getCmp("prmProjectMainCDto->" + cid);
                if (cmp && !cmp.hidden) {
                    if (isRevised) {
                        var tips = "";
                        if (me.headerChangedValues && me.headerChangedValues.hasOwnProperty(cid)) {
                            if (Scdp.ObjUtil.isNotEmpty(me.headerChangedValues[cid])) {
                                tips = me.headerChangedValues[cid];
                            } else {
                                tips = "空";
                            }
                        }
                        cmp.markInvalid(tips);
                    } else {
                        cmp.clearInvalid();
                    }
                }
            })
        }
    },
    changeIsPreFlag: function () {
        var me = this;
        var view = me.view;
        var isPreProject = view.getCmp('prmProjectMainCDto->isPreProject').gotValue();
        //有了合同，
        if (1 == isPreProject) {
            if (view.getCmp("prmContractDetailCDto").getStore().getCount() > 0) {
                Scdp.MsgUtil.info("该项目已经关联了合同，不能进行预立项！");
                view.getCmp('prmProjectMainCDto->isPreProject').sotValue(0);
                return;
            }
            view.getCmp('prmProjectMainCDto->taxType').sotEditable(true);
            view.getCmp('prmProjectMainCDto->prmCodeType').sotEditable(true);
        } else {
            view.getCmp('prmProjectMainCDto->taxType').sotEditable(false);
            view.getCmp('prmProjectMainCDto->prmCodeType').sotEditable(false);
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('prmProjectMainCDto->contractorOffice').gotValue();
        return processDeptCode;
    },
    //文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "PRM_PROJECT_CHANGE";
        Erp.FileUtil.erpFileUpload(grid, fileClassify);
    },
    fnExamDate: function () {
        var me = this;
        me.callParent(arguments);
    },
    btnMark: function () {
        var me = this;
        me.callParent(arguments);
    },
    btnUnMark: function () {
        var me = this;
        me.callParent(arguments);
    }
    ,
    //标记项目
    fnMark:function () {
        var me = this;
        me.fnIsMark("mark");
    },
    //取消标记项目
    fnUnMark:function () {
        var me = this;
        me.fnIsMark("unMark");
    },
    fnIsMark:function(markType){
        var me=this;
        var view=me.view;
        var dataState=view.getCmp("prmProjectMainCDto->state").gotValue();
        var uuid = view.getCmp("prmProjectMainCDto->uuid").gotValue();
        if(Scdp.ObjUtil.isNotEmpty(dataState)) {
            if (dataState === "1" || dataState === "9") {
                //外协预算
                var outsourceUuids="";
                var prmBudgetOutsourceCDtoGrid=me.view.getCmp("prmBudgetOutsourceCDto");
                var prmBudgetOutsourceCDtoGridSelection=prmBudgetOutsourceCDtoGrid.getSelectionModel().getSelection();
                if(prmBudgetOutsourceCDtoGridSelection.length>0){
                    for(var i =0;i<prmBudgetOutsourceCDtoGridSelection.length;i++){
                        outsourceUuids+=prmBudgetOutsourceCDtoGridSelection[i].data.uuid+",";
                    }
                }
                //设备材料
                var principalUuids="";
                var prmBudgetPrincipalCDtoGrid=me.view.getCmp("prmBudgetPrincipalCDto");
                var prmBudgetPrincipalCDtoGridSelection=prmBudgetPrincipalCDtoGrid.getSelectionModel().getSelection();
                if(prmBudgetPrincipalCDtoGridSelection.length>0){
                    for(var i =0;i<prmBudgetPrincipalCDtoGridSelection.length;i++){
                        principalUuids+=prmBudgetPrincipalCDtoGridSelection[i].data.uuid+",";
                    }
                }
                //辅助材料
                var accessoryUuids="";
                var prmBudgetAccessoryCDtoGrid=me.view.getCmp("prmBudgetAccessoryCDto");
                var prmBudgetAccessoryCDtoGridSelection=prmBudgetAccessoryCDtoGrid.getSelectionModel().getSelection();
                if(prmBudgetAccessoryCDtoGridSelection.length>0){
                    for(var i =0;i<prmBudgetAccessoryCDtoGridSelection.length;i++){
                        accessoryUuids+=prmBudgetAccessoryCDtoGridSelection[i].data.uuid+",";
                    }
                }



                var postData = {};
                postData.uuid = uuid;
                postData.outsourceUuids=outsourceUuids;
                postData.principalUuids=principalUuids;
                postData.accessoryUuids=accessoryUuids;
                postData.markType=markType;
                Scdp.doAction("prmprojectmainc-mark", postData, function (result) {
                    if (result) {
                        Scdp.MsgUtil.info("操作成功");
                    } else {
                        Scdp.MsgUtil.info("操作失败");
                    }
                });
            }else{
                Scdp.MsgUtil.info("非已提交状态，禁止标记.");
                return false;
            }
        }
    }

});