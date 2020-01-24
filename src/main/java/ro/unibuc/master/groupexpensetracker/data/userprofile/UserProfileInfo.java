package ro.unibuc.master.groupexpensetracker.data.userprofile;

import lombok.Data;

@Data
public class UserProfileInfo {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Boolean receiveNotifications;

    public UserProfileInfo() {
    }

    public UserProfileInfo(Long id, String firstName, String lastName, String password, String email, Boolean receiveNotifications) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.receiveNotifications = receiveNotifications;
    }
}
