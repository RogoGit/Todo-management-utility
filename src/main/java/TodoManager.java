import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.util.*;
import java.io.*;

public class TodoManager {

    public static ArrayList<Todo> readFromFile(String filename) throws IOException, NullPointerException, JsonParseException {
        InputStream inputStream = new FileInputStream(filename);
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        Gson gson = new Gson();
        ArrayList<Todo> todosList = gson.fromJson(reader, new TypeToken<ArrayList<Todo>>() {}.getType());
        inputStreamReader.close();
        for (Todo todo: todosList) {
            if (todo.getTitle() == null || todo.getTaskDescription() == null) {
                throw new NullPointerException();
            }
        }
        return todosList;
    }

}

