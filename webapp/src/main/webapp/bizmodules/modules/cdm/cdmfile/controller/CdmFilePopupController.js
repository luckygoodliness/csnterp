Ext.define("Cdmfile.controller.CdmFilePopupController", {
    extend: 'Scdp.mvc.AbstractController',
    viewClass: 'Cdmfile.view.CdmFilePopupView',
    uniqueValidateFields: [],
    extraEvents: [{cid: 'fileDownload', name: 'click', fn: 'downLoadFile'}],
    afterInit: function () {
        var me = this;
        if (me.postData != null) {
            var postData = me.postData;
            Scdp.doAction("file-popup-query", postData, function (result) {
                if (Scdp.ObjUtil.isNotEmpty(result.cdmFileRelationDto)) {
                    me.view.getCmp("cdmFileRelationDto").sotValue(result.cdmFileRelationDto);
                }
            })
        }
    },
    downLoadFile: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    }
});