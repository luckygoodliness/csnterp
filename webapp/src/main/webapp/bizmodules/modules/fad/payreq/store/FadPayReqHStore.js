Ext.define("Payreq.store.FadPayReqHStore", {
	extend: 'Ext.data.Store',
model: 'Payreq.model.FadPayReqHModel',
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