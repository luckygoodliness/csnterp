Ext.define("Cashreq.store.FadCashReqShareStore", {
	extend: 'Ext.data.Store',
model: 'Cashreq.model.FadCashReqShareModel',
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