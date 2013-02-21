package de.uni_leipzig.simba.limeswebservice.client.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.border.BevelBorder;

import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.limeswebservice.client.Client;

public class LearningDialog extends JDialog  implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private ALTableModel model;
	private JButton okButton;
	private JButton cancelButton;
	private JButton getMetricButton;
	private Client client = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Mapping m = new Mapping();
			m.add("a","a",1d);
			m.add("aa","aA",0.8d);
			m.add("b","cb",0.4d);
			m.add("c","d",0d);
			
			LearningDialog dialog = new LearningDialog(m);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LearningDialog(Mapping m, Client client) {
		this(m);
		this.client = client;
	}
	
	/**
	 * Create the dialog.
	 */
	public LearningDialog(Mapping m) {
		setTitle("Actice Learning Dialog");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));
		{
			
			model = new ALTableModel(m);
			
			table = new JTable(model);
			table.setColumnSelectionAllowed(true);
			table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			contentPanel.add(new JScrollPane(table));
			
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Learn");
				okButton.setActionCommand("learn");
				buttonPane.add(okButton);
				okButton.addActionListener(this);
				getRootPane().setDefaultButton(okButton);
			}

			{
				getMetricButton = new JButton("Get Metric");
				getMetricButton.setActionCommand("getmetric");
				getMetricButton.addActionListener(this);
				buttonPane.add(getMetricButton);
			}
			
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}
	
	 // Button actionsListener
    public void actionPerformed(ActionEvent e) {
      if ("learn".equals(e.getActionCommand())) {
    	  Mapping m = model.getMapping();
    	 if(client.learnMetric(m)){
    		 System.out.println("Setting new model");
    		 this.model = new ALTableModel(client.getTrainingData());
    		 this.table.setModel(model);
    	 }
      }
      if ("cancel".equals(e.getActionCommand())) {
    	  System.out.println("cancel");
    	  this.dispose();
      }
      if ("getmetric".equals(e.getActionCommand())) {
    	  System.out.println("getMetric");
      }
    }
}
