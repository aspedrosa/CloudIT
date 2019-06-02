package tqs.cloudit.domain.rest;

public class AdvancedSearch {
    private String query;
    private boolean title;
    private boolean area;
    private double fromAmount;
    private double toAmount;
    private String fromDate;
    private String toDate;

    public AdvancedSearch(String query, boolean title, boolean area, double fromAmount, double toAmount, String fromDate, String toDate) {
        this.query = query;
        this.title = title;
        this.area = area;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public AdvancedSearch() {
    }

    public boolean isTitle() {
        return title;
    }

    public void setTitle(boolean title) {
        this.title = title;
    }

    public boolean isArea() {
        return area;
    }

    public void setArea(boolean area) {
        this.area = area;
    }

    public double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public double getToAmount() {
        return toAmount;
    }

    public void setToAmount(double toAmount) {
        this.toAmount = toAmount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "AdvancedSearch{" + "query=" + query + ", title=" + title + ", area=" + area + ", fromAmount=" + fromAmount + ", toAmount=" + toAmount + ", fromDate=" + fromDate + ", toDate=" + toDate + '}';
    }
}