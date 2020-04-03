import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.util.*;
import java.io.*;

public class TodoIO {

    public static TreeMap<String, Todo> readFromFile(String filename) throws IOException, NullPointerException, JsonParseException {
        InputStream inputStream = new FileInputStream(filename);
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        Gson gson = new Gson();
        ArrayList<Todo> todosList = gson.fromJson(reader, new TypeToken<ArrayList<Todo>>() {}.getType());
        inputStreamReader.close();
        TreeMap<String, Todo> todosMap = new TreeMap<>();
        for (Todo todo: todosList) {
            todosMap.put(todo.getTitle(), todo);
        }
        return todosMap;
    }

    public static void writeToFile(TreeMap<String,Todo> todosMap, String filename) throws IOException {
        OutputStream outputStream = new FileOutputStream(filename);
        Writer outputStreamWriter = new OutputStreamWriter(outputStream);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Todo> todosList = new ArrayList<>();
        todosMap.forEach((key,value) -> todosList.add(value));
        gson.toJson(todosList, outputStreamWriter);
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }

}

