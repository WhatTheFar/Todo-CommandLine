import data.Todo;
import data.TodoRepository;

import java.util.ArrayList;
import java.util.Scanner;

public class TodoApp implements Runnable {

    public static final String COMMAND_ADD = "add_command";
    public static final String COMMAND_COMPLETE = "complete_command";
    public static final String COMMAND_DELETE = "delete_command";
    public static final String COMMAND_EXIT = "exit_command";
    public static final String COMMAND_ERROR = "error_command";

    private TodoRepository todoRepo;

    public TodoApp() {
        this.todoRepo = new TodoRepository();
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        boolean isError = false;
        while (true) {
            if (!isError) {
                printTodoList(todoRepo.getTodoList());
                printMenu();
            }
            isError = false;
            switch (getUserCommand(sc)) {
                case COMMAND_ADD:
                    addTodoFromUser(sc);
                    break;
                case COMMAND_COMPLETE:
                    updateTodoFromUser(sc);
                    break;
                case COMMAND_DELETE:
                    deleteCompletedTodo();
                    break;
                case COMMAND_EXIT:
                    return;
                default:
                    isError = true;
                    System.out.println("Error: Invalid command");
            }
        }
    }

    public void addTodoFromUser(Scanner sc) {
        System.out.println("\n\n\n");
        System.out.println("Enter title to add new Todo or nothing to stop.");
        while (true) {
            System.out.print("Title: ");
            String input = sc.nextLine().trim();
            if (input.equals("")) {
                System.out.println("\n\n\n");
                return;
            }
            boolean isSuccess = todoRepo.addTodo(input);
            if (!isSuccess) {
                System.out.println(input + " cannot be saved");
            }
        }
    }

    public void updateTodoFromUser(Scanner sc) {
        System.out.println("\n\n\n");
        System.out.println("Enter Todo ID to toggle complete or nothing to stop.");
        while (true) {
            System.out.print("Title: ");
            String input = sc.nextLine().trim();
            if (input.equals("")) {
                System.out.println("\n\n\n");
                return;
            }
            boolean isSuccess = todoRepo.toggleTodoIsComplete(input);
            if (!isSuccess) {
                System.out.println("Erro: " + "System can not update" + input);
            }
        }
    }

    public void deleteCompletedTodo() {
        boolean isSuccess = todoRepo.deleteCompletedTodo();
        System.out.println("\n\n\n");
        if (!isSuccess) {
            System.out.println("Error: System can not delete completed Todo");
        }
    }

    public String getUserCommand(Scanner sc) {
        System.out.print("Command : ");
        String input = sc.nextLine();
        switch (input) {
            case "1":
                return COMMAND_ADD;
            case "2":
                return COMMAND_COMPLETE;
            case "3":
                return COMMAND_DELETE;
            case "4":
                return COMMAND_EXIT;
            default:
                return COMMAND_ERROR;
        }
    }

    public void printTodoList(ArrayList<Todo> todos) {
        if (todos.isEmpty()) {
            return;
        }
        System.out.println("Todo | Title");
        for (Todo todo :
                todos) {
            System.out.println(todo.toString());
        }
    }

    public void printMenu() {
        System.out.println("Please select a command");
        System.out.println("1. Add new todo");
        System.out.println("2. Complete todo");
        System.out.println("3. Clear complete");
        System.out.println("4. Exit program");
    }

    public static void main(String[] args) {
        TodoApp app = new TodoApp();
        app.run();
    }
}
