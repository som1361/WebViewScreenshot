package com.example.webviewscreenshot

import com.example.webviewscreenshot.utils.isValidUrl
import org.junit.Assert
import org.junit.Test

class UrlValidatorTest {
    @Test
    fun UrlValidator_CorrectUrlSimple_ReturnsTrue() {
        Assert.assertTrue("http://www.google.com".isValidUrl())
    }
    @Test
    fun urlValidator_correctUrl_withHttpsScheme_ReturnsTrue() {
        Assert.assertTrue("https://www.google.com".isValidUrl())
    }
    @Test
    fun urlValidator_correctUrl_WithoutScheme_ReturnsTrue() {
        Assert.assertTrue("www.google.com".isValidUrl())
    }
    @Test
    fun urlValidator_correctUrl_WithoutWWW_ReturnsTrue() {
        Assert.assertTrue("http://google.com".isValidUrl())
    }
    @Test
    fun urlValidator_correctUrl_JustSLD_ReturnsTrue() {
        Assert.assertTrue("google.com".isValidUrl())
    }
    @Test
    fun urlValidator_correctUrl_WithCCTLD_ReturnsTrue() {
        Assert.assertTrue("http://www.google.co.uk".isValidUrl())
    }
    @Test
    fun urlValidator_correctUrl_WithoutTLD_ReturnsTrue() {
        Assert.assertTrue("http://www.google".isValidUrl())
    }

    @Test
    fun urlValidator_incorrectUrl_WithDoubleDots_ReturnsFalse() {
        Assert.assertFalse("http://www.google..com".isValidUrl())
    }
    @Test
    fun urlValidator_incorrectUrl_WrongScheme_ReturnsFalse() {
        Assert.assertFalse("htp://www.google.com".isValidUrl())
    }
    @Test
    fun urlValidator_incorrectUrl_WithWWWAndTLD_ReturnsFalse() {
        Assert.assertFalse("http://google".isValidUrl())
    }
    @Test
    fun urlValidator_incorrectUrl_JustTLD_ReturnsFalse() {
        Assert.assertFalse(".com".isValidUrl())
    }
    @Test
    fun urlValidator_incorrectUrl_JustSubdomain_ReturnsFalse() {
        Assert.assertFalse("google".isValidUrl())
    }
    @Test
    fun urlValidator_incorrectUrl_EmptyUrl_ReturnsFalse() {
        Assert.assertFalse("".isValidUrl())
    }

}