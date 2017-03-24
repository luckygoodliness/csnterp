Ext.define("Nonprmpurchasereq.store.PrmPurchaseReqDetailStore", {
	extend: 'Ext.data.Store',
model: 'Nonprmpurchasereq.model.PrmPurchaseReqDetailModel',
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