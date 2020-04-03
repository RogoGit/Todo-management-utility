import java.io.*;
import java.util.*;

public class Util {
    
    public static String[] parseUserInput() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Command:");
        System.out.println();
        String lineOfInput;
        List<String> inputs = new ArrayList<>();
        int braceCounter = -1;
        int firstBrace = 0;
        do {
            lineOfInput = br.readLine();
            inputs.add(lineOfInput);
            if ((lineOfInput.trim().toLowerCase().equals("help")) || (lineOfInput.trim().toLowerCase().equals("list"))
                    || (lineOfInput.trim().toLowerCase().equals("save")) || (lineOfInput.trim().toLowerCase().equals("exit"))) {
                break;
            }
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
        if (!((command.toLowerCase().equals("help")) || (lineOfInput.trim().toLowerCase().equals("list"))
                || (command.toLowerCase().equals("save")) || (command.toLowerCase().equals("exit")))) {
            System.out.println(command);
        }
        String commandParts[] = command.replaceAll(" ", "").split("\\{");
        for (int j = 0; j < commandParts.length; j++) {
            if (j != 0) {
                commandParts[j] = "{" + commandParts[j];
            }
        }
        return commandParts;
    }
    
}
