Ext.define("Payreq.store.FadPayReqCStore", {
	extend: 'Ext.data.Store',
model: 'Payreq.model.FadPayReqCModel',
	autoLoad: false,
	sorters: [
		{
			property: 'state',
			direction: 'ASC'
		},
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