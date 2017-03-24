Ext.define('Fadinterestrate.view.FadinterestrateView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/fad/fadinterestrate',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 50,
	epHeight: 100,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'fadinterestrate-query-layout.xml',
	editLayoutFile: 'fadinterestrate-edit-layout.xml',
	//extraLayoutFile: 'fadinterestrate-extra-layout.xml',
	bindings: ['fadInterestRateDto'],
	canEdit: true,
	enableColumnMove: true,
	showHeaderCheckbox: true,
	needSplitPage: true,
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
	},
	afterInit: function () {
		var me = this;
		//复制新增按钮隐藏
		me.getCmp("editPanel->copyAddBtn").hide();
		me.formatRate(me);
	},
	formatRate: function(view) {
		var rateColumns = view.getResultPanel().getColumnBydataIndex("rate");
        var depositRate = view.getResultPanel().getColumnBydataIndex("depositRate");
		rateColumns.renderer = function (value, p, record) {
            return Ext.String.format(Ext.util.Format.number(value * 100, '0.00') + '%');
		};
        depositRate.renderer = function (value, p, record) {
            return Ext.String.format(Ext.util.Format.number(value * 100, '0.00') + '%');
        };
	}
});