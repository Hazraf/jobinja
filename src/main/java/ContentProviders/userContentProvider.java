package ContentProviders;

import DataLayer.DataMappers.user.EndorsementMapper;
import DataLayer.DataMappers.user.UserMapper;
import DataLayer.DataMappers.Skill.SkillMapper;
import Exceptions.InvalidSkillException;
import Entities.*;
import Exceptions.UserAccessForbidden;
import Exceptions.UserNotFoundException;
import Repositories.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class UserContentProvider {
    private static UserMapper um = new UserMapper();
    private static SkillMapper sm = new SkillMapper();
    private static EndorsementMapper em = new EndorsementMapper();

    public static JSONObject getHTMLContentsForUser(String uID) throws UserNotFoundException {
        JSONObject contentMap = new JSONObject();
        try{
            User u = um.find(uID);
            contentMap.put("id", u.getUsername());
            contentMap.put("name", u.getFirstName());
            contentMap.put("lastname", u.getLastName());
            contentMap.put("jobTitle", u.getJobTitle());
            contentMap.put("bio", u.getBio());
            contentMap.put("proLink", u.getProfilePictureURL());
            contentMap.put("skills",getUserSkills(u.getUsername()));
            contentMap.put("possibleSkills", getUserPossibleSkills(u.getUsername()));
            contentMap.put("endorsedSkills", getUserEndorsedSkills(uID));
            System.out.println(getUserEndorsedSkills(uID));
            return contentMap;

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            return contentMap;
        }

    }

    public static JSONArray getUserSkills(String uID) throws UserNotFoundException {
        JSONArray content = new JSONArray();
        try {
            User u = um.find(uID);

            JSONObject instance;
            HashMap<String, Skill> skills = new HashMap<>(u.getSkills());
            for (HashMap.Entry<String, Skill> entry : skills.entrySet()) {
                String skillName = entry.getValue().getName();
                String skillPoint = Integer.toString(entry.getValue().getPoint());
                instance = new JSONObject();
                instance.put(skillName, skillPoint);
                content.put(instance);
            }
        }catch(SQLException e) {
            e.printStackTrace();

        }finally {
            return content;
        }
    }

    public static JSONArray getUserPossibleSkills(String uID) throws UserNotFoundException {
        try {
            System.out.println("OOOOO: ");
            User u = um.find(uID);
            System.out.println("number of khars : " + u.getLastName());
            JSONArray content = new JSONArray();
            ArrayList<String> skillNames = new ArrayList<String>(u.getSkills().keySet());
            ArrayList<Skill> skills = sm.getPossibleSkills(skillNames);
            for (int i = 0; i < skills.size(); i = i + 1) {
                content.put(skills.get(i).getName());
            }
            return content;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray getHTMLContentsForAllUsers(String currentuserID) {
        HashMap<String, User> allUsers = UserRepo.getUserList();
        System.out.println(allUsers);
        JSONArray contentMap = new JSONArray();
        JSONObject instance;
        for (HashMap.Entry<String, User> entry : allUsers.entrySet()) {
            String uid = entry.getKey();
            if (uid != currentuserID) {
                instance = new JSONObject();
                instance.put(uid, getShortUserContent(entry.getValue()));
                contentMap.put(instance);
            }

        }
        return contentMap;
    }

    private static JSONArray getUserEndorsedSkills(String thatUser){
        JSONArray content = new JSONArray();
        ArrayList<String> values = new ArrayList<>();
        values.add("1");
        values.add(thatUser);
        ArrayList<String> skills= em.getEndorsedSkills(values);
        for (int i  = 0; i < skills.size(); i++) {
            content.put(skills.get(i));
        }
        return content;
    }

    private static JSONObject getShortUserContent(User u) {
        JSONObject contentMap = new JSONObject();
        contentMap.put("id", u.getUsername());
        contentMap.put("name", u.getFirstName() + " " + u.getLastName());
        contentMap.put("jobTitle", u.getJobTitle());
        contentMap.put("proLink", u.getProfilePictureURL());
        return contentMap;

    }

    public static void validateSkill(String skillName) throws InvalidSkillException {
        if(!sm.hasSkill(skillName)){
            throw new InvalidSkillException("Invalid Skill");
        }
    }

    public static void checkCurrentUser(String id) throws UserAccessForbidden {
        if ( !(id.equals("1"))) {
            throw new UserAccessForbidden("access forbidden");

        }
    }
}
