Ext.define('Payreq.view.PayreqView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/payreq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'payreq-query-layout.xml',
    editLayoutFile: 'payreq-edit-layout.xml',
    bindings: ['fadPayReqHDto', 'fadPayReqHDto.fadPayReqCDto', 'fadPayReqHDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    resetToolbar: function () {
        var me = this;

        me.getEditToolbar().add({
            xtype: 'button',
            text: '支付',
            cid: 'payBtn',
            iconCls: "add_btn"
        });
        me.getEditToolbar().add({
            xtype: 'button',
            text: '请款',
            cid: 'cashReqBtn',
            iconCls: "add_btn"
        });

        me.getEditToolbar().add({
            xtype: 'button',
            text: '凭证',
            cid: 'toCertificate',
            iconCls: 'erp-voucher',
            disabled: "true"
        });

        me.getEditToolbar().add({
            xtype: 'button',
            text: '发送消息',
            cid: 'btnSendMsg',
            iconCls: "erp-chat"
        });

        me.getCmp("editPanel-\x3ecopyAddBtn").setVisible(false);

        me.getCmp("editPanel->payBtn").setDisabled(true);
        me.getCmp("editPanel->cashReqBtn").setDisabled(true);
    },

    afterInit: function () {
        var me = this;
        me.resetToolbar();

        var grid = me.getCmp("fadPayReqCDto");
        grid.getCmp("toolbar->copyAddRowBtn").hide();
        grid.doAddRow = me.doAddRowOverride;
        grid.afterEditGrid = me.afterEditGrid;
        grid.rowColorConfigFn = me.rowColorConfigFnReqCGrid;
        grid.addListener('cellClick', me.cellClick);

        Ext.Array.each(grid.columns, function (item) {
            if (item.summaryType) {
                Ext.apply(item, {
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return '<strong style="color: red">合计：' + Ext.util.Format.number(value, '0,000.00') + '</strong>';
                    }
                });
            }
        });
    },

    doAddRowOverride: function () {
        var controller = Scdp.getActiveModule().controller;
        controller.pickContract();
    },

    afterEditGrid: function (a, b) {
        var record = a.record;
        if (b) {
            if (a.field == 'reqMoney') {
                a.record.set("approveMoney", a.value);
                a.record.set("purchaseChiefMoney", a.value);
                a.record.set("auditMoney", a.value);
                a.record.set("scmContractExpectPaid", a.value + record.get("scmContractPaidMoney"));
            } else if (a.field == 'approveMoney') {
                a.record.set("purchaseChiefMoney", a.value);
                a.record.set("auditMoney", a.value);
            } else if (a.field == 'purchaseManagerMoney') {
                a.record.set("purchaseChiefMoney", a.value);
                a.record.set("auditMoney", a.value);
            } else if (a.field == 'purchaseChiefMoney') {
                a.record.set("auditMoney", a.value);
            }

            if (Scdp.ObjUtil.isNotEmpty(record.get("scmContractAmount")) && record.get("scmContractAmount") != 0) {
                var reqMoneyRate = Erp.MathUtil.divideNumber(
                    Erp.MathUtil.multiNumber(Erp.MathUtil.plusNumber(record.get("reqMoney"), record.get("scmContractPaidMoney"), false), 100),
                    record.get("scmContractAmount")
                );
                a.record.set("reqMoneyRate", reqMoneyRate);
                var approveMoneyRate = Erp.MathUtil.divideNumber(
                    Erp.MathUtil.multiNumber(Erp.MathUtil.plusNumber(record.get("approveMoney"), record.get("scmContractPaidMoney"), false), 100),
                    record.get("scmContractAmount"));
                a.record.set("approveMoneyRate", approveMoneyRate);

                var purchaseManagerMoneyRate = Erp.MathUtil.divideNumber(
                    Erp.MathUtil.multiNumber(Erp.MathUtil.plusNumber(record.get("purchaseManagerMoney"), record.get("scmContractPaidMoney"), false), 100),
                    record.get("scmContractAmount"));
                a.record.set("purchaseManagerMoneyRate", purchaseManagerMoneyRate);

                var purchaseChiefMoneyRate = Erp.MathUtil.divideNumber(
                    Erp.MathUtil.multiNumber(Erp.MathUtil.plusNumber(record.get("purchaseChiefMoney"), record.get("scmContractPaidMoney"), false), 100),
                    record.get("scmContractAmount"));
                a.record.set("purchaseChiefMoneyRate", purchaseChiefMoneyRate);

                var auditMoneyRate = Erp.MathUtil.divideNumber(
                    Erp.MathUtil.multiNumber(Erp.MathUtil.plusNumber(record.get("auditMoney"), record.get("scmContractPaidMoney"), false), 100),
                    record.get("scmContractAmount"));
                a.record.set("auditMoneyRate", auditMoneyRate);
            }
        }
    },

    rowColorConfigFnReqCGrid: function (a, b, c) {
        //申请支付金额大于到货确认金额-已付金额，标识
        var me = this;
        var renderStates = ["0", "1", "2", "5", "6", "9"];//只有新增（退回）、锁定、工作流中的业务数据才做颜色渲染
        if (me.ownerCt.getCmp("fadPayReqHDto") && me.ownerCt.getCmp("fadPayReqHDto->state")) {
            var state = me.ownerCt.getCmp("fadPayReqHDto->state").gotValue();
            if (renderStates.indexOf(state) >= 0) {
                var paid = Scdp.ObjUtil.isEmpty(a.data.scmContractPaidMoney) ? 0 : a.data.scmContractPaidMoney;
                var checked = Scdp.ObjUtil.isEmpty(a.data.scmContractCheckedAmount) ? 0 : a.data.scmContractCheckedAmount;
                var applyed = Scdp.ObjUtil.isEmpty(a.data.auditMoney) ? 0 : a.data.auditMoney;
                var checkResult = a.data.scmContractCode != undefined && a.data.contractNature === "0" &&
                    (Erp.MathUtil.minusNumber(Erp.MathUtil.minusNumber(checked, paid), applyed) < 0);

                if (a.data.state != '4' && a.data.state != '8') {
                    if (a.data.isBlackList == 1 && checkResult) {
                        return 'erp-grid-row-color-red';
                    } else if (a.data.isBlackList == 1) {
                        return 'erp-grid-row-color-gray';
                    } else if (checkResult) {
                        return 'x-grid-row-color-coral';
                    }
                }
            }
            return null;
        }
    },

    cellClick: function (grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        if (grid.up().getColumnByIndex(arguments[2]) &&
            grid.up().getColumnByIndex(arguments[2]).dataIndex == "scmContractCode") {
            var scmContractId = record.get("scmContractId")
            if (Scdp.ObjUtil.isNotEmpty(scmContractId)) {
                var param = {uuid: scmContractId};
                var menuCode = 'CONTRACTESTABLISHMENT';
                Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
            }
        }
    }
});