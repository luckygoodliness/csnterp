Ext.define("Certificatesetrule.store.FadProjectSetRuleStore", {
	extend: 'Ext.data.Store',
model: 'Certificatesetrule.model.FadProjectSetRuleModel',
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