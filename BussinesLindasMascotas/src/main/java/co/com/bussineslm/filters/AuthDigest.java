
package co.com.bussineslm.filters;

import co.com.bussineslm.utilities.Encrypter;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import org.joda.time.DateTime;


public class AuthDigest {
    private static final JWSHeader JWT_HEADER = new JWSHeader(JWSAlgorithm.HS256);
    private static final String TOKEN_SECRET = "F8WN{uqa-*B><qZ";
    private static final String AUTH_HEADER_KEY = "Authorization";
    
    public static String getSubject(String token) throws ParseException, JOSEException, Exception{
        return Encrypter.decrypt(decodeToken(token).getSubject());
    }

    public static ReadOnlyJWTClaimsSet decodeToken(String token) throws ParseException, JOSEException{
        SignedJWT signedJWT = SignedJWT.parse(getSerializatedToken(token));
        
        if(signedJWT.verify(new MACVerifier(TOKEN_SECRET))){
            return signedJWT.getJWTClaimsSet();
        }else{
            throw new JOSEException("Fallo la verificación de la firma.");
        }
    }
    
    public static Token createToken(String host, String userId) throws JOSEException, Exception{
        JWTClaimsSet claimset = new JWTClaimsSet();
        claimset.setSubject(Encrypter.encrypt(userId));
        claimset.setIssuer(host);
        claimset.setIssueTime(DateTime.now().toDate());
        claimset.setExpirationTime(DateTime.now().plusDays(1).toDate());
        
        JWSSigner signer = new MACSigner(TOKEN_SECRET);
        SignedJWT jwt = new SignedJWT(JWT_HEADER, claimset);
        jwt.sign(signer);
        
        return new Token(jwt.serialize());
    }
    
    public static String getSerializatedToken(String token){
        return token.split(" ")[1];
    }
    
}
