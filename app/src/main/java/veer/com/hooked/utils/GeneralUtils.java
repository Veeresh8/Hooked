package veer.com.hooked.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import veer.com.hooked.database.Collaborators;
import veer.com.hooked.database.Story;


public class GeneralUtils {

    public static String getCurrentDate() {
        return new SimpleDateFormat("MMM dd yyyy").format(Calendar.getInstance().getTime());
    }

    public static String getCollaboratorPrefix(int size) {
        if (size == 1) {
            return size + " Collaborator";
        } else {
            return size + " Collaborators";
        }
    }

    public static int getCollaborators(List<Collaborators> collaboratorsList) {
        Set<String> uniqueUsers = new HashSet<>();

        for (Collaborators collaborator : collaboratorsList) {
            uniqueUsers.add(collaborator.getUID());
        }

        return uniqueUsers.size();
    }

    public static String getWordCountPrefix(int count) {
        return String.valueOf(count + "/" + 500);
    }

    public static String getFirebaseUserName() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null)
            return "Hooked User";
        else
            return firebaseUser.getDisplayName();
    }

    public static String getFirebaseUID() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null)
            return null;
        else
            return firebaseUser.getUid();
    }


    public static int wordsLeftForCurrentUserCount(Story story) {
        int count = 0;

        for (Collaborators collaborator : story.getCollaboratorsList()) {
            if (collaborator.getUID().equals(getFirebaseUID()))
                count += collaborator.getWordCount();
        }

        return 50 - count;
    }

    public static int wordsLeftForCurrentUser(Story story) {
        int count = 0;

        for (Collaborators collaborator : story.getCollaboratorsList()) {
            if (collaborator.getUID().equals(getFirebaseUID()))
                count += collaborator.getWordCount();
        }

        return count;
    }


    public static int getStoryCount(Story story) {
        int count = 0;

        for (Collaborators collaborator : story.getCollaboratorsList()) {
            count += collaborator.getWordCount();
        }

        return count;
    }
}
