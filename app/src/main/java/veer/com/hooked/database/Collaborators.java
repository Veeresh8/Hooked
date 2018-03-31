package veer.com.hooked.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Collaborators {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String UID;
    private String author;
    private String date;
    private String message;
    private long timestamp;
    private int wordCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collaborators that = (Collaborators) o;

        if (id != that.id) return false;
        if (timestamp != that.timestamp) return false;
        if (wordCount != that.wordCount) return false;
        if (UID != null ? !UID.equals(that.UID) : that.UID != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (UID != null ? UID.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + wordCount;
        return result;
    }
}
