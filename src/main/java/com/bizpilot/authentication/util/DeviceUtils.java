package com.bizpilot.authentication.util;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class DeviceUtils {

    public String extractIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty() && !"unknown".equalsIgnoreCase(xfHeader)) {
            return xfHeader.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isEmpty()) {
            return realIp;
        }
        return request.getRemoteAddr();
    }

    public String extractDeviceName(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isBlank()) {
            return "Unknown Device";
        }

        String os = detectOs(userAgent);
        String browser = detectBrowser(userAgent);

        return browser + " on " + os;
    }

    private String detectOs(String ua) {
        String uaLower = ua.toLowerCase();
        if (uaLower.contains("android")) return "Android";
        if (uaLower.contains("iphone") || uaLower.contains("ipad")) return "iOS";
        if (uaLower.contains("windows")) return "Windows";
        if (uaLower.contains("mac os")) return "macOS";
        if (uaLower.contains("linux")) return "Linux";
        return "Unknown OS";
    }

    private String detectBrowser(String ua) {
        String uaLower = ua.toLowerCase();
        if (uaLower.contains("edg/")) return "Edge";
        if (uaLower.contains("chrome/") && !uaLower.contains("edg/")) return "Chrome";
        if (uaLower.contains("firefox/")) return "Firefox";
        if (uaLower.contains("safari/") && !uaLower.contains("chrome/")) return "Safari";
        if (uaLower.contains("opr/") || uaLower.contains("opera")) return "Opera";
        return "Unknown Browser";
    }
}