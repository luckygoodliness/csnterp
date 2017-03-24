Ext.define('Fadsuppliermapping.view.FadsuppliermappingView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/fadsuppliermapping',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 50,
	epHeight: 150,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'fadsuppliermapping-query-layout.xml',
	editLayoutFile: 'fadsuppliermapping-edit-layout.xml',
	//extraLayoutFile: 'fadsuppliermapping-extra-layout.xml',
	bindings: ['fadSupplierMappingDto'],
	canEdit: true,
	enableColumnMove: true,
	showHeaderCheckbox: true,
	needSplitPage: true,
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
		var batchDelBtn =me.getCmp("batchDelBtn");
		batchDelBtn.hidden=false;
		batchDelBtn.text="批量删除";
	},
	afterInit: function () {
		var me = this;
		var queryToolbar = me.getQueryToolbar();
		queryToolbar.add({
			text: '批量设置',
			cid: 'batchSetting',
			iconCls: 'temp_icon_16'
		});

	}
});