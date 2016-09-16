package xan_code;

import java.io.File;

public class FilenameFilter implements java.io.FilenameFilter {

    String description = "";
    String fileExt = "";

    public FilenameFilter(String extension) {
        this.fileExt = extension;
    }

    public FilenameFilter(String extension, String typeDescription) {
        this.fileExt = extension;
        this.description = typeDescription;
    }
    
    @Override
    public boolean accept(File dir, String name) {
        if (dir.isDirectory() && (name.toLowerCase().endsWith(fileExt))) {
        	return true;
        }
        return false;
    }
    
    public String getDescription() {
        return description;
    }
}