package de.uni_leipzig.simba.limeswebservice.client.view;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

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
public class LimesJFrame extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel mainPanel;
	private JSplitPane jInputSplit;
	private JPanel jMetricPane;
	private JInputPane jSourcePane;
	private JInputPane jTargetPane;
	private JButton jButton1;
	private JSpinner jRevSpinner;
	private JSpinner jAccSpinner;
	private JLabel jLabel4;
	private JLabel jLabel3;
	private JTextField metricField;
	private JLabel jLabel2;
	private JLabel jLabel1;
	
	private Client client;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LimesJFrame inst = new LimesJFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public LimesJFrame() {
		super();
		client = new Client();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				mainPanel = new JPanel();
				BorderLayout mainPanelLayout = new BorderLayout();
				mainPanel.setLayout(mainPanelLayout);
				getContentPane().add(mainPanel, BorderLayout.CENTER);
				{
					jInputSplit = new JSplitPane();
					mainPanel.add(jInputSplit, BorderLayout.CENTER);
					jInputSplit.setResizeWeight(0.5);
					jInputSplit.setPreferredSize(new java.awt.Dimension(338, 200));
					{
						jSourcePane = new JInputPane();
						jTargetPane = new JInputPane();
						jInputSplit.add(jSourcePane, JSplitPane.LEFT);
						jInputSplit.add(jTargetPane,JSplitPane.RIGHT);
					}
				}
				{
					jMetricPane = new JPanel();
					GridBagLayout jMetricPaneLayout = new GridBagLayout();
					mainPanel.add(jMetricPane, BorderLayout.SOUTH);
					jMetricPaneLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
					jMetricPaneLayout.rowHeights = new int[] {7, 7, 7, 7, 7};
					jMetricPaneLayout.columnWeights = new double[] {0.1, 0.1};
					jMetricPaneLayout.columnWidths = new int[] {7, 7};
					jMetricPane.setLayout(jMetricPaneLayout);
					{
						jLabel1 = new JLabel();
						jMetricPane.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel1.setText("metric");
					}
					{
						jLabel2 = new JLabel();
						jMetricPane.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel2.setText("metric function");
					}
					{
						metricField = new JTextField();
						jMetricPane.add(metricField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jLabel3 = new JLabel();
						jMetricPane.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel3.setText("threshold acceptance");
					}
					{
						jLabel4 = new JLabel();
						jMetricPane.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel4.setText("threshold review");
					}
					{
						SpinnerNumberModel jAccSliderModel = new SpinnerNumberModel();
						jAccSliderModel.setMinimum(0f);
						jAccSliderModel.setMaximum(1f);
						jAccSliderModel.setStepSize(0.1f);
						jAccSliderModel.setValue(0.9f);
						jAccSpinner = new JSpinner(jAccSliderModel);
						jMetricPane.add(jAccSpinner, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						
					}
					{
						SpinnerNumberModel jRevSpinnerModel = new SpinnerNumberModel();
						 jRevSpinnerModel.setMinimum(0f);
						 jRevSpinnerModel.setMaximum(1f);
						 jRevSpinnerModel.setStepSize(0.1f);
						 jRevSpinnerModel.setValue(0.8f);
						jRevSpinner = new JSpinner(jRevSpinnerModel);
						
						
						jMetricPane.add(jRevSpinner, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						
					}
					{
						jButton1 = new JButton();
						jMetricPane.add(jButton1, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButton1.setText("Submit");
						jButton1.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								jSourcePane.initInput(client, false);
								jTargetPane.initInput(client, true);
								client.setMetric(metricField.getText());
								client.setThreshholds((Float)jAccSpinner.getValue(),(Float) jRevSpinner.getValue());
								client.send();
								System.out.println(client.getSource().get("endpoint"));
							}
							
						});
					}
				}
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}
