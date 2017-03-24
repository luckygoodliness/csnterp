Ext.define("Purchasereq.store.ScmPurchaseReqStore", {
	extend: 'Ext.data.Store',
model: 'Purchasereq.model.ScmPurchaseReqModel',
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