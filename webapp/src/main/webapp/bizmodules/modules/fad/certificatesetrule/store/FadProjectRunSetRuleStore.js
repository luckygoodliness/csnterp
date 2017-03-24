Ext.define("Certificatesetrule.store.FadProjectRunSetRuleStore", {
	extend: 'Ext.data.Store',
model: 'Certificatesetrule.model.FadProjectRunSetRuleModel',
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