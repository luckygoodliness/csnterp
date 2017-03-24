Ext.define("WorkflowDelete.model.WorkFlowDeleteModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','businessKey','newBusinessKey','deleteReason','assigneeRecord','ruAssignee','hiAssignee','modules','stateField','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);