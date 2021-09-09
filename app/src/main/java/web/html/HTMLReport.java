package web.html;

public abstract class HTMLReport extends HTMLDocument implements Runnable {

    public HTMLReport(String title) {
        addTag("title", title);
        add(HTMLUtilities.DEFAULT_STYLE);
    }

    @Override
    public void run() {
        beforeShowing();
        HTMLUtilities.show(this);
        afterShowing();
    }

    public abstract void beforeShowing();

    public abstract void afterShowing();
}