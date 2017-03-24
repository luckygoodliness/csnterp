Ext.define('Operatekeyprojectsinfo.view.OperatekeyprojectsinfoView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/operate/operatekeyprojectsinfo',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 60,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'operatekeyprojectsinfo-query-layout.xml',
	editLayoutFile: 'operatekeyprojectsinfo-edit-layout.xml',
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
        //me.getCmp('editToolbar->addNew2Btn').setVisible(false);
        //me.getCmp('editToolbar->copyAddBtn').setVisible(false);
        //me.getCmp('editToolbar->modifyBtn').setVisible(false);
        //me.getCmp('editToolbar->deleteBtn').setVisible(false);
        //me.getCmp('editToolbar->cancelBtn').setVisible(false);
        //me.getCmp('editToolbar->saveBtn').setVisible(false);
	}
});