/**
 * Created by xuwm on 2016/8/10.
 */
Ext.define('Receipts.controller.ReceiptsSController',{
    extend:'ErpMvc.controller.ErpAbstractCrudController',
    viewClass:'Receipts.view.ReceiptsSView',
    uniqueValidateFields:[],
    queryAction:'receipts-query',
    afterInit:function(){
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        var queryPanelCmp=view.getQueryPanel();
        var stateQCmp=queryPanelCmp.getCmp("stateQ");
        stateQCmp.sotValue("2");
        stateQCmp.setDisabled(true);
        me.doQuery();
    }
})