Ext.define("Scmsaeapproval.store.ScmSaeApprovalStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaeapproval.model.ScmSaeApprovalModel',
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