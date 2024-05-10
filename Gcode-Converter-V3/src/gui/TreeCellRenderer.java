package gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -8986511831939797492L;

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.getUserObject() instanceof File) {
			File file = (File) node.getUserObject();

			try {

				if (file.isDirectory() && expanded) {
					setIcon(openIcon);
				} else if (file.isDirectory() && !expanded) {
					setIcon(closedIcon);
				} else {
					setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
				}

				setText(file.getName());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return this;

	}

}
