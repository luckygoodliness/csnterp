Ext.define('Prmprojectmainc.view.PrmprojectmaincView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmprojectmainc',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    //epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'prm-projectmainc-query-layout.xml',
    editLayoutFile: 'prm-projectmainc-edit-layout.xml',
    //extraLayoutFile: 'prmprojectmainc-extra-layout.xml',
    bindings: ['prmProjectMainCDto', 'prmProjectMainCDto.prmAssociatedUnitsCDto',
        'prmProjectMainCDto.prmMemberDetailPCDto', 'prmProjectMainCDto.prmPayDetailPCDto',
        'prmProjectMainCDto.prmProgressDetailPCDto', 'prmProjectMainCDto.prmSquareDetailPCDto',
        'prmProjectMainCDto.prmReceiptsDetailPCDto', 'prmProjectMainCDto.prmQsPCDto',
        'prmProjectMainCDto.prmBudgetDetailCDto', 'prmProjectMainCDto.prmBudgetOutsourceCDto',
        'prmProjectMainCDto.prmBudgetPrincipalCDto', 'prmProjectMainCDto.prmBudgetAccessoryCDto',
        'prmProjectMainCDto.prmBudgetRunCDto', 'prmProjectMainCDto.cdmFileRelationDto',
        'prmProjectMainCDto.prmContractDetailCDto'
    ],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Main',
    //税金（不含印花税），印花税，内部管理费
    taxFlexRateCodes: ['TAX_NO_STAMP', 'STAMP_TAX', 'INNER_MANAGEMENT'],
    vatFlexRateCodes: ['STAMP_TAX', 'INNER_MANAGEMENT'],
    //RUN 项目运行成本 ；TAX_NO_STAMP税金不含印花税； STAMP_TAX印花税
    costMoneyCodes: ['PROFILE', 'OUTSOURCE', 'PRINCIPAL', 'ACCESSORY', 'RUN', 'TAX_NO_STAMP', 'STAMP_TAX'],
    //总则，设备材料费，原合同暂定金，指定分包，项目技术服务费，项目不可预见费
    positiveCodes: ['PROFILE', 'PRINCIPAL', 'HAND_MONEY', 'SUBPACKAGE', 'TECHNICAL_FEE_H', 'CONTINGENCY_ALLOWANCE_H'],
    //原合同暂定金，指定分包，项目技术服务费，项目不可预见费
    negativeCodes: ['HAND_MONEY', 'SUBPACKAGE', 'TECHNICAL_FEE_H', 'CONTINGENCY_ALLOWANCE_H'],
    calVatCodes: ['OUTSOURCE', 'PRINCIPAL', 'ACCESSORY'],
    calExcludingVatCodes: ['OUTSOURCE', 'PRINCIPAL', 'ACCESSORY', 'RUN'],
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var view = this;
        view.initHeaderField();
        view.initEditToolbar();
        view.initTotalColumns();
        view.initPlanGrid();
        view.initBudgetGrid();
        view.initContractGrid();
        view.initGridColumnRender();

        var editToolbar = view.getEditToolbar();
        //标记按钮
        editToolbar.add({
            xtype: 'button',
            text: '标记项目',
            cid: 'btnMark',
            iconCls: 'temp_icon_16',
            disabled: false
        });
        //取消标记按钮
        editToolbar.add({
            xtype: 'button',
            text: '取消标记',
            cid: 'btnUnMark',
            iconCls: 'temp_icon_16',
            disabled: false
        });
    },
    initContractGrid: function () {
        var view = this;
        var contractDetailGrid = view.getCmp("prmContractDetailCDto");
        //override doAddRow
        contractDetailGrid.doAddRow = function () {
            var isPreProject = view.getCmp("prmProjectMainCDto->isPreProject").gotValue();
            if (1 == isPreProject) {
                Scdp.MsgUtil.info("预立项项目不能添加合同！");
                return;
            }
            var callBack = function (subView) {
                var grid = subView.getQueryPanel().getCmp("resultPanel");
                var selectedRecords = grid.getSelectionModel().getSelection();
                if (selectedRecords.length >= 1) {
                    var taxType = "";
                    var prmCodeType = "";
                    var isBusinessTax = 0;
                    var firstRecord = null;
                    var needDefault = false;
                    var isInnerContract = false;
                    var existedUuids = [];
                    var store = contractDetailGrid.getStore();
                    if (store.getCount() > 0) {
                        firstRecord = store.getAt(0);
                        Ext.Array.each(store.getRange(0, store.getCount() - 1), function (item) {
                            existedUuids.push(item.get("prmContractId"));
                        });
                    } else {
                        firstRecord = selectedRecords[0];
                        needDefault = true;
                    }
                    if (Scdp.ObjUtil.isNotEmpty(firstRecord.get("taxType"))) {
                        taxType = firstRecord.get("taxType");
                    }
                    if (Scdp.ObjUtil.isNotEmpty(firstRecord.get("prmCodeType"))) {
                        prmCodeType = firstRecord.get("prmCodeType");
                    }
                    if (Scdp.ObjUtil.isNotEmpty(firstRecord.get("isBusinessTax"))) {
                        isBusinessTax = firstRecord.get("isBusinessTax");
                    }
                    isInnerContract = Scdp.ObjUtil.isNotEmpty(firstRecord.get("innerPurchaseReqId"));

                    var items = [];
                    var valid = true;
                    var isMajorProject = 0;
                    //validate tax
                    for (var i = 0; i < selectedRecords.length; i++) {
                        var item = selectedRecords[i];
                        if (Ext.Array.contains(existedUuids, item.get("prmContractId"))) {
                            return;
                        }
                        var taxType_ = Scdp.ObjUtil.isNotEmpty(item.get("taxType")) ? item.get("taxType") : "";
                        var prmCodeType_ = Scdp.ObjUtil.isNotEmpty(item.get("prmCodeType")) ? item.get("prmCodeType") : "";
                        var isBusinessTax_ = Scdp.ObjUtil.isNotEmpty(item.get("isBusinessTax")) ? item.get("isBusinessTax") : 0;
                        var isInnerContract_ = Scdp.ObjUtil.isNotEmpty(item.get("innerPurchaseReqId"));
                        if ((taxType != taxType_) || (prmCodeType != prmCodeType_) || (isBusinessTax != isBusinessTax_)) {
                            valid = false;
                            Scdp.MsgUtil.warn("所选合同的“税款类别”或“代号类型”不一致!");
                            return;
                        } else if (isInnerContract != isInnerContract_) {
                            valid = false;
                            Scdp.MsgUtil.warn("内委合同和普通合同不能合并立项!");
                            return;
                        } else {
                            var obj = {};
                            obj.prmContractId = item.get("prmContractId");
                            obj.customerId = item.get("customerId");
                            obj.contractNowMoney = item.get("contractNowMoney");
                            obj.contractSignMoney = item.get("contractSignMoney");
                            obj.contractName = item.get("contractName");
                            obj.customerName = item.get("customerName");
                            obj.taxType = item.get("taxType");
                            obj.prmCodeType = item.get("prmCodeType");
                            obj.isBusinessTax = item.get("isBusinessTax");
                            obj.projectName = item.get("projectName");
                            obj.contractDuration = item.get("contractDuration");
                            obj.designerId = item.get("designerId");
                            obj.managementId = item.get("managementId");
                            obj.projectManager = item.get("projectManager");
                            obj.innerPurchaseReqId = item.get("innerPurchaseReqId");
                            items.push(obj);
                            if ("1" === item.get("isMajorProject") + '') {
                                isMajorProject = 1;
                            }
                        }
                    }
                    if (valid) {
                        //add items
                        contractDetailGrid.addRowItems(items, false);
                        if (1 === isMajorProject) {
                            view.getCmp("prmProjectMainCDto->isMajorProject").sotValue(1);
                        }
                        //set the default value
                        if (needDefault) {
                            var projectNameCmp = view.getCmp("prmProjectMainCDto->projectName");
                            if (Scdp.ObjUtil.isNotEmpty(firstRecord.get("projectName"))) {
                                projectNameCmp.sotValue(firstRecord.get("projectName"));
                            }
                            var projectManagerCmp = view.getCmp("prmProjectMainCDto->projectManager");
                            if (Scdp.ObjUtil.isNotEmpty(firstRecord.get("projectManager"))) {
                                projectManagerCmp.putValue(firstRecord.get("projectManager"));
                            }
                            var contractDurationCmp = view.getCmp("prmProjectMainCDto->contractDuration");
                            if (Scdp.ObjUtil.isNotEmpty(firstRecord.get("contractDuration"))) {
                                contractDurationCmp.sotValue(firstRecord.get("contractDuration"));
                            }
                            view.getCmp("prmProjectMainCDto->taxType").sotValue(firstRecord.get("taxType"));
                            view.getCmp("prmProjectMainCDto->prmCodeType").sotValue(firstRecord.get("prmCodeType"));
                            if (Scdp.ObjUtil.isNotEmpty(firstRecord.get("designerId"))) {
                                view.getCmp("prmProjectMainCDto->contractDesignerId").sotValue(firstRecord.get("designerId"));
                            }
                            if (Scdp.ObjUtil.isNotEmpty(firstRecord.get("managementId"))) {
                                view.getCmp("prmProjectMainCDto->contractManagementId").sotValue(firstRecord.get("managementId"));
                            }
                        }
                        return true;
                    }
                } else {
                    Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
                    return false;
                }
            };
            var queryController = Ext.create("Contract.controller.ContractQueryController");
            Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', null, null, null, 'MULTI');

            var mainCUuid = view.getCmp("prmProjectMainCDto->uuid").gotValue();
            var mainUuid = view.getCmp("prmProjectMainCDto->prmProjectMainId").gotValue();
            var exclusion = "";
            var exclusionItems = contractDetailGrid.store.data.items;
            if (exclusionItems.length > 0) {
                exclusion = "'" + exclusionItems[0].get("prmContractId") + "'";
                for (var i = 1; i < exclusionItems.length; i++) {
                    exclusion = exclusion + ",'" + exclusionItems[i].get("prmContractId") + "'";
                }
            }
            queryController.view.getConditionPanel().queryExtraParams = {pickModule: 'pickForProject', mainCUuid: mainCUuid, mainUuid: mainUuid, exclusion: exclusion};
        };
        //override doDeleteRow
    },
    initHeaderField: function () {
        var view = this;
        //var contractSignMoney = view.getCmp("prmProjectMainCDto->contractSignMoney");
        //contractSignMoney.afterSotValue = function () {
        //    var budgetDetailItems = view.getCmp("prmBudgetDetailCDto").store.data.items;
        //    Ext.Array.each(budgetDetailItems, function (item) {
        //        var budgetCode = item.get("budgetCode");
        //        if (budgetCode == 'PLANNED_PROFIT') {
        //            item.set("contractMoney", contractSignMoney.gotValue());
        //            view.totalBudgetDetail();
        //        }
        //    });
        //
        //};
        var projectManager = view.getCmp("prmProjectMainCDto->projectManager");
        projectManager.afterSotValue = function () {
            if (projectManager.isChanged() && Scdp.ObjUtil.isNotEmpty(this.codeValue)) {
                var prmMemberDetailPCGrid = view.getCmp("prmMemberDetailPCDto");
                var items = prmMemberDetailPCGrid.store.data.items;
                if (items.length > 0) {
                    for (var i = 0; i < items.length; i++) {
                        if (items[i].get("staffId") == this.codeField) {
                            return false;
                        }
                    }
                }
                var temp = {};
                temp.staffId = this.codeValue;
                temp.staffIdDesc = this.rawValue;
                prmMemberDetailPCGrid.addRowItem(temp, false);
            }
        };
        var contractManagementId = view.getCmp("prmProjectMainCDto->contractManagementId");
        contractManagementId.afterSotValue = function () {
            if (contractManagementId.isChanged() && Scdp.ObjUtil.isNotEmpty(contractManagementId)) {
                var prmAssociatedUnitsCGrid = view.getCmp("prmAssociatedUnitsCDto");
                var items = prmAssociatedUnitsCGrid.store.data.items;
                var flag = false;
                for (var i = 0; i < items.length; i++) {
                    if (items[i].get("associatedType") == "SUPERVISION") {
                        items[i].set("associatedUnitsName", contractManagementId.gotValue());
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    var temp = {};
                    temp.associatedType = 'SUPERVISION';
                    temp.associatedUnitsName = contractManagementId.gotValue();
                    prmAssociatedUnitsCGrid.addRowItem(temp, false);
                }
            }
        };
        var contractDesignerId = view.getCmp("prmProjectMainCDto->contractDesignerId");
        contractDesignerId.afterSotValue = function () {
            if (contractDesignerId.isChanged() && Scdp.ObjUtil.isNotEmpty(contractDesignerId)) {
                var prmAssociatedUnitsCGrid = view.getCmp("prmAssociatedUnitsCDto");
                var items = prmAssociatedUnitsCGrid.store.data.items;
                var flag = false;
                for (var i = 0; i < items.length; i++) {
                    if (items[i].get("associatedType") == "DESIGN") {
                        items[i].set("associatedUnitsName", contractDesignerId.gotValue());
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    var temp = {};
                    temp.associatedType = 'DESIGN';
                    temp.associatedUnitsName = contractDesignerId.gotValue();
                    prmAssociatedUnitsCGrid.addRowItem(temp, false);
                }
            }
        };
    },
    UIStatusChanged: function (view, newStatus) {
        var view = this;
        view.afterChangeStatus();
    },
    afterChangeStatus: function () {
        var view = this;
        var uiStatus = view.getUIStatus();
        var createProjectCodeBtn = view.getCmp("editPanel->createProjectCodeBtn");
        if (createProjectCodeBtn) {
            var state = view.getCmp("prmProjectMainCDto->state").gotValue();
            if ('2' == state && Scdp.Const.UI_INFO_STATUS_VIEW == uiStatus) {
                createProjectCodeBtn.setDisabled(false);
            } else {
                createProjectCodeBtn.setDisabled(true);
            }
        }

        var importFileBtn = view.getCmp("editPanel->fileUpLoad");
        if (importFileBtn) {
            if (Scdp.Const.UI_INFO_STATUS_NULL == uiStatus || Scdp.Const.UI_INFO_STATUS_VIEW == uiStatus) {
                importFileBtn.setDisabled(false);
            } else {
                importFileBtn.setDisabled(true);
            }
        }
        //init file grid
        if (Scdp.Const.UI_INFO_STATUS_NULL == uiStatus || Scdp.Const.UI_INFO_STATUS_VIEW == uiStatus) {
            view.getCmp("fileUpload").setDisabled(true);
            view.getCmp("fileDelete").setDisabled(true);
        } else {
            view.getCmp("fileUpload").setDisabled(false);
            view.getCmp("fileDelete").setDisabled(false);
        }

        view.getCmp('prmProjectMainCDto->taxType').sotEditable(false);
        view.getCmp('prmProjectMainCDto->prmCodeType').sotEditable(false);

        if (view.getCmp("editPanel->btnExamDate")) {
            var examDate = view.getCmp("prmProjectMainCDto->examDate").gotValue();
            if (Scdp.ObjUtil.isNotEmpty(examDate)) {
                view.getCmp("editPanel->btnExamDate").setDisabled(false);
            } else {
                view.getCmp("editPanel->btnExamDate").setDisabled(true);
            }
        }

    },
    initEditToolbar: function () {
        var view = this;
        var editToolbar = view.getEditToolbar();
        view.getCmp("editPanel->copyAddBtn").setVisible(false);
        editToolbar.add({
                xtype: 'button',
                text: Erp.I18N.EXCEL_UPLOAD,
                cid: 'fileUpLoad',
                iconCls: 'temp_icon_16',
                disabled: false
            },
            {
                xtype: 'button',
                text: Erp.I18N.PROJECT_CODE_GENERATE,
                cid: 'createProjectCodeBtn',
                iconCls: 'temp_icon_16',
                disabled: true
            },
            {
                xtype: 'button',
                text: Erp.I18N.PROJECT_TEMPLATE_DOWNLOAD,
                cid: 'downLoadTemplate',
                iconCls: 'temp_icon_16',
                disabled: false
            },
            {
                xtype: 'button',
                text: "考核时间修正",
                cid: 'btnExamDate',
                iconCls: 'erp-examDate',
                disabled: false
            });
    },
    initPlanGrid: function () {
        var view = this;

        var clearDataFn = function (rowData) {
            rowData.lastUuid = null;
        };
        view.getCmp("prmAssociatedUnitsCDto").beforeCopyAddRow = clearDataFn;
        view.getCmp("prmMemberDetailPCDto").beforeCopyAddRow = clearDataFn;
        view.getCmp("prmProgressDetailPCDto").beforeCopyAddRow = clearDataFn;
        view.getCmp("prmPayDetailPCDto").beforeCopyAddRow = clearDataFn;
        view.getCmp("prmSquareDetailPCDto").beforeCopyAddRow = clearDataFn;
        view.getCmp("prmReceiptsDetailPCDto").beforeCopyAddRow = clearDataFn;
        view.getCmp("prmQsPCDto").beforeCopyAddRow = clearDataFn;

        var squareGrid = view.getCmp("prmSquareDetailPCDto");
        squareGrid.afterEditGrid = view.changeSquareDetail;

    },
    initBudgetGrid: function () {
        var view = this;

        var beforeCopyDataFn = function (rowData) {
            rowData.lastUuid = null;
            rowData.serialNumber = null;
            if (Scdp.ObjUtil.isNotEmpty(rowData.splitFromUuid)) {
                rowData.splitFromUuid = null;
            }
            if (Scdp.ObjUtil.isNotEmpty(rowData.splitFromUuidNo)) {
                rowData.splitFromUuidNo = null;
            }
            if (Scdp.ObjUtil.isNotEmpty(rowData.subjectProperty)) {
                rowData.subjectProperty = null;
            }
            if (Scdp.ObjUtil.isNotEmpty(rowData.preTotalValue)) {
                rowData.preTotalValue = null;
            }
            if (Scdp.ObjUtil.isNotEmpty(rowData.isStamp)) {
                rowData.isStamp = null;
            }
        };
        var detailGrid = view.getCmp("prmBudgetDetailCDto");
        var principalGrid = view.getCmp("prmBudgetPrincipalCDto");
        var accessoryGrid = view.getCmp("prmBudgetAccessoryCDto");
        var outsourceGrid = view.getCmp("prmBudgetOutsourceCDto");
        var runGrid = view.getCmp("prmBudgetRunCDto");

        detailGrid.beforeCopyAddRow = beforeCopyDataFn;
        principalGrid.beforeCopyAddRow = beforeCopyDataFn;
        accessoryGrid.beforeCopyAddRow = beforeCopyDataFn;
        outsourceGrid.beforeCopyAddRow = beforeCopyDataFn;
        runGrid.beforeCopyAddRow = beforeCopyDataFn;

        principalGrid.afterCopyAddRow = function () {
            view.totalBudgetPrincipal(this);
        };
        principalGrid.afterDeleteRow = function () {
            view.totalBudgetPrincipal(this);
        };

        accessoryGrid.afterCopyAddRow = function () {
            view.totalBudgetAccessory(this);
        };
        accessoryGrid.afterDeleteRow = function () {
            view.totalBudgetAccessory(this);
        };

        outsourceGrid.afterCopyAddRow = function () {
            view.totalBudgetOutsource(this);
        };
        outsourceGrid.afterDeleteRow = function () {
            view.totalBudgetOutsource(this);
        };

        detailGrid.on('beforeedit', function (editor, eventObj) {
            var grid = eventObj.grid;
            var record = eventObj.record;
            var field = eventObj.field;//column name
            var budgetCode = record.get("budgetCode");
            if (field == 'remark' || field == 'explanation') {
                return grid.editable;
            } else if (field == 'contractMoney' || field == 'jointDesignMoney') {
                if (Ext.Array.contains(view.positiveCodes, budgetCode) || Ext.Array.contains(view.negativeCodes, budgetCode)) {
                    return grid.editable;
                }
            } else if (field == 'costControlMoney') {
                if (budgetCode == 'PROFILE') {
                    return grid.editable;
                }
            }
            return false;
        });

        //principalGrid.on('beforeedit', function (editor, eventObj) {
        //    var grid = eventObj.grid;
        //    var record = eventObj.record;
        //    var lastUuid = record.get("lastUuid");
        //    if (Scdp.ObjUtil.isNotEmpty(lastUuid)) {
        //        return false;
        //    }
        //    return grid.editable;
        //});
        //accessoryGrid.on('beforeedit', function (editor, eventObj) {
        //    var grid = eventObj.grid;
        //    var record = eventObj.record;
        //    var lastUuid = record.get("lastUuid");
        //    if (Scdp.ObjUtil.isNotEmpty(lastUuid)) {
        //        return false;
        //    }
        //    return grid.editable;
        //});
        //outsourceGrid.on('beforeedit', function (editor, eventObj) {
        //    var grid = eventObj.grid;
        //    var record = eventObj.record;
        //    var lastUuid = record.get("lastUuid");
        //    if (Scdp.ObjUtil.isNotEmpty(lastUuid)) {
        //        return false;
        //    }
        //    return grid.editable;
        //});

        detailGrid.afterEditGrid = view.changeBudgetDetail;
        //principalGrid.afterEditGrid = view.changeBudgetPrincipal;
        accessoryGrid.afterEditGrid = view.changeBudgetAccessory;
        //outsourceGrid.afterEditGrid = view.changeBudgetOutsource;
        runGrid.afterEditGrid = view.changeBudgetRun;

        //hide row move up and down button
        var hideRowMoveBtn = function (grid) {
            grid.getCmp("toolbar->rowMoveTopBtn").setVisible(false);
            grid.getCmp("toolbar->rowMoveUpBtn").setVisible(false);
            grid.getCmp("toolbar->rowMoveDownBtn").setVisible(false);
            grid.getCmp("toolbar->rowMoveBottomBtn").setVisible(false);
        };

        hideRowMoveBtn(outsourceGrid);
        hideRowMoveBtn(principalGrid);
        hideRowMoveBtn(accessoryGrid);

    },
    changeSquareDetail: function (eventObj, isChanged) {
        if (!isChanged) {
            return;
        }

        var grid = eventObj.grid;
        var record = eventObj.record;
        var field = eventObj.field;

        if (!(field == "schemingSquareMoney" || field == "schemingSquareCost")) {
            return;
        }
        var amount1 = record.get("schemingSquareMoney");
        if (Scdp.ObjUtil.isEmpty(amount1)) {
            amount1 = 0;
        }
        var amount2 = record.get("schemingSquareCost");
        if (Scdp.ObjUtil.isEmpty(amount2)) {
            amount2 = 0;
        }
        record.set("schemingSquareProfits", Erp.MathUtil.minusNumber(amount1, amount2));
    },
    changeBudgetDetail: function (eventObj, isChanged) {
        if (!isChanged) {
            return;
        }

        var grid = eventObj.grid;
        var view = grid.up("IView");
        var record = eventObj.record;
        var field = eventObj.field;

        if (!(field == "contractMoney" || field == "jointDesignMoney" || field == 'costControlMoney')) {
            return;
        }
        view.totalBudgetDetail();
    },
    //changeBudgetPrincipal: function (eventObj, isChanged) {
    //    if (!isChanged) {
    //        return;
    //    }
    //    var grid = eventObj.grid;
    //    var view = grid.up("IView");
    //    var record = eventObj.record;
    //    var field = eventObj.field;
    //
    //    if (!(field == "amount" || field == "contractPrice" || field == "bidPrice" || field == "schemingPrice")) {
    //        return;
    //    }
    //    var amount = record.get("amount");
    //    var contractPrice = record.get("contractPrice");
    //    var bidPrice = record.get("bidPrice");
    //    var schemingPrice = record.get("schemingPrice");
    //    var contractTotalValue = Erp.MathUtil.multiNumber(amount, contractPrice);
    //    record.set("contractTotalValue", contractTotalValue);
    //    record.set("bidTotalValue", Erp.MathUtil.multiNumber(amount, bidPrice));
    //    var schemingTotalValue = Erp.MathUtil.multiNumber(amount, schemingPrice);
    //    record.set("schemingTotalValue", schemingTotalValue);
    //    record.set("schemingGrossProfit", Erp.MathUtil.minusNumber(contractTotalValue , schemingTotalValue));
    //
    //    view.totalBudgetPrincipal(grid);
    //},
    changeBudgetAccessory: function (eventObj, isChanged) {
        if (!isChanged) {
            return;
        }
        var grid = eventObj.grid;
        var view = grid.up("IView");
        var record = eventObj.record;
        var field = eventObj.field;

        if (!(field == "amount" || field == "price")) {
            return;
        }
        var amount = record.get("amount");
        var price = record.get("price");
        var totalValue = Erp.MathUtil.multiNumber(amount, price);
        if (view.getHeader().getCmp("detailType").gotValue() == '*') {
            if (field == "amount") {
                if (Erp.MathUtil.compare(amount, record.get("lockedAmount")) < 0) {
                    Scdp.MsgUtil.info("数量不能小于已经采购申请的数量!");
                    record.set("amount", eventObj.originalValue);
                    return;
                }
            } else {
                if (Erp.MathUtil.compare(totalValue, record.get("lockedMoney")) < 0) {
                    Scdp.MsgUtil.info("计划金额不能小于已经采购申请的金额!");
                    record.set("price", eventObj.originalValue);
                    return;
                }
            }
        }

        record.set("totalValue", totalValue);

        view.totalBudgetAccessory();
    },
    //changeBudgetOutsource: function (eventObj, isChanged) {
    //    if (!isChanged) {
    //        return;
    //    }
    //    var grid = eventObj.grid;
    //    var view = grid.up("IView");
    //    var record = eventObj.record;
    //    var field = eventObj.field;
    //
    //    if (!(field == "amount" || field == "price")) {
    //        return;
    //    }
    //    var amount = record.get("amount");
    //    var price = record.get("price");
    //    record.set("totalValue", Erp.MathUtil.multiNumber(amount, price));
    //
    //    view.totalBudgetOutsource(grid);
    //},
    changeBudgetRun: function (eventObj, isChanged) {
        if (!isChanged) {
            return;
        }
        var grid = eventObj.grid;
        var view = grid.up("IView");
        var record = eventObj.record;
        var field = eventObj.field;

        if (!(field == "amount" || field == "price")) {
            return;
        }
        var amount = record.get("amount");
        var price = record.get("price");
        record.set("totalValue", Erp.MathUtil.multiNumber(amount, price));

        view.totalBudgetRun();
    },
    calBudgetPrincipal: function () {
        var view = this;
        var budgetDetailGrid = view.getCmp("prmBudgetDetailCDto");
        var grid = view.getCmp("prmBudgetPrincipalCDto");
        var store = grid.getStore();
        var totalAmount = 0;
        Ext.Array.each(store.data.items, function (record) {
            var totalValue = record.get("schemingTotalValue");
            if (Scdp.ObjUtil.isNotEmpty(totalValue)) {
                totalAmount = Erp.MathUtil.plusNumber(totalAmount, totalValue);
            }
        });

        Ext.Array.each(budgetDetailGrid.store.data.items, function (record) {
            if (record.get("budgetCode") == "PRINCIPAL") {
                record.set("costControlMoney", totalAmount);
            }
        });
    },

    totalBudgetPrincipal: function () {
        var view = this;
        view.calBudgetPrincipal();
        view.totalBudgetDetail();
    },
    calBudgetAccessory: function () {
        var view = this;
        var budgetDetailGrid = view.getCmp("prmBudgetDetailCDto");
        var grid = view.getCmp("prmBudgetAccessoryCDto");
        var store = grid.getStore();
        var totalAmount = 0;
        Ext.Array.each(store.data.items, function (record) {
            var totalValue = record.get("totalValue");
            if (Scdp.ObjUtil.isNotEmpty(totalValue)) {
                totalAmount = Erp.MathUtil.plusNumber(totalAmount, totalValue);
            }
        });

        Ext.Array.each(budgetDetailGrid.store.data.items, function (record) {
            if (record.get("budgetCode") == "ACCESSORY") {
                record.set("costControlMoney", totalAmount);
            }
        });
    },
    totalBudgetAccessory: function () {
        var view = this;
        view.calBudgetAccessory();
        view.totalBudgetDetail();
    },
    calBudgetOutsource: function () {
        var view = this;
        var budgetDetailGrid = view.getCmp("prmBudgetDetailCDto");
        var grid = view.getCmp("prmBudgetOutsourceCDto");
        var store = grid.getStore();
        var totalAmount = 0;
        Ext.Array.each(store.data.items, function (record) {
            var totalValue = record.get("totalValue");
            if (Scdp.ObjUtil.isNotEmpty(totalValue)) {
                totalAmount = Erp.MathUtil.plusNumber(totalAmount, totalValue);
            }
        });

        Ext.Array.each(budgetDetailGrid.store.data.items, function (record) {
            if (record.get("budgetCode") == "OUTSOURCE") {
                record.set("costControlMoney", totalAmount);
            }
        });
    },
    totalBudgetOutsource: function () {
        var view = this;
        view.calBudgetOutsource();
        view.totalBudgetDetail();
    },
    calBudgetRun: function () {
        var view = this;
        var budgetDetailGrid = view.getCmp("prmBudgetDetailCDto");
        var grid = view.getCmp("prmBudgetRunCDto");
        var store = grid.getStore();
        var totalAmount = 0;
        Ext.Array.each(store.data.items, function (record) {
            var totalValue = record.get("totalValue");
            if (Scdp.ObjUtil.isNotEmpty(totalValue)) {
                totalAmount = Erp.MathUtil.plusNumber(totalAmount, totalValue);
            }
        });

        Ext.Array.each(budgetDetailGrid.store.data.items, function (record) {
            if (record.get("budgetCode") == "RUN") {
                record.set("costControlMoney", totalAmount);
            }
        });

    },
    totalBudgetRun: function () {
        var view = this;
        view.calBudgetRun();
        view.totalBudgetDetail();

    },
    totalBudgetDetail: function () {
        var view = this;
        if (view.isBusinessTaxContract()) {
            view.totalBudgetDetailForTax();
        } else {
            view.totalBudgetDetailForVat();
        }
    },
    totalBudgetDetailForTax: function () {
        var view = this;
        var taxType = view.getCmp("prmProjectMainCDto->taxType").gotValue();
        var budgetDetailGrid = view.getCmp("prmBudgetDetailCDto");
        var store = budgetDetailGrid.getStore();

        //1 is contract column value
        //2 is joint column value
        //3 is cost column value
        var contractTotal1 = 0;
        var negativeMoney1 = 0;
//        var contractTotal2 = 0;
//        var negativeMoney2 = 0;
        Ext.Array.each(store.data.items, function (record) {
            var budgetCode = record.get("budgetCode");

            var amount1 = record.get("contractMoney");
//            var amount2 = record.get("jointDesignMoney");
            if (Scdp.ObjUtil.isEmpty(amount1)) {
                amount1 = 0;
            }
//            if (Scdp.ObjUtil.isEmpty(amount2)) {
//                amount2 = 0;
//            }

            if (Ext.Array.contains(view.positiveCodes, budgetCode)) {
                contractTotal1 = Erp.MathUtil.plusNumber(contractTotal1, amount1);
//                contractTotal2 = Erp.MathUtil.plusNumber(contractTotal2, amount2);
            }
            if (Ext.Array.contains(view.negativeCodes, budgetCode)) {
                negativeMoney1 = Erp.MathUtil.plusNumber(negativeMoney1, amount1);
//                negativeMoney2 = Erp.MathUtil.plusNumber(negativeMoney2, amount2);
            }

            record.set("vatAmount", null);
            record.set("excludingVatAmount", null);
        });
        var projectMoney1 = Erp.MathUtil.minusNumber(contractTotal1, negativeMoney1);
//        var projectMoney2 = Erp.MathUtil.minusNumber(contractTotal2, negativeMoney2);
        var estimatedCostModel = null;
        var plannedProfitModel = null;
        var costTotal3 = 0;
        Ext.Array.each(store.data.items, function (record) {
            var budgetCode = record.get("budgetCode");
            if (budgetCode == 'CONTRACT_TOTAL') {
                record.set("contractMoney", contractTotal1);
//                record.set("jointDesignMoney", contractTotal2);
                //record.set("costControlMoney", contractTotal1);
            } else if (budgetCode == 'PROJECT_MONEY') {
                record.set("contractMoney", projectMoney1);
//                record.set("jointDesignMoney", projectMoney2);
                //record.set("costControlMoney", projectMoney1);
            } else if (budgetCode == 'ESTIMATED_COST') {
                estimatedCostModel = record;
            } else if (budgetCode == 'PLANNED_PROFIT') {
                plannedProfitModel = record;
            } else if (Ext.Array.contains(view.taxFlexRateCodes, budgetCode)) {
                // 5%,3.41%, 0%
                // 5%,3.41%, 0%
                var rate = view.getSubjectRate(record.get("subjectComment"), taxType);
                record.set("costControlMoney", Erp.MathUtil.multiNumber(projectMoney1, rate));
            }
            if (Ext.Array.contains(view.costMoneyCodes, budgetCode)) {
                var amount3 = record.get("costControlMoney");
                if (Scdp.ObjUtil.isEmpty(amount3)) {
                    amount3 = 0;
                }
                costTotal3 = Erp.MathUtil.plusNumber(costTotal3, amount3);
            }
        });

        if (estimatedCostModel) {
            estimatedCostModel.set("costControlMoney", costTotal3);
        }
        if (plannedProfitModel) {
            plannedProfitModel.set("costControlMoney", Erp.MathUtil.minusNumber(projectMoney1, costTotal3));
        }

        view.getCmp("prmProjectMainCDto->projectMoney").sotValue(projectMoney1);
        view.getCmp("prmProjectMainCDto->costControlMoney").sotValue(costTotal3);

        //var contractorOffice = view.getCmp("prmProjectMainCDto->contractorOffice").gotValue();
        //var isMajorProject = view.getCmp("prmProjectMainCDto->isMajorProject");
        //var officeMajor = Erp.Util.getDeptMajorProjectMoney();
        //if (officeMajor == '1') {
        //    if (contractTotal1 >= 50000000) {
        //        isMajorProject.sotValue(1);
        //    } else {
        //        isMajorProject.sotValue(0);
        //    }
        //} else {
        //    if (contractTotal1 >= 5000000) {
        //        isMajorProject.sotValue(1);
        //    } else {
        //        isMajorProject.sotValue(0);
        //    }
        //}
    },
    totalBudgetDetailForVat: function () {
        var view = this;
        var codeType = view.getCmp("prmProjectMainCDto->prmCodeType").gotValue();
        var budgetDetailGrid = view.getCmp("prmBudgetDetailCDto");
        var store = budgetDetailGrid.getStore();

        //1 is contract column value
        //2 is joint column value
        //3 is cost column value
        var contractTotal1 = 0;
        var negativeMoney1 = 0;
//        var contractTotal2 = 0;
//        var negativeMoney2 = 0;
        var totalVatAmount = 0;
        Ext.Array.each(store.data.items, function (record) {
            var budgetCode = record.get("budgetCode");

            var amount1 = record.get("contractMoney");
//            var amount2 = record.get("jointDesignMoney");
            if (Scdp.ObjUtil.isEmpty(amount1)) {
                amount1 = 0;
            }
//            if (Scdp.ObjUtil.isEmpty(amount2)) {
//                amount2 = 0;
//            }

            if (Ext.Array.contains(view.positiveCodes, budgetCode)) {
                contractTotal1 = Erp.MathUtil.plusNumber(contractTotal1, amount1);
//                contractTotal2 = Erp.MathUtil.plusNumber(contractTotal2, amount2);
            }
            if (Ext.Array.contains(view.negativeCodes, budgetCode)) {
                negativeMoney1 = Erp.MathUtil.plusNumber(negativeMoney1, amount1);
//                negativeMoney2 = Erp.MathUtil.plusNumber(negativeMoney2, amount2);
            }
            if (Ext.Array.contains(view.calVatCodes, budgetCode)) {
                var vatRate = view.getSubjectRate(record.get("subjectComment"), codeType);
                var vatAmount = Erp.MathUtil.divideNumber(
                    Erp.MathUtil.multiNumber(record.get("costControlMoney"), vatRate),
                    Erp.MathUtil.plusNumber(1, vatRate));
                record.set("vatAmount", vatAmount);
                totalVatAmount = Erp.MathUtil.plusNumber(totalVatAmount, vatAmount);
            }
            if (Ext.Array.contains(view.calExcludingVatCodes, budgetCode)) {
                record.set("excludingVatAmount", Erp.MathUtil.minusNumber(record.get("costControlMoney"), record.get("vatAmount")));
            }
            if ('TECHNICAL_FEE_H' == budgetCode) {
                var vatRate = view.getSubjectRate(record.get("subjectComment"), codeType);
                var vatAmount = Erp.MathUtil.divideNumber(
                    Erp.MathUtil.multiNumber(record.get("contractMoney"), vatRate),
                    Erp.MathUtil.plusNumber(1, vatRate));
                record.set("vatAmount", vatAmount);
                record.set("excludingVatAmount", Erp.MathUtil.minusNumber(record.get("contractMoney"), vatAmount));
                totalVatAmount = Erp.MathUtil.plusNumber(totalVatAmount, vatAmount);
            }
        });
        var projectMoney1 = Erp.MathUtil.minusNumber(contractTotal1, negativeMoney1);
//        var projectMoney2 = Erp.MathUtil.minusNumber(contractTotal2, negativeMoney2);
        var estimatedCostModel = null;
        var plannedProfitModel = null;
        var costTotal3 = 0;
        Ext.Array.each(store.data.items, function (record) {
            var budgetCode = record.get("budgetCode");
            if (budgetCode == 'CONTRACT_TOTAL') {
                record.set("contractMoney", contractTotal1);
//                record.set("jointDesignMoney", contractTotal2);
                //record.set("costControlMoney", contractTotal1);
            } else if (budgetCode == 'PROJECT_MONEY') {
                record.set("contractMoney", projectMoney1);
//                record.set("jointDesignMoney", projectMoney2);
                //record.set("costControlMoney", projectMoney1);
            } else if (budgetCode == 'ESTIMATED_COST') {
                estimatedCostModel = record;
            } else if (budgetCode == 'PLANNED_PROFIT') {
                plannedProfitModel = record;
            } else if (Ext.Array.contains(view.vatFlexRateCodes, budgetCode)) {
                // 5%,3.41%, 0%
                var rate = view.getSubjectRate(record.get("subjectComment"), codeType);
                record.set("costControlMoney", Erp.MathUtil.multiNumber(projectMoney1, rate));
            } else if ("TAX_NO_STAMP" == budgetCode) {
                if ("JI_GAI" == codeType) {
                    record.set("costControlMoney", 0);
                } else {
                    var rate1 = view.getSubjectRate(record.get("subjectComment"), codeType);
                    var rate2 = view.getSubjectRate(record.get("subjectComment"), 'SURCHARGE');
                    var taxNoStamp = Erp.MathUtil.multiNumber(Erp.MathUtil.minusNumber(Erp.MathUtil.divideNumber(Erp.MathUtil.multiNumber(projectMoney1, rate1, 6), Erp.MathUtil.plusNumber(1, rate1)), totalVatAmount), Erp.MathUtil.plusNumber(1, rate2));
                    if (taxNoStamp < 0) {
                        taxNoStamp = 0;
                    }
                    record.set("costControlMoney", taxNoStamp);
                }
            }
            if (Ext.Array.contains(view.costMoneyCodes, budgetCode)) {
                var amount3 = record.get("costControlMoney");
                if (Scdp.ObjUtil.isEmpty(amount3)) {
                    amount3 = 0;
                }
                costTotal3 = Erp.MathUtil.plusNumber(costTotal3, amount3);
            }
        });

        if (estimatedCostModel) {
            estimatedCostModel.set("costControlMoney", costTotal3);
        }
        if (plannedProfitModel) {
            plannedProfitModel.set("costControlMoney", Erp.MathUtil.minusNumber(projectMoney1, costTotal3));
        }

        view.getCmp("prmProjectMainCDto->projectMoney").sotValue(projectMoney1);
        view.getCmp("prmProjectMainCDto->costControlMoney").sotValue(costTotal3);

        //var contractorOffice = view.getCmp("prmProjectMainCDto->contractorOffice").gotValue();
        //var isMajorProject = view.getCmp("prmProjectMainCDto->isMajorProject");
        //var officeMajor = Erp.Util.getDeptMajorProjectMoney();
        //if(officeMajor=='1'){
        //    if(contractTotal1>=50000000){
        //        isMajorProject.sotValue(1);
        //    }else{
        //        isMajorProject.sotValue(0);
        //    }
        //}else{
        //    if(contractTotal1>=5000000){
        //        isMajorProject.sotValue(1);
        //    }else{
        //        isMajorProject.sotValue(0);
        //    }
        //}
    },
    putGridFieldValue: function (view, grid, record, dataIndex, value) {
        record.set(dataIndex, value);
        if (grid.getCurRecord() == record) {
            var gridCid = grid.cid;
            var formCid = gridCid.substr(0, gridCid.length - 3) + "Form";
            var form = view.getCmp(formCid);
            if (form) {
                var formFieldCmp = form.getCmp(dataIndex);
                if (formFieldCmp) {
                    formFieldCmp.sotValue(value);
                }
            }
        }
    },
    isBusinessTaxContract: function () {
        var me = this;
        var contractDetailGrid = me.getCmp("prmContractDetailCDto");
        if (contractDetailGrid.getStore().getCount() == 0) {
            var headerForm = me.getHeader();
            var establishDate = headerForm.getCmp("establishDate").gotValue();
            var compareDate = Ext.Date.parse("2016-05-01", 'Y-m-d');
            return Scdp.ObjUtil.isNotEmpty(establishDate) && establishDate.getTime() < compareDate;
        } else {
            return "1" === contractDetailGrid.getStore().getAt(0).get("isBusinessTax") + "";
        }
    },
    getSubjectRate: function (rateDesc, code) {
        if (Scdp.ObjUtil.isNotEmpty(rateDesc) && Scdp.ObjUtil.isNotEmpty(code)) {
            var rateObj = Ext.JSON.decode(rateDesc);
            var rate = rateObj[code];
            if (Scdp.ObjUtil.isEmpty(rate)) {
                rate = rateObj['*'];
            }
            return Erp.MathUtil.multiNumber(rate, 0.01, 6);
        } else {
            return null;
        }
    },
    initTotalColumns: function () {
        var view = this;
        view.initTotalBudgetPrincipalColumns();
        view.initTotalBudgetAccessoryColumns();
        view.initTotalBudgetRunColumns();
    },
    initTotalBudgetPrincipalColumns: function () {
        var view = this;
        var grid = view.getCmp("prmBudgetPrincipalCDto");
        Ext.Array.each(grid.columns, function (item) {
            if (item.dataIndex == "serialNumber") {
                Ext.apply(item, {
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return '<strong style="color: red"> 合计</strong>';
                    }
                })
            }
            if (item.summaryType) {
                Ext.apply(item, {
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return '<strong style="color: red">' + Ext.util.Format.number(value, '0,000.00') + '</strong>';
                    }
                });
            }
        });
    },
    initTotalBudgetAccessoryColumns: function () {
        var view = this;
        var grid = view.getCmp("prmBudgetAccessoryCDto");
        Ext.Array.each(grid.columns, function (item) {
            if (item.summaryType) {
                Ext.apply(item, {
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return '<strong style="color: red">' + "合计：" + Ext.util.Format.number(value, '0,000.00') + '</strong>';
                    }
                });
            }
        });
    },
    initTotalBudgetRunColumns: function () {
        var view = this;
        var grid = view.getCmp("prmBudgetRunCDto");
        Ext.Array.each(grid.columns, function (item) {
            if (item.summaryType) {
                Ext.apply(item, {
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return '<strong style="color: red">' + "合计：" + Ext.util.Format.number(value, '0,000.00') + '</strong>';
                    }
                });
            }
        });
    },
    initGridColumnRender: function () {
        var me = this;
        Ext.Array.each(me.bindings, function (binding) {
            var path = binding.split(".");
            if (path.length == 2) {
                var grid = me.getCmp(path[1]);
                grid.rowColorConfigFn = me.rowColorConfigFnEGrid;
            }
        });
    },
    rowColorConfigFnEGrid: function (record) {
        if (record.get("isStamp") == "1") {
            return 'x-grid-row-summary';
        }
    }
});