package veer.com.hooked.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@Entity
public class Story implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long timestamp;
    private boolean isFavorite;
    private String author;
    private String date;
    private String title;
    private int totalWords;

    //Collaborators
    @ColumnInfo(name = "Collaborators")
    @TypeConverters(CollaboratorsConverter.class)
    private List<Collaborators> collaboratorsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public List<Collaborators> getCollaboratorsList() {
        return collaboratorsList;
    }

    public void setCollaboratorsList(List<Collaborators> collaboratorsList) {
        this.collaboratorsList = collaboratorsList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Story story = (Story) o;

        if (id != story.id) return false;
        if (timestamp != story.timestamp) return false;
        if (isFavorite != story.isFavorite) return false;
        if (totalWords != story.totalWords) return false;
        if (author != null ? !author.equals(story.author) : story.author != null) return false;
        if (date != null ? !date.equals(story.date) : story.date != null) return false;
        if (title != null ? !title.equals(story.title) : story.title != null) return false;
        return collaboratorsList != null ? collaboratorsList.equals(story.collaboratorsList) : story.collaboratorsList == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (isFavorite ? 1 : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + totalWords;
        result = 31 * result + (collaboratorsList != null ? collaboratorsList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", isFavorite=" + isFavorite +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
