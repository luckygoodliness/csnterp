Ext.define("Prmprojectmainc.controller.PrmprojectmaincController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Prmprojectmainc.view.PrmprojectmaincView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: "editPanel->fileUpLoad", name: "click", fn: "doFileUpload"},
        {cid: "prmBudgetOutsourceCForm->amount", name: "blur", fn: "changeOutsourceAmount"},
        {cid: "prmBudgetOutsourceCForm->price", name: "blur", fn: "changeOutsourceAmount"},
        {cid: "prmBudgetPrincipalCForm->contractAmount", name: "blur", fn: "changePrincipalContractAmount"},
        {cid: "prmBudgetPrincipalCForm->contractPrice", name: "blur", fn: "changePrincipalContractAmount"},
        {cid: "prmBudgetPrincipalCForm->amount", name: "blur", fn: "changePrincipalAmount"},
        {cid: "prmBudgetPrincipalCForm->schemingPrice", name: "blur", fn: "changePrincipalAmount"},
        {cid: "editPanel->createProjectCodeBtn", name: "click", fn: "doCreateProjectCode"},
        {cid: "editPanel->downLoadTemplate", name: "click", fn: "downLoadTemplate"},
        {cid: "prmProjectMainCDto->taxType", name: "change", fn: "changeTaxType"},
        {cid: "prmProjectMainCDto->prmCodeType", name: "change", fn: "changePrmCodeType"},
        {cid: "prmProjectMainCDto->isPreProject", name: "change", fn: "changeIsPreFlag"},
        {cid: 'editPanel->fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'editPanel->fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'editPanel->filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'editPanel->fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'btnExamDate', name: 'click', fn: 'fnExamDate'},
        {cid: 'btnMark', name: 'click', fn: 'fnMark'},
        {cid: 'btnUnMark', name: 'click', fn: 'fnUnMark'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto',
    queryAction: 'prmprojectmainc-query',
    loadAction: 'prmprojectmainc-load',
    addAction: 'prmprojectmainc-add',
    modifyAction: 'prmprojectmainc-modify',
    deleteAction: 'prmprojectmainc-delete',
    exportXlsAction: "prmprojectmainc-exportxls",
    mandatoryFields: {
        prmProjectMainCDto: {
            desc: '基本信息',
            fields: {
                scheduledBeginDate: '计划开始时间', scheduledEndDate: '计划完成时间', projectShortName: '项目简称',
                prmCodeType: '代号类型'
            }
        },
        prmAssociatedUnitsCDto: {desc: '关联单位明细', fields: {associatedType: '关联单位类型', associatedUnitsName: '关联单位名称'}},
        prmMemberDetailPCDto: {
            desc: '项目人员',
            fields: {staffId: '成员名称', post: '项目角色', jobShare: '专业分工', enterDate: '进入项目日期', percent: '投入百分比'}
        },
        prmPayDetailPCDto: {
            desc: '开支计划明细',
            fields: {
                projectStage: '项目阶段',
                payContent: '关联单位名称',
                payContent: '开支内容',
                payMoney: '预计开支金额',
                beginDate: '开始时间',
                endDate: '结束时间'
            }
        },
        prmProgressDetailPCDto: {
            desc: '进度计划明细',
            fields: {projectStage: '项目阶段', schemingBeginDate: '计划开始时间', schemingEndDate: '计划结束时间'}
        },
        prmSquareDetailPCDto: {
            desc: '结算计划明细',
            fields: {
                schemingSquareDate: '计划结算时间',
                schemingSquareMoney: '计划结算金额',
                schemingSquareCost: '计划结算成本金额'
            }
        },
        prmReceiptsDetailPCDto: {
            desc: '收款计划明细',
            fields: {
                projectStage: '项目阶段',
                schemingReceiptsDate: '计划收款时间',
                schemingReceiptsMoney: '计划收款金额'
            }
        },
        prmQsPCDto: {
            desc: '质量安全计划明细', fields: {
                safePrincipal: '安全负责人',
                safeContact: '安全联系人',
                safeDocument: '安全体系文件',
                safeMeasure: '安全保障措施',
                qualityPrincipal: '质量负责人',
                qualityContact: '质量联系人',
                qualityDocument: '质量体系文件',
                qualityMeasure: '质量保障措施'
            }
        },
        //prmBudgetDetailCDto:{desc:'立项预算明细',fields:{budgetCode:'',contractMoney:'',jointDesignMoney:'',costControlMoney:'',}},
        prmBudgetOutsourceCDto: {
            desc: '外协明细',
            fields: {serialNumber: '序号', supplierCode: '名称', unit: '单位', amount: '数量', price: '单价', content: '主要施工内容'}
        },
        prmBudgetPrincipalCDto: {
            desc: '设备材料明细', fields: {
                serialNumber: '序号',
                equipmentName: '名称',
                equipmentModel: '型号规格',
                factory: '生产厂家',
                unit: '单位',
                amount: '计划数量',
                contractAmount: '合同数量',
                contractPrice: '合同单价',
                schemingPrice: '计划单价'
            }
        },
        prmBudgetAccessoryCDto: {
            desc: '辅助材料明细',
            fields: {serialNumber: '序号', accessoryName: '名称', accessoryModel: '型号规格', amount: '数量', price: '单价'}
        },
        prmBudgetRunCDto: {desc: '运行成本明细', fields: {unit: '单位', amount: '数量', price: '单价'}}
    },
    prmDetailType: "NEW",
    afterInit: function () {
        var me = this;
        me.initLabelStar();
        this.callParent(arguments);
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
            uploadData.push("new");
            var postData = {uploadMeta: uploadData};
            Scdp.doAction("requirement-header-fileupload", postData, function (result) {
                if (result.saveFlag) {
                    Scdp.MsgUtil.info(Scdp.I18N.DATA_NOT_CHANGE);
                    me.loadItem(result.projectMainCUuid);
                } else {
                    Erp.Util.showLogView(Erp.I18N.EXCEL_UPLOAD_FAILURE + Erp.Const.BREAK_LINE + result.errorMsgLog);
                }
                win.close();
            }, null, null, null, subView.getForm());
        }
        var win = Scdp.openNewWinByView(formView, callBack, 'temp_icon_16', Erp.I18N.FILE_UPLOAD, Erp.I18N.UPLOAD_FILE);
    },
    doCreateProjectCode: function () {
        var me = this;
        var view = me.view;
        var projectCodeCmp = view.getCmp("prmProjectMainCDto->projectCode");
        var projectNameCmp = view.getCmp("prmProjectMainCDto->projectName").gotValue();
        var contractorOfficeCmp = view.getCmp("prmProjectMainCDto->contractorOffice").oldValue;
        var uuid = view.getCmp("prmProjectMainCDto->uuid").gotValue();
        var prmCodeType = view.getCmp("prmProjectMainCDto->prmCodeType").gotValue();
        if (Scdp.ObjUtil.isEmpty(projectCodeCmp.gotValue())) {
            var callback = function (form) {
                if (!form.validate()) {
                    Scdp.MsgUtil.info(Erp.I18N.INFO_NOT_ENOUGH);
                    return false;
                }

                var postData = form.gotValue();
                postData.uuid = uuid;
                postData.step = "1";
                postData.projectName = projectNameCmp;
                postData.contractorOffice = contractorOfficeCmp;
                Scdp.doAction("prmprojectmainc-generate-projectcode", postData, function (result) {
                    if (result && result.projectCode) {
                        Scdp.MsgUtil.info(Erp.I18N.PROJECT_CODE_GENERATE_SUCCESS + result.projectCode);
                        projectCodeCmp.sotValue(result.projectCode);
                    }
                }, null, true);
                return true;
            };
            var form = Ext.create('Scdp.container.JForm', {
                layout: {
                    type: 'form',
                    align: 'center'
                },
                width: 300,
                height: 180,
                bodyPadding: '10 10 10 10',
                items: [
                    {
                        xtype: "JCombo",
                        fieldLabel: Erp.I18N.STAMP_TYPE,
                        cid: "stampType",
                        comboType: "scdp_fmcode",
                        codeType: 'STAMP_TYPE',
                        fullInfo: true,
                        displayDesc: false,
                        upperCase: true,
                        margin: '10 0 10 0',
                        allowBlank: false
                    },
                    {
                        xtype: "JCombo",
                        fieldLabel: Erp.I18N.PRM_CODE_TYPE,
                        cid: "prmCode",
                        comboType: "scdp_fmcode",
                        codeType: 'PRM_CODE_' + prmCodeType,
                        fullInfo: true,
                        displayDesc: false,
                        upperCase: true,
                        margin: '10 0 10 0',
                        allowBlank: false
                    },
                    {
                        xtype: "JText",
                        fieldLabel: "项目代号",
                        margin: '10 0 10 0',
                        cid: "projectCode"
                    },
                    {
                        xtype: "JHidden",
                        cid: "projectCodePreview"
                    },
                    {
                        xtype: "JPanel",
                        layout: 'hbox',
                        buttonAlign: "center",
                        items: [
                            {
                                xtype: 'JDisplay',
                                flex: 1
                            },
                            {
                                xtype: 'JButton',
                                cid: 'projectCodePreviewBtn',
                                text: '预览项目代号',
                                width: 100,
                                margin: '25 0 0 0',
                                handler: function (btn, e) {
                                    if (!form.validate()) {
                                        Scdp.MsgUtil.warn(Erp.I18N.INFO_NOT_ENOUGH);
                                        return;
                                    }
                                    var postData = form.gotValue();
                                    postData.uuid = uuid;
                                    postData.step = "0";
                                    Scdp.doAction("prmprojectmainc-generate-projectcode", postData, function (result) {
                                        if (result && result.projectCode) {
                                            form.getCmp("projectCode").sotValue(result.projectCode);
                                            form.getCmp("projectCodePreview").sotValue(result.projectCode);
                                        }
                                    }, null, true);
                                }
                            },
                            {
                                xtype: 'JDisplay',
                                flex: 1
                            }
                        ]
                    }
                ]
            });
            Scdp.openNewWinByView(form, callback);
        } else {
            var callback = function (form) {
                var projectCode = form.getCmp("projectCode").gotValue();
                if (Scdp.ObjUtil.isEmpty(projectCode)) {
                    Scdp.MsgUtil.info(Erp.I18N.INFO_NOT_ENOUGH);
                    return false;
                }

                var postData = {};
                postData.uuid = uuid;
                postData.projectCode = projectCode;
                postData.step = "2";
                postData.projectName = projectNameCmp;
                postData.contractorOffice = contractorOfficeCmp;
                Scdp.doAction("prmprojectmainc-generate-projectcode", postData, function (result) {
                    if (result && result.projectCode) {
                        Scdp.MsgUtil.info(Erp.I18N.PROJECT_CODE_UPDATE_SUCCESS + result.projectCode);
                        projectCodeCmp.sotValue(result.projectCode);
                    }
                }, null, true);
                return true;
            };
            var form = Ext.create('Scdp.container.JForm', {
                layout: 'form',
                width: 300,
                height: 150,
                bodyPadding: '10 10 5 10',
                items: [
                    {
                        xtype: "JText",
                        fieldLabel: Erp.I18N.PROJECT_CODE,
                        id: "projectCode",
                        cid: "projectCode",
                        value: projectCodeCmp.gotValue()
                    }
                ]
            });
            Scdp.openNewWinByView(form, callback);
        }
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmProjectMainCDto->detailType").sotValue(me.prmDetailType);
        view.getCmp("prmProjectMainCDto->projectMoney").sotValue(0);
        view.getCmp("prmProjectMainCDto->state").sotValue("0");
        view.getCmp("prmProjectMainCDto->contractorOffice").putValue(Erp.Util.getCurrentDeptCode());

        var postData = {};
        //var postData = {subjectType: "1"};
        Scdp.doAction("prm-project-mainc-new-after-add", postData, function (result) {
            if (result) {
                view.getCmp("prmProjectMainCDto->projectManagerRoleUuid").sotValue(result.projectManagerRoleUuid);

                if (result.subjectCodes) {
                    var budgetDetailGrid = view.getCmp("prmBudgetDetailCDto");
                    var budgetRunGrid = view.getCmp("prmBudgetRunCDto");
                    var seqNo1 = 0;
                    var seqNo2 = 0;
                    Ext.Array.each(result.subjectCodes, function (item) {
                        if ("1" == item.subjectType) {
                            seqNo1 += 1;
                            var rowData = {};
                            rowData.serialNumber = seqNo1 + "";
                            rowData.budgetRate = item.rate;
                            rowData.seqNo = seqNo1;
                            rowData.budgetCode = item.subjectNo;
                            rowData.subjectComment = item.descp;
                            budgetDetailGrid.addRowItem(rowData, false);
                        } else if ("2" == item.subjectType) {
                            seqNo2 += 1;
                            var rowData = {};
                            rowData.unit = Erp.I18N.ITEM;
                            rowData.amount = 1;
                            rowData.serialNumber = seqNo2 + "";
                            rowData.seqNo = seqNo2;
                            rowData.price = 0;
                            rowData.totalValue = 0;
                            rowData.financialSubjectCode = item.subjectNo;
                            budgetRunGrid.addRowItem(rowData, false);
                        }
                    });
                }
            }
        });
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmProjectMainCDto->state").sotValue("0");
        view.getCmp("prmProjectMainCDto->projectSerialNo").sotValue("");
        view.getCmp("prmProjectMainCDto->establishDate").sotValue(null);
        view.getCmp("prmProjectMainCDto->projectCode").sotValue("");
        view.getCmp("prmProjectMainCDto->prmProjectMainId").sotValue("");
    },
    beforeModify: function () {
        var me = this;
        me.view.getCmp("editPanel->btnUnMark").setDisabled(true);
        me.view.getCmp("editPanel->btnMark").setDisabled(true);
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        if (Erp.ArrayUtil.anyContains(me.workFlowFormData, "wf_erp_enable_fields=")) {
            var clientParam = null;
            for (var i = 0; i < me.workFlowFormData.length; i++) {
                if (me.workFlowFormData[i].indexOf("wf_erp_enable_fields=") > -1) {
                    clientParam = me.workFlowFormData[i];
                    break;
                }
            }
            if (Scdp.ObjUtil.isNotEmpty(clientParam)) {
                var params = clientParam.split(";");
                for (var i = 0; i < params.length; i++) {
                    if (params[i].indexOf("wf_erp_enable_fields=") > -1) {
                        var enableFields = Scdp.StrUtil.getLastSplit(params[i], "=").split("&");
                        view.sotEditable(false);
                        Ext.Array.each(enableFields, function (item) {
                            var cmp = view.getCmp(item);
                            if (cmp.sotEditable) {
                                cmp.sotEditable(true);
                            } else if (cmp.setDisabled) {
                                cmp.setDisabled(false);
                            }
                        });
                        break;
                    }
                }
            }
        }
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        me.view.getCmp("editPanel->btnUnMark").setDisabled(false);
        me.view.getCmp("editPanel->btnMark").setDisabled(false);
        //return me.validateHeader() && me.validateDetailAmount() && me.validateDetailDate() && me.validateSerialNumber();
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        view.afterChangeStatus();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.view.getCmp("editPanel->btnUnMark").setDisabled(false);
        me.view.getCmp("editPanel->btnMark").setDisabled(false);
        me.callParent(arguments);
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
    },
    beforeBatchDel: function () {
        var me = this;
        return true;
    },
    afterBatchDel: function () {
        var me = this;
    },
    beforeExport: function () {
        var me = this;
        return true;
    },
    afterExport: function () {
        var me = this;
    },
    validateDetailSize: function () {
        var errorMsg = "";
        var me = this;
        var view = me.view;
        var associatedGrid = view.getCmp("prmAssociatedUnitsCDto");
        var memberGrid = view.getCmp("prmMemberDetailPCDto");
        var payGrid = view.getCmp("prmPayDetailPCDto");
        var progressGrid = view.getCmp("prmProgressDetailPCDto");
        var squareGrid = view.getCmp("prmSquareDetailPCDto");
        var receiptsGrid = view.getCmp("prmReceiptsDetailPCDto");
        var qsPGrid = view.getCmp("prmQsPCDto");
        var detailGrid = view.getCmp("prmBudgetDetailCDto");
        var outsourceGrid = view.getCmp("prmBudgetOutsourceCDto");
        var principalGrid = view.getCmp("prmBudgetPrincipalCDto");
        var accessoryGrid = view.getCmp("prmBudgetAccessoryCDto");
        var runGrid = view.getCmp("prmBudgetRunCDto");

        //if (associatedGrid.store.getCount() == 0) {
        //    errorMsg +=  Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_ASSOCIATED_DETAIL_PLAN_EMPTY;
        //}
        if (memberGrid.store.getCount() == 0) {
            errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_MEMBER_DETAIL_PLAN_EMPTY;
        } else {
            var i = 0;
            var projectManagerRoleUuid = view.getCmp("prmProjectMainCDto->projectManagerRoleUuid").gotValue();
            Ext.Array.each(memberGrid.store.data.items, function (item) {
                if (item.get("post") == projectManagerRoleUuid) {
                    i++;
                }
            });
            if (i > 1) {
                errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_MEMBER_DETAIL_PLAN_MANAGER_MORE;
            } else if (i == 0) {
                errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_MEMBER_DETAIL_PLAN_MANAGER_EMPTY;
            }
        }
        if (payGrid.store.getCount() == 0) {
            errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_PAY_DETAIL_PLAN_EMPTY;
        }
        if (progressGrid.store.getCount() == 0) {
            errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_PROGRESS_DETAIL_PLAN_EMPTY;
        }
        if (squareGrid.store.getCount() == 0) {
            errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_SQUARE_DETAIL_PLAN_EMPTY;
        }
        if (receiptsGrid.store.getCount() == 0) {
            errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_RECEIPTS_DETAIL_PLAN_EMPTY;
        }
        if (qsPGrid.store.getCount() == 0) {
            errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_QSP_DETAIL_PLAN_EMPTY;
        }

        if (detailGrid.store.getCount() == 0) {
            errorMsg += Erp.I18N.MSG_PRM_BUDGET_DETAIL_EMPTY;
        }
        /*else {
         var valid = false;
         Ext.Array.each(detailGrid.store.data.items, function (item) {
         if (item.get("budgetCode") == 'HAND_MONEY') {
         if (Scdp.ObjUtil.isNotEmpty(item.get("contractMoney"))) {
         valid = true;
         }
         }
         });
         if (!valid) {
         errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.BUDGET_DETAIL_HANDMONEY_CONTRACT_MONEY;
         }
         }*/
        //if (outsourceGrid.store.getCount() == 0) {
        //    errorMsg +=  Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_BUDGET_OUTSOURCE_EMPTY;
        //}
        //if (principalGrid.store.getCount() == 0) {
        //    errorMsg +=  Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_BUDGET_PRINCIPAL_EMPTY;
        //}
        //if (accessoryGrid.store.getCount() == 0) {
        //    errorMsg +=  Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_BUDGET_ACCESSORY_EMPTY;
        //}
        if (runGrid.store.getCount() == 0) {
            errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MSG_PRM_BUDGET_RUN_EMPTY;
        }

        return errorMsg;
    },
    validateContract: function () {
        var me = this;
        var view = me.view;
        var errorMsg = "";
        var isPreProject = view.getCmp("prmProjectMainCDto->isPreProject").gotValue();
        var contractDetailGrid = view.getCmp("prmContractDetailCDto");
        var prmBudgetDetailGrid = view.getCmp("prmBudgetDetailCDto");
        if (!isPreProject && !view.isBusinessTaxContract()) {
            if (contractDetailGrid.getStore().getCount() == 0) {
                errorMsg = Erp.Const.BREAK_LINE + "合同不能为空！";
            }
        }
        //计算合同表合同合计与立项预算总表的合同合计金额，确认是否一致
        var contractDetailGridSum = 0;
        var contractDetailGridLength = contractDetailGrid.store.data.items.length;
        for (var i = 0; i < contractDetailGridLength; i++) {
            var contractNowMoney = contractDetailGrid.store.data.items[i].get("contractNowMoney");
            contractDetailGridSum = Erp.MathUtil.plusNumber(contractDetailGridSum, contractNowMoney);
        }
        var contractTotalContractMoney = 0;
        var prmBudgetDetailGridLength = prmBudgetDetailGrid.store.data.items.length;
        for (var i = 0; i < prmBudgetDetailGridLength; i++) {
            var budgetCode = prmBudgetDetailGrid.store.data.items[i].get("budgetCode");
            if (budgetCode === "CONTRACT_TOTAL") {
                contractTotalContractMoney = prmBudgetDetailGrid.store.data.items[i].get("contractMoney")
                if (Scdp.ObjUtil.isEmpty(contractTotalContractMoney)) {
                    contractTotalContractMoney = 0;
                }
                break;
            }
        }
        if (Erp.MathUtil.compare(contractTotalContractMoney, contractDetailGridSum) != 0) {
            errorMsg += Erp.Const.BREAK_LINE + "预算明细中的合同合计与项目合同的签约额不相同！";
        }
        return errorMsg;
    },
    validateHeader: function () {
        var errorMsg = "";
        var me = this;
        var view = me.view;

        var beginDate = view.getCmp("prmProjectMainCDto->scheduledBeginDate").gotValue();
        var endDate = view.getCmp("prmProjectMainCDto->scheduledEndDate").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(beginDate) && Scdp.ObjUtil.isNotEmpty(endDate)) {
            if (beginDate > endDate) {
                errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.PROJECTMAIN_C_SCHEDULE_DATE_ERROR;
            }
        }
        var isPreProject = view.getCmp("prmProjectMainCDto->isPreProject").gotValue();
        if (isPreProject) {
            var fundsExplanation = view.getCmp("prmProjectMainCDto->fundsExplanation").gotValue();
            if (Scdp.ObjUtil.isEmpty(fundsExplanation)) {
                errorMsg += Erp.Const.BREAK_LINE + "预立项项目的立项经费说明不能为空！";
            }
        }
        return errorMsg;
    },
    validateSerialNumber: function () {
        var errorMsg = "";
        var me = this;
        var view = me.view;

        var principalStore = view.getCmp("prmBudgetPrincipalCDto").getStore();
        var accessoryStore = view.getCmp("prmBudgetAccessoryCDto").getStore();
        var outsourceStore = view.getCmp("prmBudgetOutsourceCDto").getStore();

        //主材序列号
        var serialNos = [];
        for (var i = 0; i < principalStore.data.items.length; i++) {
            var sno = principalStore.data.items[i].get("serialNumber");
            if (Scdp.ObjUtil.isEmpty(sno)) {
                continue;
            }
            if (Ext.Array.contains(serialNos, sno)) {
                errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.BUDGET_PRINCIPAL_SERIAL_NUMBER_DUL_ERROR_FIRST + (i + 1) + Erp.I18N.BUDGET_SERIAL_NUMBER_DUL_ERROR_SECOND;
                break;
            } else {
                serialNos.push(sno);
            }
        }

        //辅材序列号
        serialNos = [];
        for (var i = 0; i < accessoryStore.data.items.length; i++) {
            var sno = accessoryStore.data.items[i].get("serialNumber");
            if (Scdp.ObjUtil.isEmpty(sno)) {
                continue;
            }
            if (Ext.Array.contains(serialNos, sno)) {
                errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.BUDGET_ACCESSORY_SERIAL_NUMBER_DUL_ERROR_FIRST + (i + 1) + Erp.I18N.BUDGET_SERIAL_NUMBER_DUL_ERROR_SECOND;
            } else {
                serialNos.push(sno);
            }
        }

        //外协序列号
        serialNos = [];
        for (var i = 0; i < outsourceStore.data.items.length; i++) {
            var sno = outsourceStore.data.items[i].get("serialNumber");
            if (Scdp.ObjUtil.isEmpty(sno)) {
                continue;
            }
            if (Ext.Array.contains(serialNos, sno)) {
                errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.BUDGET_OUTSOURCE_SERIAL_NUMBER_DUL_ERROR_FIRST + (i + 1) + Erp.I18N.BUDGET_SERIAL_NUMBER_DUL_ERROR_SECOND;
            } else {
                serialNos.push(sno);
            }
        }

        return errorMsg;
    },
    validateDetailAmount: function () {
        var me = this;
        return (me.prmDetailType == 'NEW' ? me.validatePayDetailAmount() : '')
            + me.validateSquareDetailAmount() + me.validateReceiptsDetailAmount();
    },
    validateDetailDate: function () {
        var me = this;
        return me.validateMemberDate() + me.validatePayDate() + me.validateProgressDate();
    },
    validatePayDetailAmount: function () {
        var errorMsg = "";
        var me = this;
        var view = me.view;
        var payItems = view.getCmp("prmPayDetailPCDto").store.data.items;

        var headerAmount = view.getCmp("prmProjectMainCDto->costControlMoney").gotValue();
        if (Scdp.ObjUtil.isEmpty(headerAmount)) {
            headerAmount = 0;
        }
        var totalAmount = 0;
        for (var i = 0; i < payItems.length; i++) {
            var amount = payItems[i].get("payMoney");
            if (Scdp.ObjUtil.isNotEmpty(amount)) {
                totalAmount = Erp.MathUtil.plusNumber(totalAmount, amount);
            }
        }
        if (totalAmount > headerAmount) {
            errorMsg = Erp.Const.BREAK_LINE + Erp.I18N.PROJECT_MAIN_COST_CONTROL_MONEY_ERROR;
        }
        return errorMsg;
    },
    validateSquareDetailAmount: function () {
        var errorMsg = "";
        var me = this;
        var view = me.view;
        var payItems = view.getCmp("prmSquareDetailPCDto").store.data.items;

        var headerAmount = view.getCmp("prmProjectMainCDto->projectMoney").gotValue();
        if (Scdp.ObjUtil.isEmpty(headerAmount)) {
            headerAmount = 0;
        }
        var totalAmount = 0;
        for (var i = 0; i < payItems.length; i++) {
            var amount = payItems[i].get("schemingSquareMoney");
            if (Scdp.ObjUtil.isNotEmpty(amount)) {
                totalAmount = Erp.MathUtil.plusNumber(totalAmount, amount);
            }
        }
        if (totalAmount != headerAmount) {
            errorMsg = Erp.Const.BREAK_LINE + Erp.I18N.PROJECT_MAIN_PROJECT_MONEY_SQUARE_ERROR;
        }
        return errorMsg;
    },
    validateReceiptsDetailAmount: function () {
        var errorMsg = "";
        var me = this;
        var view = me.view;
        var payItems = view.getCmp("prmReceiptsDetailPCDto").store.data.items;

        var headerAmount = view.getCmp("prmProjectMainCDto->projectMoney").gotValue();
        if (Scdp.ObjUtil.isEmpty(headerAmount)) {
            headerAmount = 0;
        }
        var totalAmount = 0;
        for (var i = 0; i < payItems.length; i++) {
            var amount = payItems[i].get("schemingReceiptsMoney");
            if (Scdp.ObjUtil.isNotEmpty(amount)) {
                totalAmount = Erp.MathUtil.plusNumber(totalAmount, amount);
            }
        }
        if (totalAmount != headerAmount) {
            errorMsg = Erp.Const.BREAK_LINE + Erp.I18N.PROJECT_MAIN_PROJECT_MONEY_RECEIPTS_ERROR;
        }
        return errorMsg;
    },
    validateMemberDate: function () {
        var errorMsg = "";
        var me = this;
        var view = me.view;

        var memberGrid = view.getCmp("prmMemberDetailPCDto");

        var memberItems = memberGrid.store.data.items;
        for (var i = 0; i < memberItems.length; i++) {
            var record = memberItems[i];
            var enterDate = record.get("enterDate");
            var outDate = record.get("outDate");
            if (Scdp.ObjUtil.isNotEmpty(enterDate) && Scdp.ObjUtil.isNotEmpty(outDate)) {
                if (enterDate > outDate) {
                    errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.MEMBER_DETAIL_ENTERDATE_OUTDATE_ERROR_FIRST + (i + 1) + Erp.I18N.MEMBER_DETAIL_ENTERDATE_OUTDATE_ERROR_SECOND;
                }
            }
        }

        return errorMsg;
    },
    validatePayDate: function () {
        var errorMsg = "";
        var me = this;
        var view = me.view;

        var payGrid = view.getCmp("prmPayDetailPCDto");
        var payItems = payGrid.store.data.items;
        for (var i = 0; i < payItems.length; i++) {
            var record = payItems[i];
            var beginDate = record.get("beginDate");
            var endDate = record.get("endDate");
            if (Scdp.ObjUtil.isNotEmpty(beginDate) && Scdp.ObjUtil.isNotEmpty(endDate)) {
                if (beginDate > endDate) {
                    errorMsg = Erp.Const.BREAK_LINE + Erp.I18N.PAY_DETAIL_BEGINDATE_ENDDATE_ERROR_FIRST + (i + 1) + Erp.I18N.PAY_DETAIL_BEGINDATE_ENDDATE_ERROR_SECOND;
                }
            }
        }

        return errorMsg;
    },
    validateProgressDate: function () {
        var errorMsg = "";
        var me = this;
        var view = me.view;

        var progressGrid = view.getCmp("prmProgressDetailPCDto");
        var progressItems = progressGrid.store.data.items;
        var mainBeginDate = view.getCmp("prmProjectMainCDto->scheduledBeginDate").gotValue();
        var mainEndDate = view.getCmp("prmProjectMainCDto->scheduledEndDate").gotValue();
        for (var i = 0; i < progressItems.length; i++) {
            var record = progressItems[i];
            var estBeginDate = record.get("schemingBeginDate");
            var estEndDate = record.get("schemingEndDate");
            var actBeginDate = record.get("actualBeginDate");
            var actEndDate = record.get("actualEndDate");
            if (Scdp.ObjUtil.isNotEmpty(estBeginDate) && Scdp.ObjUtil.isNotEmpty(estEndDate)) {
                if (estBeginDate > estEndDate) {
                    errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.PROGRESS_DETAIL_BEGINDATE_ENDDATE_ERROR_FIRST + (i + 1) + Erp.I18N.PROGRESS_DETAIL_BEGINDATE_ENDDATE_ERROR;
                }
            }
            //取消实际开始时间和实际结束时间的校验
//            if (Scdp.ObjUtil.isNotEmpty(actBeginDate) && Scdp.ObjUtil.isNotEmpty(actEndDate)) {
//                if (actBeginDate > actEndDate) {
//                    errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.PROGRESS_DETAIL_BEGINDATE_ENDDATE_ERROR_FIRST + (i + 1) + Erp.I18N.PROGRESS_DETAIL_BEGINDATE_ENDDATE_ACTUAL_ERROR;
//                }
//            }
            if (Scdp.ObjUtil.isNotEmpty(estBeginDate) && Scdp.ObjUtil.isNotEmpty(mainBeginDate)) {
                if (mainBeginDate > estBeginDate) {
                    errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.PROGRESS_DETAIL_BEGINDATE_ENDDATE_ERROR_FIRST + (i + 1) + Erp.I18N.PROGRESS_DETAIL_BEGINDATE_ERROR;
                }
            }
            if (Scdp.ObjUtil.isNotEmpty(estEndDate) && Scdp.ObjUtil.isNotEmpty(mainEndDate)) {
                if (mainEndDate < estEndDate) {
                    errorMsg += Erp.Const.BREAK_LINE + Erp.I18N.PROGRESS_DETAIL_BEGINDATE_ENDDATE_ERROR_FIRST + (i + 1) + Erp.I18N.PROGRESS_DETAIL_ENDDATE_ERROR;
                }
            }
        }
        return errorMsg;
    },
    changePrincipalContractAmount: function (obj, event) {
        if (!obj || !obj.isChanged()) {
            return;
        }
        var me = this;
        var view = me.view;
        var grid = view.getCmp("prmBudgetPrincipalCDto");
        var form = view.getCmp("prmBudgetPrincipalCForm");
        var record = grid.getCurRecord();

        var contractAmount = form.getCmp("contractAmount").gotValue();
        var contractPrice = form.getCmp("contractPrice").gotValue();

        var contractTotalValue = Erp.MathUtil.multiNumber(contractAmount, contractPrice);
        form.getCmp("contractTotalValue").sotValue(contractTotalValue);
        record.set("contractTotalValue", contractTotalValue);

        var grossProfit = Erp.MathUtil.minusNumber(contractTotalValue, form.getCmp("schemingTotalValue").gotValue());
        form.getCmp("schemingGrossProfit").sotValue(grossProfit);
        record.set("schemingGrossProfit", grossProfit);
    },
    changePrincipalAmount: function (obj, event) {
        if (!obj || !obj.isChanged()) {
            return;
        }
        var me = this;
        var view = me.view;
        var grid = view.getCmp("prmBudgetPrincipalCDto");
        var form = view.getCmp("prmBudgetPrincipalCForm");
        var record = grid.getCurRecord();

        var schemingAmount = form.getCmp("amount").gotValue();
        var schemingPrice = form.getCmp("schemingPrice").gotValue();

        var schemingTotalValue = Erp.MathUtil.multiNumber(schemingAmount, schemingPrice);

        if (view.getHeader().getCmp("detailType").gotValue() == '*') {
            if (obj.cid == 'amount') {
                if (Erp.MathUtil.compare(schemingAmount, record.get("lockedAmount")) < 0) {
                    Scdp.MsgUtil.info("计划数量不能小于已经采购申请的数量!");
                    form.getCmp("amount").sotValue(obj.oldValue);
                    return;
                }
            }
            if (Erp.MathUtil.compare(schemingTotalValue, record.get("lockedMoney")) < 0) {
                Scdp.MsgUtil.info("计划金额不能小于已经采购申请的金额!");
                form.getCmp("schemingPrice").sotValue(obj.oldValue);
                return;
            }
        }

        var grossProfit = Erp.MathUtil.minusNumber(form.getCmp("contractTotalValue").gotValue(), schemingTotalValue);
        form.getCmp("schemingTotalValue").sotValue(schemingTotalValue);
        form.getCmp("schemingGrossProfit").sotValue(grossProfit);
        record.set("schemingTotalValue", schemingTotalValue);
        record.set("schemingGrossProfit", grossProfit);

        view.totalBudgetPrincipal();
    },
    changeOutsourceAmount: function (obj, event) {
        if (!obj || !obj.isChanged()) {
            return;
        }
        var me = this;
        var view = me.view;
        var grid = view.getCmp("prmBudgetOutsourceCDto");
        var form = view.getCmp("prmBudgetOutsourceCForm");
        var record = grid.getCurRecord();

        var amount = form.getCmp("amount").gotValue();
        var price = form.getCmp("price").gotValue();

        var totalValue = Erp.MathUtil.multiNumber(amount, price);
        if (view.getHeader().getCmp("detailType").gotValue() == '*') {
            if (obj.cid == 'amount') {
                if (Erp.MathUtil.compare(amount, record.get("lockedAmount")) < 0) {
                    Scdp.MsgUtil.info("数量不能小于已经采购申请的数量!");
                    form.getCmp("amount").sotValue(obj.oldValue);
                    return;
                }
            }
            if (Erp.MathUtil.compare(totalValue, record.get("lockedMoney")) < 0) {
                Scdp.MsgUtil.info("金额不能小于已经采购申请的金额!");
                form.getCmp("price").sotValue(obj.oldValue);
                return;
            }
        }
        form.getCmp("totalValue").sotValue(totalValue);
        record.set("totalValue", totalValue);

        view.totalBudgetOutsource();
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var state = view.getCmp("prmProjectMainCDto->state").gotValue();
        if (state != '1' && state != '2' && state != '4' && state != '9') {
            var errorMsg = me.validateContract() + me.validateHeader() + me.validateDetailSize() + me.validateEmpty() +
                me.validateDetailAmount() + me.validateDetailDate() + me.validateSerialNumber();
            if (Scdp.ObjUtil.isEmpty(errorMsg)) {
                var uuid = view.getCmp("prmProjectMainCDto->uuid").gotValue();
                var postData = {};
                postData.uuid = uuid;
                Scdp.doAction("prmprojectmainc-validate-before-submit", postData, function (result) {
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
                //Scdp.MsgUtil.info("提交失败！"+errorMsg,null,600,480);
                return false;
            }
        } else {
            return true;
        }
    },
    //提交最后一步完成之后
    afterCompelteTask: function (result) {
        var me = this;
        var view = me.view;
        this.callParent(result);
        var headerData = result.prmProjectMainCDto;
        if (Scdp.ObjUtil.isNotEmpty(headerData)) {
            if (result.state == '2') {
                view.getCmp("prmProjectMainCDto->projectSerialNo").sotValue(headerData.projectSerialNo);
                view.getCmp("prmProjectMainCDto->establishDate").sotValue(Erp.Util.parseScdpDate(headerData.establishDate));
            }
        }
    },
    refreshUIStatusBasedOnWorkFlow: function (result) {
        var me = this;
        var view = me.view;
        this.callParent(arguments);
        var wfData = result.wf_formdata;
        if (Scdp.Const.UI_INFO_STATUS_VIEW == view.getUIStatus()) {
            if (Erp.ArrayUtil.anyContains(wfData, "wf_erp_enable_modifyBtn=1")) {
                view.getCmp("editPanel->modifyBtn").setDisabled(false);
            }
        }
    },
    validateEmpty: function () {
        var me = this;
        var view = me.view;
        var errorMsg = "";

        for (var cid in me.mandatoryFields) {
            var obj = me.mandatoryFields[cid];
            var fields = obj["fields"];
            var cmp = view.getCmp(cid);
            if (cmp.isXType("JForm")) {
                Ext.Object.each(fields, function (field) {
                    var fieldCmp = cmp.getCmp(field);
                    if (fieldCmp) {
                        if (Scdp.ObjUtil.isEmpty(fieldCmp.gotValue())) {
                            errorMsg += Erp.Const.BREAK_LINE + obj.desc + ": " + fields[field] + " " + Erp.I18N.CAN_NOT_EMPTY;
                        }
                    }
                });
            } else if (cmp.isXType("JEGrid")) {
                var items = cmp.store.data.items;
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    Ext.Object.each(fields, function (field) {
                        if (Scdp.ObjUtil.isEmpty(item.get(field))) {
                            errorMsg += Erp.Const.BREAK_LINE + obj.desc + ": " + Erp.I18N.THE + (i + 1) + Erp.I18N.ROW + " " + fields[field] + " " + Erp.I18N.CAN_NOT_EMPTY;
                        }
                    });
                }
            }
        }

        return errorMsg;
    },
    downLoadTemplate: function () {
        var postData = {};
        postData.cdmFileType = 'PRM_PROJECT';
        postData.fileClassify = 'PRM_PROJECT_IMP_TEMPLATE';
        var result = Scdp.doAction("template_download", postData, null, null, false, false);
        if (Scdp.ObjUtil.isNotEmpty(result) && Scdp.ObjUtil.isNotEmpty(result.URL_LIST)) {
            Ext.each(result.URL_LIST, function (item) {
                window.open(item);
            })
        }
    },
    changeTaxType: function () {
        var me = this;
        var view = me.view;
        if (Scdp.Const.UI_INFO_STATUS_NEW == view.getUIStatus() || Scdp.Const.UI_INFO_STATUS_MODIFY == view.getUIStatus()) {
            if (view.isBusinessTaxContract()) {
                view.totalBudgetDetail();
            }
        }
    },
    changePrmCodeType: function () {
        var me = this;
        var view = me.view;
        var prmCodeType = view.getCmp('prmProjectMainCDto->prmCodeType').gotValue();
        if (Scdp.ObjUtil.isNotEmpty(prmCodeType)) {
            if (Scdp.Const.UI_INFO_STATUS_NEW == view.getUIStatus() || Scdp.Const.UI_INFO_STATUS_MODIFY == view.getUIStatus()) {
                if (!view.isBusinessTaxContract()) {
                    view.totalBudgetDetail();
                }
            }
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
//            view.getCmp('prmProjectMainCDto->taxType').sotEditable(true);
            view.getCmp('prmProjectMainCDto->prmCodeType').sotEditable(true);
        } else {
//            view.getCmp('prmProjectMainCDto->taxType').sotEditable(false);
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
        var fileClassify = "PRM_PROJECT_NEW";
        Erp.FileUtil.erpFileUpload(grid, fileClassify);
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
    initLabelStar: function () {
        var me = this;
        for (var cid in me.mandatoryFields) {
            var obj = me.mandatoryFields[cid];
            var fields = obj["fields"];
            var cmp = me.view.getCmp(cid);
            if (cmp.isXType("JForm")) {
                Ext.Object.each(fields, function (field) {
                    var fieldCmp = cmp.getCmp(field);
                    if (fieldCmp) {
                        me.addStarLabel(fieldCmp);
                    }
                });
            }
        }
    },
    //在标签后面增加红色必填标志
    addStarLabel: function (component) {
        if (component.allowBlank && Scdp.ObjUtil.isNotEmpty(component.fieldLabel) && component.fieldLabel.indexOf("*") == -1) {
            component.setFieldLabel(component.fieldLabel + '\x3cdiv style\x3d"color:red;font-size: 20px;display:inline;margin-left: 2px"\x3e*\x3c/div\x3e');
        }
    },
    fnExamDate: function () {
        var me = this;
        var examDate = me.view.getCmp("prmProjectMainCDto->examDate").gotValue();
        var uuid = me.view.getCmp("prmProjectMainCDto->uuid").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(uuid)) {
            var controller = Ext.create("PrmExamdateHistory.controller.PrmExamDateWinController");
            var callback = function (subView) {
                var form = subView.getCmp('PrmExamDateHistoryDto');
                if (!form.validate()) {
                    return false;
                }
                var postData = {'viewdata': {'prmExamDateHistoryDto': subView.getCmp('PrmExamDateHistoryDto').gotValue()},
                    'dtoClass': controller.dtoClass};
                Scdp.doAction('prmexampdatahistory-add', postData, function (retData) {
                    if (retData.success == true) {
                        me.loadItem(uuid);
                        Scdp.MsgUtil.info("操作成功！");
                    }
                });
                return true;
            };
            var win = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '考核时间修正', '确定');
            var childForm = win.down('JForm')
            childForm.getCmp('tableName').sotValue('com.csnt.scdp.bizmodules.entity.prm.PrmProjectMainC');
            childForm.getCmp('columnName').sotValue('examDate');
            childForm.getCmp('dataUuid').sotValue(uuid);
            childForm.getCmp('oldDate').sotValue(examDate);
        }
    },
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