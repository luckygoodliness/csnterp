Ext.define('Prmprojectmainc.view.Prmprojectmainc2View', {
    extend: 'Prmprojectmainc.view.PrmprojectmaincView',
    cpHeight: 100,
    queryLayoutFile: 'prm-projectmainc2-query-layout.xml',
    editLayoutFile: 'prm-projectmainc2-edit-layout.xml',
    workFlowDefinitionKey: 'Prm_Revise',
    prmBudgetOutsourceCDtoLockedFields: ['serialNumber', 'supplierCode'],
    prmBudgetOutsourceCDtoSplitLockedFields: ['serialNumber', 'supplierCode'],
    prmBudgetPrincipalCDtoLockedFields: ['serialNumber', 'equipmentName'],
    prmBudgetPrincipalCDtoSplitLockedFields: ['serialNumber', 'equipmentName'],
    prmBudgetAccessoryCDtoLockedFields: ['serialNumber', 'accessoryName'],
    prmBudgetAccessoryCDtoSplitLockedFields: ['serialNumber', 'accessoryName'],
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var view = this;
        this.callParent(arguments);
        view.initGridColumnRender();

    },
    initEditToolbar: function () {
        var view = this;
        var editToolbar = view.getEditToolbar();
        view.getCmp("editPanel->copyAddBtn").setVisible(false);
        editToolbar.add({
            xtype: 'button',
            text: Erp.I18N.EXCEL_UPLOAD,
            cid: 'fileUpLoad',
            iconCls: 'temp_icon_16'
        });
        editToolbar.add({
            xtype: 'JTbSeparator',
            text: '',
            cid: 'vertical',
            iconCls: 'temp_icon_11'
        });
        editToolbar.add({
            xtype: 'JCheck',
            cid: 'showChangeChk',
            boxLabel: Erp.I18N.ACCORDING_CHANGE,
            iconCls: '',
            hidden: true
        });
        editToolbar.add(
            {
                text: '考核时间修正',
                cid: 'btnExamDate',
                iconCls: 'erp-examDate'
            });
    },
    UIStatusChanged: function (view, uistatus) {
        var me = this;
        this.callParent(arguments);
        //
        me.afterChangeUIStatus();

    },
    afterChangeUIStatus: function () {
        var me = this;
        var showChangeCmp = me.getCmp("editPanel->showChangeChk");
        if (showChangeCmp) {
            var state = me.getCmp("prmProjectMainCDto->state").gotValue();
            showChangeCmp.sotValue(0);
            if (me.getUIStatus() == Scdp.Const.UI_INFO_STATUS_VIEW) {
                showChangeCmp.setVisible(true);
            } else {
                showChangeCmp.setVisible(false);
            }
        }
        if (me.getCmp("editPanel->btnExamDate")) {
            var examDate = me.getCmp("prmProjectMainCDto->examDate").gotValue();
            if (Scdp.ObjUtil.isNotEmpty(examDate)) {
                me.getCmp("editPanel->btnExamDate").setDisabled(false);
            } else {
                me.getCmp("editPanel->btnExamDate").setDisabled(true);
            }
        }
    },
    initBudgetGrid: function () {
        var view = this;
        this.callParent(arguments);

        var principalGrid = view.getCmp("prmBudgetPrincipalCDto");
        var accessoryGrid = view.getCmp("prmBudgetAccessoryCDto");
        var outsourceGrid = view.getCmp("prmBudgetOutsourceCDto");

        outsourceGrid.getEditToolbar().add(1, {
            xtype: 'button',
            cid: "splitItem",
            tooltip: Erp.I18N.SPLIT,
            iconCls: "erp-split",
            listeners: {
                click: function (btn) {
                    view.splitOutsource();
                }
            }
        });

        //outsourceGrid.getEditToolbar().add({
        //    xtype: 'button',
        //    tooltip: Erp.I18N.RELOAD_BUDGET_DETAIL,
        //    text: Erp.I18N.REFRESH,
        //    listeners: {
        //        click: function (btn) {
        //            view.reloadOutsource();
        //        }
        //    }
        //});
        //principalGrid.getEditToolbar().add({
        //    xtype: 'button',
        //    tooltip: Erp.I18N.RELOAD_BUDGET_DETAIL,
        //    text: Erp.I18N.REFRESH,
        //    listeners: {
        //        click: function (btn) {
        //            view.reloadPrincipal();
        //        }
        //    }
        //});
        principalGrid.getEditToolbar().add(1, {
            xtype: 'button',
            cid: "splitItem",
            tooltip: Erp.I18N.SPLIT,
            iconCls: "erp-split",
            listeners: {
                click: function (btn) {
                    view.splitPrincipal();
                }
            }
        });

        //accessoryGrid.getEditToolbar().add({
        //    xtype: 'button',
        //    tooltip: Erp.I18N.RELOAD_BUDGET_DETAIL,
        //    text: Erp.I18N.REFRESH,
        //    listeners: {
        //        click: function (btn) {
        //            view.reloadAccessory();
        //        }
        //    }
        //});
        accessoryGrid.getEditToolbar().add(1, {
            xtype: 'button',
            cid: "splitItem",
            tooltip: Erp.I18N.SPLIT,
            iconCls: "erp-split",
            listeners: {
                click: function (btn) {
                    view.splitAccessory();
                }
            }
        });

        principalGrid.on("select", view.selectBudgetGrid);
        accessoryGrid.on("select", view.selectBudgetGrid);
        outsourceGrid.on("select", view.selectBudgetGrid);

        accessoryGrid.on('beforeedit', function (editor, eventObj) {
            var grid = eventObj.grid;
            var record = eventObj.record;
            var dataIndex = eventObj.field;
            var lastUuid = record.get("lastUuid");
            if (Scdp.ObjUtil.isNotEmpty(lastUuid) && Ext.Array.contains(view.prmBudgetAccessoryCDtoLockedFields, dataIndex)) {
                return false;
            }
            var splitFromUuidNo = record.get("splitFromUuidNo");
            if (Scdp.ObjUtil.isNotEmpty(splitFromUuidNo) && Ext.Array.contains(view.prmBudgetAccessoryCDtoSplitLockedFields, dataIndex)) {
                return false;
            }
            return grid.editable;
        });

        principalGrid.setbindCompEditableAfterSelect = view.afterSelectBudgetGrid2Bind;
        outsourceGrid.setbindCompEditableAfterSelect = view.afterSelectBudgetGrid2Bind;
    },
    initContractGrid: function () {
        var view = this;
        this.callParent(arguments);
        var contractDetailGrid = view.getCmp("prmContractDetailCDto");
        contractDetailGrid.beforeDeleteRow = function (records, rowIndex) {
            var isPreProject = view.getCmp('prmProjectMainCDto->isPreProject').gotValue()
            if (isPreProject === 0) {
                if (Scdp.ObjUtil.isNotEmpty(view.mainContractUuids)) {
                    var me = this;
                    var items = me.store.data.items;
                    if (items.length < 2) {
                        Scdp.MsgUtil.warn("非预立项项目，至少需要一条合同明细！");
                        return false
                    }
                    var thisContractId = records[0].get('prmContractId');
                    var fond = false;
                    for (var i = 0; i < items.length; i++) {
                        if (fond) {
                            break;
                        }
                        if (items[i].get("prmContractId") != thisContractId) {
                            for (var j = 0; j < view.mainContractUuids.length; j++) {
                                if (view.mainContractUuids[j] == items[i].get("prmContractId")) {
                                    fond = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!fond) {
                        Scdp.MsgUtil.warn("非预立项项目，至少保留一条初始合同！");
                        return false
                    }
                }
            }

            return true;
        };
    },
    selectBudgetGrid: function (obj, record, index, eOpts) {
        var grid = this;
        var view = grid.up("IView");
        var uiStatus = view.getUIStatus();
        if (uiStatus === Scdp.Const.UI_INFO_STATUS_NEW || uiStatus === Scdp.Const.UI_INFO_STATUS_MODIFY) {
            var lastUuid = record.get("lastUuid");
            if (Scdp.ObjUtil.isNotEmpty(lastUuid)) {
                grid.getCmp("toolbar->delRowBtn").setDisabled(true);
            } else {
                grid.getCmp("toolbar->delRowBtn").setDisabled(false);
            }

            var lockedMoney = record.get("lockedMoney");
            if (Scdp.ObjUtil.isNotEmpty(lockedMoney) && lockedMoney > 0) {
                grid.getCmp("splitItem").setDisabled(true);
            } else {
                grid.getCmp("splitItem").setDisabled(false);
            }
        }
    },
    afterSelectBudgetGrid2Bind: function (obj, record, bindComp, uiStatus) {
        var grid = this;
        var lastUuid = record.get("lastUuid");
        var splitFromUuidNo = record.get("splitFromUuidNo");
        var isEditStatus = (uiStatus === Scdp.Const.UI_INFO_STATUS_NEW || uiStatus === Scdp.Const.UI_INFO_STATUS_MODIFY);
        if (isEditStatus && (Scdp.ObjUtil.isNotEmpty(lastUuid) || Scdp.ObjUtil.isNotEmpty(splitFromUuidNo))) {
            bindComp.sotEditable(true);
            var view = grid.up("IView");
            if (Scdp.ObjUtil.isNotEmpty(lastUuid)) {
                var lockedFields = view[grid.cid + "LockedFields"];
                if (lockedFields) {
                    Ext.Array.each(lockedFields, function (field) {
                        bindComp.getCmp(field).sotEditable(false);
                    });
                }
            }
            if (Scdp.ObjUtil.isNotEmpty(splitFromUuidNo)) {
                var splitLockedFields = view[grid.cid + "SplitLockedFields"];
                if (splitLockedFields) {
                    Ext.Array.each(splitLockedFields, function (field) {
                        bindComp.getCmp(field).sotEditable(false);
                    });
                }
            }
            return true;
        } else {
            return false;
        }
    },
    reloadOutsource: function () {
        var view = this;
        var projectMainUuid = view.getCmp("prmProjectMainCDto->prmProjectMainId").gotValue();
        var grid = view.getCmp("prmBudgetOutsourceCDto");
        var items = grid.store.data.items;
        if (items.length == 0) {
            return;
        }
        var inputArray = [];
        Ext.Array.each(items, function (record) {
            var lastUuid = record.get("lastUuid");
            var amount = record.get("amount");
            if (Scdp.ObjUtil.isNotEmpty(lastUuid) && Scdp.ObjUtil.isNotEmpty(amount) && amount != 0) {
                inputArray.push({lastUuid: lastUuid, amount: amount});
            }
        });
        if (inputArray.length == 0) {
            return;
        }

        var postData = {};
        postData.prmProjectMainId = projectMainUuid;
        postData.budgetCode = "OUTSOURCE";
        postData.details = inputArray;
        Scdp.doAction("prmprojectmainc-reload-budget-detail-from-purchase-plan", postData, function (result) {
            if (result && result.root) {
                var purDetails = result.root;
                Ext.Array.each(items, function (record) {
                    var lastUuid = record.lastUuid;
                    var amount = record.amount;
                    Ext.Array.each(purDetails, function (item) {
                        if (item.lastUuid == record.get("lastUuid")) {
                            view.putGridFieldValue(view, grid, record, "price", item.price);
                            view.putGridFieldValue(view, grid, record, "totalValue", item.totalValue);
                            //record.set("price",item.price);
                            //record.set("totalValue",item.totalValue);
                        }
                    });
                });
                view.totalBudgetOutsource(grid);
            }
        });
    },
    reloadPrincipal: function () {
        var view = this;
        var projectMainUuid = view.getCmp("prmProjectMainCDto->prmProjectMainId").gotValue();
        var grid = view.getCmp("prmBudgetPrincipalCDto");
        var items = grid.store.data.items;
        if (items.length == 0) {
            return;
        }
        var inputArray = [];
        Ext.Array.each(items, function (record) {
            var lastUuid = record.get("lastUuid");
            var amount = record.get("amount");
            if (Scdp.ObjUtil.isNotEmpty(lastUuid) && Scdp.ObjUtil.isNotEmpty(amount) && amount != 0) {
                inputArray.push({lastUuid: lastUuid, amount: amount});
            }
        });
        if (inputArray.length == 0) {
            return;
        }

        var postData = {};
        postData.prmProjectMainId = projectMainUuid;
        postData.budgetCode = "PRINCIPAL";
        postData.details = inputArray;
        Scdp.doAction("prmprojectmainc-reload-budget-detail-from-purchase-plan", postData, function (result) {
            if (result && result.root) {
                var purDetails = result.root;
                Ext.Array.each(items, function (record) {
                    var lastUuid = record.lastUuid;
                    var amount = record.amount;
                    Ext.Array.each(purDetails, function (item) {
                        if (item.lastUuid == record.get("lastUuid")) {
                            view.putGridFieldValue(view, grid, record, "schemingPrice", item.schemingPrice);
                            view.putGridFieldValue(view, grid, record, "schemingTotalValue", item.schemingTotalValue);
                            var grossProfit = Erp.MathUtil.minusNumber(record.get("contractTotalValue"), record.get("schemingTotalValue"));
                            view.putGridFieldValue(view, grid, record, "schemingGrossProfit", grossProfit);
                            //record.set("schemingPrice",item.schemingPrice);
                            //record.set("schemingTotalValue",item.schemingTotalValue);
                            //record.set("schemingGrossProfit",grossProfit);

                            //todo set the form value
                        }
                    });
                });
                view.totalBudgetPrincipal(grid);
            }
        });
    },
    reloadAccessory: function () {
        var view = this;
        var projectMainUuid = view.getCmp("prmProjectMainCDto->prmProjectMainId").gotValue();
        var grid = view.getCmp("prmBudgetAccessoryCDto");
        var items = grid.store.data.items;
        if (items.length == 0) {
            return;
        }
        var inputArray = [];
        Ext.Array.each(items, function (record) {
            var lastUuid = record.get("lastUuid");
            var amount = record.get("amount");
            if (Scdp.ObjUtil.isNotEmpty(lastUuid) && Scdp.ObjUtil.isNotEmpty(amount) && amount != 0) {
                inputArray.push({lastUuid: lastUuid, amount: amount});
            }
        });
        if (inputArray.length == 0) {
            return;
        }

        var postData = {};
        postData.prmProjectMainId = projectMainUuid;
        postData.budgetCode = "ACCESSORY";
        postData.details = inputArray;
        Scdp.doAction("prmprojectmainc-reload-budget-detail-from-purchase-plan", postData, function (result) {
            if (result && result.root) {
                var purDetails = result.root;
                Ext.Array.each(items, function (record) {
                    var lastUuid = record.lastUuid;
                    var amount = record.amount;
                    Ext.Array.each(purDetails, function (item) {
                        if (item.lastUuid == record.get("lastUuid")) {
                            view.putGridFieldValue(view, grid, record, "price", item.price);
                            view.putGridFieldValue(view, grid, record, "totalValue", item.totalValue);
                            //record.set("price",item.price);
                            //record.set("totalValue",item.totalValue);
                        }
                    });
                });
                view.totalBudgetAccessory(grid);
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
                grid.JCurColConfig = me.JCurColConfig;
                grid.JDateColConfig = me.JDateColConfig;
                grid.JComboColConfig = me.JComboColConfig;
                grid.JGridFieldColConfig = me.JGridFieldColConfig;
            }
        });
    },
    JCurColConfig: function (value, col, model, rowIdx, colIdx, store, view) {
        view.up('IView').columnRender && view.up('IView').columnRender(value, col, model, rowIdx, colIdx, store, view);
    },
    JDateColConfig: function (value, col, model, rowIdx, colIdx, store, view) {
        view.up('IView').columnRender && view.up('IView').columnRender(value, col, model, rowIdx, colIdx, store, view);
    },
    JComboColConfig: function (value, col, model, rowIdx, colIdx, store, view) {
        view.up('IView').columnRender && view.up('IView').columnRender(value, col, model, rowIdx, colIdx, store, view);
    },
    JGridFieldColConfig: function (value, col, model, rowIdx, colIdx, store, view) {
        view.up('IView').columnRender && view.up('IView').columnRender(value, col, model, rowIdx, colIdx, store, view);
    },
    columnRender: function (value, col, model, rowIdx, colIdx, store, view) {
        var isRevised = model.get("isRevised");
        if (isRevised) {
            var changeStatus = model.get("changeStatus");
            if (changeStatus == "变更") {
                var changedFields = model.get("changedFields");
                if (Scdp.ObjUtil.isNotEmpty(changedFields) && changedFields.indexOf("|" + col.column.dataIndex + "|") >= 0) {
                    col.style = "background-color:lightBlue;";
                }
            }
        }
    },

    rowColorConfigFnEGrid: function (record) {
        if (record.get("changeStatus") == "增加") {
            return 'erp-grid-font-color-red';
        }else  if (record.get("isStamp") == "1") {
            return 'x-grid-row-summary';
        }
        else if (record.get("changeStatus") == "变更") {
            return 'erp-grid-font-color-blue';
        } else if (record.get("changeStatus") == "原始") {
            return 'erp-grid-font-color-gray';
        } else if (record.get("changeStatus") == "删除") {
            return 'erp-grid-row-delete';
        } else {
            if (this.cid == "prmBudgetOutsourceCDto" || this.cid == "prmBudgetAccessoryCDto" || this.cid == "prmBudgetPrincipalCDto") {
                var amount = Scdp.ObjUtil.isEmpty(record.get('amount')) ? 0 : record.get('amount');
                var lockedAmount = Scdp.ObjUtil.isEmpty(record.get('lockedAmount')) ? 0 : record.get('lockedAmount');
                var lockedMoney = Scdp.ObjUtil.isEmpty(record.get('lockedMoney')) ? 0 : record.get('lockedMoney');
                var totalValue = 0;
                if (this.cid == "prmBudgetPrincipalCDto") {
                    totalValue = Scdp.ObjUtil.isEmpty(record.get('schemingTotalValue')) ? 0 : record.get('schemingTotalValue');
                } else {
                    totalValue = Scdp.ObjUtil.isEmpty(record.get('totalValue')) ? 0 : record.get('totalValue');
                }
                if (amount < lockedAmount || totalValue < lockedMoney) {
                    return 'erp-grid-row-color-lightsalmon';
                }
            } else if (this.cid == "prmBudgetRunCDto") {
                var lockedMoney = Scdp.ObjUtil.isEmpty(record.get('lockedMoney')) ? 0 : record.get('lockedMoney');
                var totalValue = Scdp.ObjUtil.isEmpty(record.get('totalValue')) ? 0 : record.get('totalValue');
                if (totalValue < lockedMoney) {
                    return 'erp-grid-row-color-lightsalmon';
                }
            } else {
                return null;
            }
        }
    },

    splitOutsource: function () {
        var view = this;
        var grid = view.getCmp("prmBudgetOutsourceCDto");
        var selectedRecord = grid.getCurRecord();
        var curIndex = grid.getStore().indexOf(selectedRecord);
        if (Scdp.ObjUtil.isNotEmpty(selectedRecord)) {
            if (view.checkIfSplit(selectedRecord)) {
                var data = Ext.clone(selectedRecord.data);
                var items = grid.store.data.items;
                var serialNumberAndIndex = view.splitInsert(data.serialNumber, items);
                if (Scdp.ObjUtil.isNotEmpty(serialNumberAndIndex)) {
                    data["splitFromUuidNo"] = data.serialNumber;
                    data["splitFromUuid"] = data.uuid;
                    data["uuid"] = null;
                    data["preTotalValue"] = null;
                    data["lastUuid"] = null;
                    data.serialNumber = serialNumberAndIndex[0];
                    var store = grid.getStore();
                    var rowModel = Ext.ModelManager.create({}, grid.store.model.modelName);
                    if (Scdp.ObjUtil.isNotEmpty(data)) {
                        var keys = Scdp.ObjUtil.getKeys(data);
                        Ext.each(keys, function (key) {
                            rowModel.beginEdit();
                            rowModel.set(key, data[key]);
                            rowModel.endEdit();
                        });
                    }
                    rowModel.set('seqNo', Scdp.getMaxSnoInStore(store) + 1);
                    var newIndex = serialNumberAndIndex[1];
                    if (newIndex == 0) {
                        newIndex = curIndex;
                    }
                    selectedRecord.set('amount', 0);
                    selectedRecord.set('totalValue', 0);
                    grid.store.insert(newIndex + 1, rowModel);
                    grid.setCurRecord(curIndex);
                }
            }
        }
    },

    splitAccessory: function () {
        var view = this;
        var grid = view.getCmp("prmBudgetAccessoryCDto");
        var selectedRecord = grid.getCurRecord();
        var curIndex = grid.getStore().indexOf(selectedRecord);
        if (Scdp.ObjUtil.isNotEmpty(selectedRecord)) {
            if (view.checkIfSplit(selectedRecord)) {
                var data = Ext.clone(selectedRecord.data);
                var items = grid.store.data.items;
                var serialNumberAndIndex = view.splitInsert(data.serialNumber, items);
                if (Scdp.ObjUtil.isNotEmpty(serialNumberAndIndex)) {
                    data["splitFromUuidNo"] = data.serialNumber;
                    data["splitFromUuid"] = data.uuid;
                    data["uuid"] = null;
                    data["preTotalValue"] = null;
                    data["lastUuid"] = null;
                    data.serialNumber = serialNumberAndIndex[0];
                    var store = grid.getStore();
                    var rowModel = Ext.ModelManager.create({}, grid.store.model.modelName);
                    if (Scdp.ObjUtil.isNotEmpty(data)) {
                        var keys = Scdp.ObjUtil.getKeys(data);
                        Ext.each(keys, function (key) {
                            rowModel.beginEdit();
                            rowModel.set(key, data[key]);
                            rowModel.endEdit();
                        });
                    }
                    rowModel.set('seqNo', Scdp.getMaxSnoInStore(store) + 1);
                    var newIndex = serialNumberAndIndex[1];
                    if (newIndex == 0) {
                        newIndex = curIndex;
                    }
                    selectedRecord.set('amount', 0);
                    selectedRecord.set('totalValue', 0);
                    grid.store.insert(newIndex + 1, rowModel);
                    grid.setCurRecord(curIndex);
                }
            }
        }
    },

    splitPrincipal: function () {
        var view = this;
        var grid = view.getCmp("prmBudgetPrincipalCDto");
        var selectedRecord = grid.getCurRecord();
        var curIndex = grid.getStore().indexOf(selectedRecord);
        if (Scdp.ObjUtil.isNotEmpty(selectedRecord)) {
            if (view.checkIfSplit(selectedRecord)) {
                if (view.checkIfSplit(selectedRecord)) {
                    var data = Ext.clone(selectedRecord.data);
                    var items = grid.store.data.items;
                    var serialNumberAndIndex = view.splitInsert(data.serialNumber, items);
                    if (Scdp.ObjUtil.isNotEmpty(serialNumberAndIndex)) {
                        data["splitFromUuidNo"] = data.serialNumber;
                        data["splitFromUuid"] = data.uuid;
                        data["uuid"] = null;
                        data["preTotalValue"] = null;
                        data["lastUuid"] = null;
                        data["contractAmount"] = 0;
                        data["contractPrice"] = 0;
                        data["contractTotalValue"] = 0;
                        data["schemingGrossProfit"] = -data["schemingTotalValue"];
                        data.serialNumber = serialNumberAndIndex[0];
                        var store = grid.getStore();
                        var rowModel = Ext.ModelManager.create({}, grid.store.model.modelName);
                        if (Scdp.ObjUtil.isNotEmpty(data)) {
                            var keys = Scdp.ObjUtil.getKeys(data);
                            Ext.each(keys, function (key) {
                                rowModel.beginEdit();
                                rowModel.set(key, data[key]);
                                rowModel.endEdit();
                            });
                        }
                        rowModel.set('seqNo', Scdp.getMaxSnoInStore(store) + 1);
                        var newIndex = serialNumberAndIndex[1];
                        if (newIndex == 0) {
                            newIndex = curIndex;
                        }
                        selectedRecord.set('amount', 0);
                        selectedRecord.set('schemingTotalValue', 0);
                        selectedRecord.set('schemingGrossProfit', selectedRecord.get('contractTotalValue'));
                        grid.store.insert(newIndex + 1, rowModel);
                        grid.setCurRecord(curIndex);
                    }
                }
            }
        }
    },

//下级拆分时的SerialNumber生成方法
    splitInsert: function (serialNum, items) {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(serialNum)) {
            var curIndex = 0;
            var prefix = serialNum + "-";
            var maxSuffixNum = 0;
            for (var i = 0; i < items.length; i++) {
                var perNum = items[i].get("serialNumber");
                if (Scdp.ObjUtil.isNotEmpty(perNum)) {
                    if (perNum.indexOf(prefix) == 0) {
                        var tmpSuffix = perNum.substr(prefix.length);
                        if (Scdp.ObjUtil.isNotEmpty(tmpSuffix)) {
                            var tmpSuffixNum = 0;
                            if (tmpSuffix.indexOf("-") == -1) {
                                tmpSuffixNum = Number(tmpSuffix);
                            } else {
                                tmpSuffixNum = Number(tmpSuffix.substr(0, tmpSuffix.indexOf("-")));
                            }
                            if (tmpSuffixNum > maxSuffixNum) {
                                maxSuffixNum = tmpSuffixNum;
                                curIndex = i;
                            }
                        }
                    }
                }
            }
            return [prefix + (maxSuffixNum + 1), curIndex];
        }
    },

    checkIfSplit: function (selectedRecord) {
        if (selectedRecord.get('subjectProperty') == "1") {
            Scdp.MsgUtil.warn("原始预算项为刚性科目，无法拆分！");
            return false
        }
        if (Scdp.ObjUtil.isEmpty(selectedRecord.get('lastUuid'))) {
            Scdp.MsgUtil.warn("非原始预算项，无法拆分！");
            return false
        }
        if (selectedRecord.get('preTotalValue') <= 0) {
            Scdp.MsgUtil.warn("原始预算项金额为0，无法拆分！");
            return false
        }
        return true;
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
                        var grid = this.ownerCt.ownerCt;
                        var valueN = view.calcColumnTotal(grid, dataIndex);
                        return  '<strong style="color: red">' + Ext.util.Format.number(valueN, '0,000.00') + '</strong>';
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
                        var grid = this.ownerCt.ownerCt;
                        var valueN = view.calcColumnTotal(grid, dataIndex);
                        return  '<strong style="color: red">' + "合计：" + Ext.util.Format.number(valueN, '0,000.00') + '</strong>';
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
                        var grid = this.ownerCt.ownerCt;
                        var valueN = view.calcColumnTotal(grid, dataIndex);
                        return  '<strong style="color: red">' + "合计：" + Ext.util.Format.number(valueN, '0,000.00') + '</strong>';
                    }
                });
            }
        });
    },
    calcColumnTotal: function (grid, dataIndex) {
        var gItems = grid.store.data.items;
        var gItemsLength = grid.store.data.items.length;
        var valueN = 0;
        for (var i = 0; i < gItemsLength; i++) {
            if (Scdp.ObjUtil.isEmpty(gItems[i].get("changeStatus")) || gItems[i].get("changeStatus") == "变更"
                || gItems[i].get("changeStatus") == "增加") {
                valueN = valueN + gItems[i].get(dataIndex);
            }
        }
        return valueN;
    }
})
;