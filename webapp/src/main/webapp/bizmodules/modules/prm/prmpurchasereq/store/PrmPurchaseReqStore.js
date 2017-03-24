Ext.define("Prmpurchasereq.store.PrmPurchaseReqStore", {
	extend: 'Ext.data.Store',
model: 'Prmpurchasereq.model.PrmPurchaseReqModel',
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