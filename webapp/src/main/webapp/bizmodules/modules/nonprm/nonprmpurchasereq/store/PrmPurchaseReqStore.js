Ext.define("Nonprmpurchasereq.store.PrmPurchaseReqStore", {
	extend: 'Ext.data.Store',
model: 'Nonprmpurchasereq.model.PrmPurchaseReqModel',
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