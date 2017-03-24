Ext.define("Prmpurchasereq.store.PrmPurchaseReqDetailStore", {
	extend: 'Ext.data.Store',
model: 'Prmpurchasereq.model.PrmPurchaseReqDetailModel',
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