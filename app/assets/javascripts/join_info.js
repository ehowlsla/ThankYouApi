function getRadioValue(obj) {
	var len = obj.length;
	if(!len && obj.checked) {
		return obj.value;
	}

	for( var i =0, m=obj.length; i < m; i++) {
		if( obj[i].checked) {
			return obj[i].value;
		}
	}
}