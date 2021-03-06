/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package csv.analyser;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Analyser {
    private static final int COL_MING_CHENG = 1;
    private static final int COL_CHENG_JIAO_LIANG = 5;
    private static final double EPSILON = 0.000001d;
    private static final int COL_C = 2;
    private static final int COL_D = 3;
    private static final int COL_F = 5;
    private static final int COL_H = 7;
    private static final int COL_K = 10;
    private static final int COL_L = 11;
    private static final int COL_M = 12;
    private static final int COL_N = 13;

    private static final char NEWLINE = '\n';
    private static final String EMPTY = "";
    private static final String SPLITTER = "\",\"";
    private static final String COMMA = ",";
    private static final BigDecimal HUND = new BigDecimal(100);
    private static final BigDecimal TEN_K = new BigDecimal(10000);
    private static final BigDecimal HUND_MIO = new BigDecimal(100000000);
    private static final File inputFile = new File("input/20211020145843.csv");
    private static final File outputFile = new File("output/20211020145843.csv");

    private final String date;

    public Analyser() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        date = dtf.format(now);
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(inputFile, StandardCharsets.UTF_8);
        Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
        Analyser analyser = new Analyser();
        analyser.processFile(scanner, writer);
        writer.close();
    }

    private void processFile(Scanner scanner, Writer writer) throws IOException {
        String processedTitle = processTitle(scanner.nextLine());
        appendLine(processedTitle, writer);

        while (scanner.hasNext()) {
            String processedLine = processLine(scanner.nextLine());
            if (!processedLine.isEmpty()) {
                appendLine(processedLine, writer);
            }
        }
    }

    private String processTitle(String titleRow) {
        String[] titles = titleRow.split(SPLITTER);
        StringBuilder result = new StringBuilder();
        result.append("????????????");

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            if (i != COL_MING_CHENG) {
                switch (i) {
                    case COL_CHENG_JIAO_LIANG:
                        title = "?????????????????????";
                        break;
                    case 3:
                        title = "?????????";
                        break;
                    case 11:
                        result.append(SPLITTER).append(title);
                        title = "??????????????????";
                        break;
                    case 13:
                        result.append(SPLITTER).append(title);
                        title = "??????????????????";
                        break;
                }
                result.append(SPLITTER).append(title);
            }
        }
        return result.toString();
    }

    private String processLine(String line) {
        StringBuilder builder = new StringBuilder();
        builder.append(date);
        String[] columns = line.split(SPLITTER);

        for (int i = 0; i < columns.length; i++) {
            String column = columns[i];
            if (i != COL_MING_CHENG) {
                switch (i) {
                    case COL_C:
                        if (column.contains("?????????") || column.equals("-")) return EMPTY;
                        break;
                    case COL_D:
                        double d = Double.parseDouble(column);
                        if (Double.compare(d, 0.4) < 0 || Double.compare(d, 100) > 0) return EMPTY;
                        break;
                    case COL_F:
                        column = unified(column);
                        long value = Long.parseLong(unified(column));
                        if (value < 10000) return EMPTY;
                        break;
                    case COL_H:
                        column += "%";
                        break;
                    case COL_K:
                        column = unified(column);
                        long valueK = Long.parseLong(unified(column));
                        if (valueK == 0) return EMPTY;
                        break;
                    case COL_L:
                        long valueL = Long.parseLong(unified(column));
                        if (valueL == 0) return EMPTY;
                        builder.append(SPLITTER).append(unified(column));
                        column = division(column, columns[COL_K]);
                        break;
                    case COL_M:
                        long valueM = Long.parseLong(unified(column));
                        if (valueM == 0) return EMPTY;
                        column = unified(column);
                        break;
                    case COL_N:
                        long valueN = Long.parseLong(unified(column));
                        if (valueN == 0) return EMPTY;
                        builder.append(SPLITTER).append(unified(column));
                        column = division(column, columns[COL_M]);
                        break;
                }
                if (i == 0) {
                    builder.append(COMMA).append(column);
                } else {
                    builder.append(SPLITTER).append(column);
                }
            }
        }

        return builder.toString();
    }

    private static String division(String top, String bottom) {
        if (top.contains("???") && bottom.contains("???")) {
            top = top.substring(0, top.length() - 1) + "???";
            bottom = bottom.substring(0, bottom.length() - 1) + "???";
        }

        long divisor = Long.parseLong(unified(top));
        long dividend = Long.parseLong(unified(bottom));
        String result;
        if (dividend > 0) {
            BigDecimal bd = new BigDecimal(divisor / (double) dividend);
            result = String.valueOf(bd.multiply(HUND));
            if (result.contains(".")) {
                result = result.substring(0, result.indexOf(".") + 2);
            }
            result += "%";
        } else {
            result = "NaN";
        }
        return result;
    }

    private static String unified(String s) {
        if (s.contains("???")) {
            s = s.substring(0, s.length() - 1);
            BigDecimal bd = new BigDecimal(s);
            s = String.valueOf(bd.multiply(TEN_K).longValue());
        } else if (s.contains("???")) {
            s = s.substring(0, s.length() - 1);
            BigDecimal bd = new BigDecimal(s);
            s = String.valueOf(bd.multiply(HUND_MIO).longValue());
        } else if (s.contains("???")) {
            s = s.substring(0, s.length() - 1);
            BigDecimal bd = new BigDecimal(s);
            s = String.valueOf(bd.multiply(HUND).longValue());
        }
        return s;
    }

    private void appendLine(String line, Writer writer) throws IOException {
        writer.append(line).append(NEWLINE);
    }

    private void print(String s) {
        PrintStream ps = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ps.println(s);
    }
}
