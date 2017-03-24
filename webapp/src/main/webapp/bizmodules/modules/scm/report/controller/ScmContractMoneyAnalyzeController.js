Ext.define("ScmReport.controller.ScmContractMoneyAnalyzeController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'ScmReport.view.ScmContractMoneyAnalyzeView',
    extraEvents: [{cid: 'conditionPanel->nowBegin', name: 'change', fn: 'nowBeginChange'},
        {cid: 'conditionPanel->nowEnd', name: 'change', fn: 'nowEndChange'}],
    queryAction: 'scmReport-query',
    afterInit: function () {
        var me = this;
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
    nowBeginChange: function () {
        var me = this;
        var view = me.view;
        var nowBeginCmp = view.getCmp("conditionPanel->nowBegin");
        var lastBeginCmp = view.getCmp("conditionPanel->lastBegin");
        var nowBegin = nowBeginCmp.gotValue();
        if (Scdp.ObjUtil.isNotEmpty(nowBegin)) {
            var years = nowBegin.getFullYear() - 1;
            var months = nowBegin.getMonth() + 1;
            var days = nowBegin.getDate();
            var lastBegin = years + "-" + months + "-" + days;
            lastBeginCmp.sotValue(lastBegin);
        }
    },
    nowEndChange: function () {
        var me = this;
        var view = me.view;
        var nowEndCmp = view.getCmp("conditionPanel->nowEnd");
        var lastEndCmp = view.getCmp("conditionPanel->lastEnd");
        var nowEnd = nowEndCmp.gotValue();
        if (Scdp.ObjUtil.isNotEmpty(nowEnd)) {
            var years = nowEnd.getFullYear() - 1;
            var months = nowEnd.getMonth() + 1;
            var days = nowEnd.getDate();
            var lastEnd = years + "-" + months + "-" + days;
            lastEndCmp.sotValue(lastEnd);
        }
    }

});