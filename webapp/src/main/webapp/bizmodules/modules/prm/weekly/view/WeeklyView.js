Ext.define('Weekly.view.WeeklyView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/weekly',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 50,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'weekly-query-layout.xml',
	editLayoutFile: 'weekly-edit-layout.xml',
	//extraLayoutFile: 'weekly-extra-layout.xml',
	bindings: ['prmWeeklyDto','prmWeeklyDto.prmBriefDto','prmWeeklyDto.prmMemberTrendDto','prmWeeklyDto.prmGoodsArrivalDto','prmWeeklyDto.prmCollectionMeasureDto','prmWeeklyDto.prmProgressDto','prmWeeklyDto.prmProblemDto','prmWeeklyDto.prmMeetingSummaryDto','prmWeeklyDto.prmWeeklyReceiptDto','prmWeeklyDto.prmWeeklyPayDto'],
	canEdit: true,
	enableColumnMove: true,
	showHeaderCheckbox: true,
    withSelMode:false,//result勾选框的显示
	needSplitPage: true,
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
	},
	afterInit: function () {
		var me = this;
        var editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '自动填充',
            cid: 'extBtnFill',
            iconCls: 'temp_icon_16',
            disabled:true
        });
        me.resetDeleteBriefToolbar();
        me.resetDeleteMemberTrendToolbar();
        me.resetDeleteGoodsArrivalToolbar();
        me.resetDeleteCollectionMeasureToolbar();
        me.resetDeleteProgressToolbar();
        me.resetDeleteProblemToolbar();
        me.resetDeleteMeetingSummaryToolbar();
        me.hidePrmWeeklyReceiptToolbar();
        me.hidePrmWeeklyPayToolbar();

    },
    resetDeleteBriefToolbar: function () {
        var me = this;
        //项目简报
        var prmBriefGrid = me.getCmp("prmBriefDto");
        prmBriefGrid.doDeleteRow = function () {
            var prmBriefGridController = Scdp.getActiveModule().controller;
            prmBriefGridController.deleteBrief();
        };
        prmBriefGrid.getCmp("toolbar->addRowBtn").hide();
        prmBriefGrid.getCmp("toolbar->copyAddRowBtn").hide();
    },
    resetDeleteMemberTrendToolbar: function () {
        var me = this;
        //人员动向
        var prmMemberTrendGrid = me.getCmp("prmMemberTrendDto");
        prmMemberTrendGrid.doDeleteRow = function () {
            var prmMemberTrendGridController = Scdp.getActiveModule().controller;
            prmMemberTrendGridController.deleteMemberTrend();
        };
        prmMemberTrendGrid.getCmp("toolbar->addRowBtn").hide();
        prmMemberTrendGrid.getCmp("toolbar->copyAddRowBtn").hide();
    },
    resetDeleteGoodsArrivalToolbar: function () {
        var me = this;
        //到货确认
        var prmGoodsArrivalGrid = me.getCmp("prmGoodsArrivalDto");
        prmGoodsArrivalGrid.doDeleteRow = function () {
            var prmGoodsArrivalGridController = Scdp.getActiveModule().controller;
            prmGoodsArrivalGridController.deleteGoodsArrival();
        };
        prmGoodsArrivalGrid.getCmp("toolbar->addRowBtn").hide();
        prmGoodsArrivalGrid.getCmp("toolbar->copyAddRowBtn").hide();
    },
    resetDeleteCollectionMeasureToolbar: function () {
        var me = this;
        //收款计量
        var prmCollectionMeasureGrid = me.getCmp("prmCollectionMeasureDto");
        prmCollectionMeasureGrid.doDeleteRow = function () {
            var prmCollectionMeasureGridController = Scdp.getActiveModule().controller;
            prmCollectionMeasureGridController.deleteCollectionMeasure();
        };
        prmCollectionMeasureGrid.getCmp("toolbar->addRowBtn").hide();
        prmCollectionMeasureGrid.getCmp("toolbar->copyAddRowBtn").hide();
    },
    resetDeleteProgressToolbar: function () {
        var me = this;
        //工期进度
        var prmProgressGrid = me.getCmp("prmProgressDto");
        prmProgressGrid.doDeleteRow = function () {
            var prmProgressGridController = Scdp.getActiveModule().controller;
            prmProgressGridController.deleteProgress();
        };
        prmProgressGrid.getCmp("toolbar->addRowBtn").hide();
        prmProgressGrid.getCmp("toolbar->copyAddRowBtn").hide();
    },
    resetDeleteProblemToolbar: function () {
        var me = this;
        //问题申报
        var prmProblemGrid = me.getCmp("prmProblemDto");
        prmProblemGrid.doDeleteRow = function () {
            var prmProblemGridController = Scdp.getActiveModule().controller;
            prmProblemGridController.deleteProblem();
        };
        prmProblemGrid.getCmp("toolbar->addRowBtn").hide();
        prmProblemGrid.getCmp("toolbar->copyAddRowBtn").hide();
    },
    resetDeleteMeetingSummaryToolbar: function () {
        var me = this;
        //会议纪要
        var prmMeetingSummaryGrid = me.getCmp("prmMeetingSummaryDto");
        prmMeetingSummaryGrid.doDeleteRow = function () {
            var  prmMeetingSummaryGridController = Scdp.getActiveModule().controller;
            prmMeetingSummaryGridController.deleteMeetingSummary();
        };
        prmMeetingSummaryGrid.getCmp("toolbar->addRowBtn").hide();
        prmMeetingSummaryGrid.getCmp("toolbar->copyAddRowBtn").hide();
    },
    hidePrmWeeklyReceiptToolbar: function () {
        var me = this;
        //收款情况
        var prmMeetingSummaryGrid = me.getCmp("prmMeetingSummaryDto");
        prmMeetingSummaryGrid.getCmp("toolbar->addRowBtn").hide();
        prmMeetingSummaryGrid.getCmp("toolbar->copyAddRowBtn").hide();
    },
    hidePrmWeeklyPayToolbar: function () {
        var me = this;
        //支付情况
        var prmMeetingSummaryGrid = me.getCmp("prmMeetingSummaryDto");
        prmMeetingSummaryGrid.getCmp("toolbar->addRowBtn").hide();
        prmMeetingSummaryGrid.getCmp("toolbar->copyAddRowBtn").hide();
    }

});