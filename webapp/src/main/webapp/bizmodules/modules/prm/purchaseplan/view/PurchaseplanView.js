Ext.define('Purchaseplan.view.PurchaseplanView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/purchaseplan',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 60,
//    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'purchaseplan-query-layout.xml',
    editLayoutFile: 'purchaseplan-edit-layout.xml',
    //extraLayoutFile: 'purchaseplan-extra-layout.xml',
    bindings: ['prmPurchasePlanDto', 'prmPurchasePlanDto.prmPurchasePlanDetailDto', 'prmPurchasePlanDto.prmPurchasePackageDto', 'prmPurchasePlanDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Purchase_Plan',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.initEditToolbar();
        //初始化leftGrid
        me.initPackageGrid();
        //初始化rightGrid
        me.initPurchasePlanDetailGrid();
    },
    initEditToolbar: function () {
        var me = this;

        me.getEditToolbar().add({
                text: '分包',
                cid: 'subpackage',
                iconCls: 'temp_icon_16'
            }, {
                text: '加入包',
                cid: 'inPackage',
                iconCls: 'temp_icon_16'
            }, {
                text: '减出包',
                cid: 'outPackage',
                iconCls: 'temp_icon_16'
            }, {
//            xtype: 'button',
                text: 'Excel导入',
                cid: 'fileUpLoad',
                iconCls: 'temp_icon_16'
            },
            {
                xtype: 'button',
                text: Erp.I18N.PROJECT_TEMPLATE_DOWNLOAD,
                cid: 'downLoadTemplate',
                iconCls: 'temp_icon_16',
                disabled: false
            });
        me.getCmp("editPanel->modifyBtn").setDisabled(true);
        me.getCmp("editPanel->subpackage").setDisabled(true);
        me.getCmp("editPanel->inPackage").setDisabled(true);
        me.getCmp("editPanel->outPackage").setDisabled(true);
        me.getCmp("editPanel->fileUpLoad").setDisabled(true);
        //eidt-layout.xml新增、复制新增、删除按钮隐藏
        me.getCmp("editPanel->addNew2Btn").setVisible(false);
        me.getCmp("editPanel->copyAddBtn").setVisible(false);
        me.getCmp("editPanel->deleteBtn").setVisible(false);

        //query-layout.xml中新增、删除按钮的隐藏
        me.getCmp("queryPanel->addNew1Btn").setVisible(false);
        me.getCmp("queryPanel->batchDelBtn").setVisible(false);
    },
    afterSelectPackageRow: function (record) {
        var me = this;
        var packageGrid = me.getCmp("prmPurchasePackageDto");
        var uiStatus = me.getUIStatus();
        if (record && Scdp.Const.UI_INFO_STATUS_MODIFY == uiStatus) {
            var packageState = record.get("packageState");
            packageGrid.getCmp("toolbar->delRowBtn").setDisabled("2" == packageState || "4" == packageState);
        }
        me.controller.filterDetailByPackage();
    },
    initPackageGrid: function () {
        var me = this;
        var packageGrid = me.getCmp("prmPurchasePackageDto");
        packageGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        packageGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        packageGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        packageGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        packageGrid.getCmp("toolbar->copyAddRowBtn").hide();

        packageGrid.getEditToolbar().add({
                xtype: 'button',
                cid: "showRevise",
                tooltip: Erp.I18N.SHOWREVISE,
                iconCls: "file_preview_icon",
                listeners: {
                    click: function (btn) {
                        me.showRevise();
                    }
                }
            }
        );
        //packageGrid.getCmp("toolbar->showRevise").setDisabled(false);

        var selectPackageRow = function (record) {
            var uiStatus = me.getUIStatus();
            if (record && Scdp.Const.UI_INFO_STATUS_MODIFY == uiStatus) {
                var packageState = record.get("packageState");
                packageGrid.getCmp("toolbar->delRowBtn").setDisabled("2" == packageState || "4" == packageState);
            }
        };

        packageGrid.afterSelect = function (record, index) {
            me.afterSelectPackageRow(record);
        };
//        packageGrid.on("select", function (grid, record, index) {
//            selectPackageRow(record);
//        });

        packageGrid.rowAddFn = function (btn) {
            //如果采购包没有数据时直接新增，不复制数据
//            if (packageGrid.getStore().data.items.length == 0) {
            packageGrid.doAddRow(btn);
//            } else {
//                packageGrid.doCopyAddRow(btn);
//            }
        };
        //新增包时
        packageGrid.afterAddRow = function () {
            var currRecord = packageGrid.getCurRecord();
            if (currRecord) {
                var prmProjectMainId = me.getCmp("prmPurchasePlanDto->prmProjectMainId").gotValue();
                var maxPackageNo = this.getMaxPackageNo();
                currRecord.set("uuid", Scdp.StrUtil.getUUID());
                currRecord.set("prmProjectMainId", prmProjectMainId);
                currRecord.set("packageNo", maxPackageNo + 1);
                currRecord.set("packageName", "包" + (maxPackageNo + 1));
                currRecord.set("pendingMoney", 0);
                currRecord.set("packageBudgetMoney", 0);
                currRecord.set("packageState", "0");
                selectPackageRow(currRecord);
            }
        };
        packageGrid.getMaxPackageNo = function () {
            var items = this.store.data.items;
            var maxPackageNo = 0;
            for (var i = 0; i < items.length; i++) {
                var tempNo = items[i].get("packageNo");
                if (maxPackageNo < tempNo) {
                    maxPackageNo = tempNo;
                }
            }
            return maxPackageNo;
        };

        var detailGrid = me.getCmp("prmPurchasePlanDetailDto");
        packageGrid.beforeDeleteRow = function (records, rowIndex) {
            if (records && records.length > 0) {
                for (var i = 0; i < records.length; i++) {
                    var record = records[i];
                    if (record.get("packageState") == '0') {
                        var detailItems = me.controller.getPackageDetailAllItems();
                        for (var j = 0; j < detailItems.length; j++) {
                            if (detailItems[j].get("purchasePackageId") == record.get("uuid")) {
                                Scdp.MsgUtil.info("该包有采购计划明细，不能删除！");
                                return false;
                            }
                        }
                    } else {
                        Scdp.MsgUtil.info("包状态不是新增状态，不能删除！");
                        return false;
                    }
                }
            }
            return true;
        };
        packageGrid.on("beforeedit", function (editor, eventObj) {
            var record = eventObj.record;
            var dataIndex = eventObj.field;
            var roleName = Erp.Util.getCurrentUserRoleName();
            if (roleName && roleName.ROLE.indexOf("供应链部采购计划主管") > -1) {
                if (dataIndex == "materialClassCode") {
                    return true;
                }
            }
            if (record.get("packageState") != "2" && record.get("packageState") != "4") {
                if (dataIndex == "packageName" || dataIndex == "materialClassCode" || dataIndex == "description") {
                    return true;
                }
            }
            if (dataIndex == "arriveDate") {
                return true;
            }
            return false;
        });
        //packageGrid.afterEditGrid=function(eventObj,isChanged){
        //    if(!isChanged){
        //        return;
        //    }
        //    var grid = eventObj.grid;
        //    var record = eventObj.record;
        //    var dataIndex = eventObj.field;
        //    if(dataIndex=="subjectProperty"){
        //        Ext.Array.each(detailGrid.store.data.items,function(item){
        //            item.set("subjectProperty",record.get(dataIndex));
        //        });
        //    }
        //    if(dataIndex=="purchaseLevel"){
        //        Ext.Array.each(detailGrid.store.data.items,function(item){
        //            item.set("purchaseLevel",record.get(dataIndex));
        //        });
        //    }
        //};

    },
    initPurchasePlanDetailGrid: function () {
        var me = this;
        var detailGrid = me.getCmp("prmPurchasePlanDetailDto");
        detailGrid.rowColorConfigFn = me.rowColorConfigFnEGrid;
//        //把“+”号按钮功能实现设置为拷贝行
//        detailGrid.getCmp("toolbar->addRowBtn").setTooltip("平级拆分");
//        detailGrid.getCmp("toolbar->copyAddRowBtn").setTooltip("下级拆分");
//        detailGrid.getCmp("toolbar").items.items[3].hide();
//        detailGrid.getCmp("toolbar").items.items[4].hide();
//        detailGrid.getCmp("toolbar").items.items[5].hide();
//        detailGrid.getCmp("toolbar").items.items[6].hide();
//
//        //detailGrid.on("select", function (grid, record, index) {
//        //    me.selectDetailRow(me, detailGrid, record);
//        //});
//        detailGrid.afterSelect = function (record, rowIndex) {
//            me.selectDetailRow(me, detailGrid, record);
//        };
//        detailGrid.rowAddFn = function (btn) {
//            var record = detailGrid.getCurRecord();
//            if (record) {
//                if (record.get("isClose")) {
//                    Scdp.MsgUtil.info("关闭的包不能进行拆分！");
//                    return;
//                }
//                if (btn.cid == "addRowBtn") {
//                    me.copyForSameLevel = true;
//                } else {
//                    me.copyForSameLevel = false;
//                }
//                detailGrid.doCopyAddRow(btn);
//            }
//        };
//        detailGrid.beforeCopyAddRow = function (data) {
//            var items = me.controller.getPackageDetailAllItems();
//            if (me.copyForSameLevel) {
//                data.serialNumber = me.addInsert(data.serialNumber, data.prmBudgetType, data.prmBudgetRefId, items);
//            } else {
//                data.serialNumber = me.copyInsert(data.serialNumber, data.prmBudgetType, data.prmBudgetRefId, items);
//            }
//            data["budgetPrice"] = 0;
//            data["isBudget"] = 0;
//            data["isReq"] = 0;
//            data["isClose"] = 0;
//            data["purchaseBudgetMoney"] = 0;
//            data["appliedAmount"] = 0;
//            data["oriBudgetAmount"] = null;
//            data["oriBudgetPrice"] = null;
//            data["oriBudgetTotalValue"] = null;
//
//            data["arriveDate"] = Scdp.DateUtil.parseDate(Scdp.DateUtil.formatDate(new Date(), "Y-m-d"));
//        };
//        detailGrid.doCopyAddRow = function (btn) {
//            var me = this;
//            if (me.validateCurrentRowBindForm()) {
//                var grid = btn.ownerCt.ownerCt;
//                var curRecord = grid.getCurRecord();
//
//                if (Scdp.ObjUtil.isEmpty(curRecord)) {
//                    Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
//                    return;
//                }
//                var data = Ext.clone(curRecord.data);
//
//                Scdp.wrapDataForMerge(data);
//                Scdp.wrapDataForRemoveSystemFields(data);
//
//                if (grid.beforeCopyAddRow) {
//                    if (grid.beforeCopyAddRow(data) === false) {
//                        return;
//                    }
//                }
//                var store = grid.getStore();
//                var rowModel = Ext.ModelManager.create({}, grid.store.model.modelName);
//                if (Scdp.ObjUtil.isNotEmpty(data)) {
//                    var keys = Scdp.ObjUtil.getKeys(data);
//                    Ext.each(keys, function (key) {
//                        rowModel.beginEdit();
//                        rowModel.set(key, data[key]);
//                        rowModel.endEdit();
//                    });
//                }
//                var curIndex = grid.getStore().indexOf(curRecord);
//                rowModel.set('seqNo', Scdp.getMaxSnoInStore(store) + 1);
//                grid.store.insert(curIndex + 1, rowModel);
//                grid.setCurRecord(curIndex + 1);
//                grid.setReadOnlyCss(grid);
//                var curCol = 1;
//                if (grid.withSelMode) {
//                    curCol = 2;
//                }
//                grid.startEditByPosition(curIndex + 1, curCol);
//
//                var view = grid.up("IView");
//                var bindCompArr = view.getCmp(grid.cid + ".bind", true);
//                if (Scdp.ObjUtil.isNotEmpty(bindCompArr)) {
//                    for (var i = 0; i < bindCompArr.length; i++) {
//                        var bindComp = bindCompArr[i];
//                        if (Scdp.ObjUtil.isNotEmpty(bindComp) && bindComp.isXType('JForm')) {
//                            bindComp.getFirstEditableField().focus();
//                            break;
//                        }
//                    }
//                }
//
//                if (grid.afterCopyAddRow) {
//                    grid.afterCopyAddRow();
//                }
//            }
//        };
//        detailGrid.afterCopyAddRow = function () {
//            me.selectDetailRow(me, detailGrid, detailGrid.getCurRecord());
//        };
//        detailGrid.beforeDeleteRow = function (records) {
//            var detailItems = me.controller.getPackageDetailAllItems();
//            if (records && records.length > 0) {
//                for (var i = 0; i < records.length; i++) {
//                    var record = records[i];
//                    if (record.get("isBudget") || record.get("isReq") || record.get("isClose")) {
//                        Scdp.MsgUtil.info("关闭的包明细、已提过采购申请的明细和原始明细不能删除！");
//                        return false;
//                    }
//                    var serialNumber = record.get("serialNumber");
//                    var prmBudgetRefId = record.get("prmBudgetRefId");
//                    var prmBudgetType = record.get("prmBudgetType");
//                    for (var j = 0; j < detailItems.length; j++) {
//                        var item = detailItems[j];
//                        if (prmBudgetRefId == item.get("prmBudgetRefId") && prmBudgetType == item.get("prmBudgetType")
//                            && item.get("serialNumber") && item.get("serialNumber").startsWith(serialNumber + "-")) {
//                            Scdp.MsgUtil.warn("该明细已进行下级拆分，不能删除！");
//                            return false;
//                }
//            }
//                }
//            }
//            return true;
//        };
//        detailGrid.afterDeleteRow = function () {
//            //todo need recalculate the package pending amount
//            me.controller.recalculatePackageMoney();
//        };
        detailGrid.on('beforeedit', function (editor, eventObj) {
            var field = eventObj.field;
            if (field == "subjectProperty" || field == "purchaseLevel" || field == "prmBudgetType") {
                return false;
            }
            var grid = eventObj.grid;
            var record = eventObj.record;
            if (record.get("isClose") && field != "budgetPrice") {//record.get("isReq") ||
                return false;
            } else if (record.get("prmBudgetType") == "RUN" && (field == "supplierId" || field == "supplierProperty")) {//record.get("isReq") ||
                return false;
            } else {
                return grid.editable;
            }

        });
//        detailGrid.afterEditGrid = function (eventObj, isChanged) {
//            if (!isChanged) {
//                return;
//            }
//            var originalValue = eventObj.originalValue;
//            var newValue = eventObj.value;
//            var record = eventObj.record;
//            var dataIndex = eventObj.field;
//            var subjectProperty = record.get("subjectProperty");
//            //1 is 刚性需求，2 is 弹性
//            if (dataIndex == "amount" && subjectProperty == "1") {
//                var originalAmount = record.get("originalAmount");
//                if (Scdp.ObjUtil.isNotEmpty(newValue)) {
//                    if (newValue > originalAmount) {
//                        Scdp.MsgUtil.info("刚性的采购明细中，数量不能超过原始数量！");
//                        record.set("amount", originalValue);
//                        return;
//                    }
//                }
//            }
//            if (dataIndex == "amount" || dataIndex == "budgetPrice") {
//                var budgetPrice = record.get("budgetPrice");
//                var amount = record.get("amount");
//                record.set("purchaseBudgetMoney", Erp.MathUtil.multiNumber(amount, budgetPrice));
//
//                var subPackageNo = record.get("subPackageNo");
//                if (subPackageNo) {
//                    me.controller.recalculatePackageMoney([subPackageNo]);
//                }
//            }
//        };
        detailGrid.startFilter = function () {
            var me = this;
            var store = me.getStore();
            var view = me.up("IView");
            var checked = view.getCmp("prmPurchasePlanDto->showAllDetail").gotValue();
            var selectPackage = view.getCmp("prmPurchasePackageDto").getCurRecord();

            me.searchValue = Scdp.StrUtil.replaceNull(me.getSearchValue());
            me.searchRegExp = new RegExp(me.searchValue, (me.caseSensitive && !me.regExpMode ? '' : 'i'));
            store.filterBy(function (record) {
                var isMatch1 = (checked && checked == 1) || (Scdp.ObjUtil.isEmpty(selectPackage))
                    || (selectPackage.get('uuid') == record.get("purchasePackageId"));
                var isMatch2 = true;
                if (isMatch1 && me.filterStatus && Scdp.ObjUtil.isNotEmpty(me.getSearchValue())) {
                    isMatch2 = false;
                    for (var i = 0; i < me.searchFilterCids.length; i++) {
                        var item = me.searchFilterCids[i];
                        if (me.searchRegExp.test(record.get(item))) {
                            isMatch2 = true;
                            break;
                        }
                    }
                }

                return isMatch1 && isMatch2;
            });
        };
        detailGrid.clearFilter = function () {
            var me = this;
            me.startFilter();
        };
    },
    rowColorConfigFnEGrid: function (record) {
        if (record.get("prmBudgetType") == "RUN") {
            return 'erp-grid-font-color-blue';
        }
    },
    selectDetailRow: function (view, detailGrid, record) {
        var uiStatus = view.getUIStatus();
        if (Scdp.Const.UI_INFO_STATUS_MODIFY == uiStatus) {
            if (record) {
                detailGrid.getCmp("toolbar->addRowBtn").setDisabled(record.get("isClose"));
                detailGrid.getCmp("toolbar->delRowBtn").setDisabled(record.get("isReq") || record.get("isClose") || record.get("isBudget"));
                detailGrid.getCmp("toolbar->copyAddRowBtn").setDisabled(record.get("isClose"));
            }
        }
    },
    setUIStatus: function () {
        var me = this;
        me.callParent(arguments);
        if (me.uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            //var rightGrid = me.getCmp("prmPurchasePlanDetailDto");
            //rightGrid.getCmp("toolbar->copyAddRowBtn").setDisabled(true);
            //rightGrid.getCmp("toolbar->delRowBtn").setDisabled(true);
            //rightGrid.getCmp("toolbar->addRowBtn").setDisabled(true);
            var leftGrid = me.getCmp("prmPurchasePackageDto");
            leftGrid.getCmp("toolbar->addRowBtn").setDisabled(true);
            leftGrid.getCmp("toolbar->delRowBtn").setDisabled(true);

            me.getCmp("editPanel->subpackage").setDisabled(true);
            me.getCmp("editPanel->inPackage").setDisabled(true);
            me.getCmp("editPanel->outPackage").setDisabled(true);
            me.getCmp("editPanel->fileUpLoad").setDisabled(true);
        } else if (me.uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY) {
            me.getCmp("editPanel->subpackage").setDisabled(false);
            me.getCmp("editPanel->inPackage").setDisabled(false);
            me.getCmp("editPanel->outPackage").setDisabled(false);
            me.getCmp("editPanel->fileUpLoad").setDisabled(false);
        }
    },
    UIStatusChanged: function (view, newStatus) {
        if (Scdp.Const.UI_INFO_STATUS_NULL != newStatus) {
            view.getCmp("prmPurchasePlanDto->showAllDetail").sotEditable(true);
        }

    },
    //下级拆分时的SerialNumber生成方法
    copyInsert: function (serialNum, budgetType, budgetRefId, items) {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(serialNum)) {
            var flag = 1;
            var prefix = serialNum + "-";
            var maxSuffixNum = 0;
            for (var i = 0; i < items.length; i++) {
                var perNum = items[i].get("serialNumber");
                var thisBudgetType = items[i].get("prmBudgetType");
                if (thisBudgetType == budgetType && Scdp.ObjUtil.isNotEmpty(perNum)) {
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
                            }
                        }
                    }

                }
            }
            return prefix + (maxSuffixNum + 1);
        } else {
            return me.addInsert(serialNum, budgetType, budgetRefId, items);
        }
    },
    //平级拆分时SerialNumber生成方法
    addInsert: function (serialNum, budgetType, budgetRefId, items) {
        if (Scdp.ObjUtil.isEmpty(serialNum)) {
            serialNum = "0";
        }
        var prefix = "";
        var maxSuffixNum = 0;
        var index = serialNum.lastIndexOf("-");
        prefix = serialNum.substr(0, index + 1);
        maxSuffixNum = Number(serialNum.substr(index + 1));

        for (var i = 1; i < items.length; i++) {
            var perNum = items[i].get("serialNumber");
            var thisBudgetType = items[i].get("prmBudgetType");
            if (thisBudgetType == budgetType && Scdp.ObjUtil.isNotEmpty(perNum)) {
                var tmpSuffix = "";
                if ((prefix == "") || (prefix != "" && perNum.indexOf(prefix) == 0)) {
                    tmpSuffix = perNum.substr(index + 1);
                    var tmpSuffixNum = 0;
                    if (tmpSuffix.indexOf("-") == -1) {
                        tmpSuffixNum = Number(tmpSuffix);
                    } else {
                        tmpSuffixNum = Number(tmpSuffix.substr(0, tmpSuffix.indexOf("-")));
                    }
                    if (tmpSuffixNum > maxSuffixNum) {
                        maxSuffixNum = tmpSuffixNum;
                    }
                }
            }
        }
        return prefix + (maxSuffixNum + 1);
    },
    showRevise: function () {
        var view = this;
        var purchaseDetailGrid = view.getCmp("prmPurchasePackageDto");
        var selectPackage = purchaseDetailGrid.getSelectionModel().getSelection();
        if (selectPackage.length == 0) {
            Scdp.MsgUtil.warn("请选择记录！");
            return;
        }
        var uuid = selectPackage[0].get("uuid");
        var callBack = function (subView) {
            return true;
        };
        var subView = Ext.create("Purchaseplan.view.PurchasePackageReviseView", {
            aHeight: 300,
            aWidth: 550,
            closable: false,
            isPopup: false,
            hideScroll: true,
            plain: true,
            layout: 'border',
            //layout: {
            //    type: 'vbox',
            //    align: 'center'
            //},
            uuid: uuid
        });
        Scdp.openNewWinByView(subView, callBack, 'temp_icon_16', '变更记录', "确认");

    }
});