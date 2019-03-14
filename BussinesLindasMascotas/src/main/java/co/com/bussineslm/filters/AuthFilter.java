
package co.com.bussineslm.filters;

import co.com.bussineslm.JPAcontrollers.UsuariosJpaController;
import co.com.bussineslm.entities.Usuarios;
import co.com.bussineslm.utilities.Encrypter;
import co.com.bussineslm.utilities.JPAFactory;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
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
import org.joda.time.DateTime;

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
                
                claims = (JWTClaimsSet) AuthDigest.decodeToken(token);
                
            } catch (ParseException e) {
                throw new IOException("Ha ocurrido un error al intentar validar el token.");
            } catch (JOSEException e) {
                throw new IOException("El token es invalido.");
            }
            
            if(new DateTime(claims.getExpirationTime()).isBefore(DateTime.now())){
               co.com.bussineslm.utilities.Response res = new co.com.bussineslm.utilities.Response();
               res.setStatus(false);
               res.setMessage("Credenciales Necesarias.");
               
               request.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(res).build()); 
            }else{
                UsuariosJpaController ctrl = new UsuariosJpaController(JPAFactory.getFACTORY());
                
                try {
                    Usuarios usuario = ctrl.findUsuarios(Encrypter.decrypt(claims.getSubject()));
                    
                    if(usuario != null){
                        Authorizer authorizer = new Authorizer(usuario.getIdUsuario().getIdentificacion().toString(), true, originalContext.isSecure());
                        request.setSecurityContext(authorizer);
                    } else{
                        throw new IOException("Ha ocurrido un error al intentar validar el token.");
                    }
                } catch (Exception e) {
                    throw new IOException("Ha ocurrido un error al intentar validar el token.");
                }
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
