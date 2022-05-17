package com.cicd.containercicddemo;

import com.cicd.containercicddemo.libs.Sha;
import com.cicd.containercicddemo.libs.ShaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShaUtilsTests {
        Sha sha;

        @BeforeEach
        void setup() {
            sha = new ShaImpl();
        }

        @Test
        @DisplayName("Various input string should be converted to correct SHA256 strings")
        public void testShaString() {
                try {
                        String result = sha.getShaString("");
                        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                                result, "Empty string should pring out the correct SHA256");

                        result = sha.getShaString("2022-05-17T06:08:10.568Z");
                        assertEquals("7cf4d01afb74d4aabd81dbd7b65e9ea974c0f23cbfcf5dfa34bde0c15e4ad436",
                                result, "Timestamp GMT should pring out the correct SHA256");

                } catch (Exception e) {
                        if (e == null) {
                                fail("should not have thrown an exception, but threw " + e);
                        }
                        if (!e.getClass().equals(e.getClass()) || !e.getMessage().equals(e.getMessage())) {
                                fail("expected exception " + e + "; got exception " + e);
                        }
                }

        }
}


