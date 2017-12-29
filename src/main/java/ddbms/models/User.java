package ddbms.models;

public class User {
    private Long timestamp;
    private String uid;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private String dept;
    private String grade;
    private String language;
    private String region;
    private String role;
    private String preferTags;
    private int obtainedCredits = 0;

    /**
     * Constructor for getting a list of users
     */
    public User(String uid, String name, String gender, String language) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
        this.language = language;
    }

    /**
     * Constructor for getting a single user
     */
    public User(String timestamp, String uid, String name, String gender, String email, String phone, String dept, String grade, String language, String region, String role, String preferTags, String obtainedCredits) {
        this(uid, name, gender, language);
        this.timestamp = Long.parseLong(timestamp);
        this.email = email;
        this.phone = phone;
        this.dept = dept;
        this.grade = grade;
        this.region = region;
        this.role = role;
        this.preferTags = preferTags;
        this.obtainedCredits = Integer.parseInt(obtainedCredits);
    }

    /**
     * Constructor for creating a new user
     */
    public User(String uid, String name, String gender, String email, String phone, String dept, String grade, String language, String region, String role, String preferTags) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.dept = dept;
        this.grade = grade;
        this.language = language;
        this.region = region;
        this.role = role;
        this.preferTags = preferTags;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDept() {
        return dept;
    }

    public String getGrade() {
        return grade;
    }

    public String getLanguage() {
        return language;
    }

    public String getRegion() {
        return region;
    }

    public String getRole() {
        return role;
    }

    public String getPreferTags() {
        return preferTags;
    }

    public int getObtainedCredits() {
        return obtainedCredits;
    }
}
