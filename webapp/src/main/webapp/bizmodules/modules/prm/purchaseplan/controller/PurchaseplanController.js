Ext.define("Purchaseplan.controller.PurchaseplanController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Purchaseplan.view.PurchaseplanView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'subpackage', name: 'click', fn: 'doPartPackage'},
        {cid: 'fileUpLoad', name: 'click', fn: 'doFileUpload'},
        {cid: 'inPackage', name: 'click', fn: 'inPackage'},
        {cid: 'outPackage', name: 'click', fn: 'outPackage'},
        {cid: 'showAllDetail', name: 'change', fn: 'filterDetailByPackage'},
//        {cid: 'prmPurchasePackageDto', name: 'select', fn: 'filterDetailByPackage'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'downLoadTemplate', name: 'click', fn: 'downLoadTemplate'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto',
    queryAction: 'purchaseplan-query',
    loadAction: 'purchaseplan-load',
    //addAction: 'purchaseplan-add',
    modifyAction: 'purchaseplan-modify',
    //deleteAction: 'purchaseplan-delete',
    exportXlsAction: "purchaseplan-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        var packageGrid = me.view.getCmp("prmPurchasePackageDto");
        me.packageGridDataAfterUptate(packageGrid);
        var role = Erp.Util.getCurrentUserRoleName();
        if (Scdp.ObjUtil.isNotEmpty(role) && role.ROLE.indexOf("采购计划采购包调整") > -1) {
            me.view.getCmp('prmPurchasePlanDetailDto->changePackage').items.push({
                iconCls: 'refresh_btn',
                width: '16',
                height: '16',
                handler: function (grid, rowIndex) {
                    var view = me.view;
                    var selectedReqDetailGrid = view.getCmp("prmPurchasePlanDetailDto");
                    var oldPackageNo = selectedReqDetailGrid.getStore().getAt(rowIndex).get("subPackageNo");
                    var oldBudgetCode = selectedReqDetailGrid.getStore().getAt(rowIndex).get("prmBudgetType");
                    var purchaseDetailuuid = selectedReqDetailGrid.getStore().getAt(rowIndex).get("uuid");
                    var prmProjectMainId = selectedReqDetailGrid.getStore().getAt(rowIndex).get("prmProjectMainId");
                    var oldPackageId = selectedReqDetailGrid.getStore().getAt(rowIndex).get("purchasePackageId");
                    var callBack = function (subView) {
                        var uuid = subView.getCmp("purchasePackageName").gotValue();
                        if (uuid != null) {//调包
                            var newPackageNo = subView.getCmp("packageNo").gotValue();
                            var newBudgetCode = subView.getCmp("budgetCode").gotValue();
                            if (Scdp.ObjUtil.isNotEmpty(oldPackageNo)) {
                                //有包号，调包
                                if (Scdp.ObjUtil.isNotEmpty(newBudgetCode) && Scdp.ObjUtil.isNotEmpty(oldBudgetCode) && oldBudgetCode != newBudgetCode) {
                                    Scdp.MsgUtil.info("包类型不同，不能进行调包");
                                    return false;
                                }

                                if (Scdp.ObjUtil.isNotEmpty(newPackageNo) && newPackageNo == oldPackageNo) {
                                    Scdp.MsgUtil.info("不能调入同一个包");
                                    return false;
                                }

                            } else {
                                //无包号，加包
                                if (Scdp.ObjUtil.isNotEmpty(newBudgetCode) && Scdp.ObjUtil.isNotEmpty(oldBudgetCode) && oldBudgetCode != newBudgetCode) {
                                    Scdp.MsgUtil.info("包类型不同，不能调入此包");
                                    return false;
                                }

                            }

                            var postData = {
                                newPackageId: uuid,
                                oldPackageId: oldPackageId,
                                purchaseDetailuuid: purchaseDetailuuid,
                                changeType: Scdp.ObjUtil.isEmpty(oldPackageNo) ? "2" : "0"//0：调包  2：加包
                            };
                            Scdp.doAction("purchaseplan-replacepackage", postData, function (retData) {
                                if (retData.success == true) {
                                    Scdp.MsgUtil.info("调包成功");
                                    var prmPurchaseUuid = me.view.getCmp("prmPurchasePlanDto->uuid").gotValue();
                                    me.loadItem(prmPurchaseUuid);
                                }
                            });
                        } else {//减包

                            if (Scdp.ObjUtil.isEmpty(oldPackageNo)) {
                                Scdp.MsgUtil.info("该明细没有进行分包，不需要减包");
                                return false;
                            }

                            var postData = {
                                newPackageId: uuid,
                                oldPackageId: oldPackageId,
                                purchaseDetailuuid: purchaseDetailuuid,
                                changeType: "1"
                            };
                            Scdp.doAction("purchaseplan-replacepackage", postData, function (retData) {
                                if (retData.success == true) {
                                    Scdp.MsgUtil.info("减包成功");
                                    var prmPurchaseUuid = me.view.getCmp("prmPurchasePlanDto->uuid").gotValue();
                                    me.loadItem(prmPurchaseUuid);
                                }
                            });
                        }

                        win.close();
                    };

                    var form = Ext.widget("JForm", {
                        height: 80,
                        width: 400,
                        layout: 'form',
                        items: [
                            {
                                xtype: 'JHidden',
                                cid: 'prmProjectMainId',
                                value: prmProjectMainId
                            },
                            {
                                xtype: 'JHidden',
                                cid: 'oldBudgetCode',
                                value: oldBudgetCode
                            },
                            {
                                xtype: 'JHidden',
                                cid: 'purchasePackageNameDesc'
                            },
                            {
                                xtype: 'JPrmPackageGrid',
                                cid: 'purchasePackageName',
                                allowBlank: false,
                                minValue: '0',
                                filterFields: 'prmProjectMainId|prmProjectMainId,oldBudgetCode|budgetCode',
                                valueField: 'uuid',
                                descField: 'packageName',
                                fieldLabel: '采购包',
                                refer: [
                                    {
                                        "refField": "packageNo",
                                        "valueField": "packageNo"
                                    },
                                    {
                                        "refField": "budgetCode",
                                        "valueField": "budgetCode"
                                    }]
                            },
                            {
                                xtype: 'JHidden',
                                cid: 'packageNo'
                            },
                            {
                                xtype: 'JHidden',
                                cid: 'budgetCode'
                            }
                        ]
                    });
                    win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '调包', "确认");
                }
            });
        }
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
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
        var packageGrid = me.view.getCmp("prmPurchasePackageDto");
        var state = me.view.getCmp("prmPurchasePlanDto->purchasePlanState").gotValue();
        me.view.afterSelectPackageRow(packageGrid.getCurRecord());
        if ('2' == state) {
            packageGrid.getCmp("toolbar->addRowBtn").setDisabled(true);
            packageGrid.getCmp("toolbar->delRowBtn").setDisabled(true);
        }
        if ('0' != state && '5' != state) {
            me.view.getCmp("editPanel->subpackage").setDisabled(true);
            me.view.getCmp("editPanel->inPackage").setDisabled(true);
            me.view.getCmp("editPanel->outPackage").setDisabled(true);
        }
    },
    getPackageDetailAllItems: function () {
        var me = this;
        var detailStore = me.view.getCmp("prmPurchasePlanDetailDto").store;
        return detailStore.snapshot ? detailStore.snapshot.items : detailStore.data.items;
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var projectMainId = view.getCmp("prmPurchasePlanDto->prmProjectMainId").gotValue();
        var detailItems = me.getPackageDetailAllItems();
        var planDetailsGroup = {};
        Ext.Array.each(detailItems, function (item) {
            var budgetType = item.get("prmBudgetType");
            var budgetRefId = item.get("prmBudgetRefId");
            var budgetMoney = item.get("purchaseBudgetMoney");
            var amount = item.get("amount");
            var subjectProperty = item.get("subjectProperty");
            var planDetails = planDetailsGroup[budgetType];
            if (!planDetails) {
                planDetails = {};
                planDetailsGroup[budgetType] = planDetails;
            }
            var detailItem = planDetails[budgetRefId];
            if (detailItem) {
                detailItem.purchaseBudgetMoney = Erp.MathUtil.plusNumber(detailItem.purchaseBudgetMoney, budgetMoney);
                if (subjectProperty == "1") {
                    detailItem.subjectProperty = subjectProperty;
                    //刚性需求的数量
                    var rigidAmounts = detailItem.rigidAmounts;
                    if (!rigidAmounts) {
                        rigidAmounts = [];
                        detailItem.rigidAmounts = rigidAmounts;
                    }
                    rigidAmounts.push({serialNumber: item.get("serialNumber"), amount: amount});
                }
            } else {
                detailItem = {
                    prmBudgetRefId: budgetRefId,
                    purchaseBudgetMoney: budgetMoney,
                    subjectProperty: subjectProperty
                };
                if (subjectProperty == "1") {
                    var rigidAmounts = [];
                    detailItem.rigidAmounts = rigidAmounts;
                    rigidAmounts.push({serialNumber: item.get("serialNumber"), amount: amount});
                }
                planDetails[budgetRefId] = detailItem;
            }
        });
        var postData = {};
        postData.prmProjectMainId = projectMainId;
        for (var budgetType in planDetailsGroup) {
            var items = [];
            var planDetails = planDetailsGroup[budgetType];
            for (var budgetRefId in planDetails) {
                items.push(planDetails[budgetRefId]);
            }
            postData[budgetType] = items;
        }
        var validateResult = Scdp.doAction("prm-purchaseplan-save-validation", postData, null, null, true, false);
        if (validateResult.errorMsg) {
            Erp.Util.showLogView("保存失败：" + validateResult.errorMsg);
            return false;
        }

        //reset the subject property when amount>500,000
        /*        var packageItems = view.getCmp("prmPurchasePackageDto").getStore().data.items;
         Ext.Array.each(packageItems, function (item) {
         var budgetMoney = item.get("packageBudgetMoney");
         var pendingMoney = item.get("pendingMoney");
         var packageNo = item.get("packageNo");
         if (budgetMoney >= 500000 || pendingMoney > 500000) {
         if (Scdp.ObjUtil.isNotEmpty(item.get("purchaseLevel")) && item.get("purchaseLevel") != "1") {
         item.set("purchaseLevel", "1");
         Ext.Array.each(detailItems, function (item1) {
         if (item1.get("subPackageNo") == packageNo) {
         item1.set("purchaseLevel", "1");
         }
         });
         }
         }
         });*/
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        //获得query-layout中选择的主表数据行
        //removed by liujingyuan, who think it is wrong logic.
        //var curRecord = me.view.getQueryPanel().down("JQGrid").getCurRecord();
        //me.loadItem(curRecord.data.uuid);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.refreshWorkFlowStatus();
        var packageGrid = me.view.getCmp("prmPurchasePackageDto");
        me.view.afterSelectPackageRow(packageGrid.getCurRecord());
        var modifyBtn = me.view.getCmp("editPanel->modifyBtn");
        me.view.getCmp("editPanel->showRevise").setDisabled(false);
        if (me.getPackageDetailAllItems().length == 0) {
            modifyBtn.setDisabled(true);
        }


    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("editPanel->showRevise").setDisabled(false);

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
    //文件上传
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
                    fieldLabel: '上传文件名',
                    cid: 'uploadFile'
                }
            ]
        });

        var callBack = function (subView) {
            var uploadFile = subView.getCmp("uploadFile");
            var uploadData = [];
            if (Scdp.ObjUtil.isEmpty(uploadFile.gotValue())) {
                Scdp.MsgUtil.info("请选择文件!");
                return;
            }
            var purchaseplanDetailUuid = view.getCmp("prmPurchasePlanDto->uuid").value;
            uploadData.push(purchaseplanDetailUuid);
            var postData = {uploadMeta: uploadData};
            Scdp.doAction("purchaseplan-detail-fileupload", postData, function (result) {
                if (result.viewData) {
                    Scdp.MsgUtil.info(result.viewData);
                } else {
                    if (result.listDatas) {
                        win.close();
                        var items = result.listDatas;
                        for (var i = 0; i < items.length; i++) {
                            var serialNumber = items[i].data.serialNumber;
                            var budgetType = items[i].data.budgetType;
                            for (var j = (i + 1); j < items.length; j++) {
                                if (serialNumber == items[j].data.serialNumber && budgetType == items[j].data.prmBudgetType) {
                                    Scdp.MsgUtil.warn("存在相同序号，请检查Excel文件！");
                                    return;
                                }
                            }
                        }
                        //TODO need change this logic
                        var detailGrid = me.view.getCmp("prmPurchasePlanDetailDto");
                        var detailItems = me.getPackageDetailAllItems();
                        var newItems = [];
//                        var updateItems = [];
                        Ext.Array.each(items, function (item) {
                            var serialNumber = item.data.serialNumber;
                            var budgetTypeDesc = item.data.prmBudgetType;
                            var budgetTypeCode = "";
                            if (budgetTypeDesc == "主材") {
                                budgetTypeCode = "PRINCIPAL";
                            } else if (budgetTypeDesc == "辅材") {
                                budgetTypeCode = "ACCESSORY";
                            } else if (budgetTypeDesc == "外协") {
                                budgetTypeCode = "OUTSOURCE";
                            }
                            item.data.prmBudgetType = budgetTypeCode;
                            var founded = false;
                            for (var i = 0; i < detailItems.length; i++) {
                                var detailRecord = detailItems[i];
                                var subPackageNo = detailRecord.get("subPackageNo");
                                if (serialNumber == detailRecord.get("serialNumber") && budgetTypeCode == detailRecord.get("prmBudgetType")
                                ) {
                                    founded = true;
                                    if (Scdp.ObjUtil.isEmpty(subPackageNo)) {
                                        if (!detailRecord.get("isClose")) {
                                            detailRecord.set("model", item.data.model);
                                            detailRecord.set("factory", item.data.factory);
                                            detailRecord.set("arriveDate", item.data.arriveDate);
                                            detailRecord.set("supplierId", item.data.supplierId);
                                            detailRecord.set("supplierProperty", item.data.supplierProperty);
                                            detailRecord.set("amount", item.data.amount);
                                            detailRecord.set("budgetPrice", item.data.budgetPrice);
                                            detailRecord.set("purchaseBudgetMoney", item.data.purchaseBudgetMoney);
                                            detailRecord.set("remark", item.data.remark);
//                                            updateItems.push(detailRecord);
                                        }
                                    }
                                    break;
                                }
                            }
                            if (!founded) {
                                newItems.push(item.data);
                            }
                        });
                        detailGrid.addRowItems(newItems, false);
//                        me.doPartPackage();
                    }
                }
            }, null, null, null, subView.getForm());
        };
        var win = Scdp.openNewWinByView(formView, callBack, 'temp_icon_16', '文件上传', "上传文件");
    },
    //采购包EditGrid数据更新时操作
    packageGridDataAfterUptate: function (packageGrid) {
        var me = this;
        //grid更新数据时事件
        packageGrid.store.on('update', function (store, record, operation, mdColumns) {
            //与rightGrid数据联动
            if (mdColumns && mdColumns.indexOf('subjectProperty') != -1) {
                var detailItems = me.getPackageDetailAllItems();
                for (var i = 0; i < detailItems.length; i++) {
                    if (detailItems[i].get("subPackageNo") == record.data.packageNo && (!detailItems[i].get("isClose"))) {
                        detailItems[i].set("subjectProperty", record.data.subjectProperty);
                    }
                }
            }
            //与rightGrid数据联动
            if (mdColumns && mdColumns.indexOf('purchaseLevel') != -1) {
                var detailItems = me.getPackageDetailAllItems();
                for (var i = 0; i < detailItems.length; i++) {
                    if (detailItems[i].get("subPackageNo") == record.data.packageNo && (!detailItems[i].get("isClose"))) {
                        detailItems[i].set("purchaseLevel", record.data.purchaseLevel);
                    }
                }
            }
            //与rightGrid数据联动
            if (mdColumns && mdColumns.indexOf('arriveDate') != -1) {
                var detailItems = me.getPackageDetailAllItems();
                for (var i = 0; i < detailItems.length; i++) {
                    if (detailItems[i].get("subPackageNo") == record.data.packageNo && (!detailItems[i].get("isClose"))) {
                        detailItems[i].set("arriveDate", record.data.arriveDate);
                    }
                }
            }
        });
    },
    //分包操作  (isAuto:来源是否自动分包)
    doPartPackage: function (btn, event) {
        var me = this;
        var view = me.view;
        //所有采购计划明细数据
        var items = me.getPackageDetailAllItems();
        if (items.length == 0) {
            Scdp.MsgUtil.warn("没有采购计划明细数据！");
            return false;
        }

        var callback = function () {
            //已经有包号的
            var existSubPackages = {};
            //没有包号的
            var notSubPackages = {};
            Ext.Array.each(items, function (item) {
                var isClosed = item.get("isClose");
                if (isClosed) {
                    return;
                }
                var subPackageNo = item.get("subPackageNo");
                var purchasePackageId = item.get("purchasePackageId");
                var budgetType = item.get("prmBudgetType");
                var supplierId = item.get("supplierId");
                var arriveDate = item.get("arriveDate");
                var purchaseBudgetMoney = item.get("purchaseBudgetMoney");
                var purchaseLevel = item.get("purchaseLevel");
                var subjectProperty = item.get("subjectProperty");
                var uniqueKey;
                if (budgetType == "RUN") {
                    uniqueKey = Scdp.StrUtil.replaceNull(budgetType, '$UNKNOWN_NO$') + subPackageNo;
                } else {
                    uniqueKey = Scdp.StrUtil.replaceNull(budgetType, '$UNKNOWN_NO$') + Scdp.StrUtil.replaceNull(supplierId, '$UNKNOWN_SUPPLIER$');

                }

                if (Scdp.ObjUtil.isEmpty(subPackageNo)) {
                    var tmpGroup = notSubPackages[uniqueKey];
                    if (tmpGroup) {//存在的，重新push，不存在，加进去
                        tmpGroup["items"].push(item);
                        tmpGroup["pendingMoney"] = Erp.MathUtil.plusNumber(purchaseBudgetMoney, tmpGroup["pendingMoney"]);
                        if (Scdp.ObjUtil.isNotEmpty(arriveDate)) {
                            if (Scdp.ObjUtil.isEmpty(tmpGroup["arriveDate"]) || tmpGroup["arriveDate"] > arriveDate) {
                                tmpGroup["arriveDate"] = arriveDate;
                            }
                        }
                    } else {
                        tmpGroup = {};
                        tmpGroup["pendingMoney"] = Erp.MathUtil.plusNumber(0, purchaseBudgetMoney);
                        tmpGroup["arriveDate"] = arriveDate;
                        tmpGroup["items"] = [item];
                        notSubPackages[uniqueKey] = tmpGroup;
                    }
                } else {
                    var tmpGroup = existSubPackages[uniqueKey];
                    if (!tmpGroup) {
                        tmpGroup = {};
                        tmpGroup["packageNo"] = subPackageNo;
                        tmpGroup["purchasePackageId"] = purchasePackageId;
                        tmpGroup["purchaseLevel"] = purchaseLevel;
                        tmpGroup["subjectProperty"] = subjectProperty;
                        existSubPackages[uniqueKey] = tmpGroup;
                    }
                }
            });//找出最大包号
            var recalAmountPackageNos = [];
            var packageGrid = view.getCmp("prmPurchasePackageDto");
            var newPackages = [];
            //如果已经存在分包，则把新的明细加到包里，如果没有分包则建一个newPackages
            for (var uniqueKey in notSubPackages) {

                var notPackage = notSubPackages[uniqueKey];
                var existPackage = existSubPackages[uniqueKey];
                var isPackage = false;
                if (uniqueKey.indexOf("RUN") > -1) {
                    //如果运行费已经分包，再次分包不再加入包
                    for (var runUniqueKey in existSubPackages) {
                        if (runUniqueKey.indexOf("RUN") > -1) {
                            isPackage = true;
                            break;
                        }
                    }
                    if (!isPackage) {
                        if (existPackage) {
                            var packageNo = existPackage["packageNo"];
                            Ext.Array.each(notPackage["items"], function (item) {
                                item.set("subPackageNo", packageNo);
                                item.set("purchasePackageId", existPackage["purchasePackageId"]);
                                item.set("purchaseLevel", existPackage["purchaseLevel"]);
                                item.set("subjectProperty", existPackage["subjectProperty"]);
                            });
                            if (!Ext.Array.contains(recalAmountPackageNos, packageNo)) {
                                recalAmountPackageNos.push(packageNo);
                            }
                        } else {
                            var arriveDate = notPackage["arriveDate"];
                            notPackage["budgetType"] = "RUN";
                            if (Scdp.ObjUtil.isEmpty(arriveDate)) {
                                newPackages.push(notPackage);
                            } else {
                                var added = false;
                                for (var i = 0; i < newPackages.length; i++) {
                                    var tmpArriveDate = newPackages[i]["arriveDate"];
                                    if ((!tmpArriveDate) || (tmpArriveDate > arriveDate)) {
                                        newPackages = Ext.Array.insert(newPackages, i, [notPackage]);
                                        added = true;
                                        break;
                                    }
                                }
                                if (!added) {
                                    newPackages.push(notPackage);
                                }
                            }
                        }
                    }

                    continue;
                }
                if (existPackage) {
                    var packageNo = existPackage["packageNo"];
                    Ext.Array.each(notPackage["items"], function (item) {
                        item.set("subPackageNo", packageNo);
                        item.set("purchasePackageId", existPackage["purchasePackageId"]);
                        item.set("purchaseLevel", existPackage["purchaseLevel"]);
                        item.set("subjectProperty", existPackage["subjectProperty"]);
                    });
                    if (!Ext.Array.contains(recalAmountPackageNos, packageNo)) {
                        recalAmountPackageNos.push(packageNo);
                    }
                } else {
                    var arriveDate = notPackage["arriveDate"];
                    if (Scdp.ObjUtil.isEmpty(arriveDate)) {
                        newPackages.push(notPackage);
                    } else {
                        var added = false;
                        for (var i = 0; i < newPackages.length; i++) {
                            var tmpArriveDate = newPackages[i]["arriveDate"];
                            if ((!tmpArriveDate) || (tmpArriveDate > arriveDate)) {
                                newPackages = Ext.Array.insert(newPackages, i, [notPackage]);
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            newPackages.push(notPackage);
                        }
                    }
                }
            }
            //找到采购包里面最大的包号，然后+1
            var maxNo = packageGrid.getMaxPackageNo();
            var prmProjectMainId = view.getCmp("prmPurchasePlanDto->prmProjectMainId").gotValue();
            //add new package and write back the new package number and packageid
            var newPakageDatas = [];
            Ext.Array.each(newPackages, function (newPackage) {

                if (newPackage["budgetType"]) {
                    var oneName;
                    var onePurchaseLevel;
                    var onePurchaseLevelDesc;
                    var oneSubjectProperty;
                    var oneSubjectPropertyDesc;
                    var twoName;
                    var twoPurchaseLevel;
                    var twoPurchaseLevelDesc;
                    var twoSubjectProperty;
                    var twoSubjectPropertyDesc;
                    var postData = {classCode: ["0-011", "0-012"]}
                    Scdp.doAction('get-materialClass-info', postData, function (retData) {
                        if (retData.success == true) {
                            oneName = retData.oneName;
                            onePurchaseLevel = retData.onePurchaseLevel;
                            onePurchaseLevelDesc = retData.onePurchaseLevelDesc;
                            oneSubjectProperty = retData.oneSubjectProperty;
                            oneSubjectPropertyDesc = retData.oneSubjectPropertyDesc;
                            twoName = retData.twoName;
                            twoPurchaseLevel = retData.twoPurchaseLevel;
                            twoPurchaseLevelDesc = retData.twoPurchaseLevelDesc;
                            twoSubjectProperty = retData.twoSubjectProperty;
                            twoSubjectPropertyDesc = retData.twoSubjectPropertyDesc;
                        }
                    }, null, true, false, null);
                    var uuid1 = Scdp.StrUtil.getUUID();
                    var newPackageData1 = {}; //运行一级
                    var newPackageNo1 = ++maxNo;
                    var runPendingMoney1 = 0;
                    newPackageData1["packageName"] = "运行一级";
                    newPackageData1["materialClassCode"] = "0-011";
                    newPackageData1["materialClassCodeDesc"] = oneName;
                    newPackageData1["purchaseLevel"] = onePurchaseLevel;
                    newPackageData1["purchaseLevelDesc"] = onePurchaseLevelDesc;
                    newPackageData1["subjectProperty"] = oneSubjectProperty;
                    newPackageData1["subjectPropertyDesc"] = oneSubjectPropertyDesc;
                    newPackageData1["uuid"] = uuid1;
                    newPackageData1["prmProjectMainId"] = prmProjectMainId;
                    newPackageData1["packageNo"] = newPackageNo1;
                    newPackageData1["packageState"] = "0";
                    newPackageData1["packageBudgetMoney"] = 0;

                    var newPackageNo2 = ++maxNo;
                    var uuid2 = Scdp.StrUtil.getUUID();
                    var newPackageData2 = {};//运行二级
                    var runPendingMoney2 = 0;
                    newPackageData2["packageName"] = "运行二级";
                    newPackageData2["materialClassCode"] = "0-012";
                    newPackageData2["materialClassCodeDesc"] = twoName;
                    newPackageData2["purchaseLevel"] = twoPurchaseLevel;
                    newPackageData2["purchaseLevelDesc"] = twoPurchaseLevelDesc;
                    newPackageData2["subjectProperty"] = twoSubjectProperty;
                    newPackageData2["subjectPropertyDesc"] = twoSubjectPropertyDesc;
                    newPackageData2["uuid"] = uuid2;
                    newPackageData2["prmProjectMainId"] = prmProjectMainId;
                    newPackageData2["packageNo"] = newPackageNo2;
                    newPackageData2["packageState"] = "0";
                    newPackageData2["packageBudgetMoney"] = 0;

                    Ext.Array.each(newPackage["items"], function (item) {
                        var name = item.data.name;
                        if (name && (name == ("工程保险费") || name == "项目房租" || name == "租车费")) {
                            if (name && name == "工程保险费") {
                                runPendingMoney1 = item.data.purchaseBudgetMoney;
                                item.set("subPackageNo", newPackageNo1);
                                item.set("purchasePackageId", uuid1);
                                item.set("purchaseLevel", onePurchaseLevel);
                                item.set("purchaseLevelDesc", onePurchaseLevelDesc);
                                item.set("subjectProperty", oneSubjectProperty);
                                item.set("subjectPropertyDesc", oneSubjectPropertyDesc);
                            } else if (name && name == "项目房租" || name == "租车费") {
                                item.set("subPackageNo", newPackageNo2);
                                item.set("purchasePackageId", uuid2);
                                item.set("purchaseLevel", twoPurchaseLevel);
                                item.set("purchaseLevelDesc", twoPurchaseLevelDesc);
                                item.set("subjectProperty", twoSubjectProperty);
                                item.set("subjectPropertyDesc", twoSubjectPropertyDesc);
                                runPendingMoney2 += item.data.purchaseBudgetMoney;
                            }
                        }
                    });

                    newPackageData1["pendingMoney"] = runPendingMoney1;
                    newPakageDatas.push(newPackageData1);

                    //需要先计算出来再加
                    newPackageData2["pendingMoney"] = runPendingMoney2;
                    newPakageDatas.push(newPackageData2);
                } else {
                    var newPackageNo = ++maxNo;
                    var uuid = Scdp.StrUtil.getUUID();
                    var newPackageData = {};
                    newPackageData["uuid"] = uuid;
                    newPackageData["prmProjectMainId"] = prmProjectMainId;
                    newPackageData["packageNo"] = newPackageNo;
                    newPackageData["packageName"] = "包" + newPackageNo;
                    newPackageData["packageState"] = "0";
                    newPackageData["packageBudgetMoney"] = 0;
                    newPackageData["pendingMoney"] = newPackage["pendingMoney"];
                    newPakageDatas.push(newPackageData);
                    Ext.Array.each(newPackage["items"], function (item) {
                        item.set("subPackageNo", newPackageNo);
                        item.set("purchasePackageId", uuid);
                    });
                }

            });
            packageGrid.addRowItems(newPakageDatas, false);
            if (recalAmountPackageNos.length > 0) {
                me.recalculatePackageMoney(recalAmountPackageNos);
            }
            Scdp.unmask();
        };
        Scdp.runWithMask(null, callback);

    },

    //加入包方法
    inPackage: function () {
        var me = this;
        var packageGrid = me.view.getCmp("prmPurchasePackageDto");
        var curPackage = packageGrid.getCurRecord();
        if (!curPackage) {
            return;
        }
        var purchaseDetailGrid = me.view.getCmp("prmPurchasePlanDetailDto");
        var selectDetails = purchaseDetailGrid.getSelectionModel().getSelection();
        if (selectDetails.length == 0) {
            Scdp.MsgUtil.warn("至少勾选一条采购计划明细！");
            return;
        }

        if (curPackage.get("packageState") == '4') {
            Scdp.MsgUtil.warn("您选择的采购包已关闭，<br/>不能更改！");
            return;
        }
        var packageBudgetType = null;
        var detailItems = me.getPackageDetailAllItems();
        for (var i = 0; i < detailItems.length; i++) {
            if (detailItems[i].get("subPackageNo") == curPackage.get("packageNo")) {
                packageBudgetType = detailItems[i].get("prmBudgetType");
                break;
            }
        }
        var currPackageNo = curPackage.get("packageNo");
        var packageItems = packageGrid.getStore().data.items;
        var errorInfo = "";
        for (var i = 0; i < selectDetails.length; i++) {
            var detailModel = selectDetails[i];
            if (detailModel.get("isRunApply")) {
                errorInfo += Erp.Const.BREAK_LINE + "序号：" + detailModel.get("serialNumber") + "," + detailModel.get("name") + "已经进行日常报销";
            }
            if (!packageBudgetType) {
                packageBudgetType = detailModel.get("prmBudgetType");
            } else if (packageBudgetType != detailModel.get("prmBudgetType")) {
                Scdp.MsgUtil.warn("您选择的采购明细的立项预算类别不一致，请重新选择。");
                return;
            }

            var subPackageNoOri = detailModel.get("subPackageNo");
            for (var j = 0; j < packageItems.length; j++) {
                var packageNo = packageItems[j].get("packageNo");
                var packageState = packageItems[j].get("packageState");
                if (packageNo !== currPackageNo && subPackageNoOri == packageNo && packageState == "2") {
                    Scdp.MsgUtil.warn("您选择的采购计划明细的包状态为已审核，不能更改包！");
                    return;
                }
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(errorInfo)) {
            Scdp.MsgUtil.info(errorInfo);
            return;
        }
        var needReCalMoney = false;
        var calPackageNos = [];
        var invalidStateSerialNos = "";

        //iuuid is for filter function
        var currentDetailIuuids = [];
        Ext.Array.each(purchaseDetailGrid.store.data.items, function (item) {
            if (Scdp.ObjUtil.isEmpty(item.data.iuuid)) {
                item.data.iuuid = Scdp.StrUtil.getUUID();
            }
            currentDetailIuuids.push(item.data.iuuid);
        });
        var addedDetailIuuids = [];
        for (var i = 0; i < selectDetails.length; i++) {
            var detailModel = selectDetails[i];
            if (detailModel.get("subPackageNo") == currPackageNo) {
                continue;
            }
            if (detailModel.get("isClose") || detailModel.get("isReq")) {
                var prmBudgetType = "";
                if (detailModel.get("prmBudgetType") == "PRINCIPAL") {
                    prmBudgetType = "主材";
                } else if (detailModel.get("prmBudgetType") == "ACCESSORY") {
                    prmBudgetType = "辅材";
                } else if (detailModel.get("prmBudgetType") == "OUTSOURCE") {
                    prmBudgetType = "外协";
                } else if (detailModel.get("prmBudgetType") == "RUN") {
                    prmBudgetType = "运行费";
                }

                invalidStateSerialNos += Erp.Const.BREAK_LINE + "序号：" + detailModel.get("serialNumber") + "," + prmBudgetType;
            } else {
                var subPackageNo = detailModel.get("subPackageNo");
                if (Scdp.ObjUtil.isNotEmpty(subPackageNo) && !Ext.Array.contains(calPackageNos, subPackageNo)) {
                    calPackageNos.push(subPackageNo);
                }
                detailModel.set("subPackageNo", currPackageNo);
                detailModel.set("purchaseLevel", curPackage.get("purchaseLevel"));
                detailModel.set("subjectProperty", curPackage.get("subjectProperty"));
                detailModel.set("purchasePackageId", curPackage.get("uuid"));
                addedDetailIuuids.push(detailModel.data.iuuid);
                if (!needReCalMoney) {
                    needReCalMoney = true;
                }
            }
        }

        if (Scdp.ObjUtil.isNotEmpty(invalidStateSerialNos)) {
            Erp.Util.showLogView("加入包成功！以下采购明细的包已经关闭或已提交采购申请：" + invalidStateSerialNos);
        } else {
            Scdp.MsgUtil.info("加入包成功!");
        }
        if (needReCalMoney) {
            calPackageNos.push(currPackageNo);
            me.recalculatePackageMoney(calPackageNos);
        }

        //filter function
        var needFilter = me.view.getCmp("prmPurchasePlanDto->showAllDetail").gotValue();
        if (!needFilter) {
            purchaseDetailGrid.store.filterBy(function (record) {
                var iuuid = record.data.iuuid;
                if (Scdp.ObjUtil.isNotEmpty(iuuid) && Ext.Array.contains(currentDetailIuuids, iuuid) && !Ext.Array.contains(addedDetailIuuids, iuuid)) {
                    return true;
                } else {
                    return false;
                }
            });
        }
    },

    //包减去方法
    outPackage: function () {
        var me = this;
        var packageGrid = me.view.getCmp("prmPurchasePackageDto");
        var curPackage = packageGrid.getCurRecord();
        if (!curPackage) {
            return;
        }
        var purchaseDetailGrid = me.view.getCmp("prmPurchasePlanDetailDto");
        var selectDetails = purchaseDetailGrid.getSelectionModel().getSelection();
        if (selectDetails.length == 0) {
            Scdp.MsgUtil.warn("至少勾选一条采购计划明细！");
            return;
        }

        var currPackageNo = curPackage.get("packageNo");
        var packageItems = packageGrid.getStore().data.items;
        var errorInfo = "";
        var errorInfo1 = "";
        var errorInfo2 = "";
        var errorInfo3 = "";
        for (var i = 0; i < selectDetails.length; i++) {
            var detailModel = selectDetails[i];
            if (detailModel.get("isRunApply")) {
                errorInfo1 += Erp.Const.BREAK_LINE + "序号：" + detailModel.get("serialNumber") + "," + detailModel.get("name");
            }
            for (var j = 0; j < packageItems.length; j++) {
                var packageNo = packageItems[j].get("packageNo");
                var packageState = packageItems[j].get("packageState");
                if (packageNo === currPackageNo && (packageState == "2" || packageState == "4")) {
                    errorInfo2 += Erp.Const.BREAK_LINE + "序号：" + detailModel.get("serialNumber") + "," + detailModel.get("name");
                    break;
                }
            }

            if (detailModel.get("isReq")) {
                var prmBudgetType = "";
                if (detailModel.get("prmBudgetType") == "PRINCIPAL") {
                    prmBudgetType = "主材";
                } else if (detailModel.get("prmBudgetType") == "ACCESSORY") {
                    prmBudgetType = "辅材";
                } else if (detailModel.get("prmBudgetType") == "OUTSOURCE") {
                    prmBudgetType = "外协";
                } else if (detailModel.get("prmBudgetType") == "RUN") {
                    prmBudgetType = "运行费";
                }

                errorInfo3 += Erp.Const.BREAK_LINE + "序号：" + detailModel.get("serialNumber") + "," + prmBudgetType;
            }

        }
        errorInfo = (Scdp.ObjUtil.isNotEmpty(errorInfo1) ? errorInfo1 + "已经进行日常报销;" : "")
                        + (Scdp.ObjUtil.isNotEmpty(errorInfo2) ? errorInfo2 + "所属包已经审核或者关闭;" : "")
                        + (Scdp.ObjUtil.isNotEmpty(errorInfo3) ? errorInfo3 + "已提交采购申请;" : "");

        if (Scdp.ObjUtil.isNotEmpty(errorInfo)) {
            Scdp.MsgUtil.info(errorInfo);
            return;
        }
        var needReCalMoney = false;
        var calPackageNos = [];

        //iuuid is for filter function
        var currentDetailIuuids = [];
        Ext.Array.each(purchaseDetailGrid.store.data.items, function (item) {
            if (Scdp.ObjUtil.isEmpty(item.data.iuuid)) {
                item.data.iuuid = Scdp.StrUtil.getUUID();
            }
            currentDetailIuuids.push(item.data.iuuid);
        });
        var addedDetailIuuids = [];
        for (var i = 0; i < selectDetails.length; i++) {
            var detailModel = selectDetails[i];
            var subPackageNo = detailModel.get("subPackageNo");
            if (Scdp.ObjUtil.isNotEmpty(subPackageNo) && !Ext.Array.contains(calPackageNos, subPackageNo)) {
                calPackageNos.push(subPackageNo);
            }
            detailModel.set("subPackageNo", null);
            detailModel.set("purchaseLevel", null);
            detailModel.set("subjectProperty", null);
            detailModel.set("purchasePackageId", null);
            addedDetailIuuids.push(detailModel.data.iuuid);
            if (!needReCalMoney) {
                needReCalMoney = true;
            }
        }
        if (needReCalMoney) {
            calPackageNos.push(currPackageNo);
            me.recalculatePackageMoney(calPackageNos);
        }
        //filter function
        var needFilter = me.view.getCmp("prmPurchasePlanDto->showAllDetail").gotValue();
        if (!needFilter) {
            purchaseDetailGrid.store.filterBy(function (record) {
                var iuuid = record.data.iuuid;
                if (Scdp.ObjUtil.isNotEmpty(iuuid) && Ext.Array.contains(currentDetailIuuids, iuuid) && !Ext.Array.contains(addedDetailIuuids, iuuid)) {
                    return true;
                } else {
                    return false;
                }
            });
        }
    },
    //packageNos is array, or null
    //if is null or undefined, calculate all packages pending money
    recalculatePackageMoney: function (packageNos) {
        var me = this;
        var view = me.view;
        var packageItems = view.getCmp("prmPurchasePackageDto").store.data.items;
        var detailItems = me.getPackageDetailAllItems();

        var totalPurchaseMoney = {};
        Ext.Array.each(detailItems, function (item) {
            var subPackageNo = item.get("subPackageNo");
            if (subPackageNo && ((!packageNos) || Ext.Array.contains(packageNos, subPackageNo))) {
                var key = (subPackageNo + "");
                if (totalPurchaseMoney[key]) {
                    totalPurchaseMoney[key] = Erp.MathUtil.plusNumber(
                        totalPurchaseMoney[key], item.get("purchaseBudgetMoney"));
                } else {
                    totalPurchaseMoney[key] = item.get("purchaseBudgetMoney");
                }
            }
        });
        Ext.Array.each(packageItems, function (item) {
            var key = (item.get("packageNo") + "");
            if (totalPurchaseMoney.hasOwnProperty(key)) {
                item.set("pendingMoney", totalPurchaseMoney[key]);
                //item.set("packageBudgetMoney", totalPurchaseMoney[key]);
            } else if ((!packageNos) || Ext.Array.contains(packageNos, item.get("packageNo"))) {
                item.set("pendingMoney", 0);
            }
        });
    },
    //提交最后一步完成之后
    afterCompelteTask: function (result) {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("prmPurchasePlanDto->uuid").gotValue();
        me.loadItem(uuid);
//        if (Scdp.ObjUtil.isNotEmpty(result.planPackages)) {
//            view.getCmp("prmPurchasePackageDto").sotValue(result.planPackages);
//        }
//        if (Scdp.ObjUtil.isNotEmpty(result.purchasePlanState)) {
//            view.getCmp("prmPurchasePlanDto->purchasePlanState").sotValue(result.purchasePlanState + "");
//        }
    },
    //根据包过滤明细
    filterDetailByPackage: function () {
        var me = this;
        var view = me.view;
        var detailGrid = view.getCmp("prmPurchasePlanDetailDto");
        detailGrid.startFilter();
    },
    //文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "CDM_FILE_TYPE_DETAIL";
        var beforeFileUpload = function (postData) {
            var uuid = me.view.getHeader().getCmp("uuid").gotValue();
            postData.needPersistence = "1";
            postData.cdmFileType = grid.cdmFileType;
            postData.module = me.menuCode;
            postData.dataId = uuid;
            return true;
        };
        Erp.FileUtil.erpFileUpload(grid, fileClassify, null, beforeFileUpload, null, "PURCHASE_PLAN");
    },
    //文件下载
    fileDownloadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    },
    ////文件删除
    //fileDeleteBtn: function () {
    //    var me = this;
    //    var grid = me.view.getCmp("cdmFileRelationDto");
    //    Erp.FileUtil.erpFileDelete(grid);
    //},
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
        var selection = grid.getSelectionModel().getSelection();
        if (selection) {
            Erp.FileUtil.erpFileDelete(grid, null, true);
        }
    },
    //下载模板
    downLoadTemplate: function () {
        var postData = {};
        postData.cdmFileType = 'PRM_PROJECT';
        postData.fileClassify = 'PRM_PURCHASE_PLAN_TEMPLATE';
        var result = Scdp.doAction("template_download", postData, null, null, false, false);
        if (Scdp.ObjUtil.isNotEmpty(result) && Scdp.ObjUtil.isNotEmpty(result.URL_LIST)) {
            Ext.each(result.URL_LIST, function (item) {
                window.open(item);
            })
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('prmPurchasePlanDto->contractorOffice').gotValue();
        return processDeptCode;
    },
    getWfStateField: function () {
        return "purchasePlanState";
    },
    refreshWorkFlowStatus: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("editPanel->modifyBtn").setDisabled(false);
    }

});
