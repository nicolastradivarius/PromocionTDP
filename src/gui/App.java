package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.PluginApp;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Color;

@SuppressWarnings("serial")
public class App extends JFrame {

	//a fin de evitar números que puedan no quepar en el espacio del resultado
	protected static final double MAXIMO_VALOR = 0x09999999;
	protected static final double MINIMO_VALOR = 0x99999999;
	private PluginApp pluginLogic;
	private JPanel contentPane;
	private JTextField primertextField;
	private JTextField segundotextField;
	private JTextPane textPaneResultado;
	private JButton btnCalcular;
	private JButton btnActualizar;
	private JComboBox<String> listaPlugins;
	private List<String> listaOpciones;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App() {
		setBackground(new Color(51, 102, 102));

		pluginLogic = new PluginApp();
		listaOpciones = new ArrayList<>();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 153, 51));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		primertextField = new JTextField();
		primertextField.setBackground(new Color(204, 204, 204));
		primertextField.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
		primertextField.setBounds(12, 60, 114, 38);
		contentPane.add(primertextField);
		primertextField.setColumns(10);

		segundotextField = new JTextField();
		segundotextField.setBackground(new Color(204, 204, 204));
		segundotextField.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
		segundotextField.setBounds(138, 60, 114, 38);
		contentPane.add(segundotextField);
		segundotextField.setColumns(10);


		btnActualizar = new JButton("Actualizar");
		btnActualizar.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		btnActualizar.setBounds(284, 58, 117, 40);
		contentPane.add(btnActualizar);

		listaPlugins = new JComboBox<>();
		listaPlugins.setBackground(new Color(204, 204, 204));
		listaPlugins.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		listaPlugins.setBounds(12, 24, 240, 24);
		contentPane.add(listaPlugins);


		textPaneResultado = new JTextPane();
		textPaneResultado.setEditable(false);
		textPaneResultado.setBounds(199, 110, 202, 25);
		contentPane.add(textPaneResultado);

		btnCalcular = new JButton("Calcular resultado:");
		btnCalcular.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		btnCalcular.setBounds(9, 110, 173, 25);
		contentPane.add(btnCalcular);


		configListaOpciones(listaPlugins);
		configBtnCalcular(btnCalcular, primertextField, segundotextField, textPaneResultado, listaPlugins);
		configBtnActualizar(btnActualizar);
	}

	private void configBtnActualizar(JButton a) {
		a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pluginLogic.actualizarPlugins();
				configListaOpciones(listaPlugins);
			}
		});
	}

	private void configListaOpciones(JComboBox<String> lp) {
		List<String> lista = pluginLogic.getPluginsNames();

		System.out.println(lista.toString());
		//solucionar problema de que se repiten las opciones a elegir (porque no detecto cuales estan repetidas)
		for (String s : lista) {
			if (!this.listaOpciones.contains(s)) {
				this.listaOpciones.add(s);
				lp.addItem(s);
			}
			else {
				
			}
		}
	}

	private void configBtnCalcular(JButton b, JTextField tf1, JTextField tf2, JTextPane res, JComboBox<String> lp) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String currentOption = (String) lp.getSelectedItem();
					double n1 = Double.parseDouble(tf1.getText());
					double n2 = Double.parseDouble(tf2.getText());
					double result = pluginLogic.runPlugin(n1, n2, currentOption);
					if (result > MAXIMO_VALOR) {
						res.setText("Incalculable");
					}
					else {
						res.setText(String.format("%.8f", result));
					}
				}
				catch (NumberFormatException err) {
					String input = tf1.getText() + "&" + tf2.getText();
					pluginLogic.getLogger().warning("Input inválido: " + input);
					String message = "¡Ingrese un número válido!";
					JOptionPane.showMessageDialog(contentPane, message);
				}
			}
		});
	}
}
