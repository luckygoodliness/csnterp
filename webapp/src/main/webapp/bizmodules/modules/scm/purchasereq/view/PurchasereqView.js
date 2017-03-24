Ext.define('Purchasereq.view.PurchasereqView', {
    extend: 'Scdp.mvc.AbstractView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/purchasereq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,l
    allowNullConditions: true,
    layoutFile: 'purchasereq-layout.xml',
    //extraLayoutFile: 'purchasereq-extra-layout.xml',
    bindings: ['scmPurchaseReqDto', 'scmPurchaseReqDto.prmPurchaseReqDetailDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    initComponent: function () {
        var me = this;
        this.callParent(arguments)
    },

    //添加报价单
    addTabPanel: function () {
        var me = this;
        var quotationTab = me.getCmp("quotationTab");
        var count = quotationTab.getLayout().getLayoutItems().length;
        var nextPanel = me.buildGridPanel();
        nextPanel.setTitle('询价单' + count);
        me.hideToolBar(nextPanel);
        nextPanel.rowDelFn = function () {
            me.removeContractItems(this, me);
        }
        //nextPanel.on('close', function () {
        //    me.closeQuotation(nextPanel);
        //});
        nextPanel.rowAddFn = function (record) {
            me.addContractItems(nextPanel, record);
        };
        quotationTab.getTabBar().on("dblclick", function () {
            Scdp.MsgUtil.prompt('修改询价单名称', function (buttonId, text) {
                if (buttonId != 'ok') return;
                quotationTab.getActiveTab().setTitle(text);
                me.controller.updateContractCode(quotationTab.getActiveTab().cid, text);
            }, null, null, quotationTab.getActiveTab().title);
        });
        quotationTab.add(nextPanel);
        quotationTab.setActiveTab(nextPanel);
    },
    buildGridPanel: function () {
        return Ext.create('Scdp.grid.JEGrid', {
            cid: 'scmContractDto',
            store: Ext.create('Prmpurchasereq.store.PrmPurchaseReqDetailStore'),
            withSelMode: true,
            closable: true,
            border: 0,
            columns: [
                {
                    xtype: 'JTextCol',
                    width: 150,
                    dataIndex: 'name',
                    cid: 'name',
                    text: '设备名称'
                },
                {
                    xtype: 'JTextCol',
                    width: 120,
                    dataIndex: 'model',
                    cid: 'model',
                    text: '规格型号'
                },
                {
                    xtype: 'JDecCol',
                    dataIndex: 'amount',
                    width: 100,
                    cid: 'amount',
                    text: '申请数量'
                },
                {
                    xtype: 'JDecCol',
                    dataIndex: 'handleAmount',
                    width: 100,
                    cid: 'handleAmount',
                    text: '选入数量'
                },
                {
                    xtype: 'JDecCol',
                    dataIndex: 'budgetPrice',
                    width: 100,
                    cid: 'budgetPrice',
                    text: '预算单价'
                },
                {
                    xtype: 'JDecCol',
                    dataIndex: 'expectedPrice',
                    width: 100,
                    cid: 'expectedPrice',
                    text: '意向单价'
                },
                {
                    xtype: 'JTextCol',
                    width: 100,
                    dataIndex: 'factory',
                    cid: 'factory',
                    text: '厂家'
                },
                {
                    xtype: 'JTextCol',
                    dataIndex: 'supplierId',
                    width: 120,
                    cid: 'supplierId',
                    text: '供应商'
                },
                {
                    xtype: 'JTextCol',
                    dataIndex: 'supplierProperty',
                    width: 120,
                    cid: 'supplierProperty',
                    text: '供应商属性'
                },
                {
                    xtype: 'JDatetimeCol',
                    dataIndex: 'arriveDate',
                    width: 120,
                    cid: 'arriveDate',
                    text: '到货时间'
                }
            ]
        });
    },

    afterInit: function () {
        var me = this;
        me.scmPurchaseReqGridInit(me);
    },

    hideToolBar: function (nextPanel) {
        nextPanel.getCmp("toolbar->rowMoveDownBtn").hide();
        nextPanel.getCmp("toolbar->rowMoveUpBtn").hide();
        nextPanel.getCmp("toolbar->rowMoveTopBtn").hide();
        nextPanel.getCmp("toolbar->rowMoveBottomBtn").hide();
        nextPanel.getCmp("toolbar->copyAddRowBtn").hide();
        nextPanel.getCmp("toolbar->addRowBtn").hide();
    },

    //关闭报价单
    closeQuotation: function (nextPanel) {
        var me = this;
        var onlyCloseTab = nextPanel.onlyCloseTab;
        if (Scdp.ObjUtil.isEmpty(onlyCloseTab) || onlyCloseTab == false) {
            var selectItems = nextPanel.getStore().data.items;
            var prmPurchaseReqDetailDto = [];
            Ext.Array.forEach(selectItems, function (item) {
                prmPurchaseReqDetailDto.push(item.data);
            });
            if (prmPurchaseReqDetailDto.length > 0) {
                Scdp.MsgUtil.confirm("请确认是否删除该报价单及该询价单中所有报价！", function (btn) {
                    if ("yes" == btn) {
                        me.controller.rollBackAll(prmPurchaseReqDetailDto, nextPanel.cid);
                    }
                })
            }
        }
    },

    //删除报价单明细
    removeContractItems: function (nextPanel, view) {
        var activeTab = view.getCmp("quotationTab").getActiveTab();
        var selectItems = activeTab.getSelectionModel().getSelection();
        if (selectItems.length < 1) {
            Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
            activeTab.getStore();
            return false;
        } else {
            Scdp.MsgUtil.confirm("请确认是否删除所选报价！", function (btn) {
                if ("yes" == btn) {
                    view.controller.rollBackDetail(nextPanel)
                } else {
                    return false;
                }
            })
        }
    },

    addContractItems: function (nextPanel, record) {
        nextPanel.addRowItem(record, false);
    },
    scmPurchaseReqGridInit: function (view) {
        openPurchaseReq = function (uuid, isProject) {
            var postData = {};
            postData.wf_businessKey = uuid;
            var menuCode = "";
            if (isProject == "1") {//项目采购申请
                menuCode = 'PRMPURCHASEREQ';
            } else {//非项目采购申请
                menuCode = 'NONPRMPURCHASEREQ';
            }
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        };
        openwithAttach = function (withAttach, prmPurchaseReqId) {
            var supplierLimitDetailController = Scdp.getActiveModule().controller;
            supplierLimitDetailController.showAllDetail();
            if ('是' == withAttach) {
                var postData = {};
                postData.dataId = prmPurchaseReqId;
                var callback = function () {
                };
                var controller = Ext.create("Cdmfile.controller.CdmFilePopupController");
                controller.postData = postData;
                var win = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '附件管理', '确定');
            }
        };
        var scmPurchaseReqGrid = view.getCmp("scmPurchaseReqDto");
        var scmPurchaseReqGridColumns = scmPurchaseReqGrid.getColumnBydataIndex("purchaseReqNo");
        var scmPurchaseReqGridColumn = scmPurchaseReqGrid.getColumnBydataIndex("withAttach");
        scmPurchaseReqGridColumns.renderer = function (value, p, record) {
            return Ext.String.format(
                    '<a href="javascript:openPurchaseReq(\'' + record.data.prmPurchaseReqId + '\',\'' + record.data.isProject + '\');" target="_blank" style="color: blue">' + value + '</a>'
            );
        };
        scmPurchaseReqGridColumn.renderer = function (value, p, record) {
            return Ext.String.format(
                    '<a href="javascript:openwithAttach(\'' + record.data.withAttach + '\',\'' + record.data.prmPurchaseReqId + '\');" target="_blank" style="color: blue">' + value + '</a>'
            );
        };
        scmPurchaseReqGrid.rowColorConfigFn = view.scmPurchaseReqRowColorConfigFn;
    },
    scmPurchaseReqRowColorConfigFn: function (record) {
        if (record.data.stampCount > 0) {
            return 'erp-grid-font-color-red';
        } else {
            return null;
        }
    }

});