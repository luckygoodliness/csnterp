/**
 * Created by xuwm on 2016/8/10.
 */
Ext.define('Receipts.view.ReceiptsSView',{
    extend:'Scdp.mvc.AbstractCrudView_1',
    modulePath:'com/csnt/scdp/bizmodules/modules/prm/receipts',
    aHeight:480,
    aWidth:900,
    cpHeight:100,
    allowNullConditions:true,
    queryLayoutFile:'receiptsS-query-layout.xml',
    bindings:['prmReceiptsDto'],
    enableColumnMove:true,
    showHeaderCheckbox:false,
	withSelMode: false,
    hideScroll:true,
    canEdit:false,

    initComponent:function(){
        var me=this;
        this.callParent(arguments);
    }

});