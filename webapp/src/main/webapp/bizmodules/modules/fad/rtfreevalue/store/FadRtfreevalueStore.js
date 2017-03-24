Ext.define("Rtfreevalue.store.FadRtfreevalueStore", {
	extend: 'Ext.data.Store',
model: 'Rtfreevalue.model.FadRtfreevalueModel',
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