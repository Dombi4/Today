package org.example;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Main {
    public static String USERDATE = "2023-11-21";
    public static void main(String[] args) {
        long time = System.nanoTime();
        for (File file : (cat("src/main/resources/in"))) {
            File fileTxt = new File("src/main/resources/in/"+file.getName());
            File fileJson = new File("src/main/resources/out/"+jsonName(file.getName())+"json");
            cat("src/main/resources/in");
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileTxt));
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileJson));
                String orig;
                String newoig;
                int n = 0;
                int n1 = 0;
                int n2 = 0;
                int n3 = 0;
                while ((orig = br.readLine()) != null) {
                    if (n<1 & orig.contains("\"loanRequestExtId\": \"")) {
                        int number = orig.indexOf(": \"");
                            newoig = orig.replace(orig.substring(number+3), "[customerRequestExtId]\"," );
                            orig = newoig;
                            n++;
                    }
                    if (n1<1 & orig.contains("\"loanRequestId\": \"")) {
                        int number = orig.indexOf(": \"");
                        newoig = orig.replace(orig.substring(number+3), "[loanRequestId]\"," );
                        orig = newoig;
                        n1++;
                    }
                    if (n2<1 & orig.contains("\"customerRequestExtId\": \"")) {
                        int number = orig.indexOf(": \"");
                        newoig = orig.replace(orig.substring(number+3), "[customerRequestExtId]\"," );
                        orig = newoig;
                        n2++;
                    }
                    if (n3<1 & orig.contains("\"customerRequestId\": \"")) {
                        int number = orig.indexOf(": \"");
                        newoig = orig.replace(orig.substring(number+3), "[customerRequestId]\"," );
                        orig = newoig;
                        n3++;
                    }
                    if (orig.contains("-")) {
                        int number = orig.indexOf(": \"");
                        char c;
                        if (number > 0) {
                            int num = number + 7;
                            try {
                                c = orig.charAt(num);
                            } catch (StringIndexOutOfBoundsException e){
                                c = ' ';
                            }

                            if (c == '-') {
                                String substr = orig.substring(num - 4, num + 6);
                                    newoig = orig.replace(substr, "[today"+ date(substr, "year") + date(substr, "month") + date(substr, "day") +"]");
                                //System.out.println(newoig);
                                orig = newoig;
                                //System.out.println(orig);
                            }
                        }
                    }
                    bw.write(orig + "\n");
                }
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        time = System.nanoTime() - time;
        System.out.println("\u001B[35m" + time/1_000_000.0+" ms"+ "\u001B[0m");
    }

    public static String date(String dateOld, String hron) {
        String manyYears = "1000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(dateOld, formatter);
        LocalDate endDate = LocalDate.parse(USERDATE);
        Period period = Period.between(startDate, endDate);
        switch (hron){
            case "day":
                /*if (period.getDays()==0)
                    return "";*/
                if (period.getDays() <0)
                    return ("+"+ period.getDays()*(-1) + "d");
                return ("-"+ period.getDays() + "d");
            case "month":
                if (period.getMonths()==0)
                    return "";
                if (period.getMonths() <0)
                    return ("+"+ period.getMonths()*(-1) + "m");
                return ("-"+ period.getMonths()+ "m");
            case "year":
                if (period.getYears()==0)
                    return "";
                if (period.getYears()<-1000)
                    return ("+"+ manyYears + "y");
                if (period.getYears() <0)
                    return ("+"+ period.getYears()*(-1) + "y");
                return ("-"+ period.getYears()+ "y");
        }
        return "NON";
    }

    public static File[] cat(String args) {
        File folder;
        folder = new File(args);
        return folder.listFiles();
    }
public static String jsonName(String name){
        String fn;
            name = name.replace("call2_", "");
            fn = name.replace("txt","");
            System.out.println(fn);
            return fn;
}
}