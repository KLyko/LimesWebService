package de.uni_leipzig.simba.limeswebservice.client.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import de.uni_leipzig.simba.data.Mapping;
/**
 * TableModel for a JTable showing mappings of the active learning method. Takes a mapping to display.
 * The entries in this mapping are transformed into MappingData instances and are stored in a List. 
 * This is done due to the fact that the TableModel interface defines access methods based on row and 
 * column indeces.
 * @author Klaus Lyko
 *
 */
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
		// Nothing to do here so far.
		
	}
	@Override
	public void removeTableModelListener(TableModelListener l) {
		// Nothing to do here so far.
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
	
	/**
	 * Method to transform the data in the mapping into a List<MappingData>, due to the index based
	 * access method of the TableModel interface.
	 */
	private void fillData() {
		for(Entry<String, HashMap<String, Double>> entry : m.map.entrySet()) {
			for(Entry<String, Double> subEntry : entry.getValue().entrySet()) {
				MappingData item = new MappingData(entry.getKey(), subEntry.getKey(), subEntry.getValue());
				dataList.add(item);
			}
		}
	}
	
	/**
	 * Method to get the Mapping out of the Model for a new learning iteration.
	 * @return
	 */
	public Mapping getMapping() {
		// FIXME Tranform list into mapping
		return m;
	}
	
	/**
	 * Nested Class which basically wraps arround the Data of a row in the TableModel.
	 * Fields have a implizit index which corresponds with the columnIndex in the table.
	 * @author Klaus Lyko
	 *
	 */
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
