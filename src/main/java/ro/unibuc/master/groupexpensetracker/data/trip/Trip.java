package ro.unibuc.master.groupexpensetracker.data.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    private String name;
    private List<UserProfile> members;
}
