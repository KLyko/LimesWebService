package de.uni_leipzig.simba.limeswebservice.client.view;
import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BoxLayout;

import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import de.uni_leipzig.simba.io.KBInfo;
import de.uni_leipzig.simba.limeswebservice.client.Client;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class JInputPane extends javax.swing.JPanel {
	private JSplitPane jPropertySplit;
	private JTextField endpointField;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JScrollPane jScrollPane1;
	private JTextField restrictionField;
	private JTextField variableField;
	private JTextField graphField;
	private JPanel jPanel1;
	private JPanel jEndPointPane;
	private JLabel jLabel1;
	private DefaultTableModel propertyModel;
	private JPropertyTable jTable;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new JInputPane());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public JInputPane() {
		super();
		initGUI();
	}
	
	public JInputPane(String name) {
		super();
		initGUI();
		this.jLabel1.setText(name);
	}

	private void initGUI() {
		try {
			BoxLayout thisLayout = new BoxLayout(this, javax.swing.BoxLayout.Y_AXIS);
			this.setLayout(thisLayout);
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("type");
			}
			{
				jPropertySplit = new JSplitPane();
				this.add(jPropertySplit);
				jPropertySplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
				{
					jEndPointPane = new JPanel();
					jPropertySplit.add(jEndPointPane, JSplitPane.TOP);
					GridBagLayout jEndPointPaneLayout = new GridBagLayout();
					jEndPointPaneLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jEndPointPaneLayout.rowHeights = new int[] {7, 7, 7, 7};
					jEndPointPaneLayout.columnWeights = new double[] {0.1, 0.1};
					jEndPointPaneLayout.columnWidths = new int[] {7, 7};
					jEndPointPane.setLayout(jEndPointPaneLayout);
					{
						endpointField = new JTextField();
						jEndPointPane.add(endpointField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						endpointField.setText("endpoint");
					}
					{
						jLabel2 = new JLabel();
						jEndPointPane.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel2.setText("endpoint");
					}
					{
						jLabel3 = new JLabel();
						jEndPointPane.add(jLabel3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel3.setText("graph");
					}
					{
						jLabel4 = new JLabel();
						jEndPointPane.add(jLabel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel4.setText("variable");
					}
					{
						jLabel5 = new JLabel();
						jEndPointPane.add(jLabel5, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel5.setText("restriction");
					}
					{
						graphField = new JTextField();
						jEndPointPane.add(graphField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						graphField.setText("graph");
					}
					{
						variableField = new JTextField();
						jEndPointPane.add(variableField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						variableField.setText("?x");
					}
					{
						restrictionField = new JTextField();
						jEndPointPane.add(restrictionField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				{
					jPanel1 = new JPanel();
					BorderLayout jPanel1Layout = new BorderLayout();
					jPanel1.setLayout(jPanel1Layout);
					jPropertySplit.add(jPanel1, JSplitPane.BOTTOM);
					{
						jScrollPane1 = new JScrollPane();
						
						{
							propertyModel = new DefaultTableModel();
							propertyModel.addColumn("property");
							propertyModel.addColumn("preprocessing");
							propertyModel.addColumn(JPropertyTable.REMOVE_COL);
							jTable = new JPropertyTable(propertyModel);
							jScrollPane1.setViewportView(jTable);
						}
						jPanel1.add(jScrollPane1, BorderLayout.CENTER);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initInput (Client client, boolean isTarget){
		String endPoint = this.endpointField.getText();
		String graph = this.graphField.getText();
		String var = this.variableField.getText();
		String clasz = this.restrictionField.getText();
		client.setInput(endPoint, graph, var, clasz, isTarget);
		HashMap<String,String> properties = new HashMap<String,String>();
		for (int row=1;row<this.propertyModel.getRowCount();row++){
			properties.put((String)propertyModel.getValueAt(row, 0),
				(String)propertyModel.getValueAt(row, 1));
		}
		client.setProperties(null, properties, isTarget);
		
	}
	
	/**
	 * Method set default vaules of fields.
	 * @param info KBInfo holding them.
	 */
	public void setDefaults(KBInfo info) {
		endpointField.setText(info.endpoint);
		graphField.setText(info.graph);
		variableField.setText(info.var);
		restrictionField.setText(info.restrictions.get(0));
		for(int i=0;i<info.properties.size();i++) {
			String prop = info.properties.get(i);
			String[] split = {prop,""};
			if(prop.contains("AS")) {
				split = prop.split("AS");
				split[0] = split[0].trim();
				split[1] = split[1].trim();
			}
			propertyModel.addRow(split);
		}
	}
	
}
