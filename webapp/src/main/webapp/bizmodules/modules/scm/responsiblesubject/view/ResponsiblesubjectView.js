Ext.define('Responsiblesubject.view.ResponsiblesubjectView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/responsiblesubject',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 60,
	epHeight: 160,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'responsiblesubject-query-layout.xml',
	editLayoutFile: 'responsiblesubject-edit-layout.xml',
	//extraLayoutFile: 'responsiblesubject-extra-layout.xml',
	bindings: ['scmResponsibleSubjectDto'],
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
        var subjectCmp=me.getCmp("scmResponsibleSubjectDto->subjectCodeName");
        subjectCmp.filterConditions={selfconditions:" need_purchase='1' "};
        var principalCmp=me.getCmp("scmResponsibleSubjectDto->principalName");
        principalCmp.filterConditions={orgfilter:" org_code='" +Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE)+"' "};
	}
});