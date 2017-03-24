var map = new Map();
var timer;
var oldRecord, oldSubRecord;
Ext.define("Purchasereq.controller.PurchasereqController", {
    extend: 'Scdp.mvc.AbstractController',
    viewClass: 'Purchasereq.view.PurchasereqView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'searchBtn', name: 'click', fn: 'queryPurchasereq'},
        {cid: 'resetBtn', name: 'click', fn: 'resetPurchasereq'},
        {cid: 'showAll', name: 'change', fn: 'showAllDetail'},
        {cid: 'addToContract', name: 'click', fn: 'addToContract'},
        {cid: 'addQuotation', name: 'click', fn: 'addQuotation'},
        {cid: 'showQuotation', name: 'click', fn: 'showQuotations'},
        {cid: 'gotoContract', name: 'click', fn: 'gotoContract'},
        {cid: 'reject', name: 'click', fn: 'rejectDetails'},
        {cid: 'scmPurchaseReqDto', name: 'select', fn: 'showAllDetail'},
        {cid: 'exportQuotation', name: 'click', fn: 'doBtnExportQuotation'},
        {cid: 'pushQuotation', name: 'click', fn: 'doBtnPushQuotation'}
    ],

    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.purchasereq.dto.ScmPurchaseReqDto',
    intValue: null,
    afterInit: function () {
        var me = this;
        var scmContractDto = this.view.getCmp("scmContractDto");
        if (scmContractDto) {
            scmContractDto.getCmp("toolbar->addRowBtn").hide();
            scmContractDto.getCmp("toolbar->copyAddRowBtn").hide();
            scmContractDto.getCmp("toolbar->rowMoveTopBtn").hide();
            scmContractDto.getCmp("toolbar->rowMoveUpBtn").hide();
            scmContractDto.getCmp("toolbar->rowMoveDownBtn").hide();
            scmContractDto.getCmp("toolbar->rowMoveBottomBtn").hide();
        }
        me.view.getCmp('scmPurchaseReqDto->changePrincipal').items.push({
            iconCls: 'refresh_btn',
            width: '16',
            height: '16',
            handler: function (grid, rowIndex) {
                Scdp.MsgUtil.confirm("此功能为采购申请转办理，点击“是”将该采购申请单提交重新分配采购办理人。", function (e) {
                    if ("yes" == e) {
                        var uuid = me.view.getCmp("scmPurchaseReqDto").getCurRecord().data.uuid;
                        var postData = {uuid: uuid};
                        //Scdp.MsgUtil.info("转办理成功!"+uuid);
                        Scdp.doAction("purchasereq-change-to-another-principal", postData, function (result) {
                            if (result.success) {
                                Scdp.MsgUtil.info("转办理成功!");
                                me.reloadItems();
                            }
                        })
                    }
                });
            }
        });
        me.view.getCmp('prmPurchaseReqDetailDto->changePackage').items.push({
            iconCls: 'refresh_btn',
            width: '16',
            height: '16',
            handler: function (grid, rowIndex) {
                var view = me.view;
                var pProjectMainId = me.view.getCmp("scmPurchaseReqDto").getCurRecord().data.prmProjectMainId;
                var selectedReqDetailGrid = view.getCmp("prmPurchaseReqDetailDto");
                var prmPurchasePlanDetailId = selectedReqDetailGrid.getStore().getAt(rowIndex).get("prmPurchasePlanDetailId");
                var scmPurchaseReqId = selectedReqDetailGrid.getStore().getAt(rowIndex).get("scmPurchaseReqId");
                var budgetCode = selectedReqDetailGrid.getStore().getAt(rowIndex).get("prmBudgetType");
                if ("1" == selectedReqDetailGrid.getStore().getAt(rowIndex).get("isfallback")) {
                    Scdp.MsgUtil.info("该数据已经被退回，不能调包");
                    return false;
                }
                var callBack = function (subView) {
                    var packageId = subView.getCmp("purchasePackageName").gotValue()
                    var postData = {
                        prmPurchasePlanDetailId: prmPurchasePlanDetailId,
                        packageId: packageId,
                        scmPurchaseReqId: scmPurchaseReqId
                    };
                    Scdp.doAction("purchasereq-replacepurchasereqpackageid", postData, function () {
                    });
                    win.close();
                    me.reloadItems();

                };

                var form = Ext.widget("JForm", {
                    height: 80,
                    width: 400,
                    layout: 'form',
                    items: [
                        {
                            xtype: 'JHidden',
                            cid: 'prmProjectMainId',
                            value: pProjectMainId
                        },
                        {
                            xtype: 'JHidden',
                            cid: 'budgetCode',
                            value: budgetCode
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
                            filterFields: 'prmProjectMainId|prmProjectMainId,budgetCode|budgetCode',
                            valueField: 'uuid',
                            descField: 'packageName',
                            fieldLabel: '采购包'
                        }
                    ]
                });
                win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '调包', "确认");
            }
        });

        var selectedReqDetailGrid = me.view.getCmp("prmPurchaseReqDetailDto");
        selectedReqDetailGrid.on("select", function (grid, record, index) {
            if (record.data.isfallback || record.data.scmContractId) {
                selectedReqDetailGrid.view.deselect(record.index);
            }
            intValue = record.data.handleAmount;
        });
        selectedReqDetailGrid.on("celldblclick", function (obj, td, cellIndex, record, tr, rowIndex, e, eOpts) {
            me.showAttachmentManage(record);
        });

        //修改选入数量
        selectedReqDetailGrid.store.on('update', function (grid, record, operation) {
            me.modifyHandleAmount(selectedReqDetailGrid, record);
        });

        var resultGrid = me.view.getCmp("scmPurchaseReqDto");
        resultGrid.needLoadItem = false;
        resultGrid.on("celldblclick", function (obj, td, cellIndex, record, tr, rowIndex, e, eOpts) {
            me.showAllDetail();
            me.showAttachmentManage(record);

        });
        resultGrid.on("cellclick", function () {
            me.showAllDetail();
        });
        me.view.getCmp('queryForm->state').sotValue("1");
        //if (Scdp.ObjUtil.isNotEmpty(me.actionParams)) {
        //    var businessObj = me.actionParams.erp_msg_business_obj;
        //    if (businessObj && businessObj.uuid) {
        //        me.view.getCmp('queryForm->purchaseReqNo').sotValue(businessObj.uuid);
        //        me.queryPurchasereq(me.view, me.view, true, true);
        //    }
        //}

        me.doTimeoutbyAutoQuery();    //M6_C3_F1_优化

    },

    showAttachmentManage: function (record) {
        var withAttach = record.get("withAttach");
        if ('是' == withAttach) {
            var postData = {};
            postData.dataId = record.get('prmPurchaseReqId');
            var callback = function () {
            };
            var controller = Ext.create("Cdmfile.controller.CdmFilePopupController");
            controller.postData = postData;
            var win = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '附件管理', '确定');
        }
    },

    //修改选入数量,...
    modifyHandleAmount: function (selectedReqDetailGrid, record) {
        var me = this;
        var row = record.data;
        var newValue = row.handleAmount;
        var amount = row.amount;
        if (intValue != newValue) {
            var contractTab = me.view.getCmp("quotationTab").getActiveTab();
            if (newValue == null || newValue < 0 || (intValue > 0 && newValue > intValue) || newValue > amount) {
                record.set("handleAmount", intValue);
            } else if (record.data.isfallback || record.data.scmContractId) {
                record.set("handleAmount", intValue);
                Scdp.MsgUtil.info("已经选入或退回的记录不能拆分！")
            } else if (!contractTab) {
                record.set("handleAmount", intValue);
                Scdp.MsgUtil.info("请选一个报价单！")
            } else {
                var purchasePackageId = record.data.purchasePackageId;
                if (!me.canAddToContract(contractTab, purchasePackageId)) {
                    Scdp.MsgUtil.info("所选记录和当前询价单中的记录不属于同一个项目、部门、包或预算科目！");
                    record.set("handleAmount", intValue);
                    return false;
                } else {
                    var newData = Ext.clone(record.data);
                    Scdp.wrapDataForMerge(newData);
                    Scdp.wrapDataForRemoveSystemFields(newData);
                    newData.puuid = record.data.uuid;
                    if (row.amount == row.handleAmount) {
                        //do nothing
                    } else {
                        if (!intValue || intValue == 0) {
                            row.handleAmount = row.amount - row.handleAmount;
                        } else {
                            row.handleAmount = intValue - row.handleAmount;
                        }
                        selectedReqDetailGrid.addRowItem(newData);
                    }
                    intValue = row.handleAmount;
                    me.addToContract(row);
                }
            }
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
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
    afterSave: function (retData) {
        var me = this;
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
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

    //添加询价单(新)
    addQuotation: function () {
        var me = this;
        var view = me.view;
        view.addTabPanel();
        var postData = {};
        Scdp.doAction("purchasereq-get-req-no", postData, function (result) {
            //更新数据
            var scmContractCode = result.scmContractCode;
            if (Scdp.ObjUtil.isNotEmpty(scmContractCode)) {
                var activeTab = me.view.getCmp("quotationTab").getActiveTab();
                activeTab.setTitle(scmContractCode);
                activeTab.state = "ADD";//新增时CID初始化为“NEW”
            }
        })
    },
    //查询
    queryPurchasereq: function (btn1, btn2, notCloseQuotation, rememberLine) {
        var me = this;
        var prmProjectMainId = this.view.getCmp('queryForm->prmProjectMainId').gotValue();
        var purchaseReqNo = this.view.getCmp('queryForm->purchaseReqNo').gotValue();
        var departmentCode = this.view.getCmp('queryForm->officeId').gotValue();
        var state = this.view.getCmp('queryForm->state').gotValue();

        var queryConditions = {};
        if (Scdp.ObjUtil.isNotEmpty(prmProjectMainId)) {
            queryConditions.prmProjectMainId = prmProjectMainId;
        }
        if (Scdp.ObjUtil.isNotEmpty(purchaseReqNo)) {
            queryConditions.purchaseReqNo = purchaseReqNo;
        }
        if (Scdp.ObjUtil.isNotEmpty(departmentCode)) {
            queryConditions.officeId = departmentCode;
        }
        if (Scdp.ObjUtil.isNotEmpty(state)) {
            queryConditions.state = state;
        }
        var modulePath = me.view.modulePath;
        var layoutFile = me.view.layoutFile;
        var postData = {queryConditions: queryConditions, modulePath: modulePath, layoutFile: layoutFile};
//        if (notCloseQuotation) {
//        } else {
//            me.closeAllQuotation();
//        }
        var scmPurchaseReq = me.view.getCmp('scmPurchaseReqDto').getCurRecord();
        var uuid = null;
        if (scmPurchaseReq) {
            uuid = scmPurchaseReq.data.uuid
        }

        Scdp.doAction("purchasereq-query", postData, function (result) {
            //-------------- begin ----------------
            //M6_C3_F1_优化
            map.clear()
            Ext.Array.forEach(result.scmPurchaseReqDto, function (item) {
                map.put(item.uuid + item.purchaseReqNo, "");
            })
            //------------ end ---------------------
            me.view.getCmp('scmPurchaseReqDto').sotValue(result.scmPurchaseReqDto);
            if (rememberLine) {
                var scmPurchaseReqStore = me.view.getCmp('scmPurchaseReqDto').getStore();
                if (uuid && scmPurchaseReqStore && scmPurchaseReqStore.getCount() > 0) {
                    for (var i = 0; i < scmPurchaseReqStore.data.items.length; i++) {
                        var subItem = scmPurchaseReqStore.data.items[i];
                        if (uuid == subItem.data.uuid) {
                            me.view.getCmp('scmPurchaseReqDto').getView().focusRow(i);
                            me.view.getCmp('scmPurchaseReqDto').getSelectionModel().select(i);
                            me.view.getCmp('prmPurchaseReqDetailDto').sotValue(subItem.data.prmPurchaseReqDetailDto);
                        }
                    }
                }
            }
            var grid = me.view.getCmp("prmPurchaseReqDetailDto");
            grid.sotEditable(true);
            me.showAllDetail();
        })
    },

    checkTabExisting: function (scmPurchaseReqUuid) {
        var me = this;
        var tabs = me.view.getCmp("quotationTab");
        var items = tabs.items.items;
        if (items && items.length > 0) {
            for (var i = items.length - 1; i >= 0; i--) {
                var item = items[i];
                if (item.cid == scmPurchaseReqUuid) {
                    return true;
                }
            }
        }
    },

    //重置
    resetPurchasereq: function () {
        this.view.getCmp('queryForm->prmProjectMainId').sotValue("");
        this.view.getCmp('queryForm->purchaseReqNo').sotValue("");
        this.view.getCmp('queryForm->officeId').sotValue("");
        this.view.getCmp('queryForm->state').sotValue("");
    },

    //确定迁入(新)
    addToContract: function (splitedRowData) {
        var me = this;
        var view = this.view;
        var selectedReqDetailGrid = view.getCmp("prmPurchaseReqDetailDto");
        var selectedReqDetails = selectedReqDetailGrid.getSelectionModel().getSelection();
        var purchasePackageId = "";
        var stampProjectUuid = "";

        if (Scdp.ObjUtil.isEmpty(selectedReqDetails)) {
            Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
            return false;
        } else {
            Ext.Array.forEach(selectedReqDetails, function (record) {
                if (record.data.uuid) {//点确定选入button
                    if (Scdp.ObjUtil.isNotEmpty(purchasePackageId)) {
                        if (purchasePackageId != record.data.purchasePackageId) {
                            Scdp.MsgUtil.info("所选记录不属于同一个包！")
                            return false;
                        }
                    } else {
                        purchasePackageId = record.data.purchasePackageId;
                    }
                    if (Scdp.ObjUtil.isNotEmpty(stampProjectUuid)) {
                        if (stampProjectUuid != record.data.stampProjectUuid) {
                            Scdp.MsgUtil.info("所选记录不属于同一个课题代号！")
                            return false;
                        }
                    } else {
                        stampProjectUuid = record.data.stampProjectUuid;
                    }
                } else {//修改选入数量，一次只会有一个
                    purchasePackageId = record.data.purchasePackageId;
                }


            });
        }
        var activeTab = me.view.getCmp("quotationTab").getActiveTab();
        if (Scdp.ObjUtil.isNotEmpty(activeTab)) {
            if (!me.canAddToContract(activeTab, purchasePackageId)) {
                Scdp.MsgUtil.info("所选记录和当前询价单中的记录不属于同一个项目、部门、包或预算科目！")
                return false;
            }
            var postData = {};
            postData = me.collectDetailInfo(selectedReqDetails, activeTab, postData, splitedRowData);
            if (Scdp.ObjUtil.isNotEmpty(postData)) {
                var scmPurchaseReqRecord = me.view.getCmp("scmPurchaseReqDto").getCurRecord();

                postData.scmContractCode = activeTab.title;
                postData.officeId = scmPurchaseReqRecord.data.officeId;
                postData.purchasePackageId = purchasePackageId;
                postData.projectId = scmPurchaseReqRecord.data.prmProjectMainId;
                postData.isProject = scmPurchaseReqRecord.data.isProject;
                postData.bugdetId = scmPurchaseReqRecord.data.bugdetId;
                postData.subjectCode = scmPurchaseReqRecord.data.subjectCode;
                Scdp.doAction("purchasereq-add-to-contract", postData, function (result) {
                    if (result.success) {
                        //重新load contract对应的数据
                        activeTab.sotValue(result.prmPurchaseReqDetailDto);
                        activeTab.cid = result.contractId;
                        activeTab.projectName = scmPurchaseReqRecord.data.projectName;
                        activeTab.projectId = scmPurchaseReqRecord.data.prmProjectMainId;
                        activeTab.purchasePackageId = purchasePackageId;
                        activeTab.officeId = scmPurchaseReqRecord.data.officeId;
                        activeTab.bugdetId = scmPurchaseReqRecord.data.bugdetId;
                        activeTab.subjectCode = scmPurchaseReqRecord.data.subjectCode;

                        me.reloadItems();
                    }
                });
                activeTab.state = "MODIFY";
            }
        } else {
            Scdp.MsgUtil.info("请选一个询价单！")
        }
    },
    collectDetailInfo: function (selectedReqDetails, activeTab, postData, splitedRowData) {
        var me = this;
        var result = [];
        var contractUuid = activeTab.cid;
        Ext.Array.forEach(selectedReqDetails, function (record) {
            if (record.data.uuid) {//点确定选入button
                if (record.data.handleAmount == null || record.data.handleAmount == 0) {
                    record.data.handleAmount = record.data.amount;
                }
                var prmPurchaseReqDetailDto = record.data;
                prmPurchaseReqDetailDto.scmContractId = contractUuid;
                prmPurchaseReqDetailDto.scmContractCode = activeTab.title;
                result.push(prmPurchaseReqDetailDto);
            } else {//修改选入数量，一次只会有一个
                var prmPurchaseReqDetailDto = record.data;
                prmPurchaseReqDetailDto.scmContractId = contractUuid;
                prmPurchaseReqDetailDto.scmContractCode = activeTab.title;
                result.push(splitedRowData);
                postData.newAddData = prmPurchaseReqDetailDto;
            }
        });
        postData.prmPurchaseReqDetailDto = result;
        postData.scmContractState = activeTab.state;
        postData.scmContractId = contractUuid;
        return postData;
    },

    canAddToContract: function (activeTab, purchasePackageId) {
        var me = this;
        var scmPurchaseReqRecord = me.view.getCmp("scmPurchaseReqDto").getCurRecord();
        var contractOfficeId = activeTab.officeId;
        var contractPackageNo = activeTab.purchasePackageId;
        var contractProjectId = activeTab.projectId;
        var contractBugdetId = activeTab.bugdetId;
        var contractSubjectCode = activeTab.subjectCode;
        var scmPurchaseOfficeId = scmPurchaseReqRecord.data.officeId;
        var scmPurchasePackageNo = purchasePackageId;
        var scmPurchaseProjectId = scmPurchaseReqRecord.data.prmProjectMainId;
        var scmPurchaseBugdetId = scmPurchaseReqRecord.data.bugdetId;
        var scmPurchaseSubjectCode = scmPurchaseReqRecord.data.subjectCode;
        if (Scdp.ObjUtil.isEmpty(contractOfficeId) && Scdp.ObjUtil.isEmpty(contractPackageNo) && Scdp.ObjUtil.isEmpty(contractProjectId)) {
            return true;
        } else if (Scdp.StrUtil.replaceNull(scmPurchaseOfficeId) == Scdp.StrUtil.replaceNull(contractOfficeId)
            && Scdp.StrUtil.replaceNull(scmPurchasePackageNo) == Scdp.StrUtil.replaceNull(contractPackageNo)
            && Scdp.StrUtil.replaceNull(scmPurchaseProjectId) == Scdp.StrUtil.replaceNull(contractProjectId)
            && Scdp.StrUtil.replaceNull(scmPurchaseBugdetId) == Scdp.StrUtil.replaceNull(contractBugdetId)
            && Scdp.StrUtil.replaceNull(scmPurchaseSubjectCode) == Scdp.StrUtil.replaceNull(contractSubjectCode)) {
            return true;
        } else {
            return false;
        }
    },

    //显示全部
    showAllDetail: function () {
        var me = this;
        var view = this.view;
        var checkBtn = view.getCmp("showAll");
        var checked = checkBtn.gotValue();
        var store = view.getCmp("prmPurchaseReqDetailDto").getStore();
        store.filterBy(function (record) {
            if (checked && checked == 1) {
                return true
            } else {
                if (record.data.scmContractCode != null) {
                    return false;
                } else {
                    return true;
                }
            }
        });
        var selectedReqDetailGrid = me.view.getCmp("prmPurchaseReqDetailDto");
        selectedReqDetailGrid.sotEditable(true);
    },

    //选出询价单
    showQuotations: function () {
        var me = this;
        var scmPurchaseReqDto = me.view.getCmp('scmPurchaseReqDto').getCurRecord();
        if (!scmPurchaseReqDto) {
            Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
            return false;
        }
        var scmPurchaseReqUuid = scmPurchaseReqDto.data.uuid;
        if (scmPurchaseReqUuid) {
            var postData = {};
            postData.scmPurchaseReqUuid = scmPurchaseReqUuid;
            Scdp.doAction("scmcontract-detail-query", postData, function (result) {
                if (result.scmContractDto) {
                    Ext.Array.forEach(result.scmContractDto, function (contract) {
                        var contractUuid = contract.uuid;
                        if (!me.checkTabExisting(contractUuid)) {
                            me.view.addTabPanel();
                            var activeTab = me.view.getCmp("quotationTab").getActiveTab();
                            ;
                            activeTab.projectName = scmPurchaseReqDto.data.projectName;
                            activeTab.setTitle(contract.scmContractCode);
                            activeTab.cid = contractUuid;
                            activeTab.state = "MODIFY";
                            activeTab.officeId = contract.officeId;
                            activeTab.projectId = contract.projectId;
                            activeTab.purchasePackageId = contract.purchasePackageId;
                            activeTab.bugdetId = contract.bugdetId;
                            activeTab.subjectCode = contract.subjectCode;
                            activeTab.sotValue(contract.prmPurchaseReqDetailDto)
                        }
                    })
                }
            })
        }
    },

    //退回询价单
    rejectDetails: function () {
        var me = this;
        var quotationTab = me.view.getCmp("quotationTab");
        var activeTab = quotationTab.getActiveTab();
        if (activeTab) {
            var selectItems = activeTab.getSelectionModel().getSelection();
            if (selectItems.length < 1) {
                Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
                return;
            }
            var callBack = function (subView) {
                var fallbackReason = subView.getCmp("fallbackReason").gotValue();
                var param = [];
                Ext.Array.forEach(selectItems, function (item) {
                    item.set("fallbackReason", fallbackReason);
                    item.set("isfallback", "1");
                    item.set("scmContractId", null);
                    item.set("scmContractCode", null);
                    param.push(item.data);
                });
                me.view.getCmp("quotationTab").getActiveTab().getStore().remove(selectItems);
                var postData = {};
                postData.fallbackReason = fallbackReason;
                postData.prmPurchaseReqDetailDto = param;
                if (activeTab.getStore().count() < 1) {
                    postData.cleanContract = true;
                    postData.contractUuid = activeTab.cid;
                    activeTab.onlyCloseTab = true;
                    activeTab.close();
                    var tabs = quotationTab.items.items;
                    if (Scdp.ObjUtil.isNotEmpty(tabs)) {
                        quotationTab.setActiveTab(tabs[0]);
                    }
                }
                Scdp.doAction("update-reject-reason", postData, function (result) {
                    if (result.success) {
                        //activeTab.purchasePackageId = null;
                        //activeTab.officeId = '';
                        //activeTab.projectId = '';
                        me.reloadItems();
                        win.close();
                    }
                })
            };
            var form = Ext.widget("JForm", {
                height: 80,
                width: 600,
                layout: 'form',
                bodyPadding: '10 10 10 10',
                items: [
                    {
                        xtype: 'JCombo',
                        comboType: 'scdp_fmcode',
                        codeType: 'RETURN_PURCHASE_REQ',
                        allowBlank: false,
                        fieldLabel: '退回原因',
                        cid: 'fallbackReason',
                        valueField: 'codedesc',
                        forceSelection: false,
                        displayDesc: true,
                        fullInfo: true,
                        width: 150,
                        height: 21
                    }
                ]
            });
            var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '退回', "处理");
        } else {
            Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
            return false;
        }
    },

    reloadItems: function () {
        var me = this;
        me.queryPurchasereq(me.view, me.view, true, true);
    },

    //删除询价单明细
    rollBackDetail: function (activeTab) {
        var me = this;
        if (activeTab) {
            var selectItems = activeTab.getSelectionModel().getSelection();
            if (selectItems.length > 0) {
                var prmPurchaseReqDetailDto = [];
                Ext.Array.forEach(selectItems, function (item) {
                    prmPurchaseReqDetailDto.push(item.data);
                    activeTab.store.remove(item);
                })
                prmPurchaseReqDetailDto.cleanContractID = Ext.clone(activeTab.cid);
                prmPurchaseReqDetailDto.cleanContract = false;
                if (activeTab.store.data.items.length == 0) {
                    activeTab.state = "ADD";
                    prmPurchaseReqDetailDto.cleanContract = true;
                    activeTab.cid = null;
                    activeTab.officeId = null;
                    activeTab.purchasePackageId = null;
                    activeTab.projectId = null;
                    activeTab.bugdetId = null;
                    activeTab.subjectCode = null;
                }
                me.deletePrmDetails(prmPurchaseReqDetailDto);
            }
        }
    },

    //删除询价单
    rollBackAll: function (selectItems, contractUuid) {
        var me = this;
        me.deletePrmDetails(selectItems, contractUuid);
    },

    deletePrmDetails: function (selectItems, contractUuid) {
        var me = this;
        var postData = {};
        postData.prmPurchaseReqDetailDto = selectItems;
        var quotationTab = me.view.getCmp("quotationTab");
        //var cleanContract = false;
        postData.cleanContract = selectItems.cleanContract;
        postData.contractUuid = selectItems.cleanContractID;
        if (Scdp.ObjUtil.isNotEmpty(contractUuid)) {//删除询价单时清空询价单内容
            postData.cleanContract = true;
            postData.contractUuid = contractUuid;
            var tabs = quotationTab.items.items;
            if (Scdp.ObjUtil.isNotEmpty(tabs)) {
                quotationTab.setActiveTab(tabs[0]);
            }
        }
        Scdp.doAction("delete-contract-detail", postData, function (result) {
            if (result.success) {
                me.reloadItems();
            }
        })
    },

    //编制合同
    gotoContract: function () {
        var me = this;
        var contractTab = me.view.getCmp("quotationTab").getActiveTab();
        if (Scdp.ObjUtil.isEmpty(contractTab)) {
            Scdp.MsgUtil.info("请选择报价单！");
            return false;
        }

        var param = {};
        var menuCode = 'CONTRACTESTABLISHMENT';
        if (Scdp.ObjUtil.isNotEmpty(contractTab)) {
            var contractUuid = contractTab.cid;
            param.uuid = contractUuid;
            Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
        }
    },

    //修改报单价title
    updateContractCode: function (uuid, scmContractCode) {
        var me = this;
        var postData = {};
        postData.uuid = uuid;
        postData.scmContractCode = scmContractCode;
        Scdp.doAction("update-contract-code", postData, function (result) {
            if (result.success) {
                me.reloadItems();
            }
        });
    },
    doBtnExportQuotation: function () {
        var me = this;
        //var scmPurchaseReq = me.view.getCmp('scmPurchaseReqDto').getCurRecord();
        //if (scmPurchaseReq) {
        //var projectName = scmPurchaseReq.data.projectName;
        var activeTab = me.view.getCmp("quotationTab").getActiveTab();
        if (activeTab) {
            var scmContractCode = activeTab.title;
            var projectName = activeTab.projectName;
            var h = Scdp.getSysConfig("base_path") + Scdp.getSysConfig("report_servlet");
            var requestUrl = h + "?reportlet=erp/scm/InquirySheet.cpt&scmContractCode=" + scmContractCode
                + "&format=excel&extype=simple" + "&__filename__=" + Scdp.StrUtil.cjkEncode(scmContractCode + "_" + projectName);
            window.open(requestUrl, "new");
            /*Ext.data.JsonP.request({
             url: requestUrl,
             params: {purchaseReqNo: purchaseReqNo, format: "excel", extype:"simple"}
             });*/
        }
        //}
    },
    doBtnPushQuotation: function () {
        var me = this;
        var activeTab = me.view.getCmp("quotationTab").getActiveTab();
        if (activeTab) {
            var postData = {scmContractId: activeTab.cid};
            Scdp.doAction("purchasereq-pushquotation", postData, function (result) {
                if(true){
                    Scdp.MsgUtil.info(result.msg);
                }

            }, false, false, false,null)
        }

    },
    //M6_C3_F1_优化
    queryByScmPurchaseReqDto: function (me) {
        try {
            var prmProjectMainId = me.view.getCmp('queryForm->prmProjectMainId').gotValue();
            var purchaseReqNo = me.view.getCmp('queryForm->purchaseReqNo').gotValue();
            var departmentCode = me.view.getCmp('queryForm->officeId').gotValue();
            var state = me.view.getCmp('queryForm->state').gotValue();
            var queryConditions = {};
            if (Scdp.ObjUtil.isNotEmpty(prmProjectMainId))
                queryConditions.prmProjectMainId = prmProjectMainId;
            if (Scdp.ObjUtil.isNotEmpty(purchaseReqNo))
                queryConditions.purchaseReqNo = purchaseReqNo;
            if (Scdp.ObjUtil.isNotEmpty(departmentCode))
                queryConditions.officeId = departmentCode;
            if (Scdp.ObjUtil.isNotEmpty(state))
                queryConditions.state = state;
            var modulePath = me.view.modulePath;
            var layoutFile = me.view.layoutFile;
            var postData = {queryConditions: queryConditions, modulePath: modulePath, layoutFile: layoutFile};
            var oldRecord = me.view.getCmp('scmPurchaseReqDto').getSelectionModel().getSelection()
            Scdp.doAction("purchasereq-query", postData, function (result) {
                Ext.Array.forEach(result.scmPurchaseReqDto, function (item) {
                    if (!map.containsKey(item.uuid + item.purchaseReqNo)) {
                        //me.view.getCmp('scmPurchaseReqDto').addRowItem(item,false);
                        me.view.getCmp('scmPurchaseReqDto').store.add(item);
                        map.put(item.uuid + item.purchaseReqNo, "");
                    }
                });
                var oldIndex;
                if (oldRecord && oldRecord.length > 0) {
                    var i = 0;
                    Ext.Array.forEach(me.view.getCmp('scmPurchaseReqDto').getStore().data.items, function (item) {
                        if (oldRecord[0].data.uuid == item.data.uuid && oldRecord[0].data.purchaseReqNo == item.data.purchaseReqNo) {
                            oldIndex = i;
                            return;
                        }
                        i++
                    });
                    if (oldIndex != null) {
                        me.view.getCmp('scmPurchaseReqDto').getSelectionModel().select(oldIndex);
                        me.view.getCmp('scmPurchaseReqDto').getView().focusRow(oldIndex);
                    }
                }
            }, false, false)
        } catch (err) {
            clearInterval(timer);
        }

    },
    doTimeoutbyAutoQuery: function () {
        var me = this;
        //每隔5分钟自动查询一次，未读记录加颜色标识
        me.view.getCmp('scmPurchaseReqDto').rowColorConfigFn = function (record) {
            if (record.data.isRead != "1")
                return 'x-grid-row-color-orange';
        };
        me.view.getCmp('scmPurchaseReqDto').on('select', function (grid, record, index) {
            if (record.data.isRead != "1") {
                record.set("isRead", "1");
                Scdp.doAction("prmpurchasereqdtl-update-isread", {uuid: record.data.uuid}, function () {
                });
            }
        });
        try {
            timer = setInterval(function () {
                me.queryByScmPurchaseReqDto(me)
            }, 5 * 60 * 1000);//js定时器
        } catch (err) {
            clearInterval(timer);
        }
        new Ext.util.DelayedTask(function () {
            me.queryByScmPurchaseReqDto(me)
        }).delay(1500)
    }
});