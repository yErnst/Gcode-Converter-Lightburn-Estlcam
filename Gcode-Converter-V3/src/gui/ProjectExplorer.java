package gui;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import main.Main;

public class ProjectExplorer extends JScrollPane {
	private static final long serialVersionUID = 1500737686808181877L;

	public JTree tree;
	private DefaultMutableTreeNode root;
	public DefaultTreeModel treeModel;
	private int count;
	ConcurrentHashMap<String, DefaultMutableTreeNode> treemap = new ConcurrentHashMap<String, DefaultMutableTreeNode>();
	public FileWatcher fileWatcher;

	public ProjectExplorer(File rootfile) {
		root = new DefaultMutableTreeNode(rootfile);
		treeModel = new DefaultTreeModel(root);
		tree = new JTree();
		tree.setCellRenderer(new TreeCellRenderer());
		tree.setModel(treeModel);
		tree.setShowsRootHandles(true);
		tree.setFocusable(false);
		tree.setLocation(0, 0);
		this.add(tree);
		this.setViewportView(tree);

		new Thread(new Runnable() {
			@Override
			public void run() {
				treemap.put(rootfile.getAbsolutePath(), root);
				createChildNodes(rootfile, root);
				if (count > Main.ProjectExplorerlimit)
					JOptionPane.showMessageDialog(null, "Der Projektordner enthält zu viele Dateien.\nBitte nutze z.B. nich C: als Projektroot.\nLimit ist" + Main.ProjectExplorerlimit);
				tree.expandPath(new TreePath(root.getPath()));
			}
		}).start();
		fileWatcher = new FileWatcher(Path.of(rootfile.getAbsolutePath()), this);
		
	}

	private void createChildNodes(File root, DefaultMutableTreeNode node) {
		try {
			File[] files = root.listFiles();
			for (File file : files) {
				if (count > Main.ProjectExplorerlimit) {
					break;
				}
				if ((!file.getName().contains(".g90.nc") || (file.isDirectory())) && !file.isHidden()) {
					count++;
					DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(file);
					node.add(childnode);
					treemap.put(file.getAbsolutePath(), childnode);
					treeModel.insertNodeInto(childnode, node, node.getChildCount() - 1);
					if (file.isDirectory()) {
						createChildNodes(file, childnode);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addFile(Path path) {
		try {
			if (path.toString().contains(".g90.nc"))return;
			File file = path.toFile();
			if (file.isHidden())return;
			if(treemap.containsKey(file.getAbsolutePath()))return;
			DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(file);
			treeModel.insertNodeInto(childnode, treemap.get(file.getParent()), 0);
			treemap.put(path.toString(), childnode);
			if (file.isDirectory())createChildNodes(file, childnode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeFile(Path path) {
		if (!treemap.containsKey(path.toString()))return;
		treeModel.removeNodeFromParent(treemap.get(path.toString()));
		for (String key : treemap.keySet()) {
			if (key.contains(path.toString())) {
				treemap.remove(key);
			}
		}
	}
}
