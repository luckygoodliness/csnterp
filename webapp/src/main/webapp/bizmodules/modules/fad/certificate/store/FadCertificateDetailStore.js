Ext.define("Certificate.store.FadCertificateDetailStore", {
	extend: 'Ext.data.Store',
model: 'Certificate.model.FadCertificateDetailModel',
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