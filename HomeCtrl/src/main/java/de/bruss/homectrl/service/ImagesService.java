package de.bruss.homectrl.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.EvictingQueue;

@Service
public class ImagesService {

	@Value("${images.path}")
	String imagePath;

	List<Path> paths = new ArrayList<Path>();

	Queue<Path> lastImages = EvictingQueue.create(50);

	@PostConstruct
	private void init() throws IOException {
		listFiles(Paths.get(imagePath));
	}

	public void listFiles(Path path) throws IOException {
		Set<Path> newPaths = new HashSet<Path>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					listFiles(entry);
				}
				newPaths.add(entry);
			}
		}

		paths.clear();
		paths.addAll(newPaths);
	}

	public Path getRandomImage() {
		Collections.shuffle(paths);
		for (Path path : paths) {
			if (!lastImages.contains(path)) {
				lastImages.add(path);
				return path;
			}
		}

		lastImages.clear();
		lastImages.add(paths.get(0));
		return paths.get(0);

	}
}
