package astah_drawio_plugin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IActivityDiagram;
import com.change_vision.jude.api.inf.model.IClassDiagram;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

import astah_drawio_plugin.internal.ServiceLocator;
import astah_drawio_plugin.internal.converter.AstahDiagramToDrawioConverter;

public class SelectDiagramsToDrawioFilesAction implements IPluginActionDelegate {

	private static ResourceBundle messages = ResourceBundle.getBundle("messages");

	class DiagramWithChecked {
		boolean isChecked;
		IDiagram diagram;

		public DiagramWithChecked(IDiagram diagram) {
			this.diagram = diagram;
			this.isChecked = false;
		}
	}

	class DiagramTableModel extends AbstractTableModel {

		private ArrayList<DiagramWithChecked> data;

		public DiagramTableModel(List<IDiagram> diagrams) {
			this.data = new ArrayList<>();
			for (var diagram : diagrams) {
				this.data.add(new DiagramWithChecked(diagram));
			}
		}

		public ArrayList<DiagramWithChecked> getData() {
			return data;
		}

		@Override
		public int getRowCount() {
			return this.data.size();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			var diagramWithChecked = this.data.get(rowIndex);
			var diagram = diagramWithChecked.diagram;
			switch (columnIndex) {
			case 0:
				return diagramWithChecked.isChecked;
			case 1:
				return diagram.getName();
			case 2:
				if (diagram instanceof IActivityDiagram) {
					return messages.getString("diagram_activity");
				} else if (diagram instanceof IClassDiagram) {
					return messages.getString("diagram_class");
				} else {
					return messages.getString("unsupported_diagram");
				}
			case 3:
				return diagram.getFullNamespace("::");
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 0;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			var diagramWithChecked = this.data.get(rowIndex);
			if (columnIndex == 0) {
				diagramWithChecked.isChecked = (Boolean) aValue;
			}
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return messages.getString("column_select");
			case 1:
				return messages.getString("column_name");
			case 2:
				return messages.getString("column_type");
			case 3:
				return messages.getString("column_path");
			}
			return null;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return Boolean.class;
			}
			return String.class;
		}

	}

	class SelectDiagramsDialog extends JDialog {

		private static List<IDiagram> findDiagrams(IModel model) {
			var diagrams = new ArrayList<IDiagram>();
			diagrams.addAll(Arrays.asList(model.getDiagrams()));
			for (var child : model.getOwnedElements()) {
				if (child instanceof IModel) {
					diagrams.addAll(findDiagrams((IModel) child));
				} else if (child instanceof IPackage) {
					diagrams.addAll(findDiagrams((IPackage) child));
				}
			}
			return diagrams;
		}

		private static List<IDiagram> findDiagrams(IPackage model) {
			var diagrams = new ArrayList<IDiagram>();
			diagrams.addAll(Arrays.asList(model.getDiagrams()));
			for (var child : model.getOwnedElements()) {
				if (child instanceof IModel) {
					diagrams.addAll(findDiagrams((IModel) child));
				} else if (child instanceof IPackage) {
					diagrams.addAll(findDiagrams((IPackage) child));
				}
			}
			return diagrams;
		}

		public SelectDiagramsDialog(Window parent)
				throws ClassNotFoundException, ProjectNotFoundException {
			super(parent);
			setSize(600, 400);

			var api = AstahAPI.getAstahAPI();
			api.getProjectAccessor().getProject();
			var tableModel = new DiagramTableModel(findDiagrams(api.getProjectAccessor().getProject()));
			var table = new JTable(tableModel);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			table.getColumnModel().getColumn(0).setPreferredWidth(40);
			table.getColumnModel().getColumn(1).setPreferredWidth(200);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			var scrollPane = new JScrollPane(table);
			add(scrollPane);

			var selectAllButton = new JButton(messages.getString("button_select_all"));
			selectAllButton.addActionListener(e -> {
				var data = tableModel.getData();
				for (var item : data) {
					item.isChecked = true;
				}
				table.repaint();
			});
			var unselectAllButton = new JButton(messages.getString("button_unselect_all"));
			unselectAllButton.addActionListener(e -> {
				var data = tableModel.getData();
				for (var item : data) {
					item.isChecked = false;
				}
				table.repaint();
			});
			JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			headerPanel.add(selectAllButton);
			headerPanel.add(unselectAllButton);
			add(headerPanel, BorderLayout.NORTH);

			var okButton = new JButton(messages.getString("button_ok"));
			okButton.addActionListener(e -> {
				try {
					var fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					var result = fileChooser.showSaveDialog(this);
					if (result == JFileChooser.APPROVE_OPTION) {
						var selectedFolder = fileChooser.getSelectedFile();
						var converter = ServiceLocator.getContainer().get(AstahDiagramToDrawioConverter.class);
						var data = tableModel.getData();
						var fileNames = new HashSet<String>();
						for (var item : data) {
							if (item.isChecked) {
								var bytes = converter.convert(item.diagram);
								var fileName = item.diagram.getName() + ".drawio";
								var num = 1;
								while (fileNames.contains(fileName)) {
									fileName = item.diagram.getName() + "(" + num + ")" + ".drawio";
									num++;
								}
								fileNames.add(fileName);
								Files.write(Paths.get(selectedFolder.getAbsolutePath(), fileName), bytes,
										StandardOpenOption.CREATE);
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				this.dispose();
			});
			JButton cancelButton = new JButton(messages.getString("button_cancel"));
			cancelButton.addActionListener(e -> this.dispose());
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}

	@Override
	public Object run(IWindow window) throws UnExpectedException {
		try {
			var dialog = new SelectDiagramsDialog(window.getParent());
			dialog.setLocationRelativeTo(window.getParent());
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
