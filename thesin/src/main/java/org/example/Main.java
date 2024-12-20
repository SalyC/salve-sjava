package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Payments {
    private String fullName;
    private int day;
    private int month;
    private int year;
    private int amount;
    public Payments(String fullName, int day, int month, int year, int amount) {
        this.fullName = fullName;
        this.day = day;
        this.month = month;
        this.year = year;
        this.amount = amount;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    @Override
    public String toString() {
        return "Payment{" +
                "fullName='" + fullName + '\'' +
                ", date=" + day + "/" + month + "/" + year +
                ", amount=" + amount + " коп." +
                '}';
    }
    public static void main(String[] args) {
        Payments payment1 = new Payments("Иванов Иван Иванович", 15, 3, 2023, 15000);
        Payments payment2 = new Payments("Петров Петр Петрович", 20, 3, 2023, 20000);
        System.out.println(payment1);
        System.out.println(payment2);
    }
}

class FinanceReports {
    private final Payments[] payments;
    private final String author;
    private final LocalDate creationDate;
    public FinanceReports(Payments[] payments, String author, LocalDate creationDate) {
        this.payments = new Payments[payments.length];
        for (int i = 0; i < payments.length; i++) {
            this.payments[i] = new Payments(payments[i].getFullName(), payments[i].getDay(), payments[i].getMonth(), payments[i].getYear(), payments[i].getAmount());
        }
        this.author = author;
        this.creationDate = creationDate;
    }
    public Payments[] getPayments() {
        return payments;
    }
    public String getAuthor() {
        return author;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
}
class FinanceReportProcessor {
    public static int getTotalPaymentsOnDate(FinanceReports report, String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат даты. Необходимо использовать dd.MM.yy.");
        }
        int total = 0;
        for (Payments payment : report.getPayments()) {
            if (payment.getDay() == date.getDayOfMonth() &&
                    payment.getMonth() == date.getMonthValue() &&
                    payment.getYear() == date.getYear()) {
                total += payment.getAmount();
            }
        }
        return total;
    }


    public static List<String> getMonthsWithNoPaymentsInYear(FinanceReports report, int year) {
        boolean[] monthsWithPayments = new boolean[12];
        for (Payments payment : report.getPayments()) {
            if (payment.getYear() == year) {
                monthsWithPayments[payment.getMonth() - 1] = true;
            }
        }
        List<String> emptyMonths = new ArrayList<>();
        for (int i = 0; i < monthsWithPayments.length; i++) {
            if (!monthsWithPayments[i]) {
                emptyMonths.add(getMonthName(i + 1));
            }
        }
        return emptyMonths;
    }

    private static String getMonthName(int month) {
        String[] monthNames = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        return monthNames[month - 1];
    }
}

public class Main {
    public static void main(String[] args) {
        Payments payment1 = new Payments("Иванов Иван Иванович", 15, 3, 2023, 15000);
        Payments payment2 = new Payments("Петров Петр Петрович", 20, 3, 2023, 20000);
        Payments[] payments = {payment1, payment2};
        FinanceReports report = new FinanceReports(payments, "Сидоров Сидор Сидорович", LocalDate.now());

        System.out.println("Суммарные платежи на 15.03.23: " + FinanceReportProcessor.getTotalPaymentsOnDate(report, "15.03.23"));
        System.out.println("Месяцы без платежей в 2023 году: " + FinanceReportProcessor.getMonthsWithNoPaymentsInYear(report, 2023));
    }
}