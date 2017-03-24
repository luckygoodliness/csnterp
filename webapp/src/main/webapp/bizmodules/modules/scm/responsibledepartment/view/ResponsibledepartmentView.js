Ext.define('Responsibledepartment.view.ResponsibledepartmentView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/responsibledepartment',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 60,
	epHeight: 160,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'responsibledepartment-query-layout.xml',
	editLayoutFile: 'responsibledepartment-edit-layout.xml',
	//extraLayoutFile: 'responsibledepartment-extra-layout.xml',
	bindings: ['scmResponsibleDepartmentDto'],
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
        var principalCmp=me.getCmp("scmResponsibleDepartmentDto->principalName");
        var attacheCmp=me.getCmp("scmResponsibleDepartmentDto->attacheName");
        principalCmp.filterConditions={orgfilter:" org_code='" +Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE)+"' "};
		attacheCmp.filterConditions={orgfilter:" org_code='" +Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE)+"' "};
	}
});