package com.pao.project.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";

    private AuditService() {
    }

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void logActiune(String numeActiune) {
        try (FileWriter fileWriter = new FileWriter(AUDIT_FILE, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter out = new PrintWriter(bufferedWriter)) {
            out.println(numeActiune + "," + LocalDateTime.now());
        } catch (IOException e) {
            throw new RuntimeException("Eroare la scrierea în fișierul de audit.", e);
        }
    }
}
