package com.afrosofttech.spring_starter.util.constants;

import java.nio.file.Paths;

/**
 *  Converting relative paths to absolute ensures that files are stored correctly
 *  regardless of the deployment environment.
 */
public class AppUtil {
    public static String getUploadPath(String fileName) {
        return Paths.get("src", "main", "resources", "static", "uploads", fileName).toFile().getAbsolutePath();
    }
}
