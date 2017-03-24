Ext.define('Subjectsubject.view.SubjectsubjectView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/subjectsubject',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 80,
	epHeight: 160,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'subjectsubject-query-layout.xml',
	editLayoutFile: 'subjectsubject-edit-layout.xml',
	//extraLayoutFile: 'subjectsubject-extra-layout.xml',
	bindings: ['nonProjectSubjectSubjectDto'],
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
        var subjectCodeCmp=me.getCmp("nonProjectSubjectSubjectDto->financialSubject");
        subjectCodeCmp.filterConditions={selfconditions:" subject_type ='0' "};
	}
});