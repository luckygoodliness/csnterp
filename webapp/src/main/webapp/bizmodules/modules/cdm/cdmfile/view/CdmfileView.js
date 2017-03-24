Ext.define('Cdmfile.view.CdmfileView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/cdm/cdmfile',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 60,
	epHeight: 160,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'cdmfile-query-layout.xml',
	editLayoutFile: 'cdmfile-edit-layout.xml',
	//extraLayoutFile: 'cdmfile-extra-layout.xml',
	bindings: ['cdmFileRelationDto'],
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
		var now = new Date();
		var editToolbar = me.getEditToolbar();
		this.getCmp("editPanel->addNew2Btn").setVisible(false);
		this.getCmp("editPanel->copyAddBtn").setVisible(false);
		this.getCmp("editPanel->modifyBtn").setVisible(false);
		this.getCmp("editPanel->deleteBtn").setVisible(false);
		this.getCmp("editPanel->cancelBtn").setVisible(false);
		this.getCmp("editPanel->saveBtn").setVisible(false);
		editToolbar.add({
				text: '模板文件导入',
				cid: 'btnTemplateImport',
				iconCls: 'temp_icon_16'
			}
		);
	}
});