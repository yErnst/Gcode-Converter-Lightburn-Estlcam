package gui;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.sun.nio.file.ExtendedWatchEventModifier;

import main.Main;

public class FileWatcher {

	public FileWatcher(Path root, ProjectExplorer explorer) {
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					WatchService watcher = FileSystems.getDefault().newWatchService();

					//no linux support. Linux does not support ExtendedWatchEventModifier.FILE_TREE
					root.register(watcher,
							new WatchEvent.Kind[] { StandardWatchEventKinds.ENTRY_CREATE,
									StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY },
							ExtendedWatchEventModifier.FILE_TREE);

					while (true) {
						WatchKey key = watcher.take();
						for (WatchEvent<?> event : key.pollEvents()) {

							//System.out.println(root.resolve(event.context().toString()) + " " + event.kind());
							if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
								explorer.addFile(root.resolve(event.context().toString()));
							} else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
								explorer.removeFile(root.resolve(event.context().toString()));
							} else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
								if(root.resolve(event.context().toString()).toString().contains(Main.window.p.getProjectFile().getAbsolutePath()) && !root.resolve(event.context().toString()).toString().contains("g90")){
									Main.window.p.convert();
									Main.window.canvas.repaint();
								}
							}

						}
						key.reset();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

}
