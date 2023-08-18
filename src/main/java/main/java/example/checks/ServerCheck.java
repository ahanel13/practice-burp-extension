package main.java.example.checks;

import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.scanner.AuditResult;
import burp.api.montoya.scanner.ConsolidationAction;
import burp.api.montoya.scanner.ScanCheck;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerCheck implements ScanCheck {
    List<String> commonWebServerHeaders = Arrays.asList("Server", "X-Power-by");

    List<String> allowedServerValues = Arrays.asList("gws", "AmazonS3");

    @Override
    public AuditResult activeAudit(HttpRequestResponse httpRequestResponse, AuditInsertionPoint auditInsertionPoint) {
        return null;
    }

    @Override
    public AuditResult passiveAudit(HttpRequestResponse httpRequestResponse) {
        List<AuditIssue> issueList = new ArrayList<>();
        for (HttpHeader header : httpRequestResponse.response().headers()) {
            if(commonWebServerHeaders.contains(header.name()) && !header.value().isBlank() && !allowedServerValues.contains(header.value())){
                issueList.add(AuditIssue.auditIssue(
                        "Name",
                        "Detail",
                        "Remediation",
                        "https://portswigger-labs.net",
                        AuditIssueSeverity.HIGH,
                        AuditIssueConfidence.CERTAIN,
                        "Background",
                        "RemediationBackground",
                        AuditIssueSeverity.HIGH,
                        httpRequestResponse)
                );
            }
        }
        return AuditResult.auditResult(issueList);
    }

    @Override
    public ConsolidationAction consolidateIssues(AuditIssue auditIssue, AuditIssue auditIssue1) {
        return null;
    }
}
