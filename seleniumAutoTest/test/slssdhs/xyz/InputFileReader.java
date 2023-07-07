package slssdhs.xyz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;

public class InputFileReader {

    private static final Map<String, Integer> MONTH_MAP = Map.ofEntries(
            new SimpleImmutableEntry<>("Jan", 1),
            new SimpleImmutableEntry<>("Feb", 2),
            new SimpleImmutableEntry<>("Mar", 3),
            new SimpleImmutableEntry<>("Apr", 4),
            new SimpleImmutableEntry<>("May", 5),
            new SimpleImmutableEntry<>("Jun", 6),
            new SimpleImmutableEntry<>("Jul", 7),
            new SimpleImmutableEntry<>("Aug", 8),
            new SimpleImmutableEntry<>("Sep", 9),
            new SimpleImmutableEntry<>("Oct", 10),
            new SimpleImmutableEntry<>("Nov", 11),
            new SimpleImmutableEntry<>("Dec", 12)
    );

    public static void main(String[] args) {
        String fileName = "input.txt"; // 设置文件路径
        Object[][] result = readFile(fileName);
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }

    public static Object[][] readFile(String fileName) {
        List<Object[]> tempList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                Object[] processed = processLine(parts);
                tempList.add(processed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 转换 List<Object[]> 为 Object[][]
        Object[][] result = new Object[tempList.size()][];
        for (int i = 0; i < tempList.size(); i++) {
            result[i] = tempList.get(i);
        }
        return result;
    }


    public static Object[] processLine(String[] parts) {
        StringBuilder location = new StringBuilder();
        int index = 0;
        while (index < parts.length) {
            String part = parts[index];
            if (part.length() > 4 &&
                    MONTH_MAP.containsKey(part.substring(0, 3)) &&
                    part.charAt(3) == '.') {
                // assume we found the month , let's break
                break;
            }
            location.append(part).append(" ");
            index++;
        }
        location.deleteCharAt(location.length() - 1); // 删除多余的空格

        String CheckInMonth = parts[index].substring(0, 3);
        int CheckInDay = Integer.parseInt(parts[index].substring(4));
        index++;

        String CheckOutMonth = parts[index].substring(0, 3);
        int CheckOutDay = Integer.parseInt(parts[index].substring(4));
        index++;

        List<Object> array = new ArrayList<>();
        array.add(location.toString());
        array.add(2023);
        array.add(MONTH_MAP.get(CheckInMonth));
        array.add(CheckInDay);
        array.add(MONTH_MAP.get(CheckOutMonth));
        array.add(CheckOutDay);

        while (index < parts.length) {
            array.add(Integer.parseInt(parts[index]));
            index++;
        }

        array.add(location.toString());

        return array.toArray();
    }
}
