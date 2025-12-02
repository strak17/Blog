package org.studyeasy.SpringStarter.util.constants;

public enum Roles {
    USER("USER"),
    ADMIN("ADMIN"),EDITOR("EDITOR");
    private String role;
    private Roles(String role){
        this.role = role;
    }
    public String getRole(){
        return role;
    }
    
}
