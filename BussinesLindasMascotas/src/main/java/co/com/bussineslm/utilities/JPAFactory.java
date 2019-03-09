
package co.com.bussineslm.utilities;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class JPAFactory {
    private static final EntityManagerFactory FACTORY;
    private static final String BD = "UPLindasMascotas";
    
    static {
        FACTORY = Persistence.createEntityManagerFactory(BD);
    }
    
    public static EntityManagerFactory getFACTORY(){
        return FACTORY;  
    }
    
}
