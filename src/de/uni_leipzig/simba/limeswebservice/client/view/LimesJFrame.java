package de.uni_leipzig.simba.limeswebservice.client.view;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

import de.uni_leipzig.simba.limeswebservice.client.Client;
import de.uni_leipzig.simba.limeswebservice.client.util.DefaultEndpointLoader;
import de.uni_leipzig.simba.limeswebservice.util.ConfigConstants;


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
public class LimesJFrame extends javax.swing.JFrame implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7987910625131144728L;

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
	private JButton specbt;
	private JPanel controlPane;
	private JPanel jPanel2;
	private JButton continueSessBt;
	private JTextField sessionField;
	private JLabel jLabel6;
	private JButton newSessionBt;
	private JButton getFitMetBt;
	private JButton subMetricBt;
	private JButton getFitPropBt;
	private JButton learnMetric;
	private JLabel jLabel5;
	private JTextField emailField;
	private JPanel jPanel1;
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
	
	private JButton getHelp;
	
	private Client client;
	private String defaultMetricExpression;

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
		super("Limes Client");
		client = new Client(this);
		client.addPropertyChangeListener(this);
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
					jMetricPane = new JPanel();
					GridBagLayout jMetricPaneLayout = new GridBagLayout();
					mainPanel.add(jMetricPane, BorderLayout.SOUTH);
					jMetricPaneLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
					jMetricPaneLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
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
						metricField.setText(defaultMetricExpression);
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
						jAccSliderModel.setMinimum(0d);
						jAccSliderModel.setMaximum(1d);
						jAccSliderModel.setStepSize(0.1d);
						jAccSliderModel.setValue(0.9d);
						jAccSpinner = new JSpinner(jAccSliderModel);
						jMetricPane.add(jAccSpinner, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						
					}
					{
						SpinnerNumberModel jRevSpinnerModel = new SpinnerNumberModel();
						 jRevSpinnerModel.setMinimum(0d);
						 jRevSpinnerModel.setMaximum(1d);
						 jRevSpinnerModel.setStepSize(0.1d);
						 jRevSpinnerModel.setValue(0.8d);
						jRevSpinner = new JSpinner(jRevSpinnerModel);
						
						
						jMetricPane.add(jRevSpinner, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						
					}
					{
						jButton1 = new JButton();
						jMetricPane.add(jButton1, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButton1.setText("calculate mapping");
						jButton1.setEnabled(false);
						jButton1.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								jSourcePane.initInput(client, false);
								jTargetPane.initInput(client, true);
								client.setMetric(metricField.getText());
								client.setThreshholds((Double)jAccSpinner.getValue(),(Double) jRevSpinner.getValue());
								client.setEmailAddress(emailField.getText());
								client.sendAll();
								learnMetric.setEnabled(true);
							}
							
						});
					}
					{
						subMetricBt = new JButton();
						jMetricPane.add(subMetricBt, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						subMetricBt.setText("submit metric");
						subMetricBt.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								client.setMetric(metricField.getText());
								client.setThreshholds((Double)jAccSpinner.getValue(),(Double) jRevSpinner.getValue());
								client.sendMetricSpec();
								jButton1.setEnabled(true);
								getFitMetBt.setEnabled(true);
								learnMetric.setEnabled(true);
							}
							
						});
					}
					{
						getFitMetBt = new JButton();
						getFitMetBt.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
							try {
								client.getMetricAdvice();
								
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
								
							}
							
						});
						getFitMetBt.setEnabled(false);
						jMetricPane.add(getFitMetBt, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						getFitMetBt.setText("suggest metric");
					}
				}
				{
					jPanel1 = new JPanel();
					GridBagLayout jPanel1Layout = new GridBagLayout();
					mainPanel.add(jPanel1, BorderLayout.NORTH);
					jPanel1Layout.rowWeights = new double[] {0.1, 0.1};
					jPanel1Layout.rowHeights = new int[] {7, 7};
					jPanel1Layout.columnWeights = new double[] {0.1, 0.0, 0.1};
					jPanel1Layout.columnWidths = new int[] {7, 155, 7};
					jPanel1.setLayout(jPanel1Layout);
					{
						jLabel5 = new JLabel();
						jPanel1.add(jLabel5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel5.setText("email Address:");
					}
					{
						emailField = new JTextField();
						jPanel1.add(emailField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						newSessionBt = new JButton();
						newSessionBt.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e) {
								client.startSession(emailField.getText());
							}
						});
						
						jPanel1.add(newSessionBt, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						newSessionBt.setText("new Session");
					}
					{
						jLabel6 = new JLabel();
						jPanel1.add(jLabel6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel6.setText("session Id:");
					}
					{
						sessionField = new JTextField();
						jPanel1.add(sessionField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						continueSessBt = new JButton();
						continueSessBt.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								try{
								int id = Integer.parseInt(sessionField.getText());
								client.continueSession(id);
								}catch (NumberFormatException nfe) {
									
								}
							}
							
						});
						jPanel1.add(continueSessBt, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						continueSessBt.setText("continue session");
					}
				}
				{
					jPanel2 = new JPanel();
					BorderLayout jPanel2Layout = new BorderLayout();
					jPanel2.setLayout(jPanel2Layout);
					mainPanel.add(jPanel2, BorderLayout.CENTER);
					{
						jInputSplit = new JSplitPane();
						jPanel2.add(jInputSplit, BorderLayout.CENTER);
						jInputSplit.setResizeWeight(0.5);
						jInputSplit.setPreferredSize(new java.awt.Dimension(576, 199));
						{
							jSourcePane = new JInputPane("source");
							jTargetPane = new JInputPane("target");
							jSourcePane.setDefaults(DefaultEndpointLoader.getDefaultEndpoints().get(DefaultEndpointLoader.DBPAUSTRONAUTSSOURCE));
							jTargetPane.setDefaults(DefaultEndpointLoader.getDefaultEndpoints().get(DefaultEndpointLoader.DBPAUSTRONAUTSTARGET));
							defaultMetricExpression = DefaultEndpointLoader.generateIntialMetric(DefaultEndpointLoader.DBPAUSTRONAUTSSOURCE, DefaultEndpointLoader.DBPAUSTRONAUTSTARGET);
							jInputSplit.add(jSourcePane, JSplitPane.LEFT);
							jInputSplit.add(jTargetPane,JSplitPane.RIGHT);
						}
					}
					{
						controlPane = new JPanel();
						GridBagLayout controlPaneLayout = new GridBagLayout();
						jPanel2.add(controlPane, BorderLayout.SOUTH);
						controlPaneLayout.rowWeights = new double[] {0.1};
						controlPaneLayout.rowHeights = new int[] {7};
						controlPaneLayout.columnWeights = new double[] {0.1, 0.1};
						controlPaneLayout.columnWidths = new int[] {7, 7};
						controlPane.setLayout(controlPaneLayout);
						{
							specbt = new JButton();
							specbt.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent e) {
									jSourcePane.initInput(client, false);
									jTargetPane.initInput(client, true);
									client.sendSpecification();
									subMetricBt.setEnabled(true);
									jButton1.setEnabled(true);
									getFitMetBt.setEnabled(true);
									learnMetric.setEnabled(true);
								}
								
							});
							controlPane.add(specbt, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							specbt.setText("submit specification");
						}
						{
							getFitPropBt = new JButton();
						
//							controlPane.add(getFitPropBt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							getFitPropBt.setText("suggest properties");
						}
						{	//@TODO
							learnMetric = new JButton();
							controlPane.add(learnMetric, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							learnMetric.setText("Learning Mapping");
							learnMetric.setEnabled(false);
							learnMetric.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent arg0) {
									client.learnMetric(null);
								}
								
							});
						}
					}
				}
			}
			pack();
			this.setSize(757, 414);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	private void setMetricFunction (HashMap<String,Object> metricMap){
		if (metricMap.containsKey(ConfigConstants.METRIC)){
			metricField.setText(metricMap.get(ConfigConstants.METRIC).toString());
			learnMetric.setEnabled(true);
		}
		if (metricMap.containsKey(ConfigConstants.ACC_THRES)){
			this.jAccSpinner.setValue((Double)metricMap.get(ConfigConstants.ACC_THRES));
		}
		if (metricMap.containsKey(ConfigConstants.VER_THRES)){
			this.jRevSpinner.setValue((Double)metricMap.get(ConfigConstants.VER_THRES));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if (evt.getPropertyName().equals(Client.GET_SPEC_SOURCE)){
			HashMap<String, Object> fetchedMap =(HashMap<String, Object>) evt.getNewValue();
			this.jSourcePane.setSpecification(fetchedMap);
		}else if (evt.getPropertyName().equals(Client.GET_SPEC_TARGET)){
			HashMap<String, Object> fetchedMap =(HashMap<String, Object>) evt.getNewValue();
			this.jTargetPane.setSpecification(fetchedMap);
		}else if (evt.getPropertyName().equals(Client.GET_METRIC)){
			HashMap<String, Object> fetchedMap =(HashMap<String, Object>) evt.getNewValue();
			this.setMetricFunction(fetchedMap);
		}else if (evt.getPropertyName().equals(Client.GET_METRIC_ADVICE)){
			metricField.setText(evt.getNewValue().toString());
		}
		
	}
	
	/**
	 * Set the the text of the session id field. 
	 * @param sessionId ID the LWS returned.
	 */
	public void setSessionId(int sessionId) {
		sessionField.setText(""+sessionId);
	}

	
}
