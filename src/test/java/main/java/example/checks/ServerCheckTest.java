package main.java.example.checks;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.scanner.AuditResult;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;
import org.junit.Assert;
import org.junit.Test;

import java.rmi.ServerError;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class ServerCheckTest {
    HttpRequest request = HttpRequest.httpRequest("GET /content/images/svg/arrow-youtube.svg HTTP/2\n" +
            "Host: portswigger.net\n" +
            "Cookie: __stripe_mid=d62cbb62-4491-4ec2-b00e-2e4b4cb042dbf6a64a; stg_returning_visitor=Sun%2C%2002%20Apr%202023%2015:42:51%20GMT; _ga_EM5SXNWFCK=GS1.1.1685655324.23.1.1685656065.0.0.0; _ga=GA1.2.1159594970.1683243004; t=aIsMSiGpWzTw5yqoBGQebg%3D%3D; AWSALBAPP-0=_remove_; AWSALBAPP-1=_remove_; AWSALBAPP-2=_remove_; AWSALBAPP-3=_remove_; stg_last_interaction=Thu%2C%2017%20Aug%202023%2001:29:01%20GMT; _pk_id.287552c2-4917-42e0-8982-ba994a2a73d7.1467=c6ff8468fd3e1377.1683243005.36.1692235741.1692228951.; SessionId=CfDJ8ImhUzb%2FSxBAi3J%2BxXV0hecsMiE93XYxwyfyW8yNvq88wNZT6ULWygN%2FPePZUKZuuzKGgJVZscZJyxEYecyqnDoRBskiDIhl00GGeFicB9KpbdcJyXkcxuayfLTkiMjjGjwjLp0MNOx3uyzmswNYdzTcILCFF1QFqohRYDPzACkm\n" +
            "Sec-Ch-Ua: \n" +
            "Sec-Ch-Ua-Mobile: ?0\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.5845.97 Safari/537.36\n" +
            "Sec-Ch-Ua-Platform: \"\"\n" +
            "Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8\n" +
            "Sec-Fetch-Site: same-origin\n" +
            "Sec-Fetch-Mode: no-cors\n" +
            "Sec-Fetch-Dest: image\n" +
            "Referer: https://portswigger.net/content/pslandingpages.css\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "Accept-Language: en-US,en;q=0.9\n" +
            "\n");

    HttpResponse response = HttpResponse.httpResponse("HTTP/2 200 OK\n" +
            "Date: Fri, 18 Aug 2023 17:13:39 GMT\n" +
            "Content-Type: image/svg+xml\n" +
            "Content-Length: 361\n" +
            "Server: Kestrel\n" +
            "Accept-Ranges: bytes\n" +
            "Cache-Control: must-revalidate, max-age=0\n" +
            "Etag: \"1d9cc6cc7842369\"\n" +
            "Last-Modified: Fri, 11 Aug 2023 15:59:16 GMT\n" +
            "Strict-Transport-Security: max-age=31536000; preload\n" +
            "X-Content-Type-Options: nosniff\n" +
            "X-Frame-Options: SAMEORIGIN\n" +
            "X-Xss-Protection: 1; mode=block\n" +
            "Content-Security-Policy: default-src 'none';base-uri 'none';child-src 'self' https://www.youtube.com/embed/;connect-src 'self' https://ps.containers.piwik.pro https://ps.piwik.pro https://www.google.com/recaptcha/;font-src 'self';frame-src 'self' https://www.youtube.com/embed/ https://www.google.com/recaptcha/;img-src 'self' https://i.ytimg.com/;media-src 'self' https://d21v5rjx8s17cr.cloudfront.net/ https://d2gl1b374o3yzk.cloudfront.net/;script-src 'self' https://ps.containers.piwik.pro/ppms.js https://ps.piwik.pro/ppms.js https://www.youtube.com/iframe_api https://www.youtube.com/s/player/ https://www.google.com/recaptcha/ https://www.gstatic.com/recaptcha/ 'nonce-dLz8zKQ3r1AZrP5voZ0FXy5H3kzBZugN';style-src 'self';\n" +
            "Content-Disposition: attachment\n" +
            "Cross-Origin-Resource-Policy: same-site\n" +
            "Cross-Origin-Opener-Policy: same-origin\n" +
            "Set-Cookie: AWSALBAPP-0=_remove_; Expires=Fri, 25 Aug 2023 17:13:39 GMT; Path=/\n" +
            "Set-Cookie: AWSALBAPP-1=_remove_; Expires=Fri, 25 Aug 2023 17:13:39 GMT; Path=/\n" +
            "Set-Cookie: AWSALBAPP-2=_remove_; Expires=Fri, 25 Aug 2023 17:13:39 GMT; Path=/\n" +
            "Set-Cookie: AWSALBAPP-3=_remove_; Expires=Fri, 25 Aug 2023 17:13:39 GMT; Path=/\n" +
            "\n" +
            "<svg width=\"74\" height=\"74\" viewBox=\"0 0 74 74\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
            "<path opacity=\"0.5\" fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M37 74C57.4345 74 74 57.4345 74 37C74 16.5655 57.4345 0 37 0C16.5655 0 0 16.5655 0 37C0 57.4345 16.5655 74 37 74ZM29.6747 52.5461L52.7539 37.0005L29.6747 21.4549L29.6747 52.5461Z\" fill=\"white\"/>\n" +
            "</svg>\n");

    @Test
    public void passiveAudit() {
        HttpRequestResponse requestResponse = HttpRequestResponse.httpRequestResponse(request, response);
        ServerCheck check = new ServerCheck();
        AuditResult auditResult = check.passiveAudit(requestResponse);
        List<AuditIssue> expectedAuditIssueList = List.of(AuditIssue.auditIssue(
                "Name",
                "Detail",
                "Remediation",
                "https://portswigger-labs.net",
                AuditIssueSeverity.HIGH,
                AuditIssueConfidence.CERTAIN,
                "Background",
                "RemediationBackground",
                AuditIssueSeverity.HIGH,
                requestResponse)
        );

        assertTrue(auditResult.auditIssues().equals(expectedAuditIssueList));
    }
}