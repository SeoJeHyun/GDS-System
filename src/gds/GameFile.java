package gds;

public class GameFile {
    private final String fileName;
    private final int fileSizeMB;

    public GameFile(String fileName, int fileSizeMB) {
        this.fileName = fileName;
        this.fileSizeMB = fileSizeMB;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSizeMB() {
        return fileSizeMB;
    }
}
