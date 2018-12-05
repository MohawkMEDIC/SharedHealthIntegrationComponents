/**
 * Copyright 2013 Mohawk College of Applied Arts and Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author tylerg
 * Date: Nov 4, 2013
 *
 */
package org.marc.shic.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Garrett
 */
public class Demographic
{

    private List<DomainIdentifier> identifiers;
    private List<Phone> phones;
    private List<PersonAddress> addresses;

    public Demographic()
    {
        identifiers = new ArrayList<DomainIdentifier>();
        addresses = new ArrayList<PersonAddress>();
        phones = new ArrayList<Phone>();
    }

    public List<DomainIdentifier> getIdentifiers()
    {
        return identifiers;
    }

    public void addIdentifier(DomainIdentifier id)
    {
        this.identifiers.add(id);
    }

    public void removeIdentifier(DomainIdentifier id)
    {
        this.identifiers.remove(id);
    }

    public DomainIdentifier getDomainIdentifier(String root)
    {
        DomainIdentifier foundIdentifier = null;

        for (DomainIdentifier id : identifiers)
        {
            if (id.getRoot().equals(root))
            {
                foundIdentifier = id;
                break;
            }
        }

        return foundIdentifier;
    }

    /**
     *  Returns the primary phone number
     * @return 
     */
    @Deprecated
    public String getPhone()
    {
        if(phones.size() > 0)
            return phones.get(0).phoneNumber;
        else
            return null;
    }
    
    public List<Phone> getPhones()
    {
        return this.phones;
    }

    public void addPhone(String phone)
    {
        this.phones.add(new Phone(phone));
    }

    /**
     *
     * @param use
     * @return
     */
    public PersonAddress getAddress(AddressUse use)
    {

        for (PersonAddress searchAddress : addresses)
        {
            if (searchAddress.getUse() == use)
            {
                return searchAddress;
            }
        }

        // TODO: Possibly throw a different exception.
        throw new IllegalArgumentException("Address " + use + " was not found.");
    }

    public List<PersonAddress> getAddresses()
    {
        return addresses;
    }

    public void addAddress(PersonAddress address)
    {
        addresses.add(address);
    }

    public void setAddresses(List<PersonAddress> addresses)
    {
        this.addresses = addresses;
    }
    
    public class Phone
    {
        private String phoneNumber;
        private String use;

        public Phone(String phone)
        {
            this.phoneNumber = phone;
        }
        
        public Phone(String phone, String use)
        {
            this.phoneNumber = phone;
            this.use = use;
        }
        
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getUse() {
            return use;
        }

        public void setUse(String use) {
            this.use = use;
        }
        
        
    }
    
}