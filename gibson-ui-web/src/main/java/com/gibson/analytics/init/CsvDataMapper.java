package com.gibson.analytics.init;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CsvDataMapper extends DefaultLineMapper<Map<String, String>> {
	private final String[] columns;

	public CsvDataMapper(String[] header){
		assert(header != null);
		
		this.columns = header;
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(columns);
		
		this.setLineTokenizer(lineTokenizer);
		this.setFieldSetMapper(new FieldSetMapper<Map<String,String>>() {
			
			@Override
			public Map<String, String> mapFieldSet(FieldSet fieldSet) throws BindException {
				Map<String, String> map = new HashMap<>();
				
				for (int i = 0; i < fieldSet.getFieldCount(); i++) {
					map.put(columns[i], fieldSet.readString(i));
				}
					
				return map;
			}
		});
		
	}
}
