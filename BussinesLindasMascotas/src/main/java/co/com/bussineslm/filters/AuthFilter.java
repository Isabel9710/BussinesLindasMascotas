
package co.com.bussineslm.filters;

import com.nimbusds.jwt.JWTClaimsSet;
import java.io.IOException;
import java.security.Principal;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter, ContainerResponseFilter{

    public AuthFilter() {
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        SecurityContext originalContext = request.getSecurityContext();
        String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
        String url = request.getUriInfo().getAbsolutePath().toString();
        
        if(token == null || token.isEmpty() || token.split(" ").length != 2){
           Authorizer authorizer = new Authorizer("", false, originalContext.isSecure());
           request.setSecurityContext(authorizer);
           
           if(!url.contains("auth")){
               co.com.bussineslm.utilities.Response res = new co.com.bussineslm.utilities.Response();
               res.setStatus(false);
               res.setMessage("Credenciales Necesarias.");
               
               request.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(res).build());
           }
        }else {
            JWTClaimsSet claims;

            try {
                
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Authorization");
    }
    
    
    public static class Authorizer implements SecurityContext{
        private String userId;
        private String role;
        private Boolean isSecure;
        private Boolean isAutenticated;

        public Authorizer(String userId, Boolean isSecure, Boolean isAutenticated) {
            this.userId = userId;
            this.isSecure = isSecure;
            this.isAutenticated = isAutenticated;
        }

        @Override
        public Principal getUserPrincipal() {
            return new User(this.userId);
        }

        @Override
        public boolean isUserInRole(String string) {
            return this.isAutenticated;
        }

        @Override
        public boolean isSecure() {
            return this.isSecure;
        }

        @Override
        public String getAuthenticationScheme() {
            return "JWT Authentication";
        }  
    }
    
    public static class User implements Principal{
        private final String userId;

        public User(String userId) {
            this.userId = userId;
        }
        
        @Override
        public String getName() {
            return this.userId;
        }
        
    }
}
