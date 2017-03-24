Ext.define('Responsibleclass.view.ResponsibleclassView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/responsibleclass',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 60,
	epHeight: 150,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'responsibleclass-query-layout.xml',
	editLayoutFile: 'responsibleclass-edit-layout.xml',
	//extraLayoutFile: 'responsibleclass-extra-layout.xml',
	bindings: ['scmResponsibleClassDto'],
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
        var principalCmp=me.getCmp("scmResponsibleClassDto->principalName");
        principalCmp.filterConditions={orgfilter:" org_code='" +Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE)+"' "};
		//2.添加查询区的工具按钮
		var editToolbar = me.getEditToolbar();
		editToolbar.add({
			text: '批量设置',
			cid: 'batchSet',
			iconCls: 'setting_btn',
			//disabled: "true"
		});
	}
});