Ext.define("Certificatesetrule.store.FadNonProjectSetRuleStore", {
	extend: 'Ext.data.Store',
model: 'Certificatesetrule.model.FadNonProjectSetRuleModel',
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