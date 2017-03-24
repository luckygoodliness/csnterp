Ext.define('Rtfreevalue.view.RtfreevalueView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/fad/rtfreevalue',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'rtfreevalue-query-layout.xml',
	editLayoutFile: 'rtfreevalue-edit-layout.xml',
	//extraLayoutFile: 'rtfreevalue-extra-layout.xml',
	bindings: ['fadRtfreevalueDto'],
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