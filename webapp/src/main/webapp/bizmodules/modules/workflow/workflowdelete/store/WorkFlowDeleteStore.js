Ext.define("WorkflowDelete.store.WorkFlowDeleteStore", {
	extend: 'Ext.data.Store',
model: 'WorkflowDelete.model.WorkFlowDeleteModel',
	autoLoad: false,
	sorters: [
		{
			property: 'seqNo',
			direction: 'ASC'
		}
	],
	proxy: {
		type: 'memory',
		reader: {
			type: 'json'
		}
	}
});