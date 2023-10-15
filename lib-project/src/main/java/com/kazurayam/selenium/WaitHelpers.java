package com.kazurayam.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public class WaitHelpers {

    /**
     * wait for the DOM to stop changing.
     * Uses a MutationObserver (JavaScript) to be notified of DOM modifications.
     * "Changing" can include addition/removal/modification of any elements in the DOM
     */
    public static void waitForDomModificationsToCease(WebDriver driver) throws InterruptedException {
        Objects.requireNonNull(driver);
        JavascriptExecutor jsx = (JavascriptExecutor)driver;
        jsx.executeScript(getObserverJS());
        long modifiedTime = 0;
        long start = System.currentTimeMillis();
        for (;;) {
            Thread.sleep(500);
            Object rs = jsx.executeScript("return window.domModifiedTime;");
            assert rs != null : "rs is null";
            assert rs instanceof Long : "rs is not instance of Long";
            long jsxResult = (long)rs;
            if (modifiedTime == jsxResult || System.currentTimeMillis() - start > 30000) {
                break;
            }
            modifiedTime = jsxResult;
        }
    }

    static String getObserverJS() {
        StringBuilder sb = new StringBuilder();
        sb.append("if (typeof observer === 'undefined') {\n");
        sb.append("    window.domModifiedTime = Date.now();\n");
        sb.append("    let targetNode = document.querySelector('body');\n");
        sb.append("    let config = {\n");
        sb.append("        attributes: true,\n");
        sb.append("        childList: true,\n");
        sb.append("        subtree: true\n");
        sb.append("    };\n");
        sb.append("    let callback = () => {\n");
        sb.append("        window.domModifiedTime = Date.now();\n");
        sb.append("    };\n");
        sb.append("    let observer = new MutationObserver(callback);\n");
        sb.append("    observer.observe(targetNode, config);\n");
        sb.append("}\n");
        return sb.toString();
    }
}
