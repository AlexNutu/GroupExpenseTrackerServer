//package ro.unibuc.master.groupexpensetracker.security;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.client.OAuth2RestOperations;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
//import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
//
//import javax.annotation.Resource;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static java.util.Optional.empty;
//import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;
//
//public class OpenIDConnectAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//    @Resource
//    private OAuth2RestOperations restTemplate;
//
//    protected OpenIDConnectAuthenticationFilter(String defaultFilterProcessesUrl) {
//        super(defaultFilterProcessesUrl);
//        setAuthenticationManager(authentication -> authentication);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException, IOException, ServletException {
//        final ResponseEntity<UserProfile> userInfoResponseEntity = restTemplate.getForEntity("https://www.googleapis.com/oauth2/v2/userinfo", UserProfile.class);
//        return new PreAuthenticatedAuthenticationToken(userInfoResponseEntity.getBody(), empty(), commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
//    }
//}