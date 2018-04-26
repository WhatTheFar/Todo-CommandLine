package data;

import java.util.ArrayList;
import java.util.function.Predicate;

public class TodoRepository {

    private TodoLocalDataSource localDataSource;
    private ArrayList<Todo> cachedTodo;

    public TodoRepository() {
        localDataSource = new TodoLocalDataSource();
    }

    public ArrayList<Todo> getTodoList() {
        if (cachedTodo == null) {
            cachedTodo = localDataSource.selectAll();
        }
        return cachedTodo;
    }

    public boolean addTodo(String title) {
        Todo todo = new Todo(title);
        boolean isSuccess = localDataSource.insert(todo);
        if (isSuccess) {
            cachedTodo.add(todo);
        }
        return isSuccess;
    }

    public boolean toggleTodoIsComplete(String title) {
        ArrayList<Todo> newCachedTodo = new ArrayList<>();
        boolean isComplete = false;
        for (Todo todo :
                cachedTodo) {
            Todo copied = todo.copy();
            if (todo.getTitle().equals(title)) {
                isComplete = !todo.isComplete();
                copied.setComplete(isComplete);
            }
            newCachedTodo.add(copied);
        }
        boolean isSuccess = false;
        if (!newCachedTodo.isEmpty()) {
            isSuccess = localDataSource.updateIsComplete(title, isComplete);
        }
        if (isSuccess) {
            cachedTodo = newCachedTodo;
        }
        return isSuccess;
    }

    public boolean deleteCompletedTodo() {
        boolean isSuccess = localDataSource.deleteCompleted();
        if (isSuccess) {
            cachedTodo.removeIf(new Predicate<Todo>() {
                @Override
                public boolean test(Todo todo) {
                    return todo.isComplete();
                }
            });
        }
        return isSuccess;
    }
}
