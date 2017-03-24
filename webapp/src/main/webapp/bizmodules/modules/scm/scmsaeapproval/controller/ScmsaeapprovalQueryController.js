/**
 * Created by lijx on 2016/9/7.
 */
Ext.define("Scmsaeapproval.controller.ScmsaeapprovalQueryController", {
    //M3_C5_F2_页面调整
    extend: 'Scdp.mvc.AbstractController',
    viewClass: 'Scmsaeapproval.view.ScmsaeapprovalQueryView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'searchBtn', name: 'click', fn: 'fnQuery'},
        {cid: 'resetBtn', name: 'click', fn: 'fnReset'},
        {cid: 'scmSaeApprovalDto->showAllDetail', name: 'change', fn: 'fnShowAll'},
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmsaeapproval.dto.ScmSaeApprovalDto',
    afterInit: function () {
        var me = this;

        me.view.getCmp("scmSaeApprovalDto->curYear").sotValue(me.actionParams.curYear);

        me.view.getCmp("scmSaeResultDto").on("cellclick", function (b, c, d, e, f, g, h) {
            var showAllDetail = me.view.getCmp("scmSaeApprovalDto->showAllDetail").gotValue();
            var curYear = me.view.getCmp("scmSaeApprovalDto->curYear").gotValue();
            var materialCode = me.view.getCmp("scmSaeApprovalDto->materialCode").gotValue();
            var supplierId = e.data.supplierId;
            Scdp.doAction("scmsaeapproval-queryobject",{
                curYear:curYear,supplierId:supplierId,materialCode:materialCode,showAllDetail:showAllDetail
            },function(res){
                me.view.getCmp("scmSaeObjectDto").store.loadRawData(res.scmSaeObjectDto);
            });
        });
    },
    fnQuery:function(){
        var me = this;
        var curYear = me.view.getCmp("scmSaeApprovalDto->curYear").gotValue();
        var supplierId = me.view.getCmp("scmSaeApprovalDto->supplierId").gotValue();
        var materialCode = me.view.getCmp("scmSaeApprovalDto->materialCode").gotValue();
        Scdp.doAction("scmsaeapproval-queryresult",{
            curYear:curYear, supplierId2:supplierId,supplierId:me.actionParams.notInRow,materialCode:materialCode
        },function(res){
            me.view.getCmp("scmSaeResultDto").store.loadRawData(res.scmSaeResultDto);
        })
    },
    fnReset:function(){
        var me = this;
        //me.view.getCmp("scmSaeApprovalDto->curYear").sotValue("");
        me.view.getCmp("scmSaeApprovalDto->supplierId").sotValue("");
        me.view.getCmp("scmSaeApprovalDto->supplierName").sotValue("");
        me.view.getCmp("scmSaeApprovalDto->materialCode").sotValue("");
    },
    fnShowAll:function(a,b,c){
        var me = this;
        var showAllDetail = me.view.getCmp("scmSaeApprovalDto->showAllDetail").gotValue();
        var curYear = me.view.getCmp("scmSaeApprovalDto->curYear").gotValue();
        var materialCode = me.view.getCmp("scmSaeApprovalDto->materialCode").gotValue();
        var record = me.view.getCmp("scmSaeResultDto").getCurRecord()
        var supplierId = "";
        if(record!=null){
            supplierId = record.get("supplierId");
        }
        Scdp.doAction("scmsaeapproval-queryobject",{
            curYear:curYear,supplierId:supplierId,materialCode:materialCode,showAllDetail:showAllDetail
        },function(res){
            me.view.getCmp("scmSaeObjectDto").store.loadRawData(res.scmSaeObjectDto);
        });
    }
});
