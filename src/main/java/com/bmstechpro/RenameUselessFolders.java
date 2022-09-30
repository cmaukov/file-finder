package com.bmstechpro;
/* file-finder
 * @created 09/30/2022
 * @author Konstantin Staykov
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RenameUselessFolders {
    private static final String folderPath = "/Users/konstantinstaykov/Public/sandbox/test_folder";
    private static final Path path = Paths.get(folderPath);

    public static void main(String[] args) throws IOException {
        List<Path> filesList;
        List<Path> foldersList;
        try (Stream<Path> walk = Files.walk(path);) {
            filesList = walk.filter(Files::isRegularFile)
                    .filter(p -> !p.getFileName().toString().equals(".DS_Store"))
                    .collect(Collectors.toList());
        }
        try (Stream<Path> walk = Files.walk(path);) {
            foldersList = walk.filter(Files::isDirectory)
                    .collect(Collectors.toList());
        }
        List<Path> uselessFolders = new ArrayList<>();
        for (Path folder : foldersList) {
            boolean addToUseless = true;
            for (Path file : filesList) {
                if (file.getParent().toString().contains(folder.toString())) {
                    addToUseless = false;
                    break;
                }

            }
            if (addToUseless) {
                uselessFolders.add(folder);
            }
        }

        uselessFolders.sort(Comparator.comparing(Path::getParent).reversed());


        uselessFolders.forEach(f -> {
            File file = f.toFile();
            if (file.exists()&&!file.toString().endsWith("-empty")) {
                if (file.renameTo(new File(f + "-empty"))) {
                    System.out.printf("%s ---> %s-empty%n", f, f);

                }
            }
        });

    }
}
