Ext.define("Goodsarrival.controller.NewgoodsarrivalController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Goodsarrival.view.NewgoodsarrivalView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'goodsArrival', name: 'click', fn: 'goodsArrival'},
        {cid: 'batchGoodsArrival', name: 'click', fn: 'batchGoodsArrival'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.goodsarrival.dto.ScmContractDetailDto',
    queryAction: 'goodsarrival-query',
    loadAction: 'goodsarrival-load',
    addAction: 'goodsarrival-add',
    modifyAction: 'goodsarrival-modify',
    deleteAction: 'goodsarrival-delete',
    exportXlsAction: "goodsarrival-exportxls",
    fillAction: "goodsarrival-fill",

    afterInit: function () {
        var me = this;
        var view =me.view;
        view.getCmp("conditionPanel->confirmState").sotValue("0");
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
        me.doQuery();
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
    //点击到货确认按钮跳出提示框并输入此次到货的数量
    goodsArrival: function () {
        var me = this;
        var selectRecord = me.view.getResultPanel().getCurRecord();
        if (!selectRecord) {
            Scdp.MsgUtil.info("未选择数据！");
            return false;
        }
        var uuidLst = [];
        var scmAmountLst = [];
        uuidLst.push(selectRecord.data.uuid);
        scmAmountLst.push(selectRecord.data.amount);
        var prmGoodsArrivalGrid = me.view.getCmp("prmGoodsArrivalDto");
        var count = prmGoodsArrivalGrid.getStore().getCount();
        var totalAmount = 0;
        var temp = 0;
        var win = Object;
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = prmGoodsArrivalGrid.getStore().getAt(i).data;
                var amount = rowData.amount;
//                prmGoodsArrivalGrid.getStore().getAt(i).set("amount", amount);
                totalAmount += amount;
            }
        }
        var name = Scdp.CacheUtil.get(Scdp.Const.USER_ID);
        var postData = {uuidLst: uuidLst, totalAmount: totalAmount, name: name};
        Scdp.doAction("goodsarrival-amount", postData, function (out) {
            temp = out.remainAmount;
            var form = Ext.widget("JForm", {
                height: 150,
                width: 300,
                layout: 'form',
                bodyPadding: '55 10 10 10',
                items: [
                    {
                        xtype: 'JDec',
                        cid: 'actualAmount',
                        allowBlank: false,
                        minValue: '0.00',
                        maxValue: temp,
                        value: temp,
                        fieldLabel: '本次到货数量'
                    }
                ]
            });
            win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '本次到货数量', "确认");
        });
        var callBack = function (subView) {
            if (!subView.validate()) {
                return false;
            }
            var actualAmount = [];
            actualAmount.push(subView.getCmp("actualAmount").gotValue());
            if (!actualAmount[0]>0) {
                Scdp.MsgUtil.info("确认数量不能为0！");
                return false;
            }
            var postData = {
                uuidLst: uuidLst,
                actualAmount: actualAmount,
                totalAmount: totalAmount,
                scmAmountLst: scmAmountLst
            };

            Scdp.doAction("goodsarrival-fill", postData, function (out) {

                win.close();
                me.loadItem(selectRecord.data.uuid);

                //M3_C17_F1_供方评价
                var params = { items:['到货时间',"设备质量","服务","综合评价"] };
                var callback = function(result){
                    Scdp.doAction("supplierinfor-supplierevaluation",{
                        uuid: selectRecord.data.supplierCode,
                        scmContractId: selectRecord.data.scmContractId,
                        arrivalTime:result.scoreArray[0],
                        equipmentQuality:result.scoreArray[1],
                        service:result.scoreArray[2],
                        comprehensive:result.scoreArray[3],
                        evaluateFrom:'3',
                        remark:result.suggest
                    },function(res){ });
                    //me.doQuery();
                }
                Scdp.FiveStarWin.show(params,callback,false,false);

            }, null, null, null, subView.getForm());
        };
    },
    //批量到货确认
    batchGoodsArrival: function () {
        var me = this;
        var selectedRecords = me.view.getResultPanel().getSelectionModel().getSelection();
        if (selectedRecords.length == 0) {
            Scdp.MsgUtil.info("未勾选数据！");
            return false;
        }
        var uuidLst = [];
        var supplierLst=[]; // //M3_C17_F1_供方评价
        for (var i = 0; i < selectedRecords.length; i++) {
            uuidLst.push(selectedRecords[i].data.uuid);
            supplierLst.push(selectedRecords[i].data.supplierCode);
        }
        var prmGoodsArrivalGrid = me.view.getCmp("prmGoodsArrivalDto");
        var name = Scdp.CacheUtil.get(Scdp.Const.USER_ID);
        var postData = {uuidLst: uuidLst, name: name};
        var callBack = function (subView) {
            win.close();
            Scdp.doAction("goodsarrival-fill", postData, function () {

                //M3_C17_F1_供方评价
                var params = { items:['到货时间',"设备质量","服务","综合评价"] };
                var callback = function(result){
                    Ext.Array.forEach(selectedRecords, function (a) {
                        Scdp.doAction("supplierinfor-supplierevaluation",{
                            uuid: a.data.supplierCode,
                            scmContractId: a.data.scmContractId,
                            arrivalTime:result.scoreArray[0],
                            equipmentQuality:result.scoreArray[1],
                            service:result.scoreArray[2],
                            comprehensive:result.scoreArray[3],
                            evaluateFrom:'3',
                            remark:result.suggest
                        },function(res){ });
                    })
                    //me.doQuery();
                }
                Scdp.FiveStarWin.show(params,callback,false,false);

            });
        };
        var form = Ext.widget("JForm", {
            height: 150,
            width: 500,
            layout: 'form',
            bodyPadding: '60 10 10 90',
            items: [
                {
                    xtype: 'label',
                    text: '批量到货将把勾选记录的剩余数量全部确认!是否继续?'
                }
            ]
        });
        var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '提示', "继续");
    }
});