package ro.unibuc.master.groupexpensetracker.data.userprofile;

import lombok.Data;

@Data
public class UserProfileInfo {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    public UserProfileInfo() {
    }

    public UserProfileInfo(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
