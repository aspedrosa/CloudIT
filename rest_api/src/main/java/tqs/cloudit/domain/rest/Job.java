package tqs.cloudit.domain.rest;

/**
 *
 * @author joaoalegria
 */
public class Job {
    
    private String title;
    
    private String description;
    
    private String area;
    
    private Integer amount;

    private String date;
    
    private String type;

    public Job(String title, String description, String area, Integer amount, String date, String type) {
        this.title = title;
        this.description = description;
        this.area = area;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public Job() {
    }

    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean allDefined() {
        Object[] tmp = new Object[]{this.title, this.description, this.date, this.amount, this.area};
        for(Object aux : tmp){
            if(aux==null){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Job{" + "title=" + title + ", description=" + description + ", area=" + area + ", amount=" + amount + ", date=" + date + ", type=" + type + '}';
    }
    
}