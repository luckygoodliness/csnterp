Ext.define("Requestallot.store.ScmPurchaseReqStore", {
	extend: 'Ext.data.Store',
model: 'Requestallot.model.ScmPurchaseReqModel',
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