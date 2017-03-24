Ext.define("Cdmfile.store.CdmFileRelationStore", {
	extend: 'Ext.data.Store',
model: 'Cdmfile.model.CdmFileRelationModel',
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