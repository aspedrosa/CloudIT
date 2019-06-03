/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.cloudit.domain.rest;

/**
 *
 * @author joaoalegria
 */
public class JobOffer {
    
    private String title;
    
    private String description;
    
    private String area;
    
    private Integer amount;

    private String date;

    public JobOffer(String title, String description, String area, Integer amount, String date) {
        this.title = title;
        this.description = description;
        this.area = area;
        this.amount = amount;
        this.date = date;
    }

    public JobOffer() {
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
        return "JobOffer{" + "title=" + title + ", description=" + description + ", area=" + area + ", amount=" + amount + ", date=" + date + '}';
    }
    
}
