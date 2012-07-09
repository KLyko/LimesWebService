package de.uni_leipzig.simba.limeswebservice.client.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;



public class JPropertyTable extends JTable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1353749977978370118L;
	public static final String REMOVE_COL = "removeCol";
	private DefaultTableModel model;
	private int removeInd;
	private de.uni_leipzig.simba.limeswebservice.client.view.JPropertyTable.RemoveCellEditor removeEditor;
	private de.uni_leipzig.simba.limeswebservice.client.view.JPropertyTable.RemoveCellRenderer removeRenderer;
	private AddRowRenderer addRowRenderer;
	private AddRowEditor addRowEditor;
	public JPropertyTable (TableModel model){
		super (model);
		this.model = (DefaultTableModel) model;
		removeEditor = new RemoveCellEditor();
		removeRenderer = new RemoveCellRenderer();
		addRowRenderer = new AddRowRenderer();
		addRowEditor = new AddRowEditor();
		removeInd=-1;
		for (int col = 0;col<this.model.getColumnCount();col++){
			if (this.model.getColumnName(col).equals(REMOVE_COL)){
				removeInd = col;
				break;
			}
		}
		this.model.addRow(new Vector());
	
	}
	
	@Override
	public boolean isCellEditable(int row, int col){
		
		if ((col==0 && row==0)||row>0)
			return true;
		else
			return  false;
		
	} 
	
	@Override
	public TableCellEditor getCellEditor(int row, int col){
		if (col == removeInd && row!=0){
			return removeEditor;
		}else if(row==0&&col ==0){
			return addRowEditor;
		}else
			return super.getCellEditor(row,col);
		
	}
	
	public TableCellRenderer getCellRenderer (int row, int col){
		if (col == removeInd && row!=0){
			return removeRenderer;
		}else if (col ==0&&row==0){
			return addRowRenderer;
		}else
			return super.getCellRenderer(row, col);
	}

	
	public void removeRow (int row){
		model.removeRow(row);
	}
	
	public void addRow(){
		this.model.addRow(new Vector());
	}
	
	
	
	
	private class AddRowRenderer extends JButton implements TableCellRenderer{

		
		/**
		 * 
		 */
		private static final long serialVersionUID = -744401025055919400L;
		AddRowRenderer (){
			super("add property");
			this.setForeground(new Color(37, 65, 23));
			
			Font font = new Font ("arial", Font.BOLD, 14);
			this.setFont(font);
		}
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			return this;
		}
		
	}
	
	
	private class RemoveCellRenderer extends JButton implements TableCellRenderer{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8544221197410207621L;
		RemoveCellRenderer(){
			super ("X");
			this.setForeground(Color.red);
			this.setToolTipText("remove");
			
			Font font = new Font ("arial", Font.BOLD, 14);
			this.setFont(font);
		}
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			return this;
		}
		
		
	}
	
	private class RemoveCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2041824441207460923L;
		JButton removeBt; 
		int selectedRow;
		RemoveCellEditor(){
			removeBt = new JButton();
			removeBt.setActionCommand("remove");
			removeBt.addActionListener(this);
		}
		
		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("remove")){
				removeRow(selectedRow);
			}
			this.cancelCellEditing();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			selectedRow = row;
			return removeBt;
		}
		
	}
	
	private class AddRowEditor extends AbstractCellEditor implements TableCellEditor,ActionListener{

		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2852591734017928771L;
		JButton addBt;
		
		AddRowEditor (){
			addBt = new JButton ();
			addBt.addActionListener(this);
			addBt.setActionCommand("add");
		}
		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (row ==0&&column==0)
			return addBt;
			else return null;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("add")){
				addRow();
			}
				
		}
		
	}
}
