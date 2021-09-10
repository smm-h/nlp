package web.html;

public abstract class HTMLReport implements Runnable {

    private final String title;
    protected HTMLDocument report;

    public HTMLReport(String title) {
        this.title = title;
    }

    @Override
    public void run() {
        report = new HTMLDocument();
        report.addTag("title", title);
        report.add(HTMLUtilities.DEFAULT_STYLE);
        if (beforeShowing()) {
            HTMLUtilities.show(report);
            afterShowing();
        }
    }

    /**
     * @return True, if it was cancelled.
     */
    public abstract boolean beforeShowing();

    public abstract void afterShowing();
}