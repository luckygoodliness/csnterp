Ext.define('Operatekeyprojectsinfo.view.ShowAllView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/operate/operatekeyprojectsinfo',
	aHeight: 600,  //指定内容面板高度
	aWidth: 1350,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'operatekeyprojectsinfo-query-layout.xml',
	editLayoutFile: 'showall-edit-layout.xml',
	//extraLayoutFile: 'operatekeyprojectsinfo-extra-layout.xml',
	bindings: ['operateKeyProjectsInfoDto'],
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