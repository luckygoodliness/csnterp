Ext.define("Responsibleproject.store.ScmResponsibleProjectStore", {
	extend: 'Ext.data.Store',
model: 'Responsibleproject.model.ScmResponsibleProjectModel',
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