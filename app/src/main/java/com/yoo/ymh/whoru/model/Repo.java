package com.yoo.ymh.whoru.model;

/**
 * Created by Yoo on 2016-08-09.
 */
public class Repo {
    private int id;
    private String name;
    private String full_name;
    private Owner owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String print_info()
    {
        return "id: "+ String.valueOf(this.id)+"\n"+
                "name: "+this.name+"\n"+
                "full_name: "+this.full_name+"\n"+
                "owner: "+this.owner.print_info()+"\n";
    }
}
class Owner{
    String login;
    String url;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String print_info()
    {
        return this.login+this.url;
    }
}
//CLASS 는 JSONOBJECT의 개념 JSONARRAY 는 LIST<CLASS>로 보는게 맞는거같음.
