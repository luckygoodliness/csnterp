var res;
var reqUuids;
var purchasePackageId;
Ext.define("Purchaseplan.controller.PickerPurchaseplanController", {
    //M3_C5_F2_页面调整
    extend: 'Scdp.mvc.AbstractController',
    viewClass: 'Purchaseplan.view.PickerPurchaseplanView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'prmPurchasePackageDto', name: 'select', fn: 'filterDetailByPackage'},
        {cid: 'showAllDetail', name: 'change', fn: 'showAllDetails'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto',
    showAllDetails: function () {
        if (Scdp.ObjUtil.isEmpty(purchasePackageId)) {
            var me = this;
            var selectPackage = me.view.getCmp("prmPurchasePackageDto").getCurRecord();
            var detailsGrid = me.view.getCmp("prmPurchasePlanDetailDto");
            me.sortDetailByPackage(selectPackage, detailsGrid);
        }
    },
    afterInit: function () {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(me.actionParams)) {
            var prmProjectMainId = me.actionParams.prmProjectMainId;
            purchasePackageId = me.actionParams.purchasePackageId;
            reqUuids = me.actionParams.reqUuids;
            me.initPackages(prmProjectMainId);
        }
        //me.filterDetailByPackage();
    },
    initPackages: function (prmProjectMainId) {
        var me = this;
        var project = {};
        project.uuid = prmProjectMainId;
        var packagesGrid = me.view.getCmp("prmPurchasePackageDto");
        project.dtoClass = "com.csnt.scdp.bizmodules.modules.prm.purchaseplan.dto.PrmPurchasePlanDto";
        Scdp.doAction("purchaseplan-load", project, function (result) {
            if (Scdp.ObjUtil.isNotEmpty(result)) {
                res = result;
                var prmPurchasePackageDto = "";
                prmPurchasePackageDto = me.setPackagesGrid(result.prmPurchasePlanDto.prmPurchasePackageDto);
                packagesGrid.sotValue(prmPurchasePackageDto);
                packagesGrid.getView().focusRow(0);
                packagesGrid.getSelectionModel().select(0);
                me.filterDetailByPackage();
            }
        });
        me.view.getCmp("prmPurchasePackageDto").sotEditable(false);
        me.view.getCmp("prmPurchasePlanDetailDto").sotEditable(false);
    },
    //包表格选择事件
    filterDetailByPackage: function () {
        var me = this;
        var selectPackage = me.view.getCmp("prmPurchasePackageDto").getCurRecord();
        var detailsGrid = me.view.getCmp("prmPurchasePlanDetailDto");
        if (Scdp.ObjUtil.isNotEmpty(selectPackage)) {
            me.sortDetailByPackage(selectPackage, detailsGrid);
        }
    },
    //根据包名过滤包详细
    sortDetailByPackage: function (selectPackage, detailsGrid) {
        var me = this;
        var showAll = me.view.getCmp("showAllDetail").gotValue();
        //这里不能用这个方法，数据量一大会有性能问题
        //detailsGrid.clearData();
        detailsGrid.getStore().removeAll();
        var Puuid = selectPackage.get("uuid");
        var details = res.prmPurchasePlanDto.prmPurchasePlanDetailDto;
        var newArr = new Array();
        //是否显示全部
        if (showAll && showAll == 1) {
            newArr = me.setDetailsGrid(details, "all");
        } else {
            newArr = me.setDetailsGrid(details, Puuid);
        }
        detailsGrid.sotValue(newArr);
    },
    //选过的不再显示
    setPackagesGrid: function (details) {
        var result = new Array();
        if (Scdp.ObjUtil.isEmpty(purchasePackageId)) {
            result = Ext.Array.filter(details,function (item){
                if(item.packageState == '2') {
                    return true;
                } else {
                    return false;
                }
            },this);
        }
        else {
            result = Ext.Array.filter(details,function (item){
                if(item.uuid == purchasePackageId && item.packageState == '2') {
                    return true;
                } else {
                    return false;
                }
            },this);
        }
        return result;
    },

    setDetailsGrid: function (details, Puuid) {
        if(Puuid == "all") {
            return Ext.Array.filter(details,function (item){
                if(Ext.Array.contains(reqUuids, item.uuid)) {
                    return false;
                } else {
                    return true;
                }
            },this);
        } else {
            return Ext.Array.filter(details,function (item){
                if(item.purchasePackageId == Puuid) {
                    if(Ext.Array.contains(reqUuids, item.uuid)) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            },this);
        }

    }
});