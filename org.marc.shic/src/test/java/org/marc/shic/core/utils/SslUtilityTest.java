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
 * @author Mohamed Ibrahim
 * Date: 16-Sep-2013
 *
 */
package org.marc.shic.core.utils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.marc.shic.core.CertificateIdentifier;

/**
 * A Test class for SslUtility
 */
public class SslUtilityTest
{
    public SslUtilityTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of generatePKCS10 method, of class SslUtility.
     */
    @Test
    public void testGeneratePKCS10() throws Exception
    {
        System.out.println("generatePKCS10");
        KeyPair keyPair = SslUtility.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String CN = "Mohamed Ibrahim";
        String OU = "Ideaworks";
        String O = "Mohawk College";
        String L = "Hamilton";
        String S = "ON";
        String C = "CA";

        CertificateIdentifier principal = new CertificateIdentifier(CN, OU, O, L, S, C);
        PKCS10CertificationRequest result = SslUtility.generatePKCS10(publicKey, privateKey, principal);
        System.out.println(SslUtility.generateCSR(result));
        
        assertTrue(result.getSubject().toString().contains(CN));
    }
}
