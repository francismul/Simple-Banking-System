
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPOutputStream;

/**
 * CustomLogger
 * 
 * Hybrid log rotation system that combines both size-base and time-base
 * rotation.
 * This logger rotates logs when they reach a certain size (1MB).
 * It also rotates logs daily (even if they don't reach the size limit).
 * Maintains a backup logs with both date-based and numbered versions.
 * Handles multiple thread safely with BlockingQueue.
 * Automatic clean up: keeps only 3 backups per day.
 * Before removing old logs, they are compressed to .gz.
 */
public class CustomLogger {
    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1MB (adjust as needed)
    private static final int MAX_BACKUP_FILES = 3; // Keep 3 backups per day
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private final String logDir;
    private String currentLogFile;
    private LocalDate lastRotationDate;
    private volatile boolean running = true;

    public CustomLogger(String logDir) {
        this.logDir = logDir;
        this.lastRotationDate = LocalDate.now();
        this.currentLogFile = getLogFileName();
        File logDirectory = new File(logDir);
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        startLoggerThread();
    }

    private void startLoggerThread() {
        Thread logThread = new Thread(() -> {
            while (running || !logQueue.isEmpty()) {
                try {
                    rotateLogsIfNeeded();
                    String logMessage = logQueue.take();
                    writeToFile(logMessage);
                    // System.out.println(logMessage);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        logThread.setDaemon(true);
        logThread.start();
    }

    public void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        logQueue.offer(logMessage);
    }

    private void rotateLogsIfNeeded() {
        File logFile = new File(currentLogFile);
        LocalDate today = LocalDate.now();

        if (logFile.exists() && logFile.length() >= MAX_FILE_SIZE || !today.equals(lastRotationDate)) {
            rotateLogs();
            lastRotationDate = today;
            currentLogFile = getLogFileName();
        }
    }

    private void rotateLogs() {
        // Compress the last backup before shifting files
        File lastBackup = new File(getBackupFileName(MAX_BACKUP_FILES));
        if (lastBackup.exists()) {
            compressFile(lastBackup);
            lastBackup.delete(); // Remove uncompressed version
        }

        // Shift log backups
        for (int i = MAX_BACKUP_FILES - 1; i > 0; i--) {
            File oldFile = new File(getBackupFileName(i));
            File newFile = new File(getBackupFileName(i + 1));
            if (oldFile.exists()) {
                oldFile.renameTo(newFile);
            }
        }

        File currentLog = new File(currentLogFile);
        File firstBackup = new File(getBackupFileName(1));
        if (currentLog.exists()) {
            currentLog.renameTo(firstBackup);
        }
    }

    private void compressFile(File file) {
        String gzipFileName = file.getAbsolutePath() + ".gz";
        try (FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(gzipFileName);
                GZIPOutputStream gzipOS = new GZIPOutputStream(fos)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, bytesRead);
            }
            System.out.println("Compressed: " + file.getName() + " → " + gzipFileName);
        } catch (IOException e) {
            System.err.println("Error compressing log file: " + e.getMessage());
        }
    }

    private String getLogFileName() {
        String date = LocalDate.now().format(DATE_FORMAT);
        return logDir + "/app-" + date + ".log";
    }

    private String getBackupFileName(int index) {
        String date = LocalDate.now().format(DATE_FORMAT);
        return logDir + "/app-" + date + "." + index + ".log";
    }

    private void writeToFile(String logMessage) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(currentLogFile, true))) {
            writer.println(logMessage);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public void shutdown() {
        running = false;
        flush();
    }

    public void flush() {
        synchronized (logQueue) {
            while (!logQueue.isEmpty()) {
                try {
                    writeToFile(logQueue.poll()); // write all pending logs
                } catch (Exception e) {
                    System.err.println("Error during log flush:" + e.getMessage());
                }
            }
        }
    }
}
