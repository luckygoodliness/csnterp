Ext.define("Scmcontract.store.PrmPurchaseReqDetailStore", {
	extend: 'Ext.data.Store',
model: 'Scmcontract.model.PrmPurchaseReqDetailModel',
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