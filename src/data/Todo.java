package data;

public class Todo {

    private String title;
    private boolean isComplete;

    public Todo(String title) {
        this(title, false);
    }

    public Todo(String title, boolean isComplete) {
        this.title = title;
        this.isComplete = isComplete;
    }

    @Override
    public String toString() {
        return (isComplete ? "Done" : "    ") + " | " + title;
    }

    protected Todo copy() {
        return new Todo(this.title, this.isComplete);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

}
