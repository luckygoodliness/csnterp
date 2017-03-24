Ext.define("Scmsaeapproval.store.ScmSaeApprovalDStore", {
	extend: 'Ext.data.Store',
model: 'Scmsaeapproval.model.ScmSaeApprovalDModel',
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