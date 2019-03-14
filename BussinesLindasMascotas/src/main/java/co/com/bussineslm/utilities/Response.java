
package co.com.bussineslm.utilities;

/**
 *
 * @author Isabel Medina
 */
public class Response {
    private Boolean status;
    private String message;
    private Object data;
    private Object permissions;

    public Response() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }
    
}
