package de.uni_leipzig.simba.limeswebservice.client.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import de.uni_leipzig.simba.data.Mapping;

public class ALTableModel implements TableModel{

	Mapping m;
	List<MappingData> dataList;
	
	public ALTableModel(Mapping m) {
		dataList = new LinkedList<MappingData>();
		this.m = m;
		fillData();
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class c = Object.class;
		switch(columnIndex) {
			case 0:	c = String.class; break;
			case 1:	c = String.class; break;
			case 2:	c = Double.class; break;
			case 3:	c = Boolean.class; break;
		}
		return c;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		String columnName="";
		switch(columnIndex) {
			case 0:	columnName = "Source URI"; break;
			case 1:	columnName = "Target URI"; break;
			case 2:	columnName = "Similarity"; break;
			case 3:	columnName = "Validate"; break;
		}
		return columnName;
	}

	@Override
	public int getRowCount() {
		return m.size;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return dataList.get(rowIndex).getValue(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(rowIndex>=0 && rowIndex<dataList.size() && columnIndex == 3)
			return true;
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		dataList.get(rowIndex).setValue(aValue, columnIndex);
	}
	
	private void fillData() {
		for(Entry<String, HashMap<String, Double>> entry : m.map.entrySet()) {
			for(Entry<String, Double> subEntry : entry.getValue().entrySet()) {
				MappingData item = new MappingData(entry.getKey(), subEntry.getKey(), subEntry.getValue());
				dataList.add(item);
			}
		}
//		for(int i = 0; i<dataList.size();i++) {
//			System.out.println(i+"= "+dataList.get(i).toString());
//		}
	}
	
	class MappingData {
		String sourceURI;//Index 0
		String targetURI;//Index 1
		Double similarity;//Index 2
		Boolean valid;//Index 3
		public MappingData(String sourceURI,String targetURI,double sim) {
			this.sourceURI = sourceURI;
			this.targetURI = targetURI;
			this.similarity = sim;
			if(sim>=0.5d)
				valid = true;
			else
				valid = false;
		}
		
		public void setValid(boolean valid) {
			this.valid = valid;
		}
		
		public Object getValue(int valueIndex) {
			Object data = sourceURI;
			switch(valueIndex) {
				case 0: data = sourceURI;break;
				case 1: data = targetURI;break;
				case 2: data = similarity;break;
				case 3: data = valid;break;
			}
			return data;
		}
		
		public void setValue(Object value, int valueIndex) {
			switch(valueIndex) {
				case 0: sourceURI = value.toString();break;
				case 1: targetURI = value.toString();break;
				case 2: similarity = Double.parseDouble(value.toString());break;
				case 3: valid = (boolean) value; 
//					System.out.println("Setting value of "+this.toString());
				break;
			}
		}
		
		@Override
		public String toString() {
			return sourceURI +"-"+targetURI+"("+similarity+")"+" isValid="+valid;
		}
	}

}
