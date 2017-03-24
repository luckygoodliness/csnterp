Ext.define("Certificate.store.FadCertificateStore", {
	extend: 'Ext.data.Store',
model: 'Certificate.model.FadCertificateModel',
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