package app;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class MyFile implements Serializable {
    private static final long serialVersionUID = -1934907147043909222L;
    private int key;
    private File file;
    private ServentInfo owner;
    private boolean isPublic;

    public MyFile(int key, File file, ServentInfo owner, boolean isPublic) {
        this.key = key;
        this.file = file;
        this.owner = owner;
        this.isPublic = isPublic;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setOwner(ServentInfo owner) {
        this.owner = owner;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getKey() {
        return key;
    }

    public File getFile() {
        return file;
    }

    public ServentInfo getOwner() {
        return owner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return "File: " + file.getName() + " | " + owner.getHashId() + " | " + isPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyFile myFile = (MyFile) o;
        return key == myFile.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
