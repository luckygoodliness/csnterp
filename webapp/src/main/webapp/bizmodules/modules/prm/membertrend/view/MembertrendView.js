Ext.define('Membertrend.view.MembertrendView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/membertrend',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 50,
	epHeight: 100,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'membertrend-query-layout.xml',
	editLayoutFile: 'membertrend-edit-layout.xml',
	//extraLayoutFile: 'membertrend-extra-layout.xml',
	bindings: ['prmMemberTrendDto'],
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
	}
});