//package ro.unibuc.master.groupexpensetracker.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Component;
//import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
//import ro.unibuc.master.groupexpensetracker.domain.service.UserProfileService;
//
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class GroupExpenseAuthenticationProvider implements AuthenticationProvider {
//
//    @Autowired
//    private UserProfileService userService;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String email = authentication.getName();
//        String password = authentication.getCredentials().toString();
//        UserProfile user = userService.getByEmail(email);
//        try {
//            if (user != null && user.getPassword().equals(Security.computeMD5(password))) {
//                List<GrantedAuthority> grantedAuths = new ArrayList<>();
//                grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//                return new UsernamePasswordAuthenticationToken(email, password, grantedAuths);
//            } else {
//                return null;
//            }
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}