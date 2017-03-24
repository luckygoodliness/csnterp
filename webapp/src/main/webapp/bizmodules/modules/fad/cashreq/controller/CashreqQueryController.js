/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define("Cashreq.controller.CashreqQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Cashreq.view.CashreqQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'pikcashreq-query',
    afterInit: function () {
        var me = this;
        var view = me.view;
        if (me.actionParams) {
            var queryPanelCmp = view.getQueryPanel();
            var notInRow = me.actionParams.notInRow;
            var renderPerson = me.actionParams.renderPerson;
            var renderPersonName = me.actionParams.renderPersonName;
            var reqType = me.actionParams.reqType;//请款类型
            var projectId = me.actionParams.projectId;//项目ID
//            var projectName = me.actionParams.projectName;//项目名称
            var isProject = me.actionParams.isProject;//是否项目
            var subjectId = me.actionParams.subjectId;//预算ID
            var subjectCode = me.actionParams.subjectCode;//科目代码
            var subjectName = me.actionParams.subjectName;//预算名称
            //日常报销-保证金报销
            var projectIdBzj = me.actionParams.projectIdBzj;//项目ID
            var projectIdBzjCmp = queryPanelCmp.getCmp("projectIdBzj");
            if (Scdp.ObjUtil.isNotEmpty(projectIdBzj)) {
                projectIdBzjCmp.sotValue(projectIdBzj);
            }
            var subjectCodeCmp = queryPanelCmp.getCmp("subjectCodeBzj");
            if (Scdp.ObjUtil.isNotEmpty(subjectCode)) {
                subjectCodeCmp.sotValue(subjectCode);
            }

            var staffIdeCmp = queryPanelCmp.getCmp("staffId");
            var staffNameCmp = queryPanelCmp.getCmp("staffName");
            var notInRowCmp = queryPanelCmp.getCmp("notInRow");
            var reqTypeCmp = queryPanelCmp.getCmp("reqType_Q");
            var projectIdCmp = queryPanelCmp.getCmp("projectId_Q");
            var isProjectCmp = queryPanelCmp.getCmp("isProject");
            var subjectIdCmp = queryPanelCmp.getCmp("budgetCUuid");//预算ID
            if (Scdp.ObjUtil.isNotEmpty(notInRow)) {
                {
                    notInRowCmp.sotValue(notInRow);
                }
            }
            if (Scdp.ObjUtil.isNotEmpty(renderPerson)) {
                {
                    staffIdeCmp.sotValue(renderPerson);
                    staffNameCmp.sotValue(renderPersonName);
                    staffNameCmp.sotEditable(false);
                }
            }
            if (Scdp.ObjUtil.isNotEmpty(reqType)) {
                {
                    reqTypeCmp.sotValue(reqType);
                }
            }
            if (Scdp.ObjUtil.isNotEmpty(isProject)) {
                {
                    isProjectCmp.sotValue(isProject);
                }
            }
            if (Scdp.ObjUtil.isNotEmpty(subjectId)) {
                {
                    subjectIdCmp.sotValue(subjectId);
                }
            }
            if (Scdp.ObjUtil.isNotEmpty(projectId)) {
                {
                    projectIdCmp.sotValue(projectId);
                }
            }
            me.doQuery();
        }
    },
    doReset: function () {
        var me = this;
        me.callParent(arguments);
        var notInRow = me.actionParams.notInRow;
        var renderPerson = me.actionParams.renderPerson;
        var renderPersonName = me.actionParams.renderPersonName;
        var reqType = me.actionParams.reqType;
        var projectId = me.actionParams.projectId;//项目ID
//            var projectName = me.actionParams.projectName;//项目名称
        var isProject = me.actionParams.isProject;//是否项目
        var subjectId = me.actionParams.subjectId;//预算ID
        var subjectCode = me.actionParams.subjectCode;//科目代码
        var subjectName = me.actionParams.subjectName;//预算名称
        //日常报销-保证金报销
        var projectIdBzj = me.actionParams.projectIdBzj;//项目ID
        if (projectIdBzj != null && projectIdBzj != '') {
            me.view.getQueryPanel().getCmp("projectIdBzj").sotValue(projectIdBzj);
        }
        if (Scdp.ObjUtil.isNotEmpty(subjectCode)) {
            me.view.getQueryPanel().getCmp("subjectCodeBzj").sotValue(subjectCode);
        }

        if (notInRow != null && notInRow != '') {
            me.view.getQueryPanel().getCmp("notInRow").sotValue(notInRow);
        }
        if (renderPerson != null && renderPerson != '') {
            me.view.getQueryPanel().getCmp("staffId").sotValue(renderPerson);
            me.view.getQueryPanel().getCmp("staffName").sotValue(renderPersonName);
        }
        if (reqType != null && reqType != '') {
            me.view.getQueryPanel().getCmp("reqType_Q").sotValue(reqType);
        }
        if (isProject != null && isProject != '') {
            me.view.getQueryPanel().getCmp("isProject").sotValue(isProject);
        }
        if (subjectId != null && subjectId != '') {
            me.view.getQueryPanel().getCmp("budgetCUuid").sotValue(subjectId);
        }
        if (projectId != null && projectId != '') {
            me.view.getQueryPanel().getCmp("projectId_Q").sotValue(projectId);
        }
    }
});