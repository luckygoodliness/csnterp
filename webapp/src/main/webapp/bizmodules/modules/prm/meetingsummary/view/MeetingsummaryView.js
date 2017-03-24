Ext.define('Meetingsummary.view.MeetingsummaryView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/meetingsummary',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 80,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'meetingsummary-query-layout.xml',
	editLayoutFile: 'meetingsummary-edit-layout.xml',
	//extraLayoutFile: 'meetingsummary-extra-layout.xml',
	bindings: ['prmMeetingSummaryDto'],
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