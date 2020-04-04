import java.io.*;
import java.util.*;

public class Util {
    
    public static String[] parseUserInput() throws IOException {
        System.out.print("Введите команду:");
        System.out.println();
        String[] commandNotFound = {""};
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String command = br.readLine();
        if (command.toLowerCase().startsWith("add_todo")) return parseAddTodoJSON(command);
        if (command.toLowerCase().trim().startsWith("help") || command.toLowerCase().trim().startsWith("list") ||
                command.toLowerCase().trim().startsWith("save") || command.toLowerCase().trim().startsWith("exit") ||
                command.toLowerCase().trim().startsWith("remove_all")) {
            String[] commandParts = {command};
            return commandParts;
        }
        if (command.startsWith("list_")) {
            return command.split(" ");
        }
        if (command.startsWith("remove_todo") || command.startsWith("set_priority") ||
                command.startsWith("mark_done") || command.startsWith("mark_undone")) {
            if (!command.matches(".*\".*\".*")) {
                System.out.println("Используйте двойные кавычки при задании заголовка дела");
                return commandNotFound;
            }
            return command.split("\"");
        }
        return commandNotFound;
    }

    private static String[] parseAddTodoJSON(String firstLine) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String lineOfInput;
        List<String> inputs = new ArrayList<>();
        int braceCounter = -1;
        int firstBrace = 0;
        boolean firstLineFlag = true;
        do {
            lineOfInput = (firstLineFlag) ? firstLine: br.readLine();
            firstLineFlag = false;
            inputs.add(lineOfInput);
            if (lineOfInput.contains("{")) {
                for (int i=0; i < lineOfInput.length(); i++) {
                    if (lineOfInput.charAt(i) == '{') {
                        if (firstBrace!=0) {
                            braceCounter++;
                        } else {
                            firstBrace++;
                            braceCounter+=2;
                        }
                    }
                }
            }
            if (lineOfInput.contains("}")) {
                for (int i=0; i < lineOfInput.length(); i++) {
                    if (lineOfInput.charAt(i) == '}') {
                        braceCounter--;
                    }
                }
            }
        } while ((braceCounter != 0));

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String s : inputs) {
            if (((s.trim().startsWith("{"))) && (i == 0)) {
                sb.append(" " + s.trim());
                i++;
            } else sb.append(s.trim());
            if ((s.endsWith("}")) && (i != 0)) {
                sb.append(" ");
            }
        }
        String command = sb.toString();
        System.out.println(command);
        String commandParts[] = command.split("\\{");
        for (int j = 0; j < commandParts.length; j++) {
            if (j != 0) {
                commandParts[j] = "{" + commandParts[j];
            }
        }
        return commandParts;
    }
    
}
