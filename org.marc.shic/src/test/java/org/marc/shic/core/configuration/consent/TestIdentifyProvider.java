/**
 *
 */
package org.marc.shic.core.configuration.consent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ibrahimm
 *
 */
public class TestIdentifyProvider implements IIdentityProvider {

    private String userId;

    public TestIdentifyProvider(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public boolean isUserInRole(String roleName) {
        // a poor-man's ACL for quick testing
        boolean retVal = false;
        Map<String, String> roles = new HashMap<String, String>();
        roles.put("mo", "Physicians");
        roles.put("garrett", "Physicians");
        roles.put("paul", "Physicians");
        
        if (roles.containsKey(userId)) {
            retVal = roles.get(userId).equalsIgnoreCase(roleName);
        }
        
        return retVal;
    }

}
