Ext.define("Cashreq.store.FadCashReqStore", {
	extend: 'Ext.data.Store',
model: 'Cashreq.model.FadCashReqModel',
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