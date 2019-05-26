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
    
    private String name;
    
    private String area;
    
    private Integer amount;

    private String date;

    public JobOffer(String name, String area, Integer amount, String date) {
        this.name = name;
        this.area = area;
        this.amount = amount;
        this.date = date;
    }

    public JobOffer() {
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Object[] tmp = new Object[]{this.name, this.date, this.amount, this.area};
        for(Object aux : tmp){
            if(aux==null){
                return false;
            }
        }
        return true;
    }
}
