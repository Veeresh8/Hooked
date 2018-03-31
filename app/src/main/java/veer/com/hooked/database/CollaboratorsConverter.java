package veer.com.hooked.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;


public class CollaboratorsConverter implements Serializable {

    @TypeConverter
    public String fromCollaboratorsList(List<Collaborators> collaborators) {
        if (collaborators == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Collaborators>>() {
        }.getType();
        return gson.toJson(collaborators, type);
    }

    @TypeConverter
    public List<Collaborators> toCollaboratorsList(String collaborators) {
        if (collaborators == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Collaborators>>() {
        }.getType();
        return gson.fromJson(collaborators, type);
    }
}
