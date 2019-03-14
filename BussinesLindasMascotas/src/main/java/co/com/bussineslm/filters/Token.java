
package co.com.bussineslm.filters;

import org.codehaus.jackson.annotate.JsonProperty;


public class Token {
    private String token;

    public Token(@JsonProperty("token") String token) {
        this.token = token;
    }
    
    public String getToken(){
        return token;
    }
}
